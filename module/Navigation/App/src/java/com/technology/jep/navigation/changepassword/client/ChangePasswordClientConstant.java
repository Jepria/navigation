package com.technology.jep.navigation.changepassword.client;

import static com.technology.jep.navigation.main.client.NavigationClientConstant.*;
 

import com.google.gwt.core.client.GWT;
import com.technology.jep.navigation.changepassword.shared.ChangePasswordConstant;
import com.technology.jep.navigation.changepassword.shared.text.ChangePasswordText;
 
public class ChangePasswordClientConstant extends ChangePasswordConstant {
 
  public static ChangePasswordText changePasswordText = (ChangePasswordText) GWT.create(ChangePasswordText.class);
  
}
