package com.technology.jep.navigation.auto;

import com.technology.jep.jepria.auto.application.JepRiaApplicationAuto;
import com.technology.jep.navigation.navigation.auto.NavigationMenuAuto;
import com.technology.jep.navigation.navigation.auto.NavigationMenuAutoImpl;

public class NavigationAutoImpl extends JepRiaApplicationAuto implements NavigationAuto {

  private NavigationMenuAuto navigationMenuAuto;
  
  public NavigationAutoImpl(String baseUrl, String browserName, String browserVersion, String browserPlatform,
      String browserPath, String driverPath, String jepriaVersion, String username, String password, String dbURL,
      String dbUser, String dbPassword) {
    super(baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, username,
        password, dbURL, dbUser, dbPassword);
  }

  @Override
  public NavigationMenuAuto getNavigationModuleAuto(boolean newIntsance) {
    if(navigationMenuAuto == null || newIntsance) {
      navigationMenuAuto = new NavigationMenuAutoImpl();
    }
    return navigationMenuAuto;
  }

}
