package com.technology.jep.navigation.navigation.client.ui.form.plain.navigation;

import static com.technology.jep.navigation.navigation.client.ui.form.plain.navigation.menu.CustomNavigationMenu.*;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.technology.jep.jepria.client.widget.field.tree.images.TreeFieldResources;

public interface NavigationTreeFieldResources extends TreeFieldResources {
  
  public static final NavigationTreeFieldResources INSTANCE = GWT.create(NavigationTreeFieldResources.class);
  
  @Source(MENU_IMAGE)
  ImageResource checked();
  
  @Source(MENU_IMAGE)
  ImageResource unchecked();
  
  @Source(AUTHORIZATION_IMAGE)
  ImageResource authorization();
  
  @Source(CHANGE_PASSWORD_IMAGE)
  ImageResource changePassword();
  
  @Source(EXIT_IMAGE)
  ImageResource exit();
  
  @Source(LANGUAGE_IMAGE)
  ImageResource language();
  
  @Source(HIDE_PANEL_IMAGE)
  ImageResource slideHidePanel();
  
  @Source(SHOW_PANEL_IMAGE)
  ImageResource showSlidePanel();
  
  @Source(BLANK_IMAGE)
  ImageResource blank();
  
  @Source("navigationStyle.css")
  public CellTree.Style cellTreeStyle();
  
}
