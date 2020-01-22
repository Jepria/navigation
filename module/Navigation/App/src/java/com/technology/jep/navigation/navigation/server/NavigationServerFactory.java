package com.technology.jep.navigation.navigation.server;

import static com.technology.jep.navigation.navigation.server.NavigationServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.navigation.navigation.server.dao.Navigation;
import com.technology.jep.navigation.navigation.server.dao.NavigationDao;

public class NavigationServerFactory extends ServerFactory<Navigation> {

  private NavigationServerFactory() {
    super(new NavigationDao(), DATA_SOURCE_JNDI_NAME);
  }

  public static final NavigationServerFactory instance = new NavigationServerFactory();

}
