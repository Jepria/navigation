package com.technology.jep.navigation.navigation.shared.exceptions;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;

public class ResourcePropertiesNotFoundException extends ApplicationException {

  public ResourcePropertiesNotFoundException() {
  }
  
  public ResourcePropertiesNotFoundException(String message) {
    super(message, new Throwable(message));
  }
}
