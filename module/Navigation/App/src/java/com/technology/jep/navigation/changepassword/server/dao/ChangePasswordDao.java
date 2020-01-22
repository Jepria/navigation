package com.technology.jep.navigation.changepassword.server.dao;
 
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.technology.jep.jepria.server.dao.CallContext;
import com.technology.jep.jepria.server.dao.JepDao;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.server.security.JepSecurityModule;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;
import com.technology.jep.navigation.changepassword.shared.dto.PasswordDto;

public class ChangePasswordDao extends JepDao implements ChangePassword {

  private static final int QUERY_TIMEOUT_IN_SECONDS = 6;

@Override
  public List<JepRecord> find(JepRecord templateRecord, Mutable<Boolean> autoRefreshFlag, Integer maxRowCount, Integer operatorId) throws ApplicationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void delete(JepRecord record, Integer operatorId) throws ApplicationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void update(JepRecord record, Integer operatorId) throws ApplicationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Integer create(JepRecord record, Integer operatorId) throws ApplicationException {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void changePassword(HttpServletRequest request, JepSecurityModule securityModule, PasswordDto password) throws ApplicationException {
	try {
		String sqlQuery = 
	      " begin" 
	      + "  pkg_Operator.ChangePassword(" 
	        + " operatorId => ?" 
	        + ", password => ?" 
	        + ", newPassword => ?" 
	        + ", newPasswordConfirm => ?" 
	      + ");" 
	      + " end;";
		Db db = CallContext.getDb();
	    try {
	      CallableStatement callableStatement = db.prepare(sqlQuery);
	      callableStatement.setQueryTimeout(QUERY_TIMEOUT_IN_SECONDS);
	      
	      Integer operatorId = securityModule.getOperatorId();
	      
	      if(operatorId != null) callableStatement.setInt(1, operatorId.intValue());
	      else callableStatement.setNull(1, Types.INTEGER);

	      if(password != null) callableStatement.setString(2, password.getPassword());
	      else callableStatement.setNull(2, Types.VARCHAR);

	      if(password.getNewPassword() != null) callableStatement.setString(3, password.getNewPassword());
	      else callableStatement.setNull(3, Types.VARCHAR);

	      if(password.getNewPasswordAgain() != null) callableStatement.setString(4, password.getNewPasswordAgain());
	      else callableStatement.setNull(4, Types.VARCHAR);

	      callableStatement.execute();

	    } finally {
	      db.closeStatement(sqlQuery);
	    }
	} catch (SQLException e) {
		throw new ApplicationException(e.getLocalizedMessage(), e);
	}
  }
}
