package com.technology.jep.navigation.navigation.client.widget.field;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.CONTEXTMENU;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_VALUE_CHECKED;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_VALUE_PARTIAL;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_VALUE_UNCHECKABLE;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_VALUE_UNCHECKED;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.widget.event.JepEventType.CHANGE_CHECK_EVENT;
import static com.technology.jep.navigation.navigation.client.ui.form.plain.navigation.NavigationTreeFieldResources.INSTANCE;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.IMAGE;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.KEY_OPTION_PROPERTY;
import static com.technology.jep.navigation.navigation.shared.field.NavigationFieldNames.REQUEST_URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager.SelectAction;
import com.technology.jep.jepria.client.async.DataLoader;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.widget.container.ElementSimplePanel;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.multistate.JepMultiStateField;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent.CheckChangeHandler;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.navigation.navigation.client.ui.form.plain.navigation.NavigationTreeFieldResources;
import com.technology.jep.navigation.navigation.client.ui.form.plain.navigation.menu.CustomNavigationMenu;
import com.technology.jep.navigation.navigation.client.widget.field.tree.NavigationTreeField;
import com.technology.jep.navigation.navigation.client.widget.field.tree.NavigationTreeField.CheckCascade;
import com.technology.jep.navigation.navigation.client.widget.field.tree.NavigationTreeField.CheckNodes;
import com.technology.jep.navigation.navigation.client.widget.field.tree.NavigationTreeField.TreeCell;

/**
 * Класс дерева навигации, кастомизированный для удобства работы с пунктами меню.
 */
@SuppressWarnings("unchecked")
public abstract class MenuTreeField extends JepMultiStateField<NavigationTreeField<JepOption>, HTML> {

  /**
   * Список узлов, которые необходимо отметить.
   */
  private List<JepOption> checkedValues = null;
  
  /**
   * Список узлов, которые необходимо раскрыть.
   */
  private List<JepOption> expandedValues = null;
  
  private final static int DEFAULT_TREE_FIELD_HEIGHT = 300;
  
  /**
   * Ссылка на основной виджет древовидного справочника (в DOM-дереве представлен таблицей).
   * Позволяет корректно изменять размерности компонента.
   */
  private Widget mainTreeFieldWidget;
  
  /**
   * Ссылка на внутренний виджет древовидного справочника (в DOM-дереве представлен таблицей).
   * Позволяет корректно изменять размерности компонента.
   */
  private Widget innerTreeFieldWidget;
  
  /**
   * Шаблон для отрисовки узлов дерева.
   */
  private final TreeFieldTemplate treeFieldTemplate = GWT.create(TreeFieldTemplate.class);
  
  /**
   * Значение отступа для узлов дерева.
   */
  private static final String PADDING = "   ";
  
  /**
   * Префикс для идентификаторов родительских узлов дерева.
   */
  private static final String FOLDER_LABEL_ID_PREFIX = "f_";
  
  /**
   * Интерфейс шаблона отрисовки узлов дерева.
   */
  interface TreeFieldTemplate extends SafeHtmlTemplates {
    /**
     * Html-шаблон для отрисовки надписи узла дерева.
     */
    @Template("<span title=\"{0}\" style=\"{1}\">{2}</span>")
    SafeHtml imageWrapper(String url, SafeStyles style, SafeHtml image);
    
    /**
     * Html-шаблон для отрисовки надписи узла дерева.
     */
    @Template("<label id=\"{0}\" title=\"{1}\" style=\"{2}\">{3}{4}</label>")
    SafeHtml labelWrapper(String id, String url, SafeStyles style, SafeHtml padding, String name);
  }
  
  {
    editablePanel.remove(editableCardLabel);
  }
  
  
  public MenuTreeField() {
    this(null);
  }
  
  public MenuTreeField(String fieldLabel){
    this(null, fieldLabel);
  }
  
  public MenuTreeField(String fieldIdAsWebEl, String fieldLabel){
    super(fieldIdAsWebEl, fieldLabel);
    // установка высоты по умолчанию
    setFieldHeight(DEFAULT_TREE_FIELD_HEIGHT);
  }
  
