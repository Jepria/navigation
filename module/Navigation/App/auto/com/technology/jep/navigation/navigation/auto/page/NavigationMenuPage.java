package com.technology.jep.navigation.navigation.auto.page;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getDriver;
import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGGED_IN_USER_ID;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import com.technology.jep.jepria.auto.module.page.JepRiaModulePage;
import com.technology.jep.jepria.auto.page.PlainPage;
import com.technology.jep.jepria.auto.util.WebDriverFactory;

public class NavigationMenuPage
  extends JepRiaModulePage {

  private final static String FOLDER_PREFIX = "f_";

  private static Logger logger = Logger.getLogger(NavigationMenuPage.class.getName());
  
  public String getLoggedInUsername() {
    return getDriver().findElement(By.id(LOGGED_IN_USER_ID)).getText();
  }
  
  public void clickLogoutButton() {
    getDriver().findElement(By.id("navigation.exit")).click();
  }
  
  public NavigationMenuPage clickOnMenu(String resourceName){
    getDriver().findElement(By.id(resourceName)).click();
    return this;
  }
  
  public NavigationMenuPage clickOnFolder(String resourceName){
    getDriver().findElement(By.id(FOLDER_PREFIX  + resourceName)).click();
    return this;
  }
  
  public NavigationMenuPage ensureMenuLoaded(String resourceName, boolean isFolder){
    getWait().until(presenceOfElementLocated(By.id((isFolder ? FOLDER_PREFIX : "") + resourceName)));
    return this;
  }
  
  public NavigationMenuPage ensureNavigationTreeLoaded(){
    getWait().until(invisibilityOfElementLocated(By.className("jepRia-disabledLayer")));
    return this;
  }
}
