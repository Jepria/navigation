package com.technology.jep.navigation.changepassword.shared.record;

import static com.technology.jep.jepria.shared.field.JepTypeEnum.*;
import static com.technology.jep.navigation.changepassword.shared.field.ChangePasswordFieldNames.*;

import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
 

import java.util.HashMap;
import java.util.Map;
 
public class ChangePasswordRecordDefinition extends JepRecordDefinition {
 
  private static final long serialVersionUID = 1L;
 
  public static final ChangePasswordRecordDefinition instance = new ChangePasswordRecordDefinition();
 
  private ChangePasswordRecordDefinition() {
    super(buildTypeMap()
      , new String[]{}
    );
  }
 
  private static Map<String, JepTypeEnum> buildTypeMap() {
    Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
    return typeMap;
  }
}
