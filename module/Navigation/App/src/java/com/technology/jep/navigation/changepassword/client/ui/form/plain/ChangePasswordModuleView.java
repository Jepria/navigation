package com.technology.jep.navigation.changepassword.client.ui.form.plain;

import com.google.gwt.event.dom.client.ClickHandler;
import com.technology.jep.jepria.client.ui.plain.PlainModuleView;
import com.technology.jep.navigation.changepassword.shared.dto.PasswordDto;

public interface ChangePasswordModuleView extends PlainModuleView {
  boolean isValid();
  void addClickHandler(ClickHandler clickHandler);
  PasswordDto getPasswordInfo();
  void clearForm();
  void clearError();
  void setError(String error);
  void bindEnterClickListener();
  void setOldPasswordFieldFocused();
}
