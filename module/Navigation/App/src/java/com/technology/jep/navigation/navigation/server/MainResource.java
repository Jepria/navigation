package com.technology.jep.navigation.navigation.server;

import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Хранит основной текстовый ресурс навигации,
 * который необходимо использовать в jsp.
 */
public enum MainResource {
  INSTANCE;
  
  private static BlockingQueue<ResourceBundle> resourceBlockingQueue = new SynchronousQueue<ResourceBundle>();
  private static ResourceBundle mainResource;
  private static ResourceBundle mainResourceEn;
  private static ResourceBundle defaultMainResource;
  
  private MainResource() {};
  
  public static ResourceBundle getMainResource(String locale) throws InterruptedException, ApplicationException {
	  ResourceBundle result = getResource(locale);
	  if(result == null){
		  if (isRu(locale)) {
			  result = mainResource = resourceBlockingQueue.poll(1, TimeUnit.SECONDS); 
		  }
		  else {
			  result = mainResourceEn = resourceBlockingQueue.poll(1, TimeUnit.SECONDS);
		  }
	  }
	  return result == null ? defaultMainResource : result;
  }

  public static void setMainResource(ResourceBundle resource, String locale) throws InterruptedException, ApplicationException {
	  if (isRu(locale) ? mainResource == null : mainResourceEn == null) {
		  resourceBlockingQueue.offer(resource);
		  defaultMainResource = resource;
	  }
  }
  
  private static ResourceBundle getResource(String locale) throws ApplicationException {
	  return isRu(locale) ? mainResource : mainResourceEn;
  }
  
  private static boolean isRu(String locale) throws ApplicationException {
	  return !"en".equalsIgnoreCase(locale);
  }
}
