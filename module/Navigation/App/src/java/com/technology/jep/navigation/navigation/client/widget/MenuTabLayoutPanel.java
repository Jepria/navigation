package com.technology.jep.navigation.navigation.client.widget;

import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;
import static com.technology.jep.navigation.navigation.client.NavigationClientConstant.navigationText;
import static com.technology.jep.navigation.navigation.client.ui.form.plain.NavigationModuleViewImpl.$100_PERCENT;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.REQUEST_URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.TextOverflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.widget.container.JepTabLayoutPanel;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.navigation.navigation.client.widget.image.MenuTabImages;

/**
 * Класс панели, на которой размещены вкладки открытых модулей
 */
public class MenuTabLayoutPanel extends JepTabLayoutPanel implements HasTabMenu {

  /**
   * Префикс идентификатора лейбла вкладки.
   */
  private static final String LABEL_ID_PREFIX = "labelId_";
  
  /**
   * Наименование css-класса, стилизующего фрейм открываемой вкладки-модуля
   */
  private static final String NAVIGATION_FRAME_STYLE_NAME = "navigationFrame";
  
  /**
   * Наименование css-класса, стилизующего кнопку закрытия вкладки
   */
  private static final String CLOSE_BUTTON_TAB_STYLE_NAME = "closeButtonTab";
  
  /**
   * Наименование css-класса, стилизующего кнопку прокручивания вкладок
   */
  private static final String SCROLL_BUTTON_TAB_STYLE_NAME = "tabPanelScrollButton";
  
  /**
   * Максимальная ширина вкладки
   */
  private static final int MAX_TAB_WIDTH = 150;
  
  /**
   * Шаг прокрутки вкладок в пикселях
   */
  private static final int SCROLL_PIXELS = 50;
  
  /**
   * Отступы кнопок прокрутки вкладок
   */
  private static final int IMAGE_PADDING_PIXELS = 4;
  
  /**
   * Панель, на которой расположены вкладки открытых модулей
   */
  private FlowPanel tabBar;
  
  /**
   * Изображение кнопки прокручивания влево открытых вкладок
   */
  private Image scrollLeftButton = new Image(MenuTabImages.INSTANCE.leftArrow());
  
  /**
   * Изображение кнопки прокручивания вправо открытых вкладок
   */
  private Image scrollRightButton = new Image(MenuTabImages.INSTANCE.rightArrow());
  
  /**
   * Главный компонент, состоящий из области вкладок и их содержимого.
   */
  private LayoutPanel tabBarPanel;
  
  /**
   * Панель прокручивания вкладок.
   */
  private FlowPanel scrollButtonPanel;
  
  /**
   * Опция стартовой страницы навигации. 
   */
  private JepOption startPageOption;
  
  /**
   * Карта соответствий опций с открытыми фреймами.
   */
  private Map<JepOption, Frame> openedTabOptions = new LinkedHashMap<JepOption, Frame>();
  
  /**
   * Карта соответствий опций и всех обработчиков, связанных с данной вкладкой.
   */
  private Map<JepOption, TabLabelElementHandler> tabHandlers = new HashMap<JepOption, TabLabelElementHandler>();
  
  /**
   * Ширина буквы в пикселях для используемого шрифта.
   */
  private double letterWidth;
  
  /**
   * Всплываемая панель контекстного меню (существует в единичном экземпляре)
   */
  private PopupPanel contextMenuPanel;
  
