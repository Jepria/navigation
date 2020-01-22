package com.technology.jep.navigation.navigation.client.util;

import static com.technology.jep.navigation.navigation.client.NavigationClientConstant.NAVIGATION_CHECKED_NODES_STORAGE_ID;
import static com.technology.jep.navigation.navigation.client.NavigationClientConstant.NAVIGATION_EXPANDED_NODES_STORAGE_ID;
import static com.technology.jep.navigation.navigation.client.NavigationClientConstant.NAVIGATION_OPENED_TABS_STORAGE_ID;
import static com.technology.jep.navigation.navigation.client.NavigationClientConstant.NAVIGATION_SELECTED_TAB_STORAGE_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.storage.client.Storage;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.field.option.JepParentOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

public class NavigationMenuState {
  
  private static List<JepOption> expandNodes;
  private static List<JepOption> selectedNodes;
  private static List<String> openedTabs;
  private static JepOption selectedTab;
  
  public static List<JepOption> getExpandNodes() {
    return expandNodes;
  }
  
  public static List<JepOption> getSelectedNodes() {
    return selectedNodes;
  }
  
  public static List<String> getOpenedTabs() {
    return openedTabs;
  }

  public static JepOption getSelectedTab() {
    return selectedTab;
  }
  
  public static void recoverState(){
    Storage sessionStorage = Storage.getSessionStorageIfSupported();
    final String expandedNodesJsonString = sessionStorage.getItem(NAVIGATION_EXPANDED_NODES_STORAGE_ID);
    final String checkedNodesJsonString = sessionStorage.getItem(NAVIGATION_CHECKED_NODES_STORAGE_ID);
    final String openedTabsJsonString = sessionStorage.getItem(NAVIGATION_OPENED_TABS_STORAGE_ID);
    final String selectedTabJsonString = sessionStorage.getItem(NAVIGATION_SELECTED_TAB_STORAGE_ID);
    if (!(JepRiaUtil.isEmpty(expandedNodesJsonString) && JepRiaUtil.isEmpty(checkedNodesJsonString))) {
      try {
        expandNodes = JsonHelper.convertJsonStringToOptionList(expandedNodesJsonString);
        openedTabs = new ArrayList<>(Arrays.asList(openedTabsJsonString.split("; ")));
        selectedNodes = JsonHelper.convertJsonStringToOptionList(checkedNodesJsonString);
        selectedTab = JsonHelper.convertJsonStringToJepOption(selectedTabJsonString);
      } catch (Exception e) {
        Log.trace("Error occured while recovering Navigation state from cookies!\n" + e.getLocalizedMessage() + "\n" + e.getStackTrace());
        return;
      }
      //Заменим JepOption на JepParentOption (Чтобы узлы в дереве развернулись корректно).
      expandNodes = expandNodes.stream().map(tempOption -> {
        JepParentOption parentOption = new JepParentOption();
        parentOption.setProperties(tempOption);
        return parentOption;
      }).collect(Collectors.toList());
    }
  }
  
  public static void saveState(List<JepOption> checkedNodes, List<JepOption> expandedNodes, String openedTabsUrls, JepOption currentTab) {
    Storage sessionStorage = Storage.getSessionStorageIfSupported();
    sessionStorage.setItem(NAVIGATION_CHECKED_NODES_STORAGE_ID, JsonHelper.convertOptionListToJsonString(checkedNodes));
    sessionStorage.setItem(NAVIGATION_EXPANDED_NODES_STORAGE_ID, JsonHelper.convertOptionListToJsonString(expandedNodes));
    sessionStorage.setItem(NAVIGATION_OPENED_TABS_STORAGE_ID, openedTabsUrls);
    sessionStorage.setItem(NAVIGATION_SELECTED_TAB_STORAGE_ID, JsonHelper.convertJepOptionToJsonString(currentTab));
  }
}
