package com.technology.jep.navigation.navigation.server.dao;

import static com.technology.jep.jepria.server.security.JepSecurityConstant.JEP_SECURITY_MODULE_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.EXTERNAL_MODULE_OPTION_PROPERTY;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.EXTERNAL_PATH_OPTION_PROPERTY;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.EXTERNAL_RESOURCE_OPTION_PROPERTY;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.MENU_EXTERNAL_MODULE_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.MENU_EXTERNAL_PATH_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.MENU_EXTERNAL_PROPERTY_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.MENU_PARENT_EXTERNAL_MODULE_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.MENU_PARENT_EXTERNAL_PATH_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.MENU_PARENT_EXTERNAL_PROPERTY_ATTRIBUTE_NAME;
import static com.technology.jep.navigation.navigation.server.Util.isIntersected;
import static com.technology.jep.navigation.navigation.server.Util.wasParameterAppended;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.FRAME;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.IMAGE;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.KEY_OPTION_PROPERTY;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.REQUEST_URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.technology.jep.jepria.server.dao.JepDao;
import com.technology.jep.jepria.server.security.JepSecurityModule;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.WrongFormatException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.field.option.JepParentOption;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.jepria.shared.util.Mutable;

public class NavigationDao extends JepDao implements Navigation {

  @Override
  public List<JepRecord> find(JepRecord templateRecord, Mutable<Boolean> autoRefreshFlag, Integer maxRowCount, Integer operatorId) throws ApplicationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void delete(JepRecord record, Integer operatorId) throws ApplicationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void update(JepRecord record, Integer operatorId) throws ApplicationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Integer create(JepRecord record, Integer operatorId) throws ApplicationException {
    throw new UnsupportedOperationException();
  }
  
  /**
   *  Корневой тег определения меню
   */
  private static final String MENU_DEFINITION_TAG_NAME = "menu-definition";
  
  /**
   *  Тег определения множества существующих ролей
   */
  private static final String ROLE_DEFINITIONS_TAG_NAME = "role-definitions";
  
  /**
   *  Тег определения роли
   */
  private static final String ROLE_DEFINITION_TAG_NAME = "role-definition";
  
  /**
   *  Тег определения набора ролей, влияющих на отображение текущего меню
   */
  private static final String ROLES_TAG_NAME = "roles";
    
  /**
   *  Тег определения роли, являющейся условием отображения меню
   */
  private static final String ROLE_TAG_NAME = "role";
  
  /**
   *  Тег определения узла меню
   */
  private final static String MENU_TAG_NAME = "menu";
  
  /**
   *  Тег определения запроса URL
   */
  private static final String REQUEST_TAG_NAME = "request";
  
  private final static String NAME_ATTR = "name";
  /**
   *  Атрибут названия роли
   */
  private static final String ROLE_DEFINITION_NAME_ATTRIBUTE_NAME = NAME_ATTR;
  /**
   *  Атрибут названия меню
   */
  private static final String MENU_NAME_ATTRIBUTE_NAME = NAME_ATTR;
  
  /**
   *  Атрибут имени файла изображения
   */
  private static final String IMAGE_ATTRIBUTE_NAME = "image";

  /**
   *  Атрибут имени фрейма, в котором выполняется действие по запросу,
   */
  private static final String FRAME_ATTRIBUTE_NAME = "frame";
  
  private int menuNodeId = 1; 
  
