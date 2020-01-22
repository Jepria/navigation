package com.technology.jep.navigation.navigation.auto;

import org.openqa.selenium.TimeoutException;
import org.testng.Reporter;

import com.technology.jep.jepria.auto.module.JepRiaModuleAutoImpl;
import com.technology.jep.navigation.navigation.auto.page.NavigationMenuPage;

public class NavigationMenuAutoImpl extends JepRiaModuleAutoImpl<NavigationMenuPage>
    implements NavigationMenuAuto {

  public NavigationMenuAutoImpl() {
    super(new NavigationMenuPage());
  }
  
  @Override
  public void openJepRiaShowcaseFolder() {
    final String menuId = "navigation.jepriashowcase";
    page.ensureMenuLoaded(menuId, true)
          .ensureNavigationTreeLoaded() 
            .clickOnFolder(menuId);
  }


  @Override
  public void openStartPageMenu() {
    final String menuId = "navigation.jepriashowcase.welcome";
    page.ensureMenuLoaded(menuId, false)
          .clickOnMenu(menuId);
    
  }


  @Override
  public void openFeatureMenu() {
    
    final String featureId = "navigation.jepriashowcase.feature";
    try{
      page.ensureMenuLoaded(featureId, false)
        .clickOnMenu(featureId);
    }catch(TimeoutException e){
      
      Reporter.log(featureId + " not found. Trying open NavigationJro.", true);
      openFeatureMenuInJro();
    }
  }

  private void openFeatureMenuInJro() {
    
    final String featureRequestId = "navigation.jepriashowcase.featureRequest";
    page.ensureMenuLoaded(featureRequestId, true)
      .ensureNavigationTreeLoaded() 
        .clickOnFolder(featureRequestId);
    
    final String featureId = "navigation.jepriashowcase.featureRequest.feature";
    page.ensureMenuLoaded(featureId, false)
      .clickOnMenu(featureId);
  }

  @Override
  public void openSupplierMenu() {
    final String menuId = "navigation.jepriashowcase.supplier";
    page.ensureMenuLoaded(menuId, false)
          .clickOnMenu(menuId);
  }
}
