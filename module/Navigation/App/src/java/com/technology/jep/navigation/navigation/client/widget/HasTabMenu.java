package com.technology.jep.navigation.navigation.client.widget;

import java.util.Collection;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.shared.field.option.JepOption;

/**
 * Интерфейс виджета, дочерние элементы которого являются вкладками.
 */
public interface HasTabMenu extends IsWidget {
  void refreshAllTabs();
  int getSelectedIndex();
  void initScrollButtonPanel();
  void setLetterWidth(double px);
  boolean addTab(JepOption option);
  void selectTab(JepOption option);  
  JepOption indexOf(int selectIndex);
  void changeStartOption(String url);
  boolean closeTab(JepOption option);
  void checkIfScrollButtonsNecessary();
  Collection<JepOption> getOpenedTabs();
  void refresh(JepOption rightFrameOption);
  <X extends Widget> X getRightFrameWidget(JepOption option);
  PopupPanel openContextMenu(JepOption option, UIObject target, NativeEvent event);
  void setTabUrl(JepOption option, String url);
}
