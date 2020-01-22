package com.technology.jep.navigation.navigation.shared.field;

/**
 * Класс для хранения характеристик приложения (информации о текущей локали и многовкладочного механизма).
 */
public class ApplicationSetting {
  
  //locale will be calculated on client side and afterwards transfer to server side
  public String locale;
  //multi-tabs mode define by settings or can be define from cookie
  public boolean isMultiTabsMode = true;
  //refresh-tabs mode define by settings or can be define from cookie
  public boolean isRefreshTabMode = false;
  //cache-state mode define by settings or can be define from cookie
  public boolean isCacheState = true;
  
  public ApplicationSetting(){}
  
  public ApplicationSetting(String locale, boolean isMultiTabsMode, boolean isRefreshTabMode, boolean isCacheState) {
    this.locale = locale;
    this.isMultiTabsMode = isMultiTabsMode;
    this.isRefreshTabMode = isRefreshTabMode;
    this.isCacheState = isCacheState;
  }
}
