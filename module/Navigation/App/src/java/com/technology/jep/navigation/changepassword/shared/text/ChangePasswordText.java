package com.technology.jep.navigation.changepassword.shared.text;

/**
 * Interface to represent the constants contained in resource bundle:
 * 	'C:/SVN/Navigation_New/App/src/java/com/technology/jep/navigation/changepassword/shared/text/ChangePasswordText.properties'.
 */
public interface ChangePasswordText extends com.google.gwt.i18n.client.Constants {
  
  /**
   * Translated "Пароль и его подтверждение должны быть одинаковыми...".
   * 
   * @return translated "Пароль и его подтверждение должны быть одинаковыми..."
   */
  @DefaultStringValue("Пароль и его подтверждение должны быть одинаковыми...")
  @Key("changePassword.newPassword.error")
  String changePassword_newPassword_error();

  /**
   * Translated "Минимальная длина пароля составляет {0} символов...".
   * 
   * @return translated "Минимальная длина пароля составляет {0} символов..."
   */
  @DefaultStringValue("Минимальная длина пароля составляет {0} символов...")
  @Key("changePassword.newPassword.minLengthError")
  String changePassword_newPassword_minLengthError();

  /**
   * Translated "Пароль успешно изменен!".
   * 
   * @return translated "Пароль успешно изменен!"
   */
  @DefaultStringValue("Пароль успешно изменен!")
  @Key("changePassword.success")
  String changePassword_success();

  /**
   * Translated "Сменить пароль".
   * 
   * @return translated "Сменить пароль"
   */
  @DefaultStringValue("Сменить пароль")
  @Key("changePassword.title")
  String changePassword_title();
}
