package com.technology.jep.navigation.changepassword.client.ui.form.plain;

import static com.technology.jep.navigation.changepassword.client.ChangePasswordClientConstant.changePasswordText;
import static com.technology.jep.navigation.navigation.client.NavigationClientConstant.navigationText;
import static com.technology.jep.navigation.navigation.client.ui.form.plain.NavigationModuleViewImpl.$100_PERCENT;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.plain.PlainModuleViewImpl;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.navigation.changepassword.shared.dto.PasswordDto;

public class ChangePasswordModuleViewImpl extends PlainModuleViewImpl 
  implements ChangePasswordModuleView {
  
  private static final int MIN_LENGTH = 8;
  private final static int PADDING_TOP = 20;
  private final static int LABEL_WIDTH = 160;
  private final static int LABEL_HEIGHT = 14;
  private final static int FIELD_WIDTH = 220;
  private final static int BUTTON_WIDTH = 75;
  
  private VerticalPanel passwordsPanel;
  private Label errorLabel;
  
  private JepTextField passwordTextField, passwordNewTextField, passwordNewAgainTextField;
  
  private Button changePasswordButton;
  
  public ChangePasswordModuleViewImpl() {

    passwordsPanel = new VerticalPanel();
    passwordsPanel.setWidth($100_PERCENT);
    setWidget(passwordsPanel);
    
    HTML changePasswordHtml = new HTML(navigationText.changePasswordEdit_title());
    passwordsPanel.add(changePasswordHtml);
    String labelHeight = LABEL_HEIGHT + Unit.PX.getType(),
        fieldWidth = FIELD_WIDTH + Unit.PX.getType(),
            changePasswordButtonWidth = BUTTON_WIDTH + Unit.PX.getType();
    changePasswordHtml.setHeight(labelHeight);
    Style style = changePasswordHtml.getElement().getStyle();
    style.setColor("navy");
    style.setFontSize(12, Unit.PX);
    style.setFontWeight(FontWeight.BOLD);
    style.setProperty("borderBottom", "1px solid navy");
    passwordsPanel.setCellHorizontalAlignment(changePasswordHtml, HasHorizontalAlignment.ALIGN_CENTER);
    
    errorLabel = new Label();
    passwordsPanel.add(errorLabel);
    errorLabel.setHeight(labelHeight);
    errorLabel.getElement().getStyle().setColor("red");
    errorLabel.getElement().getStyle().setFontSize(10, Unit.PX);
    passwordsPanel.setCellHorizontalAlignment(errorLabel, HasHorizontalAlignment.ALIGN_CENTER);
    
    HorizontalPanel passwordPanel = new HorizontalPanel();
    passwordPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    HorizontalPanel newPasswordPanel = new HorizontalPanel();
    newPasswordPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    HorizontalPanel newPasswordAgainPanel = new HorizontalPanel();
    newPasswordAgainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    
    passwordTextField = new JepTextField(navigationText.changePasswordEdit_password()){
      /**
       * {@inheritDoc}
       */
      @Override
      protected void addEditableCard() {
        editableCard = new PasswordTextBox();
        editablePanel.add(editableCard);
      }
    };
    passwordPanel.add(passwordTextField);
    passwordTextField.setLabelWidth(LABEL_WIDTH);
    passwordTextField.setAllowBlank(false);
    
    FocusHandler focusHandler = new FocusHandler() {
      @Override
      public void onFocus(FocusEvent event) {
        changePasswordButton.setEnabled(true);
      }
    };
    
    PasswordTextBox passwordTextBox = (PasswordTextBox) passwordTextField.getEditableCard();
    passwordTextBox.addFocusHandler(focusHandler);
    passwordTextBox.setWidth(fieldWidth);
    
    passwordNewTextField = new JepTextField(navigationText.changePasswordEdit_newPassword()){
      /**
       * {@inheritDoc}
       */
      @Override
      protected void addEditableCard() {
        editableCard = new PasswordTextBox();
        editablePanel.add(editableCard);
      }
    };
    newPasswordPanel.add(passwordNewTextField);
    passwordNewTextField.setLabelWidth(LABEL_WIDTH);
    passwordNewTextField.setAllowBlank(false);
    passwordTextBox = (PasswordTextBox) passwordNewTextField.getEditableCard();
    passwordTextBox.addFocusHandler(focusHandler);
    passwordTextBox.setWidth(fieldWidth);
    
    passwordNewAgainTextField = new JepTextField(navigationText.changePasswordEdit_newPasswordConfirm()){
      /**
       * {@inheritDoc}
       */
      @Override
      protected void addEditableCard() {
        editableCard = new PasswordTextBox();
        editablePanel.add(editableCard);
      }
    };
    newPasswordAgainPanel.add(passwordNewAgainTextField);
    passwordNewAgainTextField.setLabelWidth(LABEL_WIDTH);
    passwordNewAgainTextField.setAllowBlank(false);
    passwordTextBox = (PasswordTextBox) passwordNewAgainTextField.getEditableCard();
    passwordTextBox.addFocusHandler(focusHandler);
    passwordTextBox.setWidth(fieldWidth);
    
    changePasswordButton = new Button(navigationText.changePasswordEdit_button());
    changePasswordButton.setTitle(navigationText.changePasswordEdit_button());
    changePasswordButton.setWidth(changePasswordButtonWidth);
    
    HorizontalPanel whitePlacePanel = new HorizontalPanel();
    whitePlacePanel.setHeight(PADDING_TOP + Unit.PX.getType());
    passwordsPanel.add(whitePlacePanel);
    
    passwordsPanel.add(passwordPanel);
    passwordsPanel.setCellHorizontalAlignment(passwordPanel, HasHorizontalAlignment.ALIGN_CENTER);
    passwordsPanel.add(newPasswordPanel);
    passwordsPanel.setCellHorizontalAlignment(newPasswordPanel, HasHorizontalAlignment.ALIGN_CENTER);
    passwordsPanel.add(newPasswordAgainPanel);
    passwordsPanel.setCellHorizontalAlignment(newPasswordAgainPanel, HasHorizontalAlignment.ALIGN_CENTER);
    passwordsPanel.add(changePasswordButton);
    passwordsPanel.setCellHorizontalAlignment(changePasswordButton, HasHorizontalAlignment.ALIGN_CENTER);
  }

  @Override
  public boolean isValid(){
    boolean isValid = passwordTextField.isValid() & passwordNewTextField.isValid() & passwordNewAgainTextField.isValid();
    String newPassword = passwordNewTextField.getValue(), newPasswordAgain = passwordNewAgainTextField.getValue();
    boolean isTheSame = JepRiaUtil.equalWithNull(newPassword, newPasswordAgain);
    boolean isMinLengthSatisfied = passwordTextField.isValid() && newPassword.length() >= MIN_LENGTH;
    if (!isTheSame){
      passwordNewTextField.markInvalid(changePasswordText.changePassword_newPassword_error());
    }
    else if (!isMinLengthSatisfied) {
    	passwordNewTextField.markInvalid(JepClientUtil.substitute(changePasswordText.changePassword_newPassword_minLengthError(), MIN_LENGTH));
    }
    return isValid & isTheSame & isMinLengthSatisfied;
  }
  
  @Override
  public void addClickHandler(ClickHandler clickHandler){
    changePasswordButton.addClickHandler(clickHandler);
  }
  
  @Override
  public PasswordDto getPasswordInfo() {
    return new PasswordDto(passwordTextField.getValue(), passwordNewTextField.getValue(), passwordNewAgainTextField.getValue());
  }
  
  @Override
  public void clearForm() {
    passwordTextField.clear();
    passwordNewTextField.clear();
    passwordNewAgainTextField.clear();
  }
  
  @Override
  public void clearError() {
    setError(null);
  }
  
  @Override
  public void setError(String error){
    errorLabel.setText(error);
  }
  
  @Override
  public void bindEnterClickListener() {
    RootPanel.get().addDomHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
        // Для формы поиска очевидной на данное событие является реакция - поиск информации.
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          event.stopPropagation();
          event.preventDefault();
          changePasswordButton.click();
        }
      }
    }, KeyDownEvent.getType());
  }

  @Override
  public void setOldPasswordFieldFocused() {
    passwordTextField.getEditableCard().setFocus(true);
  }
}
