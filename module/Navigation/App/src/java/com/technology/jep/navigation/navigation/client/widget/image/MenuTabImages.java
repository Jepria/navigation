package com.technology.jep.navigation.navigation.client.widget.image;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface MenuTabImages extends ClientBundle {

  MenuTabImages INSTANCE = GWT.create(MenuTabImages.class);
  
  @Source("leftArrow.png")
  ImageResource leftArrow();
  
  @Source("rightArrow.png")
  ImageResource rightArrow();
  
}
