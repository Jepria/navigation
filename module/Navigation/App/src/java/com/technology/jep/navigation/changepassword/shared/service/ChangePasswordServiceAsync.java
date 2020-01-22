package com.technology.jep.navigation.changepassword.shared.service;
 
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
import com.technology.jep.navigation.changepassword.shared.dto.PasswordDto;
 
public interface ChangePasswordServiceAsync extends JepDataServiceAsync {
  void changePassword(PasswordDto password, AsyncCallback<String> callback);
}
