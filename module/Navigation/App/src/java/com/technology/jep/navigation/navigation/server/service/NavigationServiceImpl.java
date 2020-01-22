package com.technology.jep.navigation.navigation.server.service;

import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.CHANGE_PASSWORD_REQUEST_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.EXTERNAL_MODULE_OPTION_PROPERTY;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.EXTERNAL_PATH_OPTION_PROPERTY;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.FULL_ACCESS_ROLE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.MENU_EXTERNAL_MODULE_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.MENU_EXTERNAL_PATH_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.MENU_EXTERNAL_PROPERTY_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.MENU_PARENT_EXTERNAL_PATH_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.MENU_PARENT_EXTERNAL_PROPERTY_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.NAVIGATION_MODULE;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.NAVIGATION_XML;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.PROPERTY_EXTENSION;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.RESOURCE_BUNDLE_NAME;
import static com.technology.jep.navigation.navigation.server.Util.addLanguage;
import static com.technology.jep.navigation.navigation.server.Util.defineLocale;
import static com.technology.jep.navigation.navigation.server.Util.getDOM;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.REQUEST_URL;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Document;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.security.JepSecurityModule;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.field.option.JepParentOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.navigation.navigation.server.MainResource;
import com.technology.jep.navigation.navigation.server.NavigationServerFactory;
import com.technology.jep.navigation.navigation.server.Util;
import com.technology.jep.navigation.navigation.server.dao.Navigation;
import com.technology.jep.navigation.navigation.shared.exceptions.ResourcePropertiesNotFoundException;
import com.technology.jep.navigation.navigation.shared.exceptions.SessionExpiredException;
import com.technology.jep.navigation.navigation.shared.record.NavigationRecordDefinition;
import com.technology.jep.navigation.navigation.shared.service.NavigationService;
 
@RemoteServiceRelativePath("NavigationService")
public class NavigationServiceImpl extends JepDataServiceServlet<Navigation> implements NavigationService  {
 
  private static final long serialVersionUID = 1L;
 
  public NavigationServiceImpl() {
    super(NavigationRecordDefinition.instance, NavigationServerFactory.instance);
  }

  /**
   *  Атрибут, определяющий имя класса, содержащего определение ролей
   */
  private static final String MENU_DEFINITION_RESOURCE_BUNDLE_ATTRIBUTE_NAME = "resourceBundle";
  
  private final Map<String, Document> navigationMap = new HashMap<>();
  
  private final Map<String, ResourceBundle> resourceBundleMap = new HashMap<>();
  