  /**
   * Установка загрузчика узлов нижележащего уровня.
   * 
   * @param loader загрузчик узлов нижележащего уровня
   */
  public void setLoader(final DataLoader<JepOption> loader){
    editableCard.setLoader(new DataLoader<JepOption>() {
      public void load(Object loadConfig, final AsyncCallback<List<JepOption>> callback) {
        loader.load(loadConfig, new JepAsyncCallback<List<JepOption>>() {
          @Override
          public void onSuccess(List<JepOption> result) {
            callback.onSuccess(result);
            processExpanding();
            processChecking();
          }
          @Override
          public void onFailure(Throwable caught) {
            callback.onFailure(caught);
          }
        });
      }
    });
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(Object value) {
    Object oldValue = getValue();
    if(!Objects.equals(oldValue, value)) {
      // Создание копии списка элементов важно, поскольку в методе processChecking
      // происходит удаление элементов списка, что приводит к изменению состояния 
      // currentRecord при смене рабочего состояния формы
      this.checkedValues = new ArrayList<JepOption>((List<JepOption>)value);
      processChecking();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<JepOption> getValue() {
    return editableCard.getCheckedSelection();
  }

  /**
   * Устанавливает стиль поведения (каскадного выделения) для отмечаемых узлов.<br/>
   * Возможные значения:
   * <ul>
   *  <li>NONE - каскадного выделения никаких узлов не происходит</li>
   *  <li>PARENTS - каскадно выделяются все родители</li>
   *  <li>CHILDREN - каскадно выделюятся все дети</li>
   * </ul>
   * Замечание: при установке значения CHILDREN отмечаются (очевидно) только отрисованные дочерние узлы.
   * 
   * @param checkCascade стиль поведения (каскадного выделения) для отмечаемых узлов
   */
  public void setCheckStyle(NavigationTreeField.CheckCascade checkCascade) {
    editableCard.setCheckStyle(checkCascade);
  }
  
  /**
   * Устанавливает какие узлы можно отмечать.<br/>
   * Возможные значения:
   * <ul>
   *  <li>BOTH - можно отмечать и узлы и конечные листья</li>
   *  <li>PARENT - только родительские узлы (узлы, которые содержат дочерние элементы)</li>
   *  <li>LEAF - только листья (узлы, которые НЕ содержат дочерних элементов)</li>
   * </ul>
   *
   * @param checkNodes какие узлы можно отмечать
   */  
  public void setCheckNodes(NavigationTreeField.CheckNodes checkNodes) {
    editableCard.setCheckNodes(checkNodes);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setFieldHeight(int fieldHeight) {
    editableCard.setHeight(fieldHeight + Unit.PX.getType());
  }
  
  /**
   * Метод не поддерживается.
   */
  @Override
  public void setEditable(boolean editable) {
    throw new UnsupportedOperationException();
  }
  
  /**
   * Метод предка перегружен пустой реализацией, т.к. в данном компоненте карта Просмотра не используется.
   * 
   * @param value значение для карты Просмотра
   */
  @Override
  protected void setViewValue(Object value) {

  }
  
  /**
   * Проверяет: содержит ли допустимое значение поле.
   *
   * @return true - если поле содержит допустимое значение, false - в противном случае
   */
  @Override
  public boolean isValid() {
    clearInvalid();
    if(!allowBlank) {
      if(getValue().size() == 0) {
        markInvalid(JepTexts.checkForm_mandatoryField());
        return false;
      }
    }
    return true;
  }

  /**
   * Отмечает отрисованные узлы и удаляет их из списка узлов, которые необходимо отметить 
   * {@link com.technology.jep.jepria.client.widget.field.multistate.JepTreeField#checkedValues}.
   */
  protected void processChecking() {
    if(checkedValues != null && checkedValues.size() > 0) {
      Iterator<JepOption> iterator = checkedValues.iterator();
      while(iterator.hasNext()) {
        JepOption option = iterator.next();
        // Удаляем значение, т.к. открытие узлов - это разовая (в данном случае) операция
        // и НЕ нужно повторно открывать указанные узлы (которые пользователь, возможно, уже закрыл).
        editableCard.setChecked(option, true);
        iterator.remove();
      }
    }
  }
  
  /**
   * Обработчик нового состояния
   * 
   * @param newWorkstate новое состояние
   */
  @Override
  protected void onChangeWorkstate(WorkstateEnum newWorkstate) {
    
    // В данном компоненте работаем ТОЛЬКО с картой Редактирования.
    showWidget(getWidgetIndex(editablePanel));
    
    // Если карта Редактирования уже создана (первый раз метод вызывается в предке, когда карты Редактирования еще нет).
    if(editableCard != null) {
      // При смене состояния прокручиваем карту Редактирования наверх.
      editableCard.scrollToTop();
      if(WorkstateEnum.isEditableState(newWorkstate)) { // Для случая Редактирования: ...
        editableCard.setCheckable(true);// позволим отмечать узлы дерева
        editableCard.setBorders(true); // отобразим границы рабочей области компонента
        editableCard.setBackgroundColor("white"); // установим белый фон рабочей области компонента
      } else { // Для случая Просмотра: ...
        editableCard.setCheckable(false); // запретим отмечать узлы дерева
        editableCard.setBorders(false); // скроем границы рабочей области компонента
        editableCard.setBackgroundColor("transparent"); // установим прозрачный фон рабочей области компонента
      }      
    }
  
  }
  
  /**
   * {@inheritDoc}
   */
  public void setEnabled(boolean enabled) {
    // TODO реализовать блокировку поля
  }
  
  /**
   * Метод не поддерживается данным полем.
   */
  @Override
  public String getRawValue(){
    throw new UnsupportedOperationException("TreeField does not have a raw value.");
  }
  
  /**
   * Очищает значение поля.<br/>
   * После очистки значения поля, все узлы дерева сворачиваются.<br/>
   * Карта Просмотра не очищается, т.к. в данном компоненте она не используется.
   */
  @Override
  public void clear() {
    checkedValues = null;
    editableCard.clearSelection();
    expandedValues = null;
    editableCard.collapseAll();
  }
  
  /**
   * Добавление слушателя определенного типа собитий.<br/>
   * Концепция поддержки обработки событий и пример реализации метода отражен в описании пакета {@link com.technology.jep.jepria.client.widget}.
   *
   * @param eventType тип события
   * @param listener слушатель
   */
  @Override
  public void addListener(JepEventType eventType, JepListener listener) {
    switch(eventType) {
      case CHANGE_CHECK_EVENT:
        addChangeCheckListener();
        break;
    }
    super.addListener(eventType, listener);
  }
  
  /**
   * Добавление прослушивателей для реализации прослушивания события 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_CHECK_EVENT }.
   */
  protected void addChangeCheckListener() {
    editableCard.addCheckChangeHandler(new CheckChangeHandler<JepOption>() {
      @Override
      public void onCheckChange(CheckChangeEvent<JepOption> event) {
        notifyListeners(CHANGE_CHECK_EVENT, new JepEvent(MenuTreeField.this, event));
      }
    });
  }
  
  /**
   * Установка видимости флага "Выделить все".<br>
   * По умолчанию флаг невидим.
   * @param visible если true, то показать, в противном случае - скрыть
   */
  public void setSelectAllCheckBoxVisible(boolean visible) {
    editableCard.setSelectAllCheckBoxVisible(visible);
  }
  
  @Override
  protected void setWebIds() {
    super.setWebIds();
    editableCard.setCompositeWebIds(fieldIdAsWebEl);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void addEditableCard() {
    editableCard = new NavigationTreeField<JepOption>(fieldIdAsWebEl){
      @Override
      public void initWidget(Widget w){
        super.initWidget(w);
        mainTreeFieldWidget = w;
        innerTreeFieldWidget = widgetPanel;
      }
      
      @Override
      public void ensureVisible(final JepOption value){
        // prevent scrolling tree
      }
      
      @Override
      public Cell<JepOption> getTreeCell(){
        return new TreeCell(CONTEXTMENU){
          protected SafeHtml getCheckBoxHtml(JepOption value){
            if (!isLeaf(value)){
              nodeCheckedState = JEP_TREENODE_CHECKEDSTATE_VALUE_PARTIAL;
              return new SafeHtmlBuilder().toSafeHtml();
            }
            // 1st part of Composite cell - Show a checkbox image and select it "selected" property is true
            final SafeHtml checkImageHtml;
            
            if (checkedState == null) {
              checkImageHtml = SafeHtmlUtils.EMPTY_SAFE_HTML;
              nodeCheckedState = JEP_TREENODE_CHECKEDSTATE_VALUE_UNCHECKABLE;
            } else {
              final ImageResource checkImg;
              switch (checkedState) {
                case 0: {
                  String image = value.get(IMAGE); 
                  checkImg = JepRiaUtil.isEmpty(image) ? INSTANCE.unchecked() : CustomNavigationMenu.getMenuByImageId(image).getImage();
                  nodeCheckedState = JEP_TREENODE_CHECKEDSTATE_VALUE_UNCHECKED;
                  break;
                }
                case 1: {
                  String image = value.get(IMAGE); 
                  checkImg = JepRiaUtil.isEmpty(image) ? INSTANCE.checked() : CustomNavigationMenu.getMenuByImageId(image).getImage();
                  nodeCheckedState = JEP_TREENODE_CHECKEDSTATE_VALUE_CHECKED;
                  break;
                }
                case 2: default: {
                  checkImg = INSTANCE.partialChecked();
                  nodeCheckedState = JEP_TREENODE_CHECKEDSTATE_VALUE_PARTIAL;
                  break;
                }
              }
              checkImageHtml = AbstractImagePrototype.create(checkImg).getSafeHtml();
            }
            SafeStylesBuilder cssBuilder = new SafeStylesBuilder();
            cssBuilder.appendTrustedString("vertical-align: middle;");
            String url = (String) value.get(REQUEST_URL);
            if (JepRiaUtil.isEmpty(url)) url = "";
            return treeFieldTemplate.imageWrapper(url, cssBuilder.toSafeStyles(), checkImageHtml);
          }
          
          protected SafeHtml getLabelHtml(JepOption value){
              boolean isLeaf = isLeaf(value);
              // Build the css.
              SafeStylesBuilder cssBuilder = new SafeStylesBuilder();
              if ( checkedState != null) {
                cssBuilder.appendTrustedString("cursor: pointer;");
              }
              // 3nd part of Composite cell - Show a label for tree node
              return treeFieldTemplate.labelWrapper(
                  (isLeaf ? "" : FOLDER_LABEL_ID_PREFIX) + value.<String>get(KEY_OPTION_PROPERTY),
                  	value.getName(),
                      cssBuilder.toSafeStyles(),
                        SafeHtmlUtils.fromString(PADDING),
                          value.getName());
          }
          
          @Override
          public void onBrowserEvent(Context context, Element parent, JepOption value,
              NativeEvent event, ValueUpdater<JepOption> valueUpdater) {
              if (isLeaf(value) && CONTEXTMENU.equals(event.getType())) {
                openContextMenu(value, new ElementSimplePanel(parent), event);
              }
              else {
                super.onBrowserEvent(context, parent, value, event, valueUpdater);
              }
          }
        };
      }
      
      @Override
      public void setHeight(String newHeight){
        super.setHeight(newHeight);
        mainTreeFieldWidget.setHeight(newHeight);
        innerTreeFieldWidget.setHeight(newHeight);
      }
      
      @Override
      protected SelectAction translateSelectionEvent(CellPreviewEvent<JepOption> event) {
        // Handle the event.
        NativeEvent nativeEvent = event.getNativeEvent();
        if (CLICK.equals(nativeEvent.getType()) && nativeEvent.getCtrlKey()) {
          nativeEvent.preventDefault();
          nativeEvent.stopPropagation();
          JepOption currentNode = event.getValue();
          openNewWindow(currentNode);
          return SelectAction.IGNORE;
        }
        return super.translateSelectionEvent(event);
      }
    };
    editableCard.setImageResources(NavigationTreeFieldResources.INSTANCE);
    editablePanel.add(editableCard);
  }
   
  /**
   * {@inheritDoc}
   */
  @Override
  public void setFieldWidth(int fieldWidth){
    String newWidth = fieldWidth + Unit.PX.getType();
    editablePanel.setWidth(newWidth);
    editablePanel.setCellWidth(editableCard, newWidth);
    editableCard.setWidth(newWidth);

    mainTreeFieldWidget.setWidth(newWidth);
    innerTreeFieldWidget.setWidth(newWidth);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void applyStyle(){
    super.applyStyle();
    getElement().getStyle().clearMarginBottom();
  }
  
  public void setExpandedAndSelected(List<JepOption> expandedValues, List<JepOption> checkedValues){
    this.expandedValues = new ArrayList<JepOption>(expandedValues);
    this.checkedValues = new ArrayList<JepOption>(checkedValues);
    processExpanding();
  }

  /**
   * Указывает, какие узлы необходимо раскрыть.
   */
  public void setExpanded(List<JepOption> expandedValues) {
    // Создание копии списка элементов важно, поскольку в методе processExpanding
    // происходит удаление элементов списка, что может привести к потенциальным ошибкам 
    // в клиентских модулях
    this.expandedValues = new ArrayList<JepOption>(expandedValues);
    processExpanding();
  }

  /**
   * Раскрывает отрисованные узлы и удаляет их из списка узлов, которые необходимо раскрыть 
   * {@link com.technology.jep.jepria.client.widget.field.multistate.JepTreeField#expandedValues}.
   */
  protected void processExpanding() {
    if (expandedValues != null && expandedValues.size() > 0) {
      // boolean isAlreadyLoaded = false;
       JepOption option = expandedValues.get(0);
         // Удаляем значение, т.к. открытие узлов - это разовая (в данном случае) операция
         // и НЕ нужно повторно открывать указанные узлы (которые пользователь, возможно, уже закрыл).
       editableCard.setExpanded(option, true);
       if (editableCard.isNodeOpened(option)) {
         expandedValues.remove(option);
       }
       Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
         @Override
         public void execute() {
           processExpanding();
         }
       });
     } else {
       processChecking();
     }
  }
  
  /**
   * Метод раскрытия контекстного меню для данного узла дерева.
   * 
   * @param option    опция, соответствующая текущему узлу дерева
   * @param element   соответствующий DOM-элемент, относительно которого появится меню
   * @param event     нативное событие генерирующее раскрытие контекстного меню
   */
  protected abstract void openContextMenu(JepOption option, UIObject element, NativeEvent event);
  
  /**
   * Метод открытия меню в новом окне.
   * 
   * @param option    открытие в новом окне/вкладке текущего пункта меню 
   */
  protected abstract void openNewWindow(JepOption option);
}