  @Override
  public List<JepOption> getMenus(Document doc, ResourceBundle resource, List<String> roles, JepOption parentOption){
    // Обрабатываем единажды сложные роли для документа
    if (parentOption == null){
      processRoleDefinitions(doc);
    }
    
    List<JepOption> result = new ArrayList<JepOption>();
    XPathFactory xpathFactory = XPathFactory.newInstance();
    // XPath to find empty text nodes.
    XPathExpression xpathExp;
    String key = parentOption == null ? null : (String) parentOption.get(KEY_OPTION_PROPERTY);
    boolean isExternal = !JepRiaUtil.isEmpty(parentOption == null ? null : parentOption.get(EXTERNAL_PATH_OPTION_PROPERTY));
    
    boolean top = JepRiaUtil.isEmpty(key);
    try {
      xpathExp = xpathFactory.newXPath().compile(
          (top ? "/" + MENU_DEFINITION_TAG_NAME : (isExternal ? "/" + MENU_TAG_NAME : "//" + MENU_TAG_NAME + "[normalize-space(@" + MENU_NAME_ATTRIBUTE_NAME + ") = '" + key + "']")) 
               + "/" + MENU_TAG_NAME);
      NodeList emptyTextNodes = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);
      
      for (int i = 0; i < emptyTextNodes.getLength(); i++) {
          Node menuNode = emptyTextNodes.item(i);
          if (menuNode.getNodeType() == Node.ELEMENT_NODE){
            Element menuElement = (Element) menuNode;
            NodeList rolesNodes = menuElement.getElementsByTagName(ROLES_TAG_NAME);
            int nodeCount = rolesNodes.getLength();
            boolean isEveryoneAvailable = nodeCount == 0; // если роли не представлены, значит доступ для всех; 
            boolean isMenuAvailable = isEveryoneAvailable;
            for (int j = 0; j < nodeCount; j++) {
              Element rolesElement = (Element) rolesNodes.item(j);
              isMenuAvailable = isMenuAvailable(doc, rolesElement, roles);
              if (isMenuAvailable) {
                break;
              }
            }
            if (!isMenuAvailable) continue;
            
            String resourceKey = menuElement.getAttribute(MENU_NAME_ATTRIBUTE_NAME);
            String menuExternalPath = menuElement.getAttribute(MENU_EXTERNAL_PATH_ATTRIBUTE_NAME);
            String menuExternalProperty = menuElement.getAttribute(MENU_EXTERNAL_PROPERTY_ATTRIBUTE_NAME);
            String menuExternalModule = menuElement.getAttribute(MENU_EXTERNAL_MODULE_ATTRIBUTE_NAME);
            
            String optionName = resourceKey;
            try {
              optionName = resource.getString(resourceKey);
            }
            catch(MissingResourceException e){
              e.printStackTrace();
            }
            JepOption option = hasSubMenu(menuElement) || !JepRiaUtil.isEmpty(menuExternalPath) ? 
                new JepParentOption(optionName, menuNodeId++) : 
                  new JepOption(optionName, menuNodeId++);
            option.set(KEY_OPTION_PROPERTY, resourceKey);
            option.set(EXTERNAL_MODULE_OPTION_PROPERTY, menuExternalModule);
            option.set(EXTERNAL_PATH_OPTION_PROPERTY, menuExternalPath);
            option.set(EXTERNAL_RESOURCE_OPTION_PROPERTY, menuExternalProperty);
            option.set(FRAME, menuElement.getAttribute(FRAME_ATTRIBUTE_NAME));
            option.set(IMAGE, menuElement.getAttribute(IMAGE_ATTRIBUTE_NAME));
            NodeList requestNode = menuElement.getElementsByTagName(REQUEST_TAG_NAME);
            option.set(REQUEST_URL, requestNode.getLength() > 0 ? obtainRequest((Element) requestNode.item(0)) : null);
            option.set(MENU_PARENT_EXTERNAL_PATH_ATTRIBUTE_NAME, 
                parentOption != null ? (JepRiaUtil.isEmpty(parentOption.get(EXTERNAL_PATH_OPTION_PROPERTY)) ? parentOption.get(MENU_PARENT_EXTERNAL_PATH_ATTRIBUTE_NAME) : parentOption.get(EXTERNAL_PATH_OPTION_PROPERTY)) : null);
            option.set(MENU_PARENT_EXTERNAL_PROPERTY_ATTRIBUTE_NAME, 
                parentOption != null ? (JepRiaUtil.isEmpty(parentOption.get(EXTERNAL_RESOURCE_OPTION_PROPERTY)) ? parentOption.get(MENU_PARENT_EXTERNAL_PROPERTY_ATTRIBUTE_NAME) : parentOption.get(EXTERNAL_RESOURCE_OPTION_PROPERTY)) : null);
            option.set(MENU_PARENT_EXTERNAL_MODULE_ATTRIBUTE_NAME, 
                    parentOption != null ? (JepRiaUtil.isEmpty(parentOption.get(EXTERNAL_MODULE_OPTION_PROPERTY)) ? parentOption.get(MENU_PARENT_EXTERNAL_MODULE_ATTRIBUTE_NAME) : parentOption.get(EXTERNAL_MODULE_OPTION_PROPERTY)) : null);
            result.add(option); 
          }
      }
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }  
    return result;
    
  }
  
  @Override
  public void resetInitialSeedToMenus() {
    menuNodeId = 1;
  }

  private boolean hasSubMenu(Element menuElement) {
    boolean hasSubMenu = false;
    NodeList childNodes = menuElement.getChildNodes();
    for (int j = 0; j < childNodes.getLength(); j++){
      Node subMenuNode = childNodes.item(j);
      if (subMenuNode.getNodeType() == Node.ELEMENT_NODE){
        hasSubMenu = MENU_TAG_NAME.equalsIgnoreCase(subMenuNode.getNodeName());
        if (hasSubMenu) break;
      }
    }
    return hasSubMenu;
  }
  
  /**
   * Обработка тега &lt;role-definitions&gt;, подготовка хранилища ролей.
   * Хранилище ролей используется для последующего разложения сложных ролей на элементарные.
   * 
   * @param doc XML-документ
   */
  private void processRoleDefinitions(Document doc) {
    NodeList roleDefinitionsNodeList = doc.getElementsByTagName(ROLE_DEFINITIONS_TAG_NAME);
    if(roleDefinitionsNodeList.getLength() > 0){
      Element roleDefinitionsElement = (Element) roleDefinitionsNodeList.item(0);
      NodeList nodes = roleDefinitionsElement.getChildNodes();
      int nodeNumber = nodes.getLength();
      for (int i = 0; i < nodeNumber; i++) {
        Node node = nodes.item(i);
        switch (node.getNodeType()) {
        case Node.ELEMENT_NODE:
          Element element = (Element) node;
          String tagName = element.getTagName();
          if (ROLE_DEFINITION_TAG_NAME.equals(tagName)) {
            processRoleDefinition(doc, element);
          } else {
            throw new WrongFormatException("Unexpected XML element type: '" + tagName + "'", null);
          }
          break;
        case Node.TEXT_NODE:
          break;
        default:
          throw new WrongFormatException("Unexpected XML element type: '" + node.getNodeType() + "'", null);
        }
      }
    }
  }
  
  /**
   * Определение доступности меню на основе проверки ролей
   * 
   * @param doc XML-документ
   * @param rolesNode узел, соответствующий тегу &lt;roles&gt;
   * @param roles список ролей
   * @return true, если меню доступно, иначе - false
   */
  private boolean isMenuAvailable(Document doc, Element rolesNode, List<String> roles) {
    boolean result = false;
    NodeList nodes = rolesNode.getChildNodes();
    
    int nodeNumber = nodes.getLength();
    boolean isRoleDefinitionPresent = false;
    loop: for (int i = 0; i < nodeNumber; i++) {
      Node node = nodes.item(i);
      switch (node.getNodeType()) {
        case Node.ELEMENT_NODE:
          Element element = (Element) node;
          String tagName = element.getTagName();
          if (ROLE_TAG_NAME.equals(tagName)) {
            String role = element.getTextContent();
            isRoleDefinitionPresent = true;
            if (isIntersected(getRoles(doc, role), roles)) {
              result = true;
              break loop;
            }
          } else {
            throw new WrongFormatException("Unexpected XML element type: '" + tagName + "'", null);
          }
          break;
        case Node.TEXT_NODE:
          break;
        default:
          throw new WrongFormatException("Unexpected XML element type: '" + node.getNodeType() + "'", null);
      }
    }
    
    result = result || !isRoleDefinitionPresent; // Если ролей не объявлено, то доступно всем
    return result;
  }
  
  /**
   * Получение множества простых ролей по названию (сложной) роли
   * 
   * @param doc XML-документ
   * @param inputRoleName название роли
   * @return множество простых ролей
   */
  private Set<String> getRoles(Document doc, String inputRoleName) {
    Set<String> result = new HashSet<String>();
    List<String> childRoles = complexRoles.get(inputRoleName);
    if (childRoles != null) {
      for (String roleName : childRoles) {
        Set<String> roles = getRoles(doc, roleName);
        Iterator<String> iterator = roles.iterator();
        while (iterator.hasNext()) {
          String role = iterator.next();
          result.add(role);
        }
      }
    } else {
      if(inputRoleName != null) {
        result.add(inputRoleName);
      } else {
        System.out.println(this.getClass().getName() + ".getRoles(" + inputRoleName + ")");
      }
    }

    return result;
  }
  
  /**
   * Хранилище сложных ролей
   */
  private Map<String, List<String>> complexRoles = new HashMap<>();
  
  /**
   * Подготовка хранилища ролей. Обработка определения одной роли.
   * 
   * @param doc XML-документ
   * @param roleDefinitionElement элемент соответствующий тегу roleDefinition
   */
  private void processRoleDefinition(Document doc, Element roleDefinitionElement) {
    String complexRoleName = roleDefinitionElement.getAttribute(ROLE_DEFINITION_NAME_ATTRIBUTE_NAME);
    List<String> roles = new ArrayList<String>();
    NodeList nodes = roleDefinitionElement.getChildNodes();
    int nodeNumber = nodes.getLength();
    for (int i = 0; i < nodeNumber; i++) {
      Node node = nodes.item(i);
      switch (node.getNodeType()) {
      case Node.ELEMENT_NODE:
        Element element = (Element) node;
        String tagName = element.getTagName();
        if (ROLE_TAG_NAME.equals(tagName)) {
          String roleName = element.getTextContent();
          roles.add(roleName);
        } else {
          throw new WrongFormatException("Unexpected XML element type: '" + tagName + "'", null);
        }
        break;
      case Node.TEXT_NODE:
        break;
      default:
        throw new WrongFormatException("Unexpected XML element type: '" + node.getNodeType() + "'", null);
      }
    }
    complexRoles.put(complexRoleName, roles);
  }
  
  /**
   * Проверка доступности внешнего меню
   */
  @Override
  public boolean isExtMenuAvailable(Document doc, JepOption parentOption, List<String> roles) {
    XPathFactory xpathFactory = XPathFactory.newInstance();
    // XPath to find empty text nodes.
    XPathExpression xpathExp;
    try {
      xpathExp = xpathFactory.newXPath().compile("//" + ROLES_TAG_NAME);
      NodeList rolesNodes = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);
      for (int i = 0; i < rolesNodes.getLength(); i++){
        Element rolesElement = (Element) rolesNodes.item(i);
        if (isMenuAvailable(doc, rolesElement, roles)) {
          return true;
        }
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
    return false;
  }
  
  /**
   * Получение строкового представления запросной части URL
   * 
   * @param requestElement элемент, соответствующий тегу &lt;request&gt;
   * @return строковое представление запросной части URL
   */
  private String obtainRequest(Element requestElement) {
    StringBuilder result = new StringBuilder();
    String requestBody = requestElement.getTextContent();
    if (requestBody == null) requestBody = "";
    result.append(requestBody);
    NamedNodeMap attributes = requestElement.getAttributes();
    int attributeNumber = attributes.getLength();
    
    boolean wasAppended = wasParameterAppended(requestBody);
    
    for (int i = 0; i < attributeNumber; i++) {
      Node attribute = attributes.item(i);
      String parameterName = attribute.getNodeName();
      String parameterValue = attribute.getNodeValue();
      if (wasAppended) {
        result.append("&");
      } else {
        result.append("?");
        wasAppended = true;
      }
      result.append(parameterName);
      result.append("=");
      result.append(parameterValue);
    }
    
    return result.toString();
  }
  
  @Override
  public boolean isChangePassword(HttpSession session, JepSecurityModule securityModule) {
	synchronized (session) {
		try {
			return securityModule.isChangePassword(securityModule.getOperatorId());
		}
		finally {
			session.removeAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME);
			session.setAttribute(JEP_SECURITY_MODULE_ATTRIBUTE_NAME, securityModule);
		}
	}
  }
  
  @Override
  public void clearRoleCache() {
	  complexRoles.clear();
  }
}
