package com.technology.jep.navigation.main.client.ui.main;
 
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.main.MainClientFactory;
import com.technology.jep.jepria.client.ui.main.MainModulePresenter;
import com.technology.jep.jepria.client.ui.main.MainView;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

public class NavigationMainModulePresenter<E extends MainEventBus, S extends JepMainServiceAsync, F extends MainClientFactory<E, S>> 
  extends MainModulePresenter<MainView, E, S, F> {
     
  public NavigationMainModulePresenter(F clientFactory) {
    super(clientFactory);
  }
  
}
