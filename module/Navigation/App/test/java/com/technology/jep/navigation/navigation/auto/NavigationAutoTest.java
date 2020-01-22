package com.technology.jep.navigation.navigation.auto;

import static com.technology.jep.navigation.main.client.NavigationClientConstant.NAVIGATION_MODULE_ID;
import static com.technology.jep.navigation.navigation.auto.NavigationAutoTestConstant.ENTRANCE_URL_NAVIGATION_MODULE;

import org.testng.Reporter;
import org.testng.annotations.Test;

import com.technology.jep.jepria.auto.application.entrance.EntranceAutoImpl;
import com.technology.jep.jepria.auto.application.entrance.page.LoginPage;
import com.technology.jep.jepria.auto.application.page.JepRiaApplicationPage;
import com.technology.jep.jepria.auto.model.module.ModuleDescription;
import com.technology.jep.jepria.auto.test.JepRiaApplicationAutoTest;
import com.technology.jep.navigation.auto.NavigationAutoImpl;
import com.technology.jep.navigation.auto.entrance.NavigationLoginPage;
import com.technology.jep.navigation.auto.page.NavigationApplicationPage;
import com.technology.jep.test.util.DataProviderArguments;
import com.technology.jep.test.util.JepFileDataProvider;

public class NavigationAutoTest extends JepRiaApplicationAutoTest<NavigationAutoImpl>{

  @Override
  protected void createEntranceAuto() {
    entranceAuto = new EntranceAutoImpl() {
      
      @Override
      protected JepRiaApplicationPage getApplicationPage() {
        if(applicationPage == null) {
          applicationPage = new NavigationApplicationPage();
        }
        return applicationPage;
      }
      
      @Override
      protected LoginPage getLoginPage() {
        if(loginPage == null) {
          loginPage = new NavigationLoginPage();
        }
        
        return loginPage;
      }
    };
  };
  
  @Test(priority = 1, description="Тест меню.")
  public void testJepRiaShowcaseMenus() {

    NavigationMenuAuto navigationMenu = this.<NavigationMenuAuto>getCut();
    navigationMenu.openJepRiaShowcaseFolder();
    navigationMenu.openStartPageMenu();
    navigationMenu.openFeatureMenu();
    navigationMenu.openSupplierMenu();
  }

  @DataProviderArguments("filePath=test/resources/com/technology/jep/navigation/navigation/auto/auth.data")
  @Test(priority = 2,dataProviderClass = JepFileDataProvider.class, dataProvider="dataFromFile",
      description = "Тест авторизации.")
  public void testAuth(String maxCount) {
    
    int imaxCount = Integer.parseInt(maxCount);
    for(int i = 0; i<imaxCount; i++) {
      
      loginDefault();
      Reporter.log("Succes auth #"+i, true);
      //проверка, то логин успешный в ensurePageLoaded
    }
  }
  
  /**
   * Описание модуля Navigation.
   */
  protected ModuleDescription<NavigationMenuAuto> navigationMenu = null;
  
  
  @Override
  protected void beforeTestLaunch() {
    
    if(navigationMenu == null) {
      navigationMenu = new ModuleDescription<NavigationMenuAuto>(NAVIGATION_MODULE_ID, 
          ENTRANCE_URL_NAVIGATION_MODULE,
          null,
          applicationAuto.getNavigationModuleAuto(true));
    }
    
    enterModule(navigationMenu);
    if(!isLoggedIn()) loginDefault();
  }

  @Override
  protected NavigationAutoImpl provideAutomationManager(String baseUrl, String browserName, String browserVersion,
      String browserPlatform, String browserPath, String driverPath, String jepriaVersion, String username,
      String password, String dbURL, String dbUser, String dbPassword) {
    return new NavigationAutoImpl(baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, username, password, dbURL, dbUser, dbPassword);
  }
}
