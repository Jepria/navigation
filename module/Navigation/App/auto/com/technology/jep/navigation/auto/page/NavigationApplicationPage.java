package com.technology.jep.navigation.auto.page;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.GRID_GLASS_MASK_ID;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static com.technology.jep.jepria.auto.module.JepRiaModuleAutoImpl.waitForListMask;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.application.page.JepRiaApplicationPageImpl;
import com.technology.jep.jepria.auto.util.WebDriverFactory;

/**
 * Модуль Навигации отличается от стандартных модулей JepRia-приложения, <br/>
 * поэтому пришлось переопределить ensurePageLoaded, чтобы не было проверок 
 * EntrancePanel и д.р. 
 */
public class NavigationApplicationPage extends JepRiaApplicationPageImpl {

  static final String CHANGE_PASSWORD_ID = "navigation.changePassword";
  static final String LOGOUT_BUTTON_ID = "navigation.exit";
  
  @FindBy(id = LOGOUT_BUTTON_ID)
  private WebElement logoutButton;

  @FindBy(id = CHANGE_PASSWORD_ID)
  private WebElement changePasswordButton;
  
  @Override
  public void ensurePageLoaded() {
    getContent();
    getWait().until(presenceOfElementLocated(By.xpath("//body")));
    getWait().until(visibilityOfElementLocated(By.id(CHANGE_PASSWORD_ID)));
  }
  
  @Override
  public void clickLogoutButton() {
    getContent();
    getWait().until(presenceOfElementLocated(By.xpath("//body")));
    getWait().until(elementToBeClickable(logoutButton));
    
    waitForListMask();
    
    logoutButton.click();
  }
}
