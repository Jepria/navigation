package com.technology.jep.navigation.navigation.client.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.technology.jep.jepria.shared.field.option.JepOption;

public class JsonHelper {
  
  public static final String NULL = "null";
  public static final String VALUE = "value";
  
  /**
   * Преобразование JepOption в JSONObject.
   * @param option
   * @return
   */
  private static JSONObject convertOptionToJsonObject(JepOption option) {
    JSONObject result = new JSONObject();
    for(String key : option.keySet()) {
      Object value = option.get(key);
      if (value == null) value = NULL;
      result.put(key, new JSONString(value.toString()));
    }
    return result;
  }

  /**
   * Преобразование JSONObject в JepOption.
   * @param json
   * @return
   */
  private static JepOption convertJsonObjectToOption(JSONObject json) {
    JepOption result = new JepOption();
    for(String key : json.keySet()) {
      String value = json.get(key).isString().stringValue();
      result.put(key, key.equals(VALUE) ? Integer.parseInt(value) : (value.equals(NULL) ? null : value));
    }
    return result;
  }

  /**
   * Сериализация опции в JSON  
   * @param option 
   * @return jsonString если опция не пустая || пустую строку если входная опция null.
   */
  public static String convertJepOptionToJsonString(JepOption option) {
    if (option == null) return "";
    return convertOptionToJsonObject(option).toString();
  }

  
  /**
   * Десериализация JSON в список опцию
   * @return Опция || NullPointerException если входная строка пустая.
   */
  public static JepOption convertJsonStringToJepOption(String jsonString) {
    if (jsonString == null || jsonString.trim().length() == 0) throw new NullPointerException();
    JSONValue value = JSONParser.parseLenient(jsonString.trim());
    return convertJsonObjectToOption(value.isObject());
  }
  
  /**
   * Сериализация списка опций в JSON  
   * @return jsonString если список не пустой || пустую строку если входной список был пуст.
   */
  public static String convertOptionListToJsonString(List<JepOption> optionList) {
    if (optionList == null || optionList.size() == 0) return "";
    JSONArray result = new JSONArray();
    for (int i = 0; i < optionList.size(); i++) {
      result.set(i, convertOptionToJsonObject(optionList.get(i)));
    }
    return result.toString();
  }
  
  /**
   * Десериализация JSON в список опций
   * @return список опций || NullPointerException если входная строка пустая.
   */
  public static List<JepOption> convertJsonStringToOptionList(String jsonString) {
    if (jsonString == null || jsonString.trim().length() == 0) throw new NullPointerException();
    ArrayList<JepOption> result = new ArrayList<>();
    JSONValue value = JSONParser.parseLenient(jsonString.trim());
    JSONArray array = value.isArray();
    for (int i = 0; i < array.size(); i++) {
      result.add(convertJsonObjectToOption(array.get(i).isObject()));
    }
    return result;
  }
}
