package com.technology.jep.navigation.changepassword.server.dao;
 
import javax.servlet.http.HttpServletRequest;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.server.security.JepSecurityModule;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.navigation.changepassword.shared.dto.PasswordDto;

public interface ChangePassword extends JepDataStandard {
  void changePassword(HttpServletRequest request, JepSecurityModule securityModule, PasswordDto password) throws ApplicationException;
}