  public MenuTabLayoutPanel(JepOption startPageOption, String id, double barHeight, Unit barUnit) {
    super(id, barHeight, barUnit);
    this.startPageOption = startPageOption;
    // Получим главный компонент, состоящий из области вкладок и их содержимого.
    tabBarPanel = (LayoutPanel) getWidget();
    // Найдем область вкладок, которая является первой FlowPanel в главном компоненте.
    for (int i = 0; i < tabBarPanel.getWidgetCount(); ++i){
      Widget widget = tabBarPanel.getWidget(i);
      if (widget instanceof FlowPanel){
        tabBar = (FlowPanel) widget; 
        break; // Область вкладок найдена.
      }
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void selectTab(int index) {
    super.selectTab(index);
    JepOption option = indexOf(index);
    if (option != null) {
      // меняем title окна
      Window.setTitle(option.getName());
      scrollToSelected();
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void add(Widget child, Widget tab) {
    super.add(child, tab);
    checkIfScrollButtonsNecessary();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean remove(int index) {
    boolean b = super.remove(index);
    checkIfScrollButtonsNecessary();
    scrollToSelected();
    return b;
  }
  
  /**
   * Создание обработчика прокручивания вкладок.
   * 
   * @param diff    шаг прокручивания
   * @return обработчик скроллирования
   */
  private ClickHandler createScrollClickHandler(final int diff) {
    return new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        checkAndScroll(diff);
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkIfScrollButtonsNecessary() {
    // Defer size calculations until sizes are available, when calculating
    // immediately after
    // add(), all size methods return zero
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        initScrollButtonPanel();
        
        boolean isScrolling = isScrollingNecessary();
        // When the scroll buttons are being hidden, reset the scroll position
        // to zero to
        // make sure no tabs are still out of sight
        if (scrollRightButton.isVisible() && !isScrolling) {
          resetScrollPosition();
        }
        // Set visible or hide
        // Must be done before changing tabbar size if isScrolling
        scrollButtonPanel.setVisible(isScrolling);
        
        // If scrolling necessary then make room for scroll buttons
        if (isScrolling) {
         tabBar.getElement().getParentElement().getStyle().setRight(
             scrollButtonPanel.getOffsetWidth()
               + IMAGE_PADDING_PIXELS, Unit.PX);
        }
        scrollToSelected();
      }
    });
  }

  /**
   * Сброс позиции прокручивания.
   */
  private void resetScrollPosition() {
    scrollTo(0);
  }

  /**
   * Прокручивание элемента на указанное количество пикселей.
   * @param pos количество пикселей, на который будет прокручен элемент влево.
   */
  private void scrollTo(int pos) {
    tabBar.getElement().getStyle().setLeft(pos, Unit.PX);
  }

  /**
   * Проверка необходимости скроллирования.
   * 
   * @return флаг необходимости скроллирования
   */
  private boolean isScrollingNecessary() {
    Widget lastTab = getLastTab();
    if (lastTab == null)
      return false;

    return getRightOfWidget(lastTab) > getTabBarWidth();
  }

  /**
   * Получение крайне правой позиции виджета.
   *  
   * @param widget  виджет
   * @return позиция правой точки виджета
   */
  private int getRightOfWidget(Widget widget) {
    return widget.getElement().getOffsetLeft()
        + widget.getElement().getOffsetWidth();
  }

  /**
   * Получение ширины панели вкладок
   * 
   * @return  ширина панели
   */
  private int getTabBarWidth() {
    return tabBar.getElement().getParentElement().getClientWidth();
  }

  /**
   * Получение виджета последней вкладки
   * 
   * @return  виджет
   */
  private Widget getLastTab() {
    if (tabBar.getWidgetCount() == 0)
      return null;

    return tabBar.getWidget(tabBar.getWidgetCount() - 1);
  }

  /**
   * Проверка необходимости скроллирования.
   * 
   * @param diff  значение прокручиваемой позиции
   */
  private void checkAndScroll(final int diff) {
    Widget lastTab = getLastTab();
    if (lastTab == null)
      return;

    int newLeft = parsePosition(tabBar.getElement().getStyle().getLeft()) + diff;
    int rightOfLastTab = getRightOfWidget(lastTab);

    // Do not scroll too far to the left
    if (newLeft > 0) {
     newLeft = 0;
    } 

    // And not too far to the right either
    if (getTabBarWidth() - newLeft > rightOfLastTab) {
     newLeft = getTabBarWidth() - rightOfLastTab;
    }

    scrollTo(newLeft);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  /** Create and attach the scroll button images with a click handler */
  public void initScrollButtonPanel() {
   // Only initialize if not already done
   if (scrollButtonPanel == null) {
    // Initialize flow panel -
    // height and width to fit tabs and buttons
    scrollButtonPanel = new FlowPanel();
    int tabHeight = tabBar.getElement().getFirstChildElement().getClientHeight();
    scrollButtonPanel.setHeight(tabHeight + Unit.PX.getType());
    scrollButtonPanel.setWidth(tabHeight * 2 + Unit.PX.getType());
    scrollButtonPanel.setVisible(false);

    // Left button
    scrollButtonPanel.add(scrollLeftButton);
    // Right button
    scrollButtonPanel.add(scrollRightButton);

    // Click handlers
    scrollRightButton.addClickHandler(createScrollClickHandler(-1 * SCROLL_PIXELS));
    scrollLeftButton.addClickHandler(createScrollClickHandler(SCROLL_PIXELS));

    // Styles
    scrollRightButton.setStyleName(SCROLL_BUTTON_TAB_STYLE_NAME);
    scrollLeftButton.setStyleName(SCROLL_BUTTON_TAB_STYLE_NAME);

    // Insert scroll buttons in tablayoutpanel
    tabBarPanel.insert(scrollButtonPanel, 0);
    tabBarPanel.setWidgetRightWidth(scrollButtonPanel, 0, Unit.PX,
      tabHeight * 2, Unit.PX);
    tabBarPanel.setWidgetTopHeight(scrollButtonPanel, 3, Unit.PX,
        tabHeight, Unit.PX);
   }
  }

  /**
   * Парсинг значения, представленного строкой.
   * 
   * @param positionString  анализируемая строка
   * @return целочисленное значение
   */
  private static int parsePosition(String positionString) {
    int position;
    try {
      for (int i = 0; i < positionString.length(); i++) {
        char c = positionString.charAt(i);
        if (c != '-' && !(c >= '0' && c <= '9')) {
          positionString = positionString.substring(0, i);
        }
      }

      position = Integer.parseInt(positionString);
    } catch (NumberFormatException ex) {
      position = 0;
    }
    return position;
  }
  
  /**
   * Прокручивание к выбранной вкладке.
   */
  private void scrollToSelected() {
    int tabBarWidth = getTabBarWidth();
    int rightOfSelected = getRightOfWidget(getTabWidget(getSelectedIndex()).getParent());
    scrollTo(Math.min(0, tabBarWidth - rightOfSelected));
   }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public JepOption indexOf(int selectIndex){
    if (selectIndex == -1) return null;
    
    int optionValue = -1;
    Label searchLabel = getTabLabel(selectIndex);
    
    for (int i = 0; i < getWidgetCount(); i++) {
      Label label = getTabLabel(i);
      if (label.equals(searchLabel)) {
        optionValue = Integer.decode(label.getElement().getId().replace(LABEL_ID_PREFIX, ""));
        break;
      }
    }
    
    if (optionValue != -1) { 
      for (Iterator<JepOption> iter = this.openedTabOptions.keySet().iterator(); iter.hasNext();) {
        JepOption opt = iter.next();
        if (JepOption.<Integer>getValue(opt).equals(optionValue)) {
          return opt;
        }
      }
    }
    return null;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean addTab(final JepOption option){
    if (indexOf(option) >= 0) return false;
    
    Label tabLabel = new Label(option.getName());
    tabLabel.getElement().setId(LABEL_ID_PREFIX + option.getValue());
    
    Frame newFrame = getRightFrameWidget(option);
    
    final LayoutPanel lPanel = (LayoutPanel) newFrame.getParent();
    add(lPanel, tabLabel);
    lPanel.getElement().getStyle().setBorderStyle(BorderStyle.NONE);
    
    int index = indexOf(option);
    tabLabel = getTabLabel(index);
    
    Style style = tabLabel.getElement().getStyle();
    style.setColor("#15428B");
    tabLabel.getElement().addClassName(MAIN_FONT_STYLE);
    style.setFontSize(11, Unit.PX);
    style.setWhiteSpace(WhiteSpace.NOWRAP);
    style.setOverflow(Overflow.HIDDEN);
    style.setTextOverflow(TextOverflow.ELLIPSIS);
    tabLabel.getParent().addStyleName("jepRia-TabLayoutPanelTab-common");
    
    getWidget(index).getElement().getStyle().setPadding(0, Unit.PX);
    
    if (!openedTabOptions.isEmpty()) {
      bindTabHandlers(option);
    }
    
    openedTabOptions.put(option, newFrame);
    
    appendCloseButtonToFirstTab();
    return true;
  }

  /**
   * Связывание опции с обработчиками данной вкладки.
   * 
   * @param option  опция, соответствующая открытой вкладки
   */
  private void bindTabHandlers(JepOption option) {
    Label tabLabel = getTabLabel(indexOf(option));
    LayoutPanel lPanel = (LayoutPanel) getRightFrameWidget(option).getParent();
    TabLabelElementHandler handler = new TabLabelElementHandler();
    // append context menu to tab
    handler.contextMenuHandler = appendContextMenu(option, tabLabel, lPanel);
    // append close button to tab
    handler.closeElement = appendCloseTabButton(option, tabLabel);
    tabHandlers.put(option, handler);
  }
  
  /**
   * Отвязывание опции с обработчиками данной вкладки.
   * 
   * @param option  опция, соответствующая открытой вкладки
   */
  private void unbindTabHandlers(JepOption option) {
    tabHandlers.get(option).remove();
    tabHandlers.remove(option);
  }
  
  /**
   * Добавление обработчика контекстного меню вкладки.
   * 
   * @param option    опция открытой вкладки
   * @param tabLabel  лейбл вкладки
   * @param lPanel    панель
   * @return обработчик контекстного меню
   */
  private HandlerRegistration appendContextMenu(final JepOption option, final Label tabLabel, final LayoutPanel lPanel) {
    tabLabel.sinkEvents(Event.ONCONTEXTMENU);
    return tabLabel.addHandler(new ContextMenuHandler() {
       @Override
       public void onContextMenu(ContextMenuEvent event) {
         openContextMenu(option, tabLabel, event.getNativeEvent());
       }
    }, ContextMenuEvent.getType());
  }
 
  /**
   * {@inheritDoc}
   */
  @Override
  public PopupPanel openContextMenu(final JepOption option, UIObject target, NativeEvent event) {
    event.preventDefault();
    event.stopPropagation();
    if (contextMenuPanel == null) {
      final String requestUrl = (String) option.get(REQUEST_URL);
      contextMenuPanel = new DecoratedPopupPanel(true);
      final VerticalPanel optionsContainer = new VerticalPanel();
      
      class PopupContextMenu extends Anchor {
        
        private static final String POPUP_CONTEXT_MENU_NAVIGATION_STYLE_NAME = "popupContextMenuNavigation";
        ClickHandler closeClickHandler = new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            contextMenuPanel.hide();
          }
        };
        
        PopupContextMenu(String title, String url, String target){
          super(title, url, target);
          stylize();
          addClickHandler(closeClickHandler);
        }
        
        PopupContextMenu(String title){
          super(title);
          stylize();
          addClickHandler(closeClickHandler);
        }
        
        private void stylize(){
          getElement().addClassName(POPUP_CONTEXT_MENU_NAVIGATION_STYLE_NAME);
        }
        
        public void removeAllHandlers(){
          for (HandlerRegistration handler : handlers){
            handler.removeHandler();
          }
          handlers.clear();
        }
        
        private List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();
        
        @Override
        public HandlerRegistration addClickHandler(ClickHandler handler){
          HandlerRegistration handlerRegistration = super.addClickHandler(handler);
          handlers.add(handlerRegistration);
          return handlerRegistration;
        }
      }
      
      int index = indexOf(option);
      boolean isAttachedToTab = index >= 0 && getTabLabel(index).equals(target);
      
      final PopupContextMenu popInNewTabLabel = new PopupContextMenu(navigationText.navigation_tab_popup_newTab_title(), requestUrl, "_blank");
      optionsContainer.add(popInNewTabLabel);
      
      contextMenuPanel.setWidget(optionsContainer);
      contextMenuPanel.showRelativeTo(target);
      
      contextMenuPanel.addCloseHandler(new CloseHandler<PopupPanel>() {
        @Override
        public void onClose(CloseEvent<PopupPanel> event) {
          // remove all handlers
          for (int i = 0; i < optionsContainer.getWidgetCount(); i++){
            Widget widget = optionsContainer.getWidget(i);
            if (widget instanceof PopupContextMenu){
              ((PopupContextMenu) widget).removeAllHandlers();
            }
          }
          contextMenuPanel = null;
          
        }
      });
      
      if (isAttachedToTab) {
        Anchor refreshFrameTabLabel = new PopupContextMenu(navigationText.navigation_tab_popup_refresh_title());
        optionsContainer.add(refreshFrameTabLabel);
        
        Anchor closeTabLabel = new PopupContextMenu(navigationText.navigation_tab_popup_close_title());
        optionsContainer.add(closeTabLabel);
        
        Anchor closeAllTabLabel = new PopupContextMenu(navigationText.navigation_tab_popup_close_all_title());
        optionsContainer.add(closeAllTabLabel);
        
        Anchor closeAllExceptThatTabLabel = new PopupContextMenu(navigationText.navigation_tab_popup_close_all_except_that_title());
        optionsContainer.add(closeAllExceptThatTabLabel);
        
        refreshFrameTabLabel.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            refresh(option);
          }
        });
        
        closeTabLabel.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            closeTab(option);
          }
        });
        
