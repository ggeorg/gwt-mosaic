/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos
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
package org.gwt.mosaic.ui.client;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ToolButton extends LayoutComposite implements HasHTML, HasName,
    SourcesClickEvents, HasClickHandlers, HasFocus, HasAllFocusHandlers,
    HasAllKeyHandlers, HasAllMouseHandlers, SourcesMouseEvents {

  class ButtonWidget extends Button implements HasName {

    private static final String DEFAULT_STYLE_NAME = "mosaic-Button";

    private ToolButtonStyle style = ToolButtonStyle.PUSH;

    private PopupMenu menu;

    /**
     * Creates a button with no caption.
     */
    public ButtonWidget() {
      super();

      // ggeorg: for Event.ONCLICK see issue 39
      sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);

      addStyleName(DEFAULT_STYLE_NAME);
    }

    private void addNewStyleName(String styleName) {
      addStyleName(styleName);
      if (!isEnabled()) {
        addStyleName(styleName + "-disabled");
      }
    }

    public PopupMenu getMenu() {
      return menu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.dev.jjs.ast.HasName#getName()
     */
    public String getName() {
      return DOM.getElementProperty(getElement(), "name");
    }

    public ToolButtonStyle getStyle() {
      return style;
    }

    /**
     * Determines whether this check box is currently checked.
     * 
     * @return <code>true</code> if the check box is checked
     */
    public boolean isChecked() {
      String propName = isAttached() ? "checked" : "defaultChecked";
      return DOM.getElementPropertyBoolean(getElement(), propName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user
     * .client.Event)
     */
    @Override
    public void onBrowserEvent(Event event) {
      switch (DOM.eventGetType(event)) {
        case Event.ONCLICK:
          if (style == ToolButtonStyle.SPLIT) {
            final int[] paddings = DOM.getPaddingSizes(getElement());
            if (getElement().getAbsoluteLeft()
                + (getElement().getOffsetWidth() - paddings[1]) < event.getClientX()) {
              return;
            }
          }
          if (style == ToolButtonStyle.MENU) {
            if (menu != null) {
              final Dimension box = WidgetHelper.getOffsetSize(this);
              final int left = DOM.getAbsoluteLeft(getElement());
              final int top = DOM.getAbsoluteTop(getElement()) + box.height;
              menu.setPopupPosition(left, top);
              menu.show();
            }
            return;
          }
          if (style == ToolButtonStyle.CHECKBOX) {
            setChecked(!isChecked());
          }
          if (style == ToolButtonStyle.RADIO) {
            setChecked(true);
          }
          super.onBrowserEvent(event);
          break;
        case Event.ONMOUSEDOWN:
          if (style == ToolButtonStyle.SPLIT
              && event.getTarget() == getElement()) {
            final int[] p = DOM.getPaddingSizes(getElement());
            if (getElement().getAbsoluteLeft()
                + (getElement().getOffsetWidth() - p[1]) < event.getClientX()) {
              addStyleName("mosaic-Split-Button-activeoption");
              return;
            }
          }
          super.onBrowserEvent(event);
          break;
        case Event.ONMOUSEUP:
          if (style == ToolButtonStyle.SPLIT
              && event.getTarget() == getElement()) {
            final int[] m = DOM.getPaddingSizes(getElement());
            if (getElement().getAbsoluteLeft()
                + (getElement().getOffsetWidth() - m[1]) < event.getClientX()) {
              if (menu != null) {
                // TODO fix that code
                // Region r = DOM.getRegion(getElement());
                final Dimension box = WidgetHelper.getOffsetSize(this);
                final int left = DOM.getAbsoluteLeft(getElement());
                final int top = DOM.getAbsoluteTop(getElement()) + box.height;
                menu.setPopupPosition(left, top);
                menu.show();
              }
              removeStyleName("mosaic-Split-Button-activeoption");
              return;
            }
          }
          super.onBrowserEvent(event);
          break;
        case Event.ONMOUSEOVER:
          if (style == ToolButtonStyle.SPLIT) {
            addStyleName("mosaic-Split-Button-hover");
          }
          super.onBrowserEvent(event);
          break;
        case Event.ONMOUSEOUT:
          if (style == ToolButtonStyle.SPLIT) {
            removeStyleName("mosaic-Split-Button-hover");
          }
          super.onBrowserEvent(event);
          break;
      }
    }

    private void removeOldStyleName(String styleName) {
      removeStyleName(styleName);
      if (!isEnabled()) {
        removeStyleName(styleName + "-disabled");
      }
    }

    /**
     * Checks or unchecks this check box.
     * 
     * @param checked <code>true</code> to check the check box
     */
    public void setChecked(boolean checked) {
      if (style == ToolButtonStyle.RADIO) {
        final Widget parent = ToolButton.this.getParent();
        if (parent instanceof IndexedPanel) {
          final IndexedPanel panel = (IndexedPanel) parent;
          for (int i = 0, n = panel.getWidgetCount(); i < n; i++) {
            final Widget widget = panel.getWidget(i);
            if (widget instanceof ToolButton) {
              final ToolButton toolButton = (ToolButton) widget;
              final String name = toolButton.getName();
              if (toolButton.getStyle() == ToolButtonStyle.RADIO
                  && toolButton.isChecked() && name != null
                  && name.equals(getName())) {
                Element btnE = toolButton.button.getElement();
                DOM.setElementPropertyBoolean(btnE, "checked", false);
                DOM.setElementPropertyBoolean(btnE, "defaultChecked", false);
                toolButton.button.removeStyleName("mosaic-Checkbox-Button-checked");
              }
            }
          }
        }
      }
      DOM.setElementPropertyBoolean(getElement(), "checked", checked);
      DOM.setElementPropertyBoolean(getElement(), "defaultChecked", checked);
      if (checked) {
        addStyleName("mosaic-Checkbox-Button-checked");
      } else {
        removeStyleName("mosaic-Checkbox-Button-checked");
      }
    }

    /**
     * Sets whether this widget is enabled.
     * 
     * @param enabled <code>true</code> to enable the widget, <code>false</code>
     *          to disable it
     */
    public void setEnabled(boolean enabled) {
      super.setEnabled(enabled);
      if (enabled) {
        removeStyleName("mosaic-Button-disabled");
        sinkEvents(Event.ONCLICK | Event.ONMOUSEDOWN | Event.ONMOUSEUP
            | Event.ONMOUSEOVER | Event.ONMOUSEOUT);
        if (this.style == ToolButtonStyle.MENU) {
          removeStyleName("mosaic-Menu-Button-disabled");
        } else if (this.style == ToolButtonStyle.SPLIT) {
          removeStyleName("mosaic-Split-Button-disabled");
        } else if (this.style == ToolButtonStyle.CHECKBOX) {
          removeStyleName("mosaic-Checkbox-Button-disabled");
        }
      } else {
        addStyleName("mosaic-Button-disabled");
        unsinkEvents(Event.ONCLICK | Event.ONMOUSEDOWN | Event.ONMOUSEUP
            | Event.ONMOUSEOVER | Event.ONMOUSEOUT);
        if (style == ToolButtonStyle.MENU) {
          addStyleName("mosaic-Menu-Button-disabled");
        } else if (style == ToolButtonStyle.SPLIT) {
          addStyleName("mosaic-Split-Button-disabled");
        } else if (this.style == ToolButtonStyle.CHECKBOX) {
          addStyleName("mosaic-Checkbox-Button-disabled");
        }
      }
    }

    public void setMenu(PopupMenu menu) {
      this.menu = menu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.HasName#setName(java.lang.String)
     */
    public void setName(String name) {
      DOM.setElementProperty(getElement(), "name", name);
    }

    public void setStyle(ToolButtonStyle style) {
      if (this.style != style) {
        if (this.style == ToolButtonStyle.MENU) {
          removeOldStyleName("mosaic-Menu-Button");
        } else if (this.style == ToolButtonStyle.SPLIT) {
          removeOldStyleName("mosaic-Split-Button");
        } else if (this.style == ToolButtonStyle.CHECKBOX) {
          removeOldStyleName("mosaic-Checkbox-Button");
        }
        if (style == ToolButtonStyle.MENU) {
          addNewStyleName("mosaic-Menu-Button");
        } else if (style == ToolButtonStyle.SPLIT) {
          addNewStyleName("mosaic-Split-Button");
        } else if (this.style == ToolButtonStyle.CHECKBOX) {
          addNewStyleName("mosaic-Checkbox-Button");
        }
        this.style = style;
      }
    }
  }

  public enum ToolButtonStyle {
    PUSH, MENU, SPLIT, RADIO, CHECKBOX
  }

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-ToolButton";

  private final ButtonWidget button = new ButtonWidget();

  @Override
  public Dimension getPreferredSize() {
    return getLayoutPanel().getPreferredSize();
  }

  @Override
  public void invalidate(Widget widget) {
    super.invalidate(widget);
    button.setSize("auto", "auto");
  }

  /**
   * Creates a tool button with no caption.
   */
  public ToolButton() {
    super();
    getLayoutPanel().add(button);
    setStyleName(DEFAULT_STYLENAME);
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing
   * element. This element must be a &lt;button&gt; element.
   * 
   * @param element the element to be used
   */
  protected ToolButton(Element element) {
    // TODO
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a tool button with the given HTML caption.
   * 
   * @param html the HTML caption
   */
  public ToolButton(String html) {
    this();
    setHTML(html);
  }

  /**
   * Creates a tool button with the given HTML caption and click handler.
   * 
   * @param html the HTML caption
   * @param handler the click handler
   */
  public ToolButton(String html, ClickHandler handler) {
    this(html);
    addClickHandler(handler);
  }

  /**
   * Creates a tool button with the given HTML caption and click listener.
   * 
   * @param html the HTML caption
   * @param listener the click listener
   * @deprecated use {@link ToolButton#ToolButton(String, ClickHandler)} instead
   */
  @Deprecated
  public ToolButton(String html, ClickListener listener) {
    this(html);
    addClickListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.event.dom.client.HasBlurHandlers#addBlurHandler(com.google
   * .gwt.event.dom.client.BlurHandler)
   */
  public HandlerRegistration addBlurHandler(BlurHandler handler) {
    return button.addBlurHandler(handler);
  }

  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return button.addClickHandler(handler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.SourcesClickEvents#addClickListener(com.google
   * .gwt.user.client.ui.ClickListener)
   */
  @Deprecated
  public void addClickListener(ClickListener listener) {
    button.addClickListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.event.dom.client.HasFocusHandlers#addFocusHandler(com.google
   * .gwt.event.dom.client.FocusHandler)
   */
  public HandlerRegistration addFocusHandler(FocusHandler handler) {
    return button.addFocusHandler(handler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.SourcesFocusEvents#addFocusListener(com.google
   * .gwt.user.client.ui.FocusListener)
   */
  @Deprecated
  public void addFocusListener(FocusListener listener) {
    button.addFocusListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.SourcesKeyboardEvents#addKeyboardListener
   * (com.google.gwt.user.client.ui.KeyboardListener)
   */
  @Deprecated
  public void addKeyboardListener(KeyboardListener listener) {
    button.addKeyboardListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.event.dom.client.HasKeyDownHandlers#addKeyDownHandler(com
   * .google.gwt.event.dom.client.KeyDownHandler)
   */
  public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
    return button.addKeyDownHandler(handler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.event.dom.client.HasKeyPressHandlers#addKeyPressHandler(
   * com.google.gwt.event.dom.client.KeyPressHandler)
   */
  public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
    return button.addKeyPressHandler(handler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.event.dom.client.HasKeyUpHandlers#addKeyUpHandler(com.google
   * .gwt.event.dom.client.KeyUpHandler)
   */
  public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
    return button.addKeyUpHandler(handler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.event.dom.client.HasMouseDownHandlers#addMouseDownHandler
   * (com.google.gwt.event.dom.client.MouseDownHandler)
   */
  public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
    return button.addMouseDownHandler(handler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.SourcesMouseEvents#addMouseListener(com.google
   * .gwt.user.client.ui.MouseListener)
   */
  @Deprecated
  public void addMouseListener(MouseListener listener) {
    button.addMouseListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.event.dom.client.HasMouseMoveHandlers#addMouseMoveHandler
   * (com.google.gwt.event.dom.client.MouseMoveHandler)
   */
  public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
    return button.addMouseMoveHandler(handler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.event.dom.client.HasMouseOutHandlers#addMouseOutHandler(
   * com.google.gwt.event.dom.client.MouseOutHandler)
   */
  public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
    return button.addMouseOutHandler(handler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.event.dom.client.HasMouseOverHandlers#addMouseOverHandler
   * (com.google.gwt.event.dom.client.MouseOverHandler)
   */
  public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
    return button.addMouseOverHandler(handler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.event.dom.client.HasMouseUpHandlers#addMouseUpHandler(com
   * .google.gwt.event.dom.client.MouseUpHandler)
   */
  public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
    return button.addMouseUpHandler(handler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.event.dom.client.HasMouseWheelHandlers#addMouseWheelHandler
   * (com.google.gwt.event.dom.client.MouseWheelHandler)
   */
  public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
    return button.addMouseWheelHandler(handler);
  }

  @Deprecated
  public void addMouseWheelListener(MouseWheelListener listener) {
    button.addMouseWheelListener(listener);
  }

  /**
   * Programmatic equivalent of the user clicking the button.
   */
  public void click() {
    ButtonElement elem = button.getElement().cast();
    elem.click();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasHTML#getHTML()
   */
  public String getHTML() {
    return button.getHTML();
  }

  public PopupMenu getMenu() {
    return button.getMenu();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasName#getName()
   */
  public String getName() {
    return button.getName();
  }

  public ToolButtonStyle getStyle() {
    return button.getStyle();
  }

  public int getTabIndex() {
    return button.getTabIndex();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#getText()
   */
  public String getText() {
    return button.getText();
  }

  /**
   * Determines whether this check box is currently checked.
   * 
   * @return <code>true</code> if the check box is checked
   */
  public boolean isChecked() {
    return button.isChecked();
  }

  public boolean isEnabled() {
    return button.isEnabled();
  }

  public void removeClickListener(ClickListener listener) {
    button.removeClickListener(listener);
  }

  @Deprecated
  public void removeFocusListener(FocusListener listener) {
    button.removeFocusListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.SourcesKeyboardEvents#removeKeyboardListener
   * (com.google.gwt.user.client.ui.KeyboardListener)
   */
  @Deprecated
  public void removeKeyboardListener(KeyboardListener listener) {
    button.removeKeyboardListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.SourcesMouseEvents#removeMouseListener(com
   * .google.gwt.user.client.ui.MouseListener)
   */
  @Deprecated
  public void removeMouseListener(MouseListener listener) {
    button.removeMouseListener(listener);
  }

  public void setAccessKey(char key) {
    button.setAccessKey(key);
  }

  /**
   * Checks or unchecks this check box.
   * 
   * @param checked <code>true</code> to check the check box
   */
  public void setChecked(boolean checked) {
    if (button.getStyle() == ToolButtonStyle.CHECKBOX
        || button.getStyle() == ToolButtonStyle.RADIO) {
      button.setChecked(checked);
    }
  }

  public void setEnabled(boolean enabled) {
    button.setEnabled(enabled);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Focusable#setFocus(boolean)
   */
  public void setFocus(boolean focused) {
    button.setFocus(focused);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasHTML#setHTML(java.lang.String)
   */
  public void setHTML(String html) {
    button.setHTML(html);
    invalidate(button);
  }

  public void setMenu(PopupMenu menu) {
    button.setMenu(menu);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasName#setName(java.lang.String)
   */
  public void setName(String name) {
    button.setName(name);
  }

  public void setStyle(ToolButtonStyle style) {
    button.setStyle(style);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Focusable#setTabIndex(int)
   */
  public void setTabIndex(int index) {
    button.setTabIndex(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
   */
  public void setText(String text) {
    button.setText(text);
    invalidate(button);
  }

}
