package com.technology.jep.navigation.navigation.shared.exceptions;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;

public class SessionExpiredException extends ApplicationException {

  public SessionExpiredException() {
  }
  
  public SessionExpiredException(String message, Throwable cause) {
    super(message, cause);
  }
}
