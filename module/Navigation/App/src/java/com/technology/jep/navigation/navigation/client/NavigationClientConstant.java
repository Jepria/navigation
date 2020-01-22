package com.technology.jep.navigation.navigation.client;

import com.google.gwt.core.client.GWT;
import com.technology.jep.navigation.navigation.shared.NavigationConstant;
import com.technology.jep.navigation.navigation.shared.text.NavigationText;
 
public class NavigationClientConstant extends NavigationConstant {
 
  public static NavigationText navigationText = (NavigationText) GWT.create(NavigationText.class);
  public static final String NAVIGATION_OPENED_TABS_STORAGE_ID = "NAVIGATION_OPENED_TABS_STORAGE_ID";
  public static final String NAVIGATION_EXPANDED_NODES_STORAGE_ID = "NAVIGATION_EXPANDED_NODES_STORAGE_ID";
  public static final String NAVIGATION_CHECKED_NODES_STORAGE_ID = "NAVIGATION_CHECKED_NODES_STORAGE_ID";
  public static final String NAVIGATION_SELECTED_TAB_STORAGE_ID = "NAVIGATION_SELECTED_TAB_STORAGE_ID";
  
}
