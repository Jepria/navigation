package com.technology.jep.navigation.navigation.server;

import static com.technology.jep.jepria.server.security.JepSecurityConstant.JEP_SECURITY_MODULE_ATTRIBUTE_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.HTTP_REQUEST_PARAMETER_LOCALE;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.CHANGE_PASSWORD_REQUEST_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.DEFAULT_FULL_ACCESS_ROLE;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.FULL_ACCESS_ROLE_ENVIRONMENT_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.NAVIGATION_MENU_ENVIRONMENT_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.NAVIGATION_MODULE_ENVIRONMENT_NAME;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.LOCALE_AND_MULTITABS_MODE_COOKIE_NAME;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.technology.jep.jepria.server.security.JepSecurityModule;
import com.technology.jep.jepria.server.security.SecurityFactory;
import com.technology.jep.jepria.server.util.JepServerUtil;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.navigation.navigation.shared.field.CookieHelper;

public class Util {

  public static boolean wasParameterAppended(String url){
    return !JepRiaUtil.isEmpty(url) && url.contains("?");
  }
  
  public static String addLanguage(String beginStr, Locale locale) {
    StringBuilder result = new StringBuilder(beginStr);
    if (wasParameterAppended(beginStr)) {
      result.append("&");
    } else {
      result.append("?");
    }
    result.append(HTTP_REQUEST_PARAMETER_LOCALE);
    result.append("=");
    result.append(locale.getLanguage());
    return result.toString();
  }
  
  /**
   * The good way to store the locale information in user cookie.
   * 
   * @param request		запрос клиента
   * 
   * @return  locale of current user
   * 
   * @throws ApplicationException ошибка при раскодировании Cookie
   */
  public static Locale defineLocale(HttpServletRequest request) throws ApplicationException {
    Cookie[] cookies  = request.getCookies();
    String localeRequestParameter = null;
    if (cookies != null)
      for (Cookie cookie : cookies){
        if (LOCALE_AND_MULTITABS_MODE_COOKIE_NAME.equalsIgnoreCase(cookie.getName())){
          localeRequestParameter = cookie.getValue();
          break;
        }
      }
    String locale;
    try {
      locale = !JepRiaUtil.isEmpty(localeRequestParameter) ? CookieHelper.parseApplicationSetting(URLDecoder.decode(localeRequestParameter, "UTF-8")).locale : null;
    } catch (UnsupportedEncodingException e) {
      throw new ApplicationException(e.getLocalizedMessage(), e);
    }
    return !JepRiaUtil.isEmpty(locale) ? new Locale(locale) : request.getLocale();
  }
  

  
  /**
   * Проверка пересечения множеств
   * 
   * @param set1 первое множество
   * @param set2 второе множество
   * @return true, если множества пересекаются (пересечение непустое)
   */
  public static boolean isIntersected(Collection<String> set1, Collection<String> set2) {
    return set1.removeAll(set2);
  }
  
  /**
   * Получение структуры документа
   * 
   * @param is входной поток файла настроек или пути для генерации структуры
   * @return документ
   * @throws IOException					отсутствует файл
   * @throws SAXException					ошибка парсинга
   * @throws ParserConfigurationException	ошибка настройки конфигурации парсинга
   */
  public static final Document getDOM(InputStream is)
      throws IOException, SAXException, ParserConfigurationException {
    DocumentBuilder db = createDocumentBuilder();
    Document doc = db.parse(is);
    return doc;
  }
  /**
   * Создание билдера для построения DOM-структуры
   * 
   * @return билдер
   * @throws ParserConfigurationException	ошибка при конфигурации парсера
   */
  public static final DocumentBuilder createDocumentBuilder()
      throws ParserConfigurationException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true); // never forget this!
    dbf.setIgnoringElementContentWhitespace(true);
    DocumentBuilder db = dbf.newDocumentBuilder();
    return db;
  }
  
  public static final String getNavigationModule(){
	  String navigationMenuEnvironmentValue = JepServerUtil.getEnvironmentValue(NAVIGATION_MODULE_ENVIRONMENT_NAME);
	  return JepRiaUtil.isEmpty(navigationMenuEnvironmentValue) ? "/Navigation" : navigationMenuEnvironmentValue;
  }
  
  public static final String getNavigationXML(){
    String navigationMenuEnvironmentValue = JepServerUtil.getEnvironmentValue(NAVIGATION_MENU_ENVIRONMENT_NAME);
    return JepRiaUtil.isEmpty(navigationMenuEnvironmentValue) ?
        "/WEB-INF/resources/com/technology/jep/navigation/menu/navigation.xml" : navigationMenuEnvironmentValue;
  }
  
  public static final String getFullAccessRoleName(){
    String fullAccessRoleEnvironmentValue = JepServerUtil.getEnvironmentValue(FULL_ACCESS_ROLE_ENVIRONMENT_NAME);
    return JepRiaUtil.isEmpty(fullAccessRoleEnvironmentValue) ? DEFAULT_FULL_ACCESS_ROLE : fullAccessRoleEnvironmentValue;
  }
  
  public static boolean isGuest(JepSecurityModule securityModule) {
	  return !securityModule.isAuthorizedBySso();
  }
  
  public static boolean needToChangePassword(HttpServletRequest request) {
	  JepSecurityModule jepSecurityModule = getSecurityModule(request);
	  HttpSession session = request.getSession(false);
	  if (session == null) {
		  session = request.getSession();
	  }
	  boolean result = !isGuest(jepSecurityModule) && NavigationServerFactory.instance.getDao().isChangePassword(session, jepSecurityModule);
	  session.setAttribute(CHANGE_PASSWORD_REQUEST_ATTRIBUTE_NAME, result);
	  return result;
  }
  
  public static synchronized JepSecurityModule getSecurityModule(HttpServletRequest request) {
	  HttpSession session = request.getSession(false);
	  JepSecurityModule securityModule = session == null ? null : (JepSecurityModule) session.getAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME);
	  Principal user = request.getUserPrincipal();
	  if (securityModule == null || user == null ||
			  !Objects.equals(securityModule.getUsername(), user.getName())) {
		  securityModule = SecurityFactory.getSecurityModule(request);
	  }
	  return securityModule;
  }
}
