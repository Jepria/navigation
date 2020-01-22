package com.technology.jep.navigation.navigation.client.ui.form.plain;
 
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.navigation.changepassword.client.ChangePasswordClientConstant.changePasswordText;
import static com.technology.jep.navigation.navigation.client.NavigationClientConstant.navigationText;
import static com.technology.jep.navigation.navigation.client.ui.form.plain.NavigationModuleView.LOCALE_PARAMETER_VALUE;
import static com.technology.jep.navigation.navigation.client.ui.form.plain.NavigationModuleView.settingCacheStateNode;
import static com.technology.jep.navigation.navigation.client.ui.form.plain.NavigationModuleView.settingCommonNode;
import static com.technology.jep.navigation.navigation.client.ui.form.plain.NavigationModuleView.settingRefreshContentNode;
import static com.technology.jep.navigation.navigation.client.ui.form.plain.NavigationModuleView.settingResetCacheNode;
import static com.technology.jep.navigation.navigation.client.ui.form.plain.NavigationModuleView.settingTabsNode;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.FRAME;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.IMAGE;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.NAVIGATION_TREE_ID;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.SETTING_TREE_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.client.async.DataLoader;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.entrance.Entrance;
import com.technology.jep.jepria.client.ui.eventbus.event.EnterModuleEvent;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainModulePresenter;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.multistate.JepTreeField;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.field.option.JepParentOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.navigation.navigation.client.ui.form.plain.navigation.menu.CustomNavigationMenu;
import com.technology.jep.navigation.navigation.client.util.NavigationMenuState;
import com.technology.jep.navigation.navigation.client.widget.field.MenuTreeField;
import com.technology.jep.navigation.navigation.client.widget.field.SettingTreeField;
import com.technology.jep.navigation.navigation.client.widget.field.tree.NavigationTreeField;
import com.technology.jep.navigation.navigation.shared.service.NavigationServiceAsync;

