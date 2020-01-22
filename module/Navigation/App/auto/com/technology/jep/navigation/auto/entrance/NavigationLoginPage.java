package com.technology.jep.navigation.auto.entrance;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getDriver;
import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGIN_BUTTON_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGIN_PASSWORD_FIELD_ID;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.LOGIN_USERNAME_FIELD_ID;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.application.entrance.page.DefaultLoginPage;
import com.technology.jep.jepria.auto.condition.ExpectedConditions;
import com.technology.jep.jepria.auto.util.WebDriverFactory;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;

public class NavigationLoginPage extends DefaultLoginPage {

  private final String INNER_FRAME_NAME = "gwt-Frame";
  
  @FindBy(id = JepRiaAutomationConstant.LOGIN_USERNAME_FIELD_ID)
  private WebElement loginField;

  @FindBy(id = JepRiaAutomationConstant.LOGIN_PASSWORD_FIELD_ID)
  private WebElement pswdField;

  @FindBy(id = JepRiaAutomationConstant.LOGIN_BUTTON_ID)
  private WebElement loginButton;

  /* (non-Javadoc)
   * @see com.technology.jep.auto.entrance.pages.LoginPage#setUsername(java.lang.String)
   */
  @Override
  public void setUsername(String login) {
    switchToLoginIframe();
    loginField.sendKeys(login);
  }

  /* (non-Javadoc)
   * @see com.technology.jep.auto.entrance.pages.LoginPage#setPassword(java.lang.String)
   */
  @Override
  public void setPassword(String pswd) {
    switchToLoginIframe();
    pswdField.sendKeys(pswd);
  }

  /* (non-Javadoc)
   * @see com.technology.jep.auto.entrance.pages.LoginPage#doLogin()
   */
  @Override
  public void doLogin() {
    switchToLoginIframe();
    loginButton.click();
  }

  /* (non-Javadoc)
   * @see com.technology.jep.auto.entrance.pages.LoginPage#ensurePageLoaded()
   */
  @Override
  public void ensurePageLoaded() {
    switchToLoginIframe();
    getWait().until(ExpectedConditions.allElementsLocatedVisible(
        By.id(LOGIN_USERNAME_FIELD_ID),
        By.id(LOGIN_PASSWORD_FIELD_ID),
        By.id(LOGIN_BUTTON_ID)));
  }
  
  private void switchToLoginIframe() {
    getContent();
    getWait().until(presenceOfElementLocated(By.xpath("//body")));
    
    try {
      //После лоцирования фрейма может произойти ошибка при переключении в него.
      //Поэтому необходимо подождать полсекунды (значение подобрано имперически).
      Thread.sleep(500);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    getDriver().switchTo().frame(getWait().until(presenceOfElementLocated(By.className(INNER_FRAME_NAME))));
  }
}
