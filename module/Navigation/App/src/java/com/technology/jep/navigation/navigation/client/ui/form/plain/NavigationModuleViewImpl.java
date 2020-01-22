package com.technology.jep.navigation.navigation.client.ui.form.plain;
 
import static com.technology.jep.jepria.client.JepRiaClientConstant.ENTRY_MODULE_NAME_REQUEST_PARAMETER;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;
import static com.technology.jep.jepria.shared.JepRiaConstant.HTTP_REQUEST_PARAMETER_LOCALE;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.MODULE_PANEL_ID;
import static com.technology.jep.navigation.main.client.NavigationClientConstant.CHANGEPASSWORD_MODULE_ID;
import static com.technology.jep.navigation.navigation.client.NavigationClientConstant.navigationText;
import static com.technology.jep.navigation.navigation.client.ui.form.plain.navigation.NavigationTreeFieldResources.INSTANCE;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.FRAME;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.LOCALE_AND_MULTITABS_MODE_COOKIE_NAME;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.NAVIGATION_TREE_ID;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.REQUEST_URL;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.SETTING_TREE_ID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.*;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.security.ClientSecurity;
import com.technology.jep.jepria.client.ui.plain.PlainModuleViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepMultiStateField;
import com.technology.jep.jepria.client.widget.field.tree.TreeNodeInfo;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.navigation.navigation.client.widget.HasTabMenu;
import com.technology.jep.navigation.navigation.client.widget.MenuTabLayoutPanel;
import com.technology.jep.navigation.navigation.client.widget.field.MenuTreeField;
import com.technology.jep.navigation.navigation.client.widget.field.SettingTreeField;
import com.technology.jep.navigation.navigation.client.widget.field.tree.NavigationTreeField;
import com.technology.jep.navigation.navigation.shared.field.CookieHelper;

