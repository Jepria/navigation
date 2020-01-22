package com.technology.jep.navigation.navigation.client;
 
import static com.technology.jep.navigation.main.client.NavigationClientConstant.NAVIGATION_MODULE_ID;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactoryImpl;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
import com.technology.jep.navigation.navigation.client.ui.form.plain.NavigationModulePresenter;
import com.technology.jep.navigation.navigation.client.ui.form.plain.NavigationModuleViewImpl;
import com.technology.jep.navigation.navigation.shared.record.NavigationRecordDefinition;
import com.technology.jep.navigation.navigation.shared.service.NavigationService;
import com.technology.jep.navigation.navigation.shared.service.NavigationServiceAsync;
 
public class NavigationClientFactoryImpl<E extends PlainEventBus, S extends NavigationServiceAsync>
  extends PlainClientFactoryImpl<E, S> {
 
  public static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
 
  public NavigationClientFactoryImpl() {
    super(NavigationRecordDefinition.instance);
    initActivityMappers(this);
  }
 
  static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
    if(instance == null) {
      instance = GWT.create(NavigationClientFactoryImpl.class);
    }
    return instance;
  }
 
  public JepPresenter createPlainModulePresenter(Place place) {
    return new NavigationModulePresenter(NAVIGATION_MODULE_ID, place, this);
  }
   
  public IsWidget getModuleView() {
    if(moduleView == null) {
      moduleView = new NavigationModuleViewImpl();
    }
    return moduleView;
  }
 
  public S getService() {
    if(dataService == null) {
      dataService = (S) GWT.create(NavigationService.class);
    }
    return dataService;
  }
}