public class NavigationModulePresenter<V 
  extends NavigationModuleView, E extends PlainEventBus, S extends NavigationServiceAsync, F extends PlainClientFactory<E, S>> 
    extends PlainModulePresenter<V, E, S, F> {
    
   private static final String REFRESH_TREE_LAYER_ID = "refreshLayerId";
   private static final String CONTENT_FRAME_ATTRIBUTE_VALUE = "content";
   private static final String NEW_WIN_FRAME_ATTRIBUTE_VALUE = "_blank";
   private static final String DEFAULT_FONT_NAME = "Tahoma";
   private static final int DEFAULT_FONT_SIZE = 11;
   private static Map<JepParentOption, List<JepOption>> ALL_NODES;
   private static List<JepOption> expandList;
   private static List<JepOption> checkedList;
   private static List<String> openTabList;
   private static boolean logout = false;
   
   private JepOption changePasswordOption;
   
   public NavigationModulePresenter(String moduleId, Place place, F clientFactory) {
    super(moduleId, place, clientFactory);
   }
   
   public void bind() {
    super.bind();
    
    service.getLetterWidth(DEFAULT_FONT_NAME, DEFAULT_FONT_SIZE, new JepAsyncCallback<Double>() {
      @Override
      public void onSuccess(Double result) {
        view.setLetterWidth(result);
      }
    });
    
    exportFunctions();
    
    view.changeLocale(LOCALE_PARAMETER_VALUE);
    
    // firstly the basic styles should be injected and after that custom styles implements here
    final SettingTreeField settingTreeField = view.<SettingTreeField>getTreeById(SETTING_TREE_ID);
    settingTreeField.getEditableCard().setLoader(new DataLoader<JepOption>() {
      @Override
      public void load(Object loadConfig, final AsyncCallback<List<JepOption>> asyncCallback) {
        if (loadConfig == null){
          final List<JepOption> result = new ArrayList<JepOption>(Arrays.asList(settingCommonNode));
          asyncCallback.onSuccess(result);
          Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
              settingTreeField.setExpanded(result);
            }
          });
        }
        else if (settingCommonNode.equals(loadConfig)){
          service.isFullAccess(new JepAsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean isResetCacheAvailable) {
              final List<JepOption> result = isResetCacheAvailable ? 
                  Arrays.asList(settingTabsNode, settingResetCacheNode, settingRefreshContentNode, settingCacheStateNode) :
                    Arrays.asList(settingTabsNode, settingRefreshContentNode, settingCacheStateNode); 
              asyncCallback.onSuccess(result);
              
              List<JepOption> values = new ArrayList<JepOption>();
              if (view.getMultiTabsModeByCookie()){
                values.add(settingTabsNode);
              }
              if (view.getRefreshTabsModeByCookie()){
                values.add(settingRefreshContentNode);
              }
              if (view.getCacheStateModeByCookie()){
                values.add(settingCacheStateNode);
              }
              settingTreeField.setValue(values);
            }
          });
        }
      }
    });
    settingTreeField.addListener(JepEventType.CHANGE_CHECK_EVENT, new JepListener() {
      @SuppressWarnings("unchecked")
      @Override
      public void handleEvent(JepEvent event) {
        CheckChangeEvent<JepOption> ev = (CheckChangeEvent<JepOption>) event.getParameter();
        JepOption selectOption = ev.getSelectedOption();
        view.layout();
        if (settingTabsNode.equals(selectOption)) {
          // because this event caused earlier than value will be toggled, we should invert it manually
          boolean isMultiTabsMode = !view.isMultiTabsMode();
          view.changeMultiTabsMode(isMultiTabsMode);
          view.switchOnSingleTabMode(!isMultiTabsMode);
        }
        else if (settingResetCacheNode.equals(selectOption)) {
          service.resetCache(new JepAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
              view.setVisibleWidget(NAVIGATION_TREE_ID);
              refreshMenuTree();
            }
          });
        }
        else if (settingRefreshContentNode.equals(selectOption)) {
          view.changeRefreshTabsMode(!view.isRefreshTabsMode());
        }
        else if (settingCacheStateNode.equals(selectOption)) {
          view.changeCacheStateMode(!view.isCacheStateMode());
        }
      }
    });
    
    final MenuTreeField navigationTreeField = view.<MenuTreeField>getTreeById(NAVIGATION_TREE_ID);
    navigationTreeField.setLoader(new DataLoader<JepOption>() {
      @Override
      public void load(final Object loadConfig, final AsyncCallback<List<JepOption>> asyncCallback) {
        getAllMenus(new JepAsyncCallback<Map<JepParentOption,List<JepOption>>>() {
          @Override
          public void onSuccess(Map<JepParentOption, List<JepOption>> result) {
            JepClientUtil.hideLoadingPanel(REFRESH_TREE_LAYER_ID);
            asyncCallback.onSuccess(result.get((JepParentOption) loadConfig));
            view.layout();
          }
        });
      }
    });
    
    navigationTreeField.addListener(JepEventType.CHANGE_CHECK_EVENT, new JepListener() {
      @SuppressWarnings("unchecked")
      @Override
      public void handleEvent(JepEvent event) {
        CheckChangeEvent<JepOption> ev = (CheckChangeEvent<JepOption>) event.getParameter();
        JepOption selectOption = ev.getSelectedOption();
        NavigationTreeField<JepOption> treeField = navigationTreeField.getEditableCard();
        if (!treeField.isLeaf(selectOption)) {
          treeField.setExpanded(selectOption, !treeField.isNodeOpened(selectOption));
          ev.setCancelled(true);
          return;
        }
        final CustomNavigationMenu menu = CustomNavigationMenu.getMenuByImageId((String) selectOption.get(IMAGE));
        String frame = (String) selectOption.get(FRAME);
        boolean isTabMenu = CONTENT_FRAME_ATTRIBUTE_VALUE.equalsIgnoreCase(frame);
        boolean isCancelled = false;
        switch(menu){
          case LANGUAGE : {
            view.returnHome(navigationText.navigation_language_switchTo());
            return;
          }
          case AUTHORIZATION :
          case EXIT : {
            logout();
            isCancelled = true;
            break;
          }
          case CHANGE_PASSWORD : {
            changePasswordOption = selectOption;
            view.openTab(selectOption);
            break;
          }
          case ORDINAL:
          default: {
            if (isTabMenu) {
              view.openTab(selectOption);
            }
            break;
          }
        }
        if (!isCancelled) {
          if (NEW_WIN_FRAME_ATTRIBUTE_VALUE.equalsIgnoreCase(frame)){
            view.openNewWindow(selectOption);
            isCancelled = true;
          }
          else if (!isTabMenu){
            view.redirect(selectOption);
          }
        }
        // Если опция уже выделена, то отменяем событие снятия выделения
        if (treeField.isSelected(selectOption) || isCancelled){
          ev.setCancelled(true);
          return;
        }
      }
    });
    /*
     * Добавление слушателя события закрытия страницы/обновления страницы/перехода на другую страницу, сохраняет список открытых модулей в SessionStorage.
     */
    Window.addWindowClosingHandler(new ClosingHandler() {
      @Override
      public void onWindowClosing(ClosingEvent event) {
        if (Storage.isSessionStorageSupported()) {
          Storage.getSessionStorageIfSupported().clear();
          if (!logout && view.isCacheStateMode()) {
            NavigationMenuState.saveState(view.getNavigationCheckedNodes(), view.getNavigationTreeExpandedNodes(), view.getOpenedTabsUrls(), view.getCurrentTab());
          } else {
            logout = false;
          }
        }
      }
    });
  }
   @Override
   public void onEnterModule(EnterModuleEvent event) {
     super.onEnterModule(event);
     if (view.getCacheStateModeByCookie() && Storage.isSessionStorageSupported()) {
       NavigationMenuState.recoverState();
       final MenuTreeField navigationTreeField = view.<MenuTreeField>getTreeById(NAVIGATION_TREE_ID);
       Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
         @Override
         public void execute() {
           expandList = NavigationMenuState.getExpandNodes();
           checkedList = NavigationMenuState.getSelectedNodes();
           if (expandList != null && expandList.size() > 0) {
             navigationTreeField.setExpandedAndSelected(expandList, checkedList);
             expandList = null;
           }
           openTabList = NavigationMenuState.getOpenedTabs();
           if (openTabList != null && openTabList.size() > 0) {
             for (int i = 0; i < checkedList.size(); i++) {
               view.openTab(checkedList.get(i), openTabList.get(i));
             }
             if (!JepRiaUtil.isEmpty(NavigationMenuState.getSelectedTab())) view.selectTab(NavigationMenuState.getSelectedTab());
             openTabList = null;
             checkedList = null;
           }
         }
       });
     } else {
       Log.trace(this.getClass() + " no saved Navigation state found");
     }
   }

   /**
    * Получение информации по всей структуре узлов дерева меню.
    * 
    * @param callback     колбэк, содержащий информацию о структуре меню
    */
  private void getAllMenus(final JepAsyncCallback<Map<JepParentOption, List<JepOption>>> callback) {
    if (ALL_NODES == null) { 
      service.getAllMenus(new JepAsyncCallback<Map<JepParentOption,List<JepOption>>>() {
        @Override
        public void onSuccess(Map<JepParentOption, List<JepOption>> result) {
        	callback.onSuccess(ALL_NODES = result);
        }
      });
    }
    else { 
      callback.onSuccess(ALL_NODES);
    }
  }
  
  
  /**
   * Установка главного виджета(-контейнера) приложения.
   * В методе используется вызов вида : <code>mainEventBus.setMainView(clientFactory.getMainClientFactory().getMainView());</code>
   * При этом, при передаче <code>null</code> в качестве главного виджета приложения, текущий главный виджет удаляется с RootPanel'и.
   * Т.о., перегрузкой данного метода можно установить, при заходе на модуль приложения, любой главный виджет приложения или скрыть текущий.
   */
  protected void setMainView() {
    Log.trace(this.getClass() + ".setMainView()");
    mainEventBus.setMainView(view.asWidget());
  }
  
  /**
   * Метод обновления дерева меню
   * 
   * @param userName			авторизованное имя пользователя
   */
  private void navigationTreeFieldRefresh(String userName) {
    if (!view.isAuthorized()) {
      view.authorizedBy(userName);
      refreshMenuTree();
      refreshSettingTree();
    }
  }

  /**
   * Перезагрузка меню навигации
   */
  private void refreshMenuTree() {
    JepClientUtil.showLoadingPanel(REFRESH_TREE_LAYER_ID, JepTexts.loadingPanel_dataLoading(), null);
    clearMenus();
    view.refreshAllTabs();
    view.<MenuTreeField>getTreeById(NAVIGATION_TREE_ID).getEditableCard().refresh();
  }
  
  /**
   * Перезагрузка дерева настроек
   */
  private void refreshSettingTree() {
    view.<SettingTreeField>getTreeById(SETTING_TREE_ID).getEditableCard().refresh();
  }

  /**
   * Метод закрытия вкладки смены пароля
   * 
   * @param userName		авторизованное имя пользователя
   */
  private void closeChangePasswordTab(String userName){
    if (changePasswordOption == null) {//принудительная смена пароля
      view.restoreWelcomePage();
      navigationTreeFieldRefresh(userName);
    }
    else {
      view.closeTab(changePasswordOption);
    }
    messageBox.alert(changePasswordText.changePassword_success());
  }
  
  /**
   * Экспорта метода возможного обновления дерева меню, доступного для "внешних" модулей, живущих в одном DOM
   */
  private native void exportFunctions() /*-{
    var that = this;
    $wnd.navigationTreeFieldRefresh = 
      $entry(function(username) {
          that.@com.technology.jep.navigation.navigation.client.ui.form.plain.NavigationModulePresenter::navigationTreeFieldRefresh(Ljava/lang/String;)(username);
      });
    $wnd.closeChangePasswordTab = 
      $entry(function(username) {
          that.@com.technology.jep.navigation.navigation.client.ui.form.plain.NavigationModulePresenter::closeChangePasswordTab(Ljava/lang/String;)(username);
      });
  }-*/;

  /**
   * Выход из системы
   */
  private void logout() {
    logout = true;
    Entrance.logout();
    // Если вышли из приложения по кнопке выход, при проверке прав произошла ошибка
    view.resetAuthorization();
    clearMenus();
  }
  
  /**
   * Сброс структуры дерева
   */
  private void clearMenus() {
    ALL_NODES = null;
  }
}
