package com.technology.jep.navigation.changepassword.client;
 
import static com.technology.jep.navigation.main.client.NavigationClientConstant.CHANGEPASSWORD_MODULE_ID;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactoryImpl;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
import com.technology.jep.navigation.changepassword.client.ui.form.plain.ChangePasswordModulePresenter;
import com.technology.jep.navigation.changepassword.client.ui.form.plain.ChangePasswordModuleViewImpl;
import com.technology.jep.navigation.changepassword.shared.record.ChangePasswordRecordDefinition;
import com.technology.jep.navigation.changepassword.shared.service.ChangePasswordService;
import com.technology.jep.navigation.changepassword.shared.service.ChangePasswordServiceAsync;
 
public class ChangePasswordClientFactoryImpl<E extends PlainEventBus, S extends ChangePasswordServiceAsync>
  extends PlainClientFactoryImpl<E, S> {
 
  public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
 
  public ChangePasswordClientFactoryImpl() {
    super(ChangePasswordRecordDefinition.instance);
    initActivityMappers(this);
  }
 
  static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
    if(instance == null) {
      instance = GWT.create(ChangePasswordClientFactoryImpl.class);
    }
    return instance;
  }
 
  public JepPresenter createPlainModulePresenter(Place place) {
    return new ChangePasswordModulePresenter(CHANGEPASSWORD_MODULE_ID, place, this);
  }
   
  public IsWidget getModuleView() {
    if(moduleView == null) {
      moduleView = new ChangePasswordModuleViewImpl();
    }
    return moduleView;
  }
 
  public S getService() {
    if(dataService == null) {
      dataService = (S) GWT.create(ChangePasswordService.class);
    }
    return dataService;
  }
}