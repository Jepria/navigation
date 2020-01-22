package com.technology.jep.navigation.changepassword.server.service;

import com.technology.jep.navigation.navigation.server.Util;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.security.JepSecurityModule;
import com.technology.jep.jepria.server.security.SecurityFactory;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.navigation.changepassword.server.ChangePasswordServerFactory;
import com.technology.jep.navigation.changepassword.server.dao.ChangePassword;
import com.technology.jep.navigation.changepassword.shared.dto.PasswordDto;
import com.technology.jep.navigation.changepassword.shared.record.ChangePasswordRecordDefinition;
import com.technology.jep.navigation.changepassword.shared.service.ChangePasswordService;
 
@RemoteServiceRelativePath("ChangePasswordService")
public class ChangePasswordServiceImpl extends JepDataServiceServlet<ChangePassword> implements ChangePasswordService  {
 
  private static final long serialVersionUID = 1L;
 
  public ChangePasswordServiceImpl() {
    super(ChangePasswordRecordDefinition.instance, ChangePasswordServerFactory.instance);
  }
  
  public String changePassword(PasswordDto password) throws ApplicationException {
	JepSecurityModule securityModule = Util.getSecurityModule(getThreadLocalRequest());
    dao.changePassword(getThreadLocalRequest(), securityModule, password);
    return securityModule.getUsername();
  }
}
