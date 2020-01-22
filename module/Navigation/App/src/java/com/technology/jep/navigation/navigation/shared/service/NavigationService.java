package com.technology.jep.navigation.navigation.shared.service;
 
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.field.option.JepParentOption;
import com.technology.jep.jepria.shared.service.data.JepDataService;

@RemoteServiceRelativePath("NavigationService")
public interface NavigationService extends JepDataService {
  Map<JepParentOption, List<JepOption>> getAllMenus() throws ApplicationException;
  void resetCache() throws ApplicationException;
  double getLetterWidth(String fontFamily, int fontWidth);
  boolean isFullAccess() throws ApplicationException;
}
