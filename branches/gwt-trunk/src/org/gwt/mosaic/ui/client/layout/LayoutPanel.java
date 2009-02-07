/*
 * Copyright 2008-2009 Georgios J. Georgopoulos
 * Copyright 2008 Cameron Braid.
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
import org.gwt.mosaic.ui.client.CollapsedListener;
import org.gwt.mosaic.ui.client.DecoratedLayoutPopupPanel;
import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.event.CollapseHandler;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.google.gwt.widgetideas.client.ResizableWidgetCollection;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class LayoutPanel extends AbsolutePanel implements HasLayoutManager {

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

  /**
   * Creates a new <code>LayoutPanel</code> with <code>FillLayout</code>.
   */
  public LayoutPanel() {
    this(new FillLayout());
  }

  /**
   * Creates a LayoutPanel with the given element. This is protected so that it
   * can be used by a subclass that wants to substitute another element. The
   * element is presumed to be a &lt;div&gt;.
   * 
   * @param elem the element to be used for this panel.
   */
  protected LayoutPanel(Element elem) {
    super(elem);
    
    // Setting the panel's position style to 'relative' causes it to be treated
    // as a new positioning context for its children.
    DOM.setStyleAttribute(getElement(), "position", "relative");
    DOM.setStyleAttribute(getElement(), "overflow", "hidden");
    
    setStyleName(DEFAULT_STYLENAME);
    setLayout(new FillLayout());
  }

  /**
   * Creates a new <code>LayoutPanel</code> with the specified layout manager.
   * 
   * @param layout the <code>LayoutManager</code> to use
   */
  public LayoutPanel(LayoutManager layout) {
    super();
    setStyleName(DEFAULT_STYLENAME);
    setLayout(layout);
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
    BaseLayout.setLayoutData(widget, layoutData);
    if (layoutData.hasDecoratorPanel()) {
      final DecoratorPanel decPanel = layoutData.decoratorPanel;
      decPanel.setWidget(widget);
      decPanel.setVisible(widget.isVisible());
      super.add(decPanel);
    } else {
      super.add(widget);
    }
  }

  private Widget getDecoratorWidget(Widget widget) {
    LayoutData layoutData = (LayoutData) BaseLayout.getLayoutData(widget);
    if (layoutData != null && layoutData.hasDecoratorPanel()) {
      return layoutData.decoratorPanel;
    }
    return widget;
  }

  public LayoutManager getLayout() {
    return layout;
  }

  public int getPadding() {
    return DOM.getIntStyleAttribute(getElement(), "padding");
  }

  private int[] preferredSizeCache = {-1, -1};

  void setPreferredSize(int width, int height) {
    preferredSizeCache[0] = width;
    preferredSizeCache[1] = height;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayout#getPreferredSize()
   */
  public int[] getPreferredSize() {
    if (preferredSizeCache[0] >= -1 && preferredSizeCache[1] >= 0) {
      return preferredSizeCache;
    } else {
      return layout.getPreferredSize(this);
    }
  }

  private Widget getUnDecoratedWidget(Widget widget) {
    if (widget instanceof DecoratorPanel) {
      widget = ((DecoratorPanel) widget).getWidget();
      LayoutData layoutData = (LayoutData) BaseLayout.getLayoutData(widget);
      if (layoutData != null && layoutData.hasDecoratorPanel()) {
        return layoutData.decoratorPanel.getWidget();
      }
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

  /**
   * @param w
   * @param layoutData
   */
  public void insert(Widget w, LayoutData layoutData, int beforeIndex) {
    if (w instanceof DecoratorPanel) {
      throw new IllegalArgumentException(
          "Adding a DecoratorPanel is not allowed!");
    }
    BaseLayout.setLayoutData(w, layoutData);
    if (layoutData.hasDecoratorPanel()) {
      final DecoratorPanel decPanel = layoutData.decoratorPanel;
      decPanel.setWidget(w);
      super.insert(decPanel, getElement(), beforeIndex, true);
    } else {
      super.insert(w, getElement(), beforeIndex, true);
    }
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

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayout#layout()
   */
  public void layout() {
    layout.layoutPanel(this);
    if (layout.runTwice()) {
      layout.layoutPanel(this);
    }
    layoutChildren();
  }

  /**
   * 
   */
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

  @Override
  public boolean remove(Widget w) {
    final Widget widget = getDecoratorWidget(w);
    if (w != widget) {
      ((DecoratorPanel) widget).remove(w);
    }
    return super.remove(widget);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayout#setLayout(org.mosaic.ui.client.layout.LayoutManager)
   */
  public void setLayout(LayoutManager layout) {
    this.layout = layout;
    if (layoutClassName != null) {
      removeStyleName(layoutClassName);
    }
    layoutClassName = layout.getClass().getName();
    final int dotPos = layoutClassName.lastIndexOf('.');
    layoutClassName = layoutClassName.substring(dotPos + 1,
        layoutClassName.length());
    addStyleName(getStylePrimaryName() + "-" + layoutClassName);

    // System.out.println(getStyleName());
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
  public void setWidgetPosition(Widget w, int left, int top) {
    super.setWidgetPosition(getDecoratorWidget(w), left, top);
  }

  public void setWidgetSpacing(int widgetSpacing) {
    this.widgetSpacing = widgetSpacing;
  }

  public boolean isCollapsed(Widget widget) {
    if (getLayout() instanceof BorderLayout) {
      final BorderLayout borderLayout = (BorderLayout) getLayout();
      return borderLayout.isCollapsed(this, widget);
    }
    return false;
  }

  public void setCollapsed(Widget widget, boolean collapse) {
    if (getLayout() instanceof BorderLayout) {
      final BorderLayout borderLayout = (BorderLayout) getLayout();
      borderLayout.setCollapsed(this, widget, collapse);
    }
  }

  @Override
  protected void onLoad() {
    super.onLoad();

    Widget parent = getParent();

    if (parent == getDecoratorWidget(this)) {
      parent = parent.getParent();
    } else {
      if (parent instanceof LayoutComposite) {
        parent = parent.getParent();
        if (parent == getDecoratorWidget(parent)) {
          parent = parent.getParent();
        }
      }
      if (parent instanceof FormPanel) {
        parent = parent.getParent();
        if (parent == getDecoratorWidget(parent)) {
          parent = parent.getParent();
        }
      }
      if (parent instanceof DecoratorPanel) {
        if (parent.getParent() instanceof DecoratedLayoutPopupPanel) {
          parent = parent.getParent();
        }
      }
    }

    if (parent instanceof HasLayoutManager || parent instanceof Viewport) {
      return;
    }

//    GWT.log("Parent of '" + this.getClass().getName() + "' ('"
//        + parent.getClass().getName()
//        + "') is not an instance of HasLayoutManager.", null);

    // Set the initial size & layout
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        if (width != null && height != null) {
          LayoutPanel.super.setWidth(width);
          LayoutPanel.super.setHeight(height);
        } else {
          final int[] size = getPreferredSize();
          if (width != null) {
            LayoutPanel.super.setWidth(width);
            BaseLayout.setSize(LayoutPanel.this, -1, size[1]);
          } else if (height != null) {
            BaseLayout.setSize(LayoutPanel.this, size[0], -1);
            LayoutPanel.super.setHeight(height);
          } else {
            BaseLayout.setSize(LayoutPanel.this, size[0], size[1]);
          }
        }
        layout();
      }
    });

    // Add to Resizable Collection
    ResizableWidgetCollection.get().add(new ResizableWidget() {
      public Element getElement() {
        return LayoutPanel.this.getElement();
      }

      public boolean isAttached() {
        return LayoutPanel.this.isAttached();
      }

      public void onResize(int width, int height) {
        LayoutPanel.this.layout();
      }
    });
  }

  private String height;

  @Override
  public void setHeight(String height) {
    if (!isAttached()) {
      this.height = height;
    } else {
      super.setHeight(height);
    }
  }

  private String width;

  @Override
  public void setWidth(String width) {
    if (!isAttached()) {
      this.width = width;
    } else {
      super.setWidth(width);
    }
  }

  public HandlerRegistration addCollapseHandler(Widget widget, CollapseHandler handler) {
	  if (getLayout() instanceof BorderLayout) {
        final BorderLayoutData layoutData = (BorderLayoutData) BaseLayout.getLayoutData(widget);
        return layoutData.addCollapseHandler(handler);
      }
	  else {
		  return null;
	  }
  }

  @Deprecated
  public void addCollapsedListener(Widget widget, CollapsedListener listener) {
    if (getLayout() instanceof BorderLayout) {
      final BorderLayoutData layoutData = (BorderLayoutData) BaseLayout.getLayoutData(widget);
      layoutData.addCollapsedListener(listener);
    }
  }

  @Deprecated
  public void removeCollapsedListener(Widget widget, CollapsedListener listener) {
    if (getLayout() instanceof BorderLayout) {
      final BorderLayoutData layoutData = (BorderLayoutData) BaseLayout.getLayoutData(widget);
      layoutData.removeCollapsedListener(listener);
    }
  }
}