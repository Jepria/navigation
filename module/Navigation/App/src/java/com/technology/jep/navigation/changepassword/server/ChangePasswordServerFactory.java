package com.technology.jep.navigation.changepassword.server;

import static com.technology.jep.navigation.changepassword.server.ChangePasswordServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.navigation.changepassword.server.dao.ChangePassword;
import com.technology.jep.navigation.changepassword.server.dao.ChangePasswordDao;

public class ChangePasswordServerFactory extends ServerFactory<ChangePassword> {

  private ChangePasswordServerFactory() {
    super(new ChangePasswordDao(), DATA_SOURCE_JNDI_NAME);
  }

  public static final ChangePasswordServerFactory instance = new ChangePasswordServerFactory();

}
