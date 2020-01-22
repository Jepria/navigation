package com.technology.jep.navigation.main.client;
 
import static com.technology.jep.navigation.main.client.NavigationClientConstant.CHANGEPASSWORD_MODULE_ID;
import static com.technology.jep.navigation.main.client.NavigationClientConstant.NAVIGATION_MODULE_ID;
import static com.technology.jep.navigation.main.client.NavigationClientConstant.navigationText;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.technology.jep.jepria.client.ModuleItem;
import com.technology.jep.jepria.client.async.LoadAsyncCallback;
import com.technology.jep.jepria.client.async.LoadPlainClientFactory;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.main.MainClientFactory;
import com.technology.jep.jepria.client.ui.main.MainClientFactoryImpl;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
import com.technology.jep.navigation.changepassword.client.ChangePasswordClientFactoryImpl;
import com.technology.jep.navigation.main.client.ui.main.NavigationMainModulePresenter;

public class NavigationClientFactoryImpl<E extends MainEventBus, S extends JepMainServiceAsync>
  extends MainClientFactoryImpl<E, S>
    implements MainClientFactory<E, S> {
    
  public static MainClientFactory<MainEventBus, JepMainServiceAsync> getInstance() {
    if(instance == null) {
      instance = GWT.create(NavigationClientFactoryImpl.class);
    }
    return instance;
  }
 
   private NavigationClientFactoryImpl() {
    super(
        new ModuleItem(NAVIGATION_MODULE_ID, navigationText.submodule_navigation_title()),
        new ModuleItem(CHANGEPASSWORD_MODULE_ID, navigationText.submodule_changepassword_title()));
 
    initActivityMappers(this);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Activity createMainModulePresenter() {
    return new NavigationMainModulePresenter(this);
  }
 
  public void getPlainClientFactory(String moduleId, final LoadAsyncCallback<PlainClientFactory<PlainEventBus, JepDataServiceAsync>> callback) {
    if(CHANGEPASSWORD_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(NavigationClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + CHANGEPASSWORD_MODULE_ID);
          return ChangePasswordClientFactoryImpl.getInstance();
        }
      });
    }
    else if(NAVIGATION_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(NavigationClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + NAVIGATION_MODULE_ID);
          return com.technology.jep.navigation.navigation.client.NavigationClientFactoryImpl.getInstance();
        }
      });
    }
  }
}
