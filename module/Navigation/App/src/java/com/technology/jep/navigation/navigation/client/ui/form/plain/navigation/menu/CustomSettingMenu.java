package com.technology.jep.navigation.navigation.client.ui.form.plain.navigation.menu;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import com.google.gwt.resources.client.ImageResource;

/**
 * Перечисление с кастомизированными элементами меню дерева настроек.
 */
public enum CustomSettingMenu {
  
    RESET_CACHE(JepImages.refresh());
    
    CustomSettingMenu(ImageResource resource){
      this.resource = resource;
    }
    
    ImageResource resource;
    
    public ImageResource getImage(){
      return resource;
    }
}
