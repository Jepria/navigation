package com.technology.jep.navigation.navigation.shared.service;
 
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.field.option.JepParentOption;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
 
public interface NavigationServiceAsync extends JepDataServiceAsync {
  void getAllMenus(AsyncCallback<Map<JepParentOption, List<JepOption>>> callback);
  void resetCache(AsyncCallback<Void> callback);
  void getLetterWidth(String fontFamily, int fontWidth, AsyncCallback<Double> callback);
  void isFullAccess(AsyncCallback<Boolean> callback);
}
