package com.technology.jep.navigation.navigation.server.dao;
 
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Document;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.server.security.JepSecurityModule;
import com.technology.jep.jepria.shared.field.option.JepOption;
 
public interface Navigation extends JepDataStandard {
  List<JepOption> getMenus(Document doc, ResourceBundle resource, List<String> roles, JepOption parentOption);
  
  boolean isExtMenuAvailable(Document doc, JepOption parentOption, List<String> roles);
  
  void resetInitialSeedToMenus();
  
  boolean isChangePassword(HttpSession session, JepSecurityModule securityModule);
  
  void clearRoleCache();
}
