package com.technology.jep.navigation.main.client.entrance;
 
import com.technology.jep.jepria.client.entrance.JepEntryPoint;
import com.technology.jep.navigation.main.client.NavigationClientFactoryImpl;

public class NavigationEntryPoint extends JepEntryPoint {
    
  NavigationEntryPoint() {
    super(NavigationClientFactoryImpl.getInstance());
  }
}
