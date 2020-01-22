package com.technology.jep.navigation.changepassword.shared.dto;

import java.io.Serializable;

public class PasswordDto implements Serializable {

  private String password;
  private String newPassword;
  private String newPasswordAgain;
  
  private PasswordDto(){}
  
  public PasswordDto(String password, String newPassword, String newPasswordAgain) {
    this.password = password;
    this.newPassword = newPassword;
    this.newPasswordAgain = newPasswordAgain;
  }
  public String getPassword() {
    return password;
  }
  public String getNewPassword() {
    return newPassword;
  }
  public String getNewPasswordAgain() {
    return newPasswordAgain;
  }
}
