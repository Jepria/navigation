package com.technology.jep.navigation.navigation.client.ui.form.plain.navigation.menu;

import static com.technology.jep.navigation.navigation.client.ui.form.plain.navigation.NavigationTreeFieldResources.INSTANCE;

import com.google.gwt.resources.client.ImageResource;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Перечисление с кастомизированными элементами дерева навигации.
 */
public enum CustomNavigationMenu {
  
  ORDINAL(INSTANCE.checked()),
  AUTHORIZATION(INSTANCE.authorization()),
  CHANGE_PASSWORD(INSTANCE.changePassword()),
  LANGUAGE(INSTANCE.language()),
  EXIT(INSTANCE.exit());
  
  CustomNavigationMenu(ImageResource resource){
    this.resource = resource;
  }
  
  ImageResource resource;
  
  /**
   * Метод получения меню по идентификатору картинки.
   * 
   * @param name  идентификатор изображения меню
   * @return меню
   */
  public static CustomNavigationMenu getMenuByImageId(String name){
    if(JepRiaUtil.isEmpty(name)) return ORDINAL;
      
    if (AUTHORIZATION_IMAGE.equals(name)){
      return AUTHORIZATION;
    }
    else if (CHANGE_PASSWORD_IMAGE.equals(name)){
      return CHANGE_PASSWORD;
    }
    else if (EXIT_IMAGE.equals(name)){
      return EXIT;
    }
    else if (LANGUAGE_IMAGE.equals(name)){
      return LANGUAGE;
    }
    return ORDINAL;
  }
  
  /**
   * Получение ресурса картинки меню
   * @return изображение меню
   */
  public ImageResource getImage(){
    return this.resource;
  }

  public final static String MENU_IMAGE = "page.gif";
  public final static String AUTHORIZATION_IMAGE = "authorization.gif";
  public final static String CHANGE_PASSWORD_IMAGE = "changePassword.gif";
  public final static String EXIT_IMAGE = "close.gif";
  public final static String LANGUAGE_IMAGE = "language.gif";
  public final static String HIDE_PANEL_IMAGE = "hidePanel.png";
  public final static String SHOW_PANEL_IMAGE = "showPanel.png";
  public final static String BLANK_IMAGE = "blank.png";
}
