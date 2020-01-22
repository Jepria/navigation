package com.technology.jep.navigation.changepassword.client.ui.form.plain;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.message.JepMessageBoxImpl;
import com.technology.jep.jepria.client.ui.eventbus.event.EnterModuleEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainModulePresenter;
import com.technology.jep.navigation.changepassword.shared.service.ChangePasswordServiceAsync;

public class ChangePasswordModulePresenter<V extends ChangePasswordModuleView, E extends PlainEventBus, S extends ChangePasswordServiceAsync, F extends PlainClientFactory<E, S>>
    extends PlainModulePresenter<V, E, S, F> {

  private S service = clientFactory.getService();
  
  public ChangePasswordModulePresenter(String moduleId, Place place, F clientFactory) {
    super(moduleId, place, clientFactory);
  }
  
  public void bind() {
    super.bind();
    
    view.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        view.clearError();
        if (view.isValid()) {
          service.changePassword(view.getPasswordInfo(), new JepAsyncCallback<String>() {
            @Override
            public void onSuccess(String username) {
              view.clearForm();
              callJSNIMethod("closeChangePasswordTab", username);
            }
            @Override
            public void onFailure(Throwable th){
              view.setError(JepMessageBoxImpl.instance.prepareFineMessage(th));
            }
          });
        }
      }
    });
    
    view.bindEnterClickListener();
  }

  /**
   * Установка главного виджета(-контейнера) приложения.<br/>
   * В методе используется вызов вида : <code>mainEventBus.setMainView(clientFactory.getMainClientFactory().getMainView());</code> <br/>
   * При этом, при передаче <code>null</code> в качестве главного виджета приложения, текущий главный виджет удаляется с RootPanel'и.<br/>
   * Т.о., перегрузкой данного метода можно установить, при заходе на модуль приложения, любой главный виджет приложения или скрыть текущий.
   */
  protected void setMainView() {
    Log.trace(this.getClass() + ".setMainView()");
    mainEventBus.setMainView(view.asWidget());
  }
  
  private native static void callJSNIMethod(String methodName, String arg) /*-{
    if ($wnd.parent && typeof $wnd.parent[methodName] === 'function') {
      $wnd.parent[methodName](arg);
    }
  }-*/;  
  
  /**
   * Переопределённый метод устанавливает фокус на поле Старый пароль.
   * @param event событие входа в модуль
   */
  @Override
  public void onEnterModule(EnterModuleEvent event) {
    super.onEnterModule(event);  
    view.setOldPasswordFieldFocused();
  }
}