  private List<JepOption> getMenus(JepParentOption parentOption, JepSecurityModule jepSecurityModule, HttpSession session) throws ApplicationException {
    HttpServletRequest request = getThreadLocalRequest();
    boolean isGuest = Util.isGuest(jepSecurityModule) || (session.getAttribute(CHANGE_PASSWORD_REQUEST_ATTRIBUTE_NAME) == null ? Util.needToChangePassword(request) : (boolean) session.getAttribute(CHANGE_PASSWORD_REQUEST_ATTRIBUTE_NAME));
    List<String> roles = isGuest ? jepSecurityModule.getGuestRoles() : jepSecurityModule.getRoles();

    String navigationXml = NAVIGATION_XML, resource = RESOURCE_BUNDLE_NAME;
    String context = NAVIGATION_MODULE;

    if (parentOption != null){
      context = parentOption.get(MENU_EXTERNAL_MODULE_ATTRIBUTE_NAME);

      String xml = parentOption.get(MENU_EXTERNAL_PATH_ATTRIBUTE_NAME);
      if (!JepRiaUtil.isEmpty(xml)) {
        navigationXml = xml;
      }
      else {
        String parentPath = parentOption.get(MENU_PARENT_EXTERNAL_PATH_ATTRIBUTE_NAME);
        if (!JepRiaUtil.isEmpty(parentPath)) {
          navigationXml = parentPath;
        }
      }
      String res = parentOption.get(MENU_EXTERNAL_PROPERTY_ATTRIBUTE_NAME);
      if (!JepRiaUtil.isEmpty(res)) {
        resource = res;
      }
      else {
        String parentRes = parentOption.get(MENU_PARENT_EXTERNAL_PROPERTY_ATTRIBUTE_NAME);
        if (!JepRiaUtil.isEmpty(parentRes)) {
          resource = parentRes;
        }
      }
    }

    Document document = getDocument(navigationXml, context);
    if (NAVIGATION_XML.equalsIgnoreCase(navigationXml)){
      String menuResourceElement = document.getDocumentElement().getAttribute(MENU_DEFINITION_RESOURCE_BUNDLE_ATTRIBUTE_NAME); 
      if (!JepRiaUtil.isEmpty(menuResourceElement)) {
        resource = menuResourceElement;
      }
      try {
        MainResource.setMainResource(getResource(resource, context), Util.defineLocale(request).getLanguage());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    List<JepOption> menus = dao.getMenus(
        document
        , getResource(resource, context)
        , roles
        , parentOption);
    filterizeUnavailableExternalMenu(menus, roles);

    if (isGuest && menus.size() == 0) {
      throw new SessionExpiredException("Session was expired", new Throwable());
    }

    return menus;  
  }
  
  @Override
  public void resetCache() throws ApplicationException {
    navigationMap.clear();
    resourceBundleMap.clear();
    dao.resetInitialSeedToMenus();
    dao.clearRoleCache();
  }
  
  private Document getDocument(String xml, String externalContext) throws ApplicationException {
    if (JepRiaUtil.isEmpty(xml)) throw new NullPointerException("NavigationDefinition file must be defined!");
    
    if (navigationMap.containsKey(xml)){
      return navigationMap.get(xml);
    }
    else {
      Document doc = null;
      ServletContext servletContext = getThreadLocalRequest().getServletContext();
	  if (!JepRiaUtil.isEmpty(externalContext)) {
		  servletContext = servletContext.getContext(externalContext);
	  }
	  if (servletContext != null) {
        try (InputStream is = servletContext.getResourceAsStream(xml)) {
          doc = getDOM(is);
          navigationMap.put(xml, doc);
        } catch (Throwable e) {
          throw new ApplicationException(MessageFormat.format("Please check file {0} in the context {1}! It caused exception: {2}", xml, externalContext, e.getLocalizedMessage()), e);
        }
        return doc;
      }
      String fileNotFoundException = MessageFormat.format("Please check file {0} in the context {1}!", xml, externalContext);
      throw new ApplicationException(fileNotFoundException, new Throwable(fileNotFoundException));
    }
  }

  private ResourceBundle getResource(String resourceBundle, String context) throws ApplicationException {
    if (JepRiaUtil.isEmpty(resourceBundle)) throw new NullPointerException("ResourceBundle file must be defined!");
    Locale locale = defineLocale(getThreadLocalRequest());
    String resourceBundleWithLang = resourceBundle + locale.getLanguage();

    if (resourceBundleMap.containsKey(resourceBundleWithLang)){
      return resourceBundleMap.get(resourceBundleWithLang);
    }
    else {
      ResourceBundle resource = null;
      try {
        ServletContext sc = getThreadLocalRequest().getServletContext().getContext(context);
        if (sc != null) {
          URL resourceUrl = sc.getResource(resourceBundle);  
          if (resourceUrl != null) {
            logger.trace("BEGIN GETTING RESOURCE BUNDLE FROM DOCUMENT '" + resourceBundle + "'");
            // directory is a place to search the necessary bundles (consider the user locale)
            ResourceBundle.clearCache();
            resource = ResourceBundle.getBundle(resourceBundle, locale, new CrossContextControl(sc, locale));
            logger.trace("END GETTING RESOURCE BUNDLE FROM DOCUMENT '" + resourceBundle + "'");
          }
        }
        else {
          try {
            resource = ResourceBundle.getBundle(resourceBundle, locale);
          }
          catch(MissingResourceException e){
            throw new ResourcePropertiesNotFoundException("The file '" + resourceBundle + "' is not found! Please check if file '" + NAVIGATION_XML + "' has right attributes '" + MENU_DEFINITION_RESOURCE_BUNDLE_ATTRIBUTE_NAME + "'");
          }
        }
      } catch(MissingResourceException e){
        throw new ApplicationException(MessageFormat.format("Missing Resource {0} in the context {1}! It caused exception: {2}", resourceBundle, context, e.getLocalizedMessage()), e);
      } catch(MalformedURLException e){
        throw new ApplicationException(e.getLocalizedMessage(), e);
      }

      resourceBundleMap.put(resourceBundleWithLang, resource);
      return resource;
    }
  }

  /**
   * Удаляем меню из внешних источников, xml-структура которых не доступна
   * @param menus  входной список меню
   * @param roles  входной список ролей для фильтрации
   * 
   * @throws ApplicationException	пробрасываем в клиентский код сообщение об ошибке
   */
  private void filterizeUnavailableExternalMenu(List<JepOption> menus, List<String> roles) throws ApplicationException {
    HttpServletRequest request = getThreadLocalRequest();
    for (Iterator<JepOption> iter = menus.iterator(); iter.hasNext();){
      JepOption menu = iter.next();
      String extModule = menu.get(EXTERNAL_MODULE_OPTION_PROPERTY);
      String extMenuPath = menu.get(EXTERNAL_PATH_OPTION_PROPERTY);
      if (!JepRiaUtil.isEmpty(extMenuPath)) {
        ServletContext sc = request.getServletContext().getContext(extModule);
        if (sc == null) {
          iter.remove();
          continue;
        }
        else {
          try {
            if (sc.getResource(extMenuPath) == null) {
              iter.remove();
              continue;
            }
          }	
          catch(MalformedURLException e){
            iter.remove();
            continue;
          }

          Document doc = getDocument(extMenuPath, extModule);
          if (doc == null || !dao.isExtMenuAvailable(doc, menu, roles)) {
            iter.remove();
            continue;
          }
        }
      }
      String requestUrl = menu.get(REQUEST_URL);
      if (!JepRiaUtil.isEmpty(requestUrl)){
        menu.set(REQUEST_URL, addLanguage(requestUrl, defineLocale(request)));
      }
    }
  }

  private static final String TEXT = "text";
  @Override
  public double getLetterWidth(String fontFamily, int fontWidth){
    AffineTransform affinetransform = new AffineTransform();     
    FontRenderContext frc = new FontRenderContext(affinetransform, true, true);     
    Font font = new Font(fontFamily, Font.PLAIN, fontWidth);
    return font.getStringBounds(TEXT, frc).getWidth() / (double) TEXT.length();
  }
  
  @Override
  public boolean isFullAccess() throws ApplicationException {
    try {
      return Util.getSecurityModule(getThreadLocalRequest()).isRole(FULL_ACCESS_ROLE_NAME, false);
    } catch (Exception e) {
      throw new ApplicationException(e.getLocalizedMessage(), e);
    }
  }

  @Override
  public Map<JepParentOption, List<JepOption>> getAllMenus() throws ApplicationException {
    Map<JepParentOption, List<JepOption>> menuStructure = new HashMap<JepParentOption, List<JepOption>>();
    HttpServletRequest request = getThreadLocalRequest();
    JepSecurityModule jepSecurityModule = Util.getSecurityModule(request);
    HttpSession session = request.getSession(false);
    if (session == null) {
      session = request.getSession();
    }
    fillMenuStructure(menuStructure, null, jepSecurityModule, session);
    resetCache();
    return menuStructure;
  }

  private synchronized void fillMenuStructure(Map<JepParentOption, List<JepOption>> menuStructure, JepParentOption parentOption, JepSecurityModule jepSecurityModule, HttpSession session) throws ApplicationException {
    List<JepOption> childrenNodes = getMenus(parentOption, jepSecurityModule, session);
    menuStructure.put(parentOption, childrenNodes);
    for (JepOption node : childrenNodes) {
      if (node instanceof JepParentOption) {
        fillMenuStructure(menuStructure, (JepParentOption) node, jepSecurityModule, session);    	
      }
    }
  }
}

class CrossContextControl extends Control {
	
	private ServletContext sc;
	private Locale userLocale;
	
	public CrossContextControl(ServletContext servletContext, Locale locale) {
		this.sc = servletContext;
		this.userLocale = locale;
	}
	
	public ResourceBundle newBundle(String fullPath, Locale locale, String format, ClassLoader loader, boolean reload)
			throws IllegalAccessException, InstantiationException, IOException {
        ResourceBundle bundle = null;
        Path path = Paths.get(fullPath);
        if (locale.equals(this.userLocale)){
        	fullPath = path.resolveSibling(path.getFileName().toString().replace(PROPERTY_EXTENSION, "").concat("_").concat(locale.getLanguage()).concat(PROPERTY_EXTENSION)).toString();
        }
        InputStream stream = sc.getResourceAsStream(fullPath);
        if (stream != null) {
            try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8.name())) {
                bundle = new PropertyResourceBundle(reader);
            }
            finally {
            	stream.close();
            }
        }
        return bundle;
    }
}