public class NavigationModuleViewImpl extends PlainModuleViewImpl
   implements NavigationModuleView {

   private static final int TAB_HEIGHT = 22;
   private static final int LEFT_FRAME_WIDTH = 200;
   private static final int SLIDE_BUTTON_WIDTH = 16;//15;
   private static final int SLIDE_BUTTON_HEIGHT = 16;//10;
   private static final int MINIMIZE_LEFT_FRAME_WIDTH = 1;//SLIDE_BUTTON_WIDTH + 10;
   private static final int FRAME_SPLITTER_WIDTH = 2;
   private static final int STACK_MENU_HEIGHT = TAB_HEIGHT;
   
   
   public static final String $100_PERCENT = "100%";
   private static final String RIGHT_FRAME_ID = "RightTabFrame";
   private static final String WELCOME_PAGE_URL = "/Navigation/Welcome.jsp?" + HTTP_REQUEST_PARAMETER_LOCALE + "=" + LOCALE_PARAMETER_VALUE;
  
   private StackLayoutPanel leftStackFrame = new StackLayoutPanel(Unit.PX);
   private DeckPanel rightPanel = new DeckPanel();
   private SimplePanel rightSingleTabPanel;
   private SplitLayoutPanel mainWidgetContainer = null;
   
   private HeaderPanel leftNavigationFrame = new HeaderPanel();
   private HTML navigationHtml = new HTML(navigationText.navigation_title());
   private HTML settingsHtml = new HTML(navigationText.navigation_settings());
   
   private JepOption startPageOption, lastRightTabOption = null;
   
   private HandlerRegistration windowResizeHandler;
   
   {
     startPageOption = new JepOption(navigationText.welcome_title(), 0);
     startPageOption.set(REQUEST_URL, WELCOME_PAGE_URL);
   }
   
   private HasTabMenu rightTabFrame = new MenuTabLayoutPanel(startPageOption, RIGHT_FRAME_ID, TAB_HEIGHT, Unit.PX){
     @Override
     public boolean closeTab(final JepOption option) {
      boolean closed = super.closeTab(option);
      if (closed){
        navigationTree.getEditableCard().setChecked(option, false);
      }
      return closed;
     }
   };
   
   @SuppressWarnings("unchecked")
   private MenuTreeField navigationTree = new MenuTreeField(){
     @Override
     public void openContextMenu(JepOption option, UIObject target, NativeEvent event){
       rightTabFrame.openContextMenu(option, target, event);
     }
     @Override
     public void openNewWindow(JepOption option) {
       NavigationModuleViewImpl.this.openNewWindow(option);
     }
   };
   
   private SettingTreeField settingTree = new SettingTreeField();
   
   private boolean formInitialized = false;
   
   private int previousWidthLeftPanel = LEFT_FRAME_WIDTH;
   
   private Image showNavigationPanelIcon;
   
   public NavigationModuleViewImpl() {
    
    HorizontalPanel hPanel = new HorizontalPanel();
    hPanel.setWidth($100_PERCENT);
    Image hideNavigationPanelIcon = new Image(INSTANCE.slideHidePanel());
    hideNavigationPanelIcon.setTitle(navigationText.navigation_hidePanelButton_title());
    hideNavigationPanelIcon.setWidth(SLIDE_BUTTON_WIDTH + "px");
    hideNavigationPanelIcon.setHeight(SLIDE_BUTTON_HEIGHT + "px");
    hideNavigationPanelIcon.addClickHandler(clickevent -> {
        previousWidthLeftPanel = Double.valueOf(mainWidgetContainer.getWidgetSize(leftStackFrame)).intValue();
        minimizeLeftPanel(hideNavigationPanelIcon, hPanel);
        if (showNavigationPanelIcon == null) {
          showNavigationPanelIcon = new Image(INSTANCE.showSlidePanel());
          showNavigationPanelIcon.setTitle(navigationText.navigation_showPanelButton_title());
          Element showNavigationPanelIconElement = showNavigationPanelIcon.getElement();
          showNavigationPanelIconElement.getStyle().setPosition(Position.ABSOLUTE);
          showNavigationPanelIconElement.getStyle().setPaddingTop(300, Unit.PX);
          showNavigationPanelIconElement.getStyle().setPaddingLeft(10, Unit.PX);
          DOM.getElementById(MODULE_PANEL_ID).appendChild(showNavigationPanelIconElement);
          Event.sinkEvents(showNavigationPanelIconElement, Event.ONCLICK);
          Event.setEventListener(showNavigationPanelIconElement, event -> {
            restoreLeftPanel(hideNavigationPanelIcon, hPanel, previousWidthLeftPanel);
          });
        } else {
          showNavigationPanelIcon.setVisible(true);
        }
    });
    hPanel.add(hideNavigationPanelIcon);
    hPanel.setCellHorizontalAlignment(hideNavigationPanelIcon, HasHorizontalAlignment.ALIGN_LEFT);
    hPanel.add(navigationHtml);
    // Прозрачная картинка для центрирования заголовка "Навигация".
    Image blankImage = new Image(INSTANCE.blank());
    blankImage.setWidth(SLIDE_BUTTON_WIDTH + "px");
    blankImage.setHeight(SLIDE_BUTTON_HEIGHT + "px");
    hPanel.add(blankImage);
    hPanel.setCellHorizontalAlignment(blankImage, HasHorizontalAlignment.ALIGN_RIGHT);
    
    leftStackFrame.add(leftNavigationFrame, hPanel, STACK_MENU_HEIGHT);
    applyFontSizeStyle(navigationHtml);
    leftStackFrame.add(settingTree, settingsHtml, STACK_MENU_HEIGHT);
    applyFontSizeStyle(settingsHtml);
    // Main Widget Container
    mainWidgetContainer = new SplitLayoutPanel(FRAME_SPLITTER_WIDTH){
      @Override
      public void onLoad(){
        super.onLoad();
        adjustTreesSize();
      }
      @Override
      public void onUnload(){
        super.onUnload();
        if (windowResizeHandler != null) {
          windowResizeHandler.removeHandler();
          windowResizeHandler = null;
        }
      }
      @Override
      public void onResize(){
        super.onResize();

        //  restore left panel original size
        restoreHeaderLeftPanel(hideNavigationPanelIcon, hPanel);
        adjustTreesSize();
      }
    }; // 2px width of splitter
    
    mainWidgetContainer.addWest(leftStackFrame, LEFT_FRAME_WIDTH); // as is in Navigation frameset
    mainWidgetContainer.setWidgetMinSize(leftStackFrame, LEFT_FRAME_WIDTH);
    
    leftNavigationFrame.setContentWidget(navigationTree);
    
    settingTree.setCheckNodes(NavigationTreeField.CheckNodes.LEAF);
    
    settingTree.getEditableCard().setBorders(false);
    
    navigationTree.getEditableCard().setBorders(false);
    
    mainWidgetContainer.add(rightPanel);
    
    rightPanel.setSize($100_PERCENT, $100_PERCENT);
    rightPanel.add(rightTabFrame);
    rightPanel.add(rightSingleTabPanel = new SimplePanel());
    rightSingleTabPanel.setSize($100_PERCENT, $100_PERCENT);
    
    boolean isMultiTabsMode = getMultiTabsModeByCookie();
    
    if (isMultiTabsMode){
      rightTabFrame.addTab(startPageOption);
      rightTabFrame.selectTab(startPageOption);
    }
    else {
      initRightTab(startPageOption);
    }

    switchOnSingleTabMode(!isMultiTabsMode);
    mainWidgetContainer.setSize($100_PERCENT, $100_PERCENT);
    
    // place main widget
    setWidget(mainWidgetContainer);
    // bind resize handler to current window
    windowResizeHandler = Window.addResizeHandler(new ResizeHandler() {
      @Override
      public void onResize(ResizeEvent event) {
        // recalculate height of content widget
        leftNavigationFrame.onResize();
        mainWidgetContainer.onResize();
        if (getMultiTabsModeByCookie()) {
	        // check to show scroll buttons after window resizing
	        rightTabFrame.checkIfScrollButtonsNecessary();
        }
        // restore left panel original size
        restoreHeaderLeftPanel(hideNavigationPanelIcon, hPanel);
      }
    });
    
    formInitialized = true;
   }
   
   @Override
   public void changeLocale(String locale){
     Cookies.setCookie(LOCALE_AND_MULTITABS_MODE_COOKIE_NAME, CookieHelper.getApplicationSettingAsString(locale, Boolean.toString(getMultiTabsModeByCookie()), Boolean.toString(getRefreshTabsModeByCookie()), Boolean.toString(getCacheStateModeByCookie())), getNextYear());
   }
   
   @Override
   public void changeMultiTabsMode(boolean isMultiTabsMode){
     Cookies.setCookie(LOCALE_AND_MULTITABS_MODE_COOKIE_NAME, CookieHelper.getApplicationSettingAsString(CookieHelper.parseApplicationSetting(Cookies.getCookie(LOCALE_AND_MULTITABS_MODE_COOKIE_NAME)).locale, Boolean.toString(isMultiTabsMode), Boolean.toString(getRefreshTabsModeByCookie()), Boolean.toString(getCacheStateModeByCookie())), getNextYear());
   }
   
   @Override
   public void changeRefreshTabsMode(boolean isRefreshTab){
     Cookies.setCookie(LOCALE_AND_MULTITABS_MODE_COOKIE_NAME, CookieHelper.getApplicationSettingAsString(CookieHelper.parseApplicationSetting(Cookies.getCookie(LOCALE_AND_MULTITABS_MODE_COOKIE_NAME)).locale, Boolean.toString(getMultiTabsModeByCookie()), Boolean.toString(isRefreshTab), Boolean.toString(getCacheStateModeByCookie())), getNextYear());
   }
   
   @Override
   public void changeCacheStateMode(boolean isCacheState){
     Cookies.setCookie(LOCALE_AND_MULTITABS_MODE_COOKIE_NAME, CookieHelper.getApplicationSettingAsString(CookieHelper.parseApplicationSetting(Cookies.getCookie(LOCALE_AND_MULTITABS_MODE_COOKIE_NAME)).locale, Boolean.toString(getMultiTabsModeByCookie()), Boolean.toString(getRefreshTabsModeByCookie()), Boolean.toString(isCacheState)), getNextYear());
   }
      
   private int getContentHeight(StackLayoutPanel panel){
     return Window.getClientHeight() 
           - panel.getWidgetCount() * STACK_MENU_HEIGHT
             - 4; // border size
   }
   
   /**
    * Инициализация необходимого URL для правого фрейма отображения контента
    */
   private void initRightTab(JepOption option) {
     if (option == null) return;
     
     if (!option.equals(lastRightTabOption) || option.equals(startPageOption) || isRefreshTabsMode()) {
       Widget rightFrame = rightTabFrame.getRightFrameWidget(option);
       rightSingleTabPanel.setWidget(rightFrame.getParent());
       lastRightTabOption = option;
     }
     
     if (formInitialized) {
       uncheckAll();
       navigationTree.getEditableCard().setChecked(lastRightTabOption, true);
     }
   }

  private void uncheckAll() {
    for (JepOption oldSelectedOption : navigationTree.getValue()) {
       navigationTree.getEditableCard().setChecked(oldSelectedOption, false);
     }
  }
   
   /**
    * Перенаправление на новый адрес
    */
   @Override
   public void redirect(JepOption option){
     String url = (String) option.get(REQUEST_URL);
     if (!JepRiaUtil.isEmpty(url)) {
       Window.Location.assign(url);
     }
   }
   
   @Override
   public void openNewWindow(JepOption option) {
     String url = (String) option.get(REQUEST_URL);
     if (!JepRiaUtil.isEmpty(url)) {
       Window.open(url, null, (String) option.get(FRAME));
     }
   }
   
   @SuppressWarnings("unchecked")
   @Override
   public <W extends JepMultiStateField> W getTreeById(String id){
     if (NAVIGATION_TREE_ID.equalsIgnoreCase(id)){
       return (W) navigationTree;
     }
     else if (SETTING_TREE_ID.equalsIgnoreCase(id)){
       return (W) settingTree;
     }
     else {
       throw new IllegalArgumentException("Argument 'id' of widget is invalid and has value: '" + id + "'");
     }
   }
   
   @Override
   public void switchOnSingleTabMode(boolean singleTabMode){
     rightPanel.showWidget(rightPanel.getWidgetIndex(singleTabMode ? rightSingleTabPanel : rightTabFrame));
     
     if (formInitialized){
       if (singleTabMode) {
         JepOption currentOption = rightTabFrame.indexOf(rightTabFrame.getSelectedIndex());
         initRightTab(currentOption);
       }
       else {
         // При переключении смотрим на последнюю открытую вкладку в одновкладочном режиме
         // если таковой нет среди открытых в многовкладочном режиме, добавляем к имеющимся
         Collection<JepOption> tabOptions = rightTabFrame.getOpenedTabs();
         if (lastRightTabOption != null){
           if(!tabOptions.contains(lastRightTabOption)){
             rightTabFrame.addTab(lastRightTabOption);
           } else if (startPageOption.equals(lastRightTabOption)){ // каждый раз обновляем SSO-страницу 
             // (для получения актуальной информации о количестве некорректных попыток входа в систему)
             rightTabFrame.refresh(lastRightTabOption);
           }
           rightTabFrame.selectTab(lastRightTabOption); 
         }
         uncheckAll();
         for (Iterator<JepOption> iter = tabOptions.iterator(); iter.hasNext();){
           JepOption currentOption = iter.next();
           navigationTree.getEditableCard().setChecked(currentOption, true);
         }
       }
     }
   }
   
   private void applyFontSizeStyle(Widget widget){
     Element element = widget.getElement();
     element.addClassName(MAIN_FONT_STYLE);
     Style style = element.getStyle(); 
     style.setFontSize(12, Unit.PX);
     style.setTextAlign(TextAlign.CENTER);
   }
   
   @Override
   public boolean isAuthorized() {
     return !JepRiaUtil.isEmpty(ClientSecurity.instance.getUsername());
   }
   
   @Override
   public void resetAuthorization() {
     ClientSecurity.instance.setUsername(null);
   }
   
   @Override
   public void authorizedBy(String userName) {
     ClientSecurity.instance.setUsername(userName);
     JepScopeStack.instance.setUserEntered();
   }
   
   @Override
   public boolean isMultiTabsMode() {
     return settingTree.getEditableCard().isSelected(settingTabsNode);
   }
   
   @Override
   public boolean isRefreshTabsMode() {
     return settingTree.getEditableCard().isSelected(settingRefreshContentNode);
   }
   
   @Override
   public boolean isCacheStateMode() {
     return settingTree.getEditableCard().isSelected(settingCacheStateNode);
   }
      
   @Override
   public void openTab(final JepOption selectOption) {
     boolean isMultiTabsMode = getMultiTabsModeByCookie();
     if (isMultiTabsMode) {
       boolean added = rightTabFrame.addTab(selectOption);
       if (!added && isRefreshTabsMode()) {  
         rightTabFrame.refresh(selectOption);
       }
       rightTabFrame.selectTab(selectOption);
     }
     else {
       initRightTab(selectOption);
     }
   }
   
   @Override
   public void openTab(final JepOption selectOption, final String url) {
     openTab(selectOption);
     rightTabFrame.setTabUrl(selectOption, url);
   }
   
   @Override
   public void selectTab(final JepOption selectOption) {
     rightTabFrame.selectTab(selectOption);
   }
   
   @Override
   public void forceToChangePassword(){
     changeRequestUrlForStartPageOption(GWT.getHostPageBaseURL() + "?" + ENTRY_MODULE_NAME_REQUEST_PARAMETER + "=" + CHANGEPASSWORD_MODULE_ID);
   }
   
   @Override
   public void restoreWelcomePage(){
     changeRequestUrlForStartPageOption(WELCOME_PAGE_URL);
   }
   
   private void changeRequestUrlForStartPageOption(String url){
     boolean isMultiTabsMode = getMultiTabsModeByCookie();
     if (isMultiTabsMode){
       rightTabFrame.changeStartOption(url);
     }
     else {
       startPageOption.set(REQUEST_URL, url);
       initRightTab(startPageOption);
     }
   }
   
   @Override
   public void setVisibleWidget(String id){
     if (NAVIGATION_TREE_ID.equalsIgnoreCase(id)){
       leftStackFrame.showWidget(navigationTree.getParent());
     }
     else if (SETTING_TREE_ID.equalsIgnoreCase(id)){
       leftStackFrame.showWidget(settingTree);
     }
     else {
       throw new IllegalArgumentException("Argument 'id' of widget is invalid and has value: '" + id + "'");
     }
   }
   
   @Override
   public boolean getMultiTabsModeByCookie(){
     return CookieHelper.parseApplicationSetting(Cookies.getCookie(LOCALE_AND_MULTITABS_MODE_COOKIE_NAME)).isMultiTabsMode;
   }
   
   @Override
   public boolean getRefreshTabsModeByCookie(){
     return CookieHelper.parseApplicationSetting(Cookies.getCookie(LOCALE_AND_MULTITABS_MODE_COOKIE_NAME)).isRefreshTabMode;
   }
   
   @Override
   public boolean getCacheStateModeByCookie(){
     return CookieHelper.parseApplicationSetting(Cookies.getCookie(LOCALE_AND_MULTITABS_MODE_COOKIE_NAME)).isCacheState;
   }


  @Override
  public void refreshCurrentTab() {
    int currentTabIndex = rightTabFrame.getSelectedIndex();
    if (currentTabIndex != -1)
      rightTabFrame.refresh(rightTabFrame.indexOf(currentTabIndex));
  }

  /**
   * Получения списка открытых в дереве Навигации опций
   */
  @Override
  public List<JepOption> getNavigationTreeExpandedNodes() {
    NavigationTreeField<JepOption> tree = navigationTree.getEditableCard();
    List<JepOption> options = tree.getCheckedSelection();
    List<JepOption> result = new ArrayList<>();
    for(JepOption option : options) {
      TreeNodeInfo<JepOption> node = tree.getNodeInfo(option);
      if (node == null) continue;
      while (node.getParent() != null) {
        JepOption parentOption = node.getParent();
        result.add(0, parentOption);
        node = tree.getNodeInfo(parentOption);
      }
    }
    return result;
  }
  
  @Override
  public List<JepOption> getNavigationCheckedNodes() {
    NavigationTreeField<JepOption> tree = navigationTree.getEditableCard();
    return tree.getCheckedSelection();
  }

  /**
   * Получение текущих URL открытых вкладок в виде строки через '; '.
   */
  @Override
  public String getOpenedTabsUrls() {
    List<JepOption> tabList = new ArrayList<>(rightTabFrame.getOpenedTabs());
    String result = "";
    for (int i = 0; i < tabList.size(); i++) {
      if (tabList.get(i).get(REQUEST_URL).equals(WELCOME_PAGE_URL)) {
        continue;
      }
      result += getTabRequestUrl(i) + "; ";
    }
    return result.trim().substring(0, result.length() - 2);
  }

  /**
   * Получение опции текущей вкладки.
   * @return логическое представление текущей вкладки
   */
  @Override
  public JepOption getCurrentTab() {
    return rightTabFrame.indexOf(rightTabFrame.getSelectedIndex());
  }
  
  private native String getTabRequestUrl(int i)
  /*-{
    var frame = $doc.getElementsByClassName("navigationFrame")[i];
    return (frame.contentDocument || frame.contentWindow.document).location.href;
  }-*/;

  private void setTabUrl(JepOption tabOption, String url) {
    rightTabFrame.<Frame>getRightFrameWidget(tabOption).setUrl(url);
  }
  
  @Override
  public void refreshAllTabs() {
    rightTabFrame.refreshAllTabs();
  }

  @Override
  public void closeTab(JepOption option) {
    rightTabFrame.closeTab(option);
  }
  
  @Override
  public void setLetterWidth(double px) {
    rightTabFrame.setLetterWidth(px);
  }
  
  private void adjustTreesSize(){
    int height = getContentHeight(leftStackFrame);
    navigationTree.setFieldHeight(height);
    settingTree.setFieldHeight(height);
    int width = leftStackFrame.getElement().getOffsetWidth();
    if (width > 0) {
      navigationTree.setFieldWidth(width);
      settingTree.setFieldWidth(width);
    }
  }
  
  @Override
  public void layout(){
    mainWidgetContainer.forceLayout();
  }

  @SuppressWarnings("deprecation")
  private Date getNextYear() {
    Date expires = new Date();
    expires.setYear(expires.getYear() + 1);
    return expires;
  }
  
  @Override
  public void returnHome(String locale){
    JepOption option = new JepOption();
    option.set(REQUEST_URL, GWT.getHostPageBaseURL() + "?" + HTTP_REQUEST_PARAMETER_LOCALE + "=" + (JepRiaUtil.isEmpty(locale) ? LOCALE_PARAMETER_VALUE : locale));
    redirect(option);
  }
  
  protected void minimizeLeftPanel(Widget slidePanel, HorizontalPanel hPanel) {
    mainWidgetContainer.setWidgetSize(leftStackFrame, MINIMIZE_LEFT_FRAME_WIDTH);
    mainWidgetContainer.setWidgetMinSize(leftStackFrame, MINIMIZE_LEFT_FRAME_WIDTH);
    hPanel.setCellHorizontalAlignment(slidePanel, HasHorizontalAlignment.ALIGN_LEFT);
    navigationHtml.setVisible(false);
    settingsHtml.setVisible(false);
    navigationTree.setVisible(false);
  }
  
  protected void restoreLeftPanel(Widget slidePanel, HorizontalPanel hPanel, int width) {
    mainWidgetContainer.setWidgetSize(leftStackFrame, width);
    mainWidgetContainer.setWidgetMinSize(leftStackFrame, LEFT_FRAME_WIDTH);
    navigationHtml.setVisible(true);
    settingsHtml.setVisible(true);
    navigationTree.setVisible(true);
    hPanel.setCellHorizontalAlignment(slidePanel, HasHorizontalAlignment.ALIGN_LEFT);
    if (showNavigationPanelIcon != null){
      showNavigationPanelIcon.setVisible(false);
    }
  }
  
  protected void restoreHeaderLeftPanel(Widget slidePanel, HorizontalPanel hPanel) {
    navigationHtml.setVisible(true);
    settingsHtml.setVisible(true);
    navigationTree.setVisible(true);
    mainWidgetContainer.setWidgetMinSize(leftStackFrame, LEFT_FRAME_WIDTH);
    hPanel.setCellHorizontalAlignment(slidePanel, HasHorizontalAlignment.ALIGN_LEFT);
    if (showNavigationPanelIcon != null){
      showNavigationPanelIcon.setVisible(false);
    }
  }
}

