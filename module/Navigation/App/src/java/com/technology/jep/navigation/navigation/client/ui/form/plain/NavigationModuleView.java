package com.technology.jep.navigation.navigation.client.ui.form.plain;

import static com.technology.jep.navigation.navigation.client.NavigationClientConstant.navigationText;

import java.util.List;

import com.google.gwt.i18n.client.LocaleInfo;
import com.technology.jep.jepria.client.ui.plain.PlainModuleView;
import com.technology.jep.jepria.client.widget.field.multistate.JepMultiStateField;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.field.option.JepParentOption;
import com.technology.jep.navigation.navigation.client.widget.field.MenuTreeField;

public interface NavigationModuleView extends PlainModuleView {

  String LOCALE_PARAMETER_VALUE = LocaleInfo.getCurrentLocale().getLocaleName();
  
  JepParentOption settingCommonNode = new JepParentOption(navigationText.navigation_settings_common(), 1);
  JepOption settingTabsNode = new JepOption(navigationText.navigation_settings_multiTabs(), 2);
  JepOption settingResetCacheNode = new JepOption(navigationText.navigation_settings_resetCache(), 3);
  JepOption settingRefreshContentNode = new JepOption(navigationText.navigation_settings_refreshContent(), 4);
  JepOption settingCacheStateNode = new JepOption(navigationText.navigation_settings_cacheState(), 5);
      
  <W extends JepMultiStateField> W getTreeById(String id);
  void setVisibleWidget(String id);
  void openTab(JepOption selectOption);
  void openTab(JepOption selectOption, String url);
  void closeTab(JepOption option);
  void refreshAllTabs();
  void refreshCurrentTab();
  void switchOnSingleTabMode(boolean singleTabMode);
  boolean isAuthorized();
  void resetAuthorization();
  void authorizedBy(String userName);
  void redirect(JepOption option);
  void returnHome(String locale);
  void openNewWindow(JepOption option);
  boolean isMultiTabsMode();
  boolean isRefreshTabsMode();
  boolean isCacheStateMode();
  
  void forceToChangePassword();
  void restoreWelcomePage();
  
  void changeLocale(String locale);
  void changeMultiTabsMode(boolean isMultiTabsMode);
  void changeRefreshTabsMode(boolean isRefreshTab);
  void changeCacheStateMode(boolean isCacheState);
  
  boolean getMultiTabsModeByCookie();
  boolean getRefreshTabsModeByCookie();
  boolean getCacheStateModeByCookie();
  
  void setLetterWidth(double px);
  
  void layout();
  List<JepOption> getNavigationTreeExpandedNodes();
  List<JepOption> getNavigationCheckedNodes();
  String getOpenedTabsUrls();
  JepOption getCurrentTab();
  void selectTab(JepOption selectOption);
}
