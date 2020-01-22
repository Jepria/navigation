package com.technology.jep.navigation.navigation.shared.record;

import static com.technology.jep.jepria.shared.field.JepTypeEnum.*;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.*;

import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
 

import java.util.HashMap;
import java.util.Map;
 
public class NavigationRecordDefinition extends JepRecordDefinition {
 
  private static final long serialVersionUID = 1L;
 
  public static final NavigationRecordDefinition instance = new NavigationRecordDefinition();
 
  private NavigationRecordDefinition() {
    super(buildTypeMap()
      , new String[]{}
    );
  }
 
  private static Map<String, JepTypeEnum> buildTypeMap() {
    Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
    return typeMap;
  }
}
