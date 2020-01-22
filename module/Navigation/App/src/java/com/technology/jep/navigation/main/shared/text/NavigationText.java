package com.technology.jep.navigation.main.shared.text;

/**
 * Interface to represent the constants contained in resource bundle:
 * 	'D:/Project/JEPSvn/Module/Navigation/Trunk/App/src/java/com/technology/rfi/navigation/main/shared/text/NavigationText.properties'.
 */
public interface NavigationText extends com.google.gwt.i18n.client.Constants {
  
  /**
   * Translated "Navigation".
   * 
   * @return translated "Navigation"
   */
  @DefaultStringValue("Navigation")
  @Key("module.title")
  String module_title();

  /**
   * Translated "Сменить пароль".
   * 
   * @return translated "Сменить пароль"
   */
  @DefaultStringValue("Сменить пароль")
  @Key("submodule.changepassword.title")
  String submodule_changepassword_title();

  /**
   * Translated "Навигация".
   * 
   * @return translated "Навигация"
   */
  @DefaultStringValue("Навигация")
  @Key("submodule.navigation.title")
  String submodule_navigation_title();
}
