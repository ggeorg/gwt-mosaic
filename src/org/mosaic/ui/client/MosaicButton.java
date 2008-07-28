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
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Widget;

public class MosaicButton extends Button implements HasName {

  public enum ButtonStyle {
    PUSH, MENU, SPLIT, RADIO, CHECKBOX
  }

  private static final String DEFAULT_STYLE_NAME = "mosaic-Button";

  private ButtonStyle style = ButtonStyle.PUSH;

  private PopupMenu menu;

  /**
   * Creates a button with no caption.
   */
  public MosaicButton() {
    super();
    init();
  }

  /**
   * Creates a button with the given HTML caption.
   * 
   * @param html the HTML caption
   */
  public MosaicButton(String html) {
    super(html);
    init();
  }

  /**
   * Creates a button with the given HTML caption and click listener.
   * 
   * @param html the HTML caption
   * @param listener the click listener
   */
  public MosaicButton(String html, ClickListener listener) {
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

  public ButtonStyle getStyle() {
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
        if (style == ButtonStyle.SPLIT) {
          final int[] paddings = DOM.getPaddingSizes(getElement());
          if (getElement().getAbsoluteLeft()
              + (getElement().getOffsetWidth() - paddings[1]) < event.getClientX()) {
            return;
          }
        }
        if (style == ButtonStyle.MENU) {
          if (menu != null) {
            Region r = DOM.getRegion(getElement());
            menu.setPopupPosition(r.left, r.bottom);
            menu.show();
          }
          return;
        }
        if (style == ButtonStyle.CHECKBOX) {
          setChecked(!isChecked());
        }
        if (style == ButtonStyle.RADIO) {
          final Widget parent = getParent();
          if (parent instanceof IndexedPanel) {
            final IndexedPanel panel = (IndexedPanel) getParent();
            for (int i = 0, n = panel.getWidgetCount(); i < n; i++) {
              final Widget widget = panel.getWidget(i);
              if (widget instanceof MosaicButton) {
                final MosaicButton button = (MosaicButton) widget;
                final String name = button.getName();
                if (button.getStyle() == ButtonStyle.RADIO && button.isChecked()
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
        if (style == ButtonStyle.SPLIT && event.getTarget() == getElement()) {
          final int[] p = DOM.getPaddingSizes(getElement());
          if (getElement().getAbsoluteLeft() + (getElement().getOffsetWidth() - p[1]) < event.getClientX()) {
            addStyleName("mosaic-Split-Button-activeoption");
            return;
          }
        }
        super.onBrowserEvent(event);
        break;
      case Event.ONMOUSEUP:
        if (style == ButtonStyle.SPLIT && event.getTarget() == getElement()) {
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
        if (style == ButtonStyle.SPLIT) {
          addStyleName("mosaic-Split-Button-hover");
        }
        super.onBrowserEvent(event);
        break;
      case Event.ONMOUSEOUT:
        if (style == ButtonStyle.SPLIT) {
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
   * @param enabled <code>true</code> to enable the widget, <code>false</code>
   *            to disable it
   */
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    if (enabled) {
      removeStyleName("mosaic-Button-disabled");
      sinkEvents(Event.ONCLICK | Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEOVER
          | Event.ONMOUSEOUT);
      if (this.style == ButtonStyle.MENU) {
        removeStyleName("mosaic-Menu-Button-disabled");
      } else if (this.style == ButtonStyle.SPLIT) {
        removeStyleName("mosaic-Split-Button-disabled");
      } else if (this.style == ButtonStyle.CHECKBOX) {
        removeStyleName("mosaic-Checkbox-Button-disabled");
      }
    } else {
      addStyleName("mosaic-Button-disabled");
      unsinkEvents(Event.ONCLICK | Event.ONMOUSEDOWN | Event.ONMOUSEUP
          | Event.ONMOUSEOVER | Event.ONMOUSEOUT);
      if (style == ButtonStyle.MENU) {
        addStyleName("mosaic-Menu-Button-disabled");
      } else if (style == ButtonStyle.SPLIT) {
        addStyleName("mosaic-Split-Button-disabled");
      } else if (this.style == ButtonStyle.CHECKBOX) {
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

  public void setStyle(ButtonStyle style) {
    if (this.style != style) {
      if (this.style == ButtonStyle.MENU) {
        removeOldStyleName("mosaic-Menu-Button");
      } else if (this.style == ButtonStyle.SPLIT) {
        removeOldStyleName("mosaic-Split-Button");
      } else if (this.style == ButtonStyle.CHECKBOX) {
        removeOldStyleName("mosaic-Checkbox-Button");
      }
      if (style == ButtonStyle.MENU) {
        addNewStyleName("mosaic-Menu-Button");
      } else if (style == ButtonStyle.SPLIT) {
        addNewStyleName("mosaic-Split-Button");
      } else if (this.style == ButtonStyle.CHECKBOX) {
        addNewStyleName("mosaic-Checkbox-Button");
      }
      this.style = style;
    }
  }

}
