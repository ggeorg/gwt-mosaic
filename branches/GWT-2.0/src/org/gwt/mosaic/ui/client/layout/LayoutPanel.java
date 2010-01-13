/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwt.mosaic.ui.client.layout;

import java.util.Iterator;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.core.client.util.DefaultUnitConverter;
import org.gwt.mosaic.core.client.util.UnitConverter;
import org.gwt.mosaic.ui.client.CollapsedListener;
import org.gwt.mosaic.ui.client.DecoratedLayoutPopupPanel;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.LayoutPopupPanel;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.layout.LayoutData.ParsedSize;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.ElementParserToUse;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.AttachDetachException;
import com.google.gwt.user.client.ui.AttachDetachHelper;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.google.gwt.widgetideas.client.ResizableWidgetCollection;

/**
 * An {@code AbsolutePanel}
 * 
 * .mosaic-LayoutPanel
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ElementParserToUse(className = "org.gwt.mosaic.ui.elementparsers.LayoutPanelParser")
public class LayoutPanel extends AbsolutePanel implements HasLayoutManager,
    HasAnimation, DropController {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-LayoutPanel";

  /**
   * Layout manager for this panel.
   */
  private LayoutManager layout;

  private String layoutClassName;

  private int widgetSpacing = 4;

  private Dimension preferredSizeCache = new Dimension(-1, -1);

  private String onLoadHeight;

  private String onLoadWidth;

  private boolean invalid = true;

  private UnitConverter unitConverter;

  /**
   * Creates a new {@code LayoutPanel} with {@link FillLayout}.
   */
  public LayoutPanel() {
    this(new FillLayout());
  }

  /**
   * Creates a {@code LayoutPanel} with {@link FillLayout} and with the given
   * element. This is protected so that it can be used by a subclass that wants
   * to substitute another element. The element is presumed to be a &lt;div&gt;.
   * 
   * @param elem the element to be used for this panel.
   */
  protected LayoutPanel(Element elem) {
    this(elem, new FillLayout());
  }

  /**
   * Creates a {@code LayoutPanel} with the specified layout manager and with
   * the given element. This is protected so that it can be used by a subclass
   * that wants to substitute another element. The element is presumed to be a
   * &lt;div&gt;.
   * 
   * @param elem the element to be used for this panel.
   * @param layout the {@link LayoutManager} to use
   */
  protected LayoutPanel(Element elem, LayoutManager layout) {
    super(elem);

    // Setting the panel's position style to 'relative' causes it to be treated
    // as a new positioning context for its children.
    DOM.setStyleAttribute(getElement(), "position", "relative");
    DOM.setStyleAttribute(getElement(), "overflow", "hidden");

    setStyleName(DEFAULT_STYLENAME);
    setLayout(layout);
  }

  /**
   * Creates a new {@code LayoutPanel} with the specified layout manager.
   * 
   * @param layout the {@code LayoutManager} to use
   */
  public LayoutPanel(LayoutManager layout) {
    super();
    setStyleName(DEFAULT_STYLENAME);
    setLayout(layout);
  }

  /**
   * Adds a child widget to this panel.
   * 
   * @param w the child widget to be added
   */
  @Override
  public void add(Widget w) {
    addImpl(w);
    invalidate(w);
  }

  /**
   * Appends the specified widget to the end of this container.
   * 
   * @param widget
   * @param layoutData
   */
  public void add(Widget widget, LayoutData layoutData) {
    if (widget instanceof DecoratorPanel) {
      throw new IllegalArgumentException(
          "Adding a DecoratorPanel is not allowed!");
    }
    if (widget instanceof FormPanel) {
      // (ggeorg) see WidgetHelper.setBounds() why
      DOM.setStyleAttribute(widget.getElement(), "border", "none");
      DOM.setStyleAttribute(widget.getElement(), "padding", "0px");
      DOM.setStyleAttribute(widget.getElement(), "margin", "0px");
    }
    widget.setLayoutData(layoutData);
    if (layoutData.hasDecoratorPanel()) {
      final DecoratorPanel decPanel = layoutData.decoratorPanel;
      add(decPanel);
      decPanel.setWidget(widget);
    } else {
      add(widget);
    }
  }

  protected void addImpl(Widget w) {
    super.add(w);
  }

  private void clearPreferredSizeCache() {
    preferredSizeCache.setSize(-1, -1);
  }

  public Widget findParent() {
    Widget parent = getParent();

    if (parent == getDecoratorWidget(this)) {
      parent = parent.getParent();
    } else {
      if (parent instanceof Viewport) {
        return parent;
      } else if (parent instanceof LayoutComposite
          || parent instanceof Composite) {
        Widget thiz = parent;
        parent = thiz.getParent();
        if (parent == getDecoratorWidget(thiz)) {
          parent = parent.getParent();
        }
      }
      if (parent instanceof FormPanel) {
        Widget thiz = parent;
        parent = thiz.getParent();
        if (parent == getDecoratorWidget(thiz)) {
          parent = parent.getParent();
        }
      }
      if (parent instanceof DecoratorPanel) {
        if (parent.getParent() instanceof DecoratedLayoutPopupPanel) {
          parent = parent.getParent();
        }
      }
    }

    return parent;
  }

  /**
   * @return the widget's DecoratorPanel if any, else widget.
   */
  private Widget getDecoratorWidget(Widget widget) {
    LayoutData layoutData = (LayoutData) widget.getLayoutData();
    if (layoutData != null && layoutData.hasDecoratorPanel()) {
      return layoutData.decoratorPanel;
    }
    return widget;
  }

  /**
   * Returns the {@link LayoutManager} which is associated with this panel, or
   * {@link FillLayout} if one has not been set.
   * 
   * @return the panel's {@link LayoutManager}
   * 
   * @see #layout()
   * @see #setLayout(LayoutManager)
   */
  public LayoutManager getLayout() {
    return layout;
  }

  public int getPadding() {
    return DOM.getIntStyleAttribute(getElement(), "padding");
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.mosaic.ui.client.layout.HasLayout#getPreferredSize()
   */
  public Dimension getPreferredSize() {
    if (!isAttached()) {
      return new Dimension();
    }

    preferredSizeCache = layout.getPreferredSize(this);

    // XXX get text line wrapping working...
    WidgetHelper.setSize(this, preferredSizeCache);
    layout.layoutPanel(this);
    preferredSizeCache = layout.getPreferredSize(this);

    return preferredSizeCache;
  }

  private Widget getUnDecoratedWidget(Widget widget) {
    if (widget instanceof DecoratorPanel) {
      return ((DecoratorPanel) widget).getWidget();
    }
    return widget;
  }

  @Override
  public Widget getWidget(int index) {
    return getUnDecoratedWidget(super.getWidget(index));
  }

  @Override
  public int getWidgetIndex(Widget child) {
    return super.getWidgetIndex(getDecoratorWidget(child));
  }

  /**
   * Gets the position of the left outer border edge of the widget relative to
   * the left outer border edge of the panel.
   * 
   * @param w the widget whose position is to be retrieved
   * @return the widget's left position
   */
  @Override
  public int getWidgetLeft(Widget w) {
    return super.getWidgetLeft(getDecoratorWidget(w));
  }

  public int getWidgetSpacing() {
    return widgetSpacing;
  }

  /**
   * Gets the position of the top outer border edge of the widget relative to
   * the top outer border edge of the panel.
   * 
   * @param w the widget whose position is to be retrieved
   * @return the widget's top position
   */
  @Override
  public int getWidgetTop(Widget w) {
    return super.getWidgetTop(getDecoratorWidget(w));
  }

  @Override
  protected void insert(Widget child, Element container, int beforeIndex,
      boolean domInsert) {
    if (child instanceof DecoratorPanel) {
      throw new IllegalArgumentException(
          "Adding a DecoratorPanel is not allowed!");
    }
    final Object layoutDataObject = child.getLayoutData();
    if (layoutDataObject != null && layoutDataObject instanceof LayoutData
        && ((LayoutData) layoutDataObject).hasDecoratorPanel()) {
      final DecoratorPanel decPanel = ((LayoutData) layoutDataObject).decoratorPanel;
      super.insert(decPanel, getElement(), beforeIndex, true);
      decPanel.setWidget(child);
    } else {
      super.insert(child, container, beforeIndex, domInsert);
    }
    invalidate(child);
  }

  /**
   * @param w
   * @param layoutData
   */
  public void insert(Widget w, LayoutData layoutData, int beforeIndex) {
    if (w instanceof DecoratorPanel) {
      throw new IllegalArgumentException(
          "Adding a DecoratorPanel is not allowed!");
    }
    w.setLayoutData(layoutData);
    insert(w, getElement(), beforeIndex, true);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#needsLayout()
   */
  public boolean needsLayout() {
    return invalid;
  }

  public void invalidate() {
    invalidate(null);
  }

  public void invalidate(Widget widget) {
    getLayout().invalidateLayout(widget);

    if (invalid) {
      return;
    }

    invalid = true;

    clearPreferredSizeCache();

    final Widget parent = findParent();
    if (parent instanceof HasLayoutManager && !(parent instanceof Viewport)
        && !(parent instanceof DecoratedLayoutPopupPanel)
        && !(parent instanceof LayoutPopupPanel)) {
      ((HasLayoutManager) parent).invalidate(this.getParent());
      ((HasLayoutManager) parent).invalidate(this);
    }
  }

  @Override
  protected void doAttachChildren() {
    AttachDetachException.tryCommand(this, new AttachDetachException.Command() {
      public void execute(Widget w) {
        AttachDetachHelper.onAttach(getDecoratorWidget(w));
      }
    });
  }

  @Override
  protected void doDetachChildren() {
    AttachDetachException.tryCommand(this, new AttachDetachException.Command() {
      public void execute(Widget w) {
        AttachDetachHelper.onDetach(getDecoratorWidget(w));
      }
    });
  }

  @Override
  public Iterator<Widget> iterator() {
    return new Iterator<Widget>() {
      final Iterator<Widget> iter = LayoutPanel.super.iterator();

      public boolean hasNext() {
        return iter.hasNext();
      }

      public Widget next() {
        return getUnDecoratedWidget(iter.next());
      }

      public void remove() {
        iter.remove();
      }
    };
  }

  public void layout() {
    // if (invalid) { TODO: after we cache width & size for each child widget
    if (isAttached() && isVisible()) {
      onLayout();
      layout.layoutPanel(this);
      invalid = false;
      layoutChildren();
    }
  }

  protected void layoutChildren() {
    final int count = getWidgetCount();
    for (int i = 0; i < count; i++) {
      Widget child = getWidget(i);
      if (child instanceof DecoratorPanel) {
        child = ((DecoratorPanel) child).getWidget();
      }
      if (child instanceof FormPanel) {
        child = ((FormPanel) child).getWidget();
      }
      if (child instanceof HasLayoutManager
          && DOM.isVisible(child.getElement())) {
        ((HasLayoutManager) child).layout();
      }
    }
  }

  protected void onLayout() {
    getElement().setScrollTop(0);
    getElement().setScrollLeft(0);
  }

  @Override
  protected void onLoad() {
    super.onLoad();

    Widget parent = findParent();

    if (parent instanceof HasLayoutManager || parent instanceof Viewport) {
      return;
    }

    GWT.log("Parent of '" + this.getClass().getName() + "' ('"
        + parent.getClass().getName()
        + "') is not an instance of HasLayoutManager!!!", null);

    // Set the initial size & layout

    if (onLoadWidth != null && onLoadHeight != null) {
      setSize(onLoadWidth, onLoadHeight);
      onLoadWidth = onLoadHeight = null;
    } else {
      Dimension d = getPreferredSize();
      if (onLoadWidth != null) {
        setSize(onLoadWidth, d.height + "px");
        onLoadWidth = null;
      } else if (onLoadHeight != null) {
        setSize(d.width + "px", onLoadHeight);
        onLoadHeight = null;
      } else {
        setSize(d.width + "px", d.height + "px");
      }
    }

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        layout();
      }
    });

    // Add to Resizable Collection
    if (resizableWidget != null) {
      ResizableWidgetCollection.get().remove(resizableWidget);
      ResizableWidgetCollection.get().add(resizableWidget);
    }
  }

  /**
   * Set the {@code ResizableWidget} to add to a {@code
   * ResizableWidgetCollection} that periodically checks the outer dimensions of
   * a widget and redraws it as necessary.
   * 
   * @param resizableWidget the {@code ResizableWidget}
   */
  public void setResizableWidget(ResizableWidget resizableWidget) {
    ResizableWidget oldResizableWidget = this.resizableWidget;
    this.resizableWidget = resizableWidget;
    if (isAttached()) {
      if (oldResizableWidget != null) {
        ResizableWidgetCollection.get().remove(resizableWidget);
      }
      if (resizableWidget != null) {
        ResizableWidgetCollection.get().add(resizableWidget);
      }
    }
  }

  /**
   * Gets the {@code ResizableWidget} used.
   * 
   * @return the {@code ResizableWidget}
   */
  public ResizableWidget getResizableWidget() {
    return resizableWidget;
  }

  private ResizableWidget resizableWidget = new ResizableWidget() {
    public Element getElement() {
      return LayoutPanel.this.getElement();
    }

    public boolean isAttached() {
      return LayoutPanel.this.isAttached();
    }

    public void onResize(int width, int height) {
      LayoutPanel.this.layout();
    }
  };

  /**
   * Removes a child widget to this panel.
   * 
   * @param w the child widget to be removed
   */
  @Override
  public boolean remove(Widget w) {
    // XXX workaround for: iter.remove();
    if (w instanceof DecoratorPanel) {
      w = getUnDecoratedWidget(w);
    }

    final Widget widget = getDecoratorWidget(w);
    if (widget instanceof DecoratorPanel) {
      ((DecoratorPanel) widget).remove(w);
    }

    if (removeImpl(widget)) {
      //invalidate(w);
      invalidate();
      return true;
    } else {
      return false;
    }
  }

  protected boolean removeImpl(Widget w) {
    return super.remove(w);
  }

  // Helper methods for Collapsible child widgets **************************

  public boolean isCollapsed(Widget widget) {
    if (getLayout() instanceof HasCollapsibleWidgets) {
      return ((HasCollapsibleWidgets) getLayout()).isCollapsed(this, widget);
    }
    return false;
  }

  public void setCollapsed(Widget widget, boolean collapse) {
    if (getLayout() instanceof HasCollapsibleWidgets) {
      if (collapse != isCollapsed(widget)) {
        ((HasCollapsibleWidgets) getLayout()).setCollapsed(this, widget,
            collapse);
        invalidate(widget);
      }
    }
  }

  public void addCollapsedListener(Widget widget, CollapsedListener listener) {
    if (getLayout() instanceof HasCollapsibleWidgets) {
      ((Collapsible) widget.getLayoutData()).addCollapsedListener(listener);
    }
  }

  public void removeCollapsedListener(Widget widget, CollapsedListener listener) {
    if (getLayout() instanceof HasCollapsibleWidgets) {
      ((Collapsible) widget.getLayoutData()).removeCollapsedListener(listener);
    }
  }

  // -------

  /**
   * Sets the {@link LayoutManager} for this panel.
   * 
   * @param layout the specified layout manager
   * 
   * @see org.mosaic.ui.client.layout.HasLayout#setLayout(org.mosaic.ui.client.layout
   *      .LayoutManager)
   * 
   * @see #layout()
   * @see #setLayout(LayoutManager)
   */
  public void setLayout(LayoutManager layout) {
    this.layout = layout;
    if (layoutClassName != null) {
      removeStyleName(getStylePrimaryName() + "-" + layoutClassName);
    }
    layoutClassName = layout.getClass().getName();
    final int dotPos = layoutClassName.lastIndexOf('.');
    layoutClassName = layoutClassName.substring(dotPos + 1,
        layoutClassName.length());
    addStyleName(getStylePrimaryName() + "-" + layoutClassName);
    invalidate();
  }

  public void setPadding(int padding) {
    DOM.setStyleAttribute(getElement(), "padding", padding + "px");
  }

  /**
   * Sets the position of the specified child widget. Setting a position of
   * <code>(-1, -1)</code> will cause the child widget to be positioned
   * statically.
   * 
   * @param w the child widget to be positioned
   * @param left the widget's left position
   * @param top the widget's top position
   */
  @Override
  public void setWidgetPosition(Widget w, int left, int top) {
    super.setWidgetPosition(getDecoratorWidget(w), left, top);
  }

  public void setWidgetSpacing(int widgetSpacing) {
    this.widgetSpacing = widgetSpacing;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.UIObject#setWidth(java.lang.String)
   */
  @Override
  public void setWidth(String width) {
    super.setWidth(width);
    if (!isAttached()) {
      this.onLoadWidth = width;
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.UIObject#setHeight(java.lang.String)
   */
  @Override
  public void setHeight(String height) {
    super.setHeight(height);

    if (!isAttached()) {
      this.onLoadHeight = height;
    }
  }

  public UnitConverter getUnitConverter() {
    if (unitConverter == null) {
      DefaultUnitConverter unitConverter = DefaultUnitConverter.getInstance();
      unitConverter.setFontSize(toPixelSize("1em", true));
      unitConverter.setXHeight(toPixelSize("1ex", true));
      this.unitConverter = unitConverter;
    }
    return unitConverter;
  }

  private Element toPixelSizeTestElem = null;

  int toPixelSize(final ParsedSize parsedSize, final boolean useWidthAttribute) {
    Unit unit = parsedSize.getUnit();
    if (unit == Unit.CM) {
      return getUnitConverter().centimeterAsPixel(parsedSize.getSize());
    } else if (unit == Unit.EM) {
      return getUnitConverter().fontSizeAsPixel(parsedSize.getSize());
    } else if (unit == Unit.EX) {
      return getUnitConverter().xHeightAsPixel(parsedSize.getSize());
    } else if (unit == Unit.IN) {
      return getUnitConverter().inchAsPixel(parsedSize.getSize());
    } else if (unit == Unit.MM) {
      return getUnitConverter().millimeterAsPixel(parsedSize.getSize());
    } else if (unit == Unit.PC) {
      return getUnitConverter().picaAsPixel(parsedSize.getSize());
    } else if (unit == Unit.PCT) {
      return toPixelSize(parsedSize.getValue(), useWidthAttribute);
    } else if (unit == Unit.PT) {
      return getUnitConverter().pointAsPixel(
          (int) Math.round(parsedSize.getSize()));
    } else if (parsedSize.getUnit() == Unit.PX) {
      return (int) Math.round(parsedSize.getSize());
    }
    throw new IllegalArgumentException("Invalid size: " + parsedSize.getValue());
  }

  protected int toPixelSize(final String value, final boolean useWidthAttribute) {
    if (toPixelSizeTestElem == null) {
      toPixelSizeTestElem = DOM.createSpan();
      DOM.setStyleAttribute(toPixelSizeTestElem, "position", "absolute");
      DOM.setStyleAttribute(toPixelSizeTestElem, "visibility", "hidden");
      DOM.setStyleAttribute(toPixelSizeTestElem, "left", "0px");
      DOM.setStyleAttribute(toPixelSizeTestElem, "top", "0px");
      getElement().appendChild(toPixelSizeTestElem);
      // DOM.setStyleAttribute(toPixelSizeTestElem, "background", "red");
    }
    DOM.setStyleAttribute(toPixelSizeTestElem, "width", value);
    DOM.setStyleAttribute(toPixelSizeTestElem, "height", value);

    Dimension size = DOM.getBoxSize(toPixelSizeTestElem);

    return (useWidthAttribute) ? size.width : size.height;
  }

  private boolean animationEnabled;

  public boolean isAnimationEnabled() {
    return animationEnabled;
  }

  public void setAnimationEnabled(final boolean enable) {
    this.animationEnabled = enable;
  }

  /**
   * Used by UiBinder.
   * 
   * @param enable
   */
  public void setAnimationEnabled(String enable) {
    enable = enable.trim().toLowerCase();
    if (enable.equals("true".intern())) {
      setAnimationEnabled(true);
    } else if (enable.equals("false".intern())) {
      setAnimationEnabled(false);
    }
  }

  AnimationCallback animationCallback;

  /**
   * @return the animationCallback
   */
  public AnimationCallback getAnimationCallback() {
    return animationCallback;
  }

  /**
   * @param animationCallback the animationCallback to set
   */
  public void setAnimationCallback(AnimationCallback animationCallback) {
    this.animationCallback = animationCallback;
  }

  // Drag & Drop support ---------------------------------------------------

  private LayoutPanelDragController dragController;

  public void enableDragAndDrop(boolean allowDroppingOnBoundaryPanel) {
    if (dragController != null) {
      dragController.unregisterDropControllers();
      dragController.clearSelection();
      dragController.resetCache();
    }
    dragController = new LayoutPanelDragController(this,
        allowDroppingOnBoundaryPanel);
    dragController.setBehaviorMultipleSelection(false);
    dragController.setBehaviorDragProxy(true);
  }

  public boolean isDragAndDropEnabled() {
    return dragController != null;
  }

  public void makeDraggable(Widget draggable) {
    if (isDragAndDropEnabled()) {
      dragController.makeDraggable(draggable);
    }
  }

  public void makeDraggable(Widget draggable, Widget dragHandle) {
    if (isDragAndDropEnabled()) {
      dragController.makeDraggable(draggable, dragHandle);
    }
  }

  public void makeNotDraggable(Widget draggable) {
    if (isDragAndDropEnabled()) {
      dragController.makeNotDraggable(draggable);
    }
  }

  public void registerDropController(DropController dropController) {
    if (isDragAndDropEnabled()) {
      dragController.registerDropController(dropController);
    }
  }

  public void unregisterDropController(DropController dropController) {
    if (isDragAndDropEnabled()) {
      dragController.unregisterDropController(dropController);
    }
  }

  private final LayoutPanelDropController dropController = new LayoutPanelDropController(
      this);

  public void unregisterDropControllers() {
    if (isDragAndDropEnabled()) {
      dragController.unregisterDropControllers();
    }
  }

  public Widget getDropTarget() {
    return dropController.getDropTarget();
  }

  public void onDrop(DragContext context) {
    dropController.onDrop(context);
  }

  public void onEnter(DragContext context) {
    dropController.onEnter(context);
  }

  public void onLeave(DragContext context) {
    dropController.onLeave(context);
  }

  public void onMove(DragContext context) {
    dropController.onMove(context);
  }

  public void onPreviewDrop(DragContext context) throws VetoDragException {
    dropController.onPreviewDrop(context);
  }
}