        closeAllTabLabel.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            closeAllTabs();
          }
        });
        
        closeAllExceptThatTabLabel.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            closeAllTabsExceptThat(option);
          }
        });
      }
    }
    else {
      contextMenuPanel.hide();
      contextMenuPanel = null;
      openContextMenu(option, target, event);
    }
    return contextMenuPanel;
  }
  
  /**
   * Добавление кнопки закрытия вкладки.
   * 
   * @param option   опция добавляемой вкладки
   * @param tabLabel лейбл текущей вкладки
   * @return DOM-элемент кнопки закрытия 
   */
  private Element appendCloseTabButton(final JepOption option, Label tabLabel) {
    String tabTitle = tabLabel.getText();
    // extend right border for close tab button
    Style style = tabLabel.getElement().getStyle();
    style.setPaddingRight(8, Unit.PX);
    style.setTextAlign(TextAlign.LEFT);
    tabLabel.getElement().setTitle(tabTitle);
    
    if (tabTitle.length() * letterWidth > MAX_TAB_WIDTH) {
      style.setWidth(MAX_TAB_WIDTH, Unit.PX);
      tabLabel.getElement().getParentElement().getStyle().setWidth(tabLabel.getOffsetWidth() + 15, Unit.PX);
    }
    else {
      tabLabel.getElement().getParentElement().getStyle().setWidth(tabLabel.getOffsetWidth() + 20, Unit.PX);
    }
    
    Element closeElement = DOM.createDiv();
    closeElement.setInnerText("x");
     tabLabel.getElement().appendChild(closeElement);
     closeElement.addClassName(CLOSE_BUTTON_TAB_STYLE_NAME);
     
     Event.sinkEvents(closeElement, Event.ONCLICK);
     Event.setEventListener(closeElement, new EventListener() {
       @Override
       public void onBrowserEvent(Event event) {
         closeTab(option);
       }
     });
     
     return closeElement;
  }
  
  /**
   * Индекс вкладки, соответствующей опции.
   * 
   * @param opt опция
   * @return позиция вкладки
   */
  private int indexOf(JepOption opt){
    for (int i = 0; i < getWidgetCount(); i++) {
      Label label = getTabLabel(i);
      if (label.getElement().getId().equalsIgnoreCase(LABEL_ID_PREFIX + opt.getValue())) {
        return i;
      }
    }
    return -1;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean closeTab(final JepOption option) {
   if (openedTabOptions.size() == 1) return false;
   
   int widgetIndex = indexOf(option);
   boolean first = widgetIndex == 0; 
   if (first) {
     selectTab(widgetIndex + 1);
   } else {
     if (widgetIndex == getSelectedIndex()) {
       selectTab(widgetIndex - 1);
     }
   }
   //to avoid memory leaks remove handlers for tab
   unbindTabHandlers(option);
   
   remove(widgetIndex);
   
   openedTabOptions.remove(option);
   
   hideCloseButtonOfSingleTab();
   return true;
  }
  
  /**
   * Метод закрытия всех вкладок.
   */
  private void closeAllTabs(){
    for (JepOption option : new HashSet<JepOption>(openedTabOptions.keySet())){
      closeTab(option);
    }
  }
  
  /**
   * Метод закрытия всех вкладок, кроме указанной.
   * 
   * @param that  вкладка-исключение
   */
  private void closeAllTabsExceptThat(JepOption that){
    Set<JepOption> result = new HashSet<JepOption>(openedTabOptions.keySet());
    result.remove(that);
    for (JepOption option : result){
      closeTab(option);
    }
    selectTab(that);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void selectTab(JepOption option){
    selectTab(indexOf(option));
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshAllTabs() {
    for (JepOption option : openedTabOptions.keySet()){
      refresh(option);
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void refresh(JepOption rightFrameOption){
    Frame frame = openedTabOptions.get(rightFrameOption);
    frame.setUrl((String) rightFrameOption.get(REQUEST_URL));
  }
  
  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <X extends Widget> X getRightFrameWidget(final JepOption option){
     final LayoutPanel lPanel = new LayoutPanel(); 
     final Frame frame = new Frame();
     lPanel.add(frame);
     lPanel.setSize($100_PERCENT, $100_PERCENT);
     Scheduler.get().scheduleDeferred(new ScheduledCommand() {
       @Override
       public void execute() {
         frame.setUrl((String) option.get(REQUEST_URL));
       }
     });
     frame.setSize($100_PERCENT, $100_PERCENT);
     frame.getElement().addClassName(NAVIGATION_FRAME_STYLE_NAME);
     return (X) frame;  
  }

  @Override
  public void setTabUrl(final JepOption option, final String url) {
    final Frame tabFrame = this.openedTabOptions.get(option);
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        tabFrame.setUrl(url);
      }
    });
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void changeStartOption(String url){
    Frame frame = openedTabOptions.get(startPageOption);
    startPageOption.set(REQUEST_URL, url);
    openedTabOptions.put(startPageOption, frame);
    refresh(startPageOption);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<JepOption> getOpenedTabs(){
    return openedTabOptions.keySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLetterWidth(double px) {
    this.letterWidth = px;
  }
  
  /**
   * Добавление кнопки закрытия для первой вкладки
   */
  private void appendCloseButtonToFirstTab() {
    if (openedTabOptions.size() > 1){
      JepOption option = openedTabOptions.keySet().iterator().next();
      if (openedTabOptions.containsKey(option)){
        if (!tabHandlers.containsKey(option)){
          bindTabHandlers(option);
        }
        else {
          tabHandlers.get(option).closeElement.getStyle().setDisplay(Display.BLOCK);
        }
      }
    }
  }

  /**
   * Сокрытие кнопки закрытия единственной вкладки
   */
  private void hideCloseButtonOfSingleTab() {
    if (openedTabOptions.size() == 1){
      JepOption option = openedTabOptions.keySet().iterator().next();
      if (openedTabOptions.containsKey(option)){
        tabHandlers.get(option).closeElement.getStyle().setDisplay(Display.NONE);
      }
    }
  }
  
  /**
   * Класс обработчика контекстного меню и кнопки закрытия
   */
  class TabLabelElementHandler {
    Element closeElement;
    HandlerRegistration contextMenuHandler;
    
    void remove(){
      Event.setEventListener(closeElement, null);
      closeElement.removeFromParent();
      contextMenuHandler.removeHandler();
    }
  }
}
