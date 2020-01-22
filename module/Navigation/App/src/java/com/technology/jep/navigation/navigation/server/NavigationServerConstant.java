package com.technology.jep.navigation.navigation.server;
 
import com.technology.jep.jepria.server.JepRiaServerConstant;
 
public class NavigationServerConstant extends JepRiaServerConstant {
 
  public static final String RESOURCE_BUNDLE_NAME = "com.technology.jep.navigation.navigation.shared.text.NavigationText";
 
  public static final String NAVIGATION_MODULE_ENVIRONMENT_NAME = "navigationModuleName";
  
  public static final String NAVIGATION_MODULE = Util.getNavigationModule();
  
  public static final String NAVIGATION_MENU_ENVIRONMENT_NAME = "navigationMenuXmlPath";
  
  public static final String NAVIGATION_XML = Util.getNavigationXML();
  
  public static final String FULL_ACCESS_ROLE_ENVIRONMENT_NAME = "FULL_ACCESS_ROLE";
  
  public static final String DEFAULT_FULL_ACCESS_ROLE = "NavAll";
  
  public static final String FULL_ACCESS_ROLE_NAME = Util.getFullAccessRoleName();
  
  public static final String DATA_SOURCE_JNDI_NAME = "jdbc/RFInfoDS";
  
  /**
   *  Атрибут наименования модуля для подключения внешней навигации
   */
  public static final String MENU_EXTERNAL_MODULE_ATTRIBUTE_NAME = "extContextPath";
  
  /**
   *  Атрибут пути навигации для внешних ссылок
   */
  public static final String MENU_EXTERNAL_PATH_ATTRIBUTE_NAME = "extMenu";
  
  /**
   *  Атрибут пути файлов ресурсов для внешних ссылок
   */
  public static final String MENU_EXTERNAL_PROPERTY_ATTRIBUTE_NAME = "extProperty";
  
  /**
   *  Атрибут наименования внешней навигации
   */
  public static final String MENU_PARENT_EXTERNAL_MODULE_ATTRIBUTE_NAME = "parentExtContextPath";
  
  /**
   *  Атрибут пути навигации для внешних ссылок
   */
  public static final String MENU_PARENT_EXTERNAL_PATH_ATTRIBUTE_NAME = "parentExtMenu";
  
  /**
   *  Атрибут пути файлов ресурсов для внешних ссылок
   */
  public static final String MENU_PARENT_EXTERNAL_PROPERTY_ATTRIBUTE_NAME = "parentExtProperty";
  
  public final static String EXTERNAL_MODULE_OPTION_PROPERTY = MENU_EXTERNAL_MODULE_ATTRIBUTE_NAME;
  
  public final static String EXTERNAL_PATH_OPTION_PROPERTY = MENU_EXTERNAL_PATH_ATTRIBUTE_NAME;
  
  public final static String EXTERNAL_RESOURCE_OPTION_PROPERTY = MENU_EXTERNAL_PROPERTY_ATTRIBUTE_NAME;
  
  
  /**
   * Расширение файлов - текстовых ресурсов
   */
  public static final String PROPERTY_EXTENSION = ".properties";
  
  public static final String CHANGE_PASSWORD_REQUEST_ATTRIBUTE_NAME = "changePasswordRequestAttributeName";
  
  public static final String LOCALE_REQUEST_NAME = "locale";
 
}
