/*
 * Copyright 2008 Georgios J. Georgopoulos.
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
package org.mosaic.ui.client;

import org.mosaic.core.client.DOM;
import org.mosaic.core.client.Region;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesFocusEvents;
import com.google.gwt.user.client.ui.SourcesKeyboardEvents;
import com.google.gwt.user.client.ui.Widget;

public class ToolButton extends LayoutComposite implements HasHTML, HasName,
    SourcesClickEvents, SourcesFocusEvents, HasFocus, SourcesKeyboardEvents {

  public enum ToolButtonStyle {
    PUSH, MENU, SPLIT, RADIO, CHECKBOX
  }

  class ButtonWidget extends Button implements HasName {

    private static final String DEFAULT_STYLE_NAME = "mosaic-Button";

    private ToolButtonStyle style = ToolButtonStyle.PUSH;

    private PopupMenu menu;

    /**
     * Creates a button with no caption.
     */
    public ButtonWidget() {
      super();
      init();
    }

    /**
     * Creates a button with the given HTML caption.
     * 
     * @param html the HTML caption
     */
    public ButtonWidget(String html) {
      super(html);
      init();
    }

    /**
     * Creates a button with the given HTML caption and click listener.
     * 
     * @param html the HTML caption
     * @param listener the click listener
     */
    public ButtonWidget(String html, ClickListener listener) {
      this(html);
      init();
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

    protected void init() {
      sinkEvents(Event.MOUSEEVENTS);
      addStyleName(DEFAULT_STYLE_NAME);
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
     * @see com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user.client.Event)
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
              Region r = DOM.getRegion(ToolButton.this.getElement());
              menu.setPopupPosition(r.left, r.bottom);
              menu.show();
            }
            return;
          }
          if (style == ToolButtonStyle.CHECKBOX) {
            setChecked(!isChecked());
          }
          if (style == ToolButtonStyle.RADIO) {
            final Widget parent = ToolButton.this.getParent();
            if (parent instanceof IndexedPanel) {
              final IndexedPanel panel = (IndexedPanel) parent;
              for (int i = 0, n = panel.getWidgetCount(); i < n; i++) {
                final Widget widget = panel.getWidget(i);
                if (widget instanceof ToolButton) {
                  final ToolButton button = (ToolButton) widget;
                  final String name = button.getName();
                  if (button.getStyle() == ToolButtonStyle.RADIO && button.isChecked()
                      && name != null && name.equals(getName())) {
                    button.setChecked(false);
                  }
                }
              }
            }
            setChecked(true);
          }
          super.onBrowserEvent(event);
          break;
        case Event.ONMOUSEDOWN:
          if (style == ToolButtonStyle.SPLIT && event.getTarget() == getElement()) {
            final int[] p = DOM.getPaddingSizes(getElement());
            if (getElement().getAbsoluteLeft() + (getElement().getOffsetWidth() - p[1]) < event.getClientX()) {
              addStyleName("mosaic-Split-Button-activeoption");
              return;
            }
          }
          super.onBrowserEvent(event);
          break;
        case Event.ONMOUSEUP:
          if (style == ToolButtonStyle.SPLIT && event.getTarget() == getElement()) {
            final int[] m = DOM.getPaddingSizes(getElement());
            if (getElement().getAbsoluteLeft() + (getElement().getOffsetWidth() - m[1]) < event.getClientX()) {
              if (menu != null) {
                Region r = DOM.getRegion(getElement());
                menu.setPopupPosition(r.left, r.bottom);
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
     * @param enabled <code>true</code> to enable the widget,
     *            <code>false</code> to disable it
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

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-ToolButton";

  private final ButtonWidget button = new ButtonWidget();

  /**
   * Creates a tool button with no caption.
   */
  public ToolButton() {
    super();
    getWidget().add(button);
    setStyleName(DEFAULT_STYLENAME);
  }

  /**
   * Creates a button with the given HTML caption.
   * 
   * @param html the HTML caption
   */
  public ToolButton(String html) {
    this();
    setHTML(html);
  }

  /**
   * Creates a tool button with the given HTML caption and click listener.
   * 
   * @param html the HTML caption
   * @param listener the click listener
   */
  public ToolButton(String html, ClickListener listener) {
    this(html);
    addClickListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasHTML#getHTML()
   */
  public String getHTML() {
    return button.getHTML();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasHTML#setHTML(java.lang.String)
   */
  public void setHTML(String html) {
    button.setHTML(html);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#getText()
   */
  public String getText() {
    return button.getText();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
   */
  public void setText(String text) {
    button.setText(text);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasName#getName()
   */
  public String getName() {
    return button.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasName#setName(java.lang.String)
   */
  public void setName(String name) {
    button.setName(name);
  }

  public void addClickListener(ClickListener listener) {
    button.addClickListener(listener);
  }

  public void removeClickListener(ClickListener listener) {
    button.removeClickListener(listener);
  }

  public void addFocusListener(FocusListener listener) {
    button.addFocusListener(listener);
  }

  public void removeFocusListener(FocusListener listener) {
    button.removeFocusListener(listener);
  }

  public int getTabIndex() {
    return button.getTabIndex();
  }

  public void setAccessKey(char key) {
    button.setAccessKey(key);
  }

  public void setFocus(boolean focused) {
    button.setFocus(focused);
  }

  public void setTabIndex(int index) {
    button.setTabIndex(index);
  }

  public void addKeyboardListener(KeyboardListener listener) {
    button.addKeyboardListener(listener);
  }

  public void removeKeyboardListener(KeyboardListener listener) {
    button.removeKeyboardListener(listener);
  }

  public void setEnabled(boolean enabled) {
    button.setEnabled(enabled);
  }

  public boolean isEnabled() {
    return button.isEnabled();
  }

  public ToolButtonStyle getStyle() {
    return button.getStyle();
  }

  public void setStyle(ToolButtonStyle style) {
    button.setStyle(style);
  }

  public PopupMenu getMenu() {
    return button.getMenu();
  }

  public void setMenu(PopupMenu menu) {
    button.setMenu(menu);
  }

  /**
   * Determines whether this check box is currently checked.
   * 
   * @return <code>true</code> if the check box is checked
   */
  public boolean isChecked() {
    return button.isChecked();
  }

  /**
   * Checks or unchecks this check box.
   * 
   * @param checked <code>true</code> to check the check box
   */
  public void setChecked(boolean checked) {
    button.setChecked(checked);
  }

}
