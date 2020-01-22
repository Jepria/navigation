package com.technology.jep.navigation.changepassword.shared.service;
 
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.service.data.JepDataService;
import com.technology.jep.navigation.changepassword.shared.dto.PasswordDto;

@RemoteServiceRelativePath("ChangePasswordService")
public interface ChangePasswordService extends JepDataService {
	String changePassword(PasswordDto password) throws ApplicationException;
}
