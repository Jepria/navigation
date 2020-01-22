package com.technology.jep.navigation.main.client;

import com.google.gwt.core.client.GWT;
import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.jep.navigation.main.shared.text.NavigationText;
 
public class NavigationClientConstant extends JepRiaConstant {
 
  public static final String CHANGEPASSWORD_MODULE_ID = "ChangePassword";
  public static final String NAVIGATION_MODULE_ID = "Navigation";
  
  public static NavigationText navigationText = (NavigationText) GWT.create(NavigationText.class);
  
  public static final String URL_CHANGEPASSWORD_MODULE = "/Navigation/Navigation.jsp?em=ChangePassword&es=sh";
  public static final String URL_NAVIGATION_MODULE = "/Navigation/Navigation.jsp?em=Navigation&es=sh";
}
