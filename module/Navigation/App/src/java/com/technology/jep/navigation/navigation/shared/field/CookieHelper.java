package com.technology.jep.navigation.navigation.shared.field;

public class CookieHelper {
  /**
   * Разделитель в строке, хранящей информацию о характеристиках таблицы
   */
  private static final String CHARACTERISTIC_SEPARATOR = ";";
  
  /**
   * Возврат характеристик колонки в виде строки
   * 
   * @param locale				значение локали
   * @param isMultiTabs			признак поддержки многовкладочности
   * @param isRefreshTab		признак необходимости обновления модуля при открытии вкладки
   * 
   * @return      строковое представление характеристик колонок
   */
  public static String getApplicationSettingAsString(String locale, String isMultiTabs, String isRefreshTab, String isCacheState) {
    StringBuilder colCharacteristics = new StringBuilder();
    colCharacteristics.append(locale);
    colCharacteristics.append(CHARACTERISTIC_SEPARATOR);
    colCharacteristics.append(isMultiTabs);
    colCharacteristics.append(CHARACTERISTIC_SEPARATOR);
    colCharacteristics.append(isRefreshTab);
    colCharacteristics.append(CHARACTERISTIC_SEPARATOR);
    colCharacteristics.append(isCacheState);
    return colCharacteristics.toString();
  }
  
  /**
   * Метод извлечения данных о таблице данных из строки
   * 
   * @param cookieString      строка, хранящая информация по таблице данных
   * @return карта соответствий идентификатора колонки и ее характеристик
   */
  public static ApplicationSetting parseApplicationSetting(String cookieString) {
    if (cookieString != null) {
      String[] characteristics = cookieString.split(CHARACTERISTIC_SEPARATOR);
      if (characteristics.length >= 4) {
        String locale = characteristics[0];
        Boolean isMultiTabsMode = Boolean.valueOf(characteristics[1]);
        Boolean isRefreshTab = Boolean.valueOf(characteristics[2]);
        Boolean isCacheState = Boolean.valueOf(characteristics[3]);
        return new ApplicationSetting(locale, isMultiTabsMode, isRefreshTab, isCacheState);
      }
    }
    return new ApplicationSetting();
  }
}
