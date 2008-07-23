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

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusListenerCollection;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesFocusEvents;
import com.google.gwt.user.client.ui.SourcesKeyboardEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;

/**
 * The Button Control enables the creation of rich, graphical buttons that
 * function like traditional HTML form buttons; the Button Control provides
 * more-impactful and easier-to-use replacements for simple buttons, radio
 * buttons, and checkboxes, and it can go beyond the HTML form-control
 * functionality by providing an extensible platform for various split-button
 * implementations (including menu buttons).
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.mosaic-Button { }</li>
 * </ul>
 */
public class Button extends Widget implements HasFocus, HasName, HasHTML,
    SourcesClickEvents, SourcesFocusEvents, SourcesKeyboardEvents {

  public enum ButtonStyle {
    PUSH, MENU, SPLIT, RADIO, CHECKBOX
  }

  private static final FocusImpl impl = FocusImpl.getFocusImplForWidget();

  private static final String DEFAULT_STYLE_NAME = "mosaic-Button";

  static native Element adjustType(Element button)
  /*-{
    // Check before setting this attribute, as not all browsers define it.
    if (button.type == 'submit') {
      try { 
        button.setAttribute("type", "button"); 
      } catch (e) { 
      }
    }
    return button;
  }-*/;

  static native void click(Element button)
  /*-{
    button.click();
  }-*/;

  private ButtonStyle style = ButtonStyle.PUSH;

  private final Element firstChild;

  private final Element button;

  private PopupMenu menu;

  private ClickListenerCollection clickListeners;
  private FocusListenerCollection focusListeners;
  private KeyboardListenerCollection keyboardListeners;

  /**
   * Creates a button with no caption.
   */
  public Button() {
    setElement(DOM.createDiv());
    firstChild = DOM.createDiv();
    DOM.setElementProperty(firstChild, "className", "first-child");
    getElement().appendChild(firstChild);
    button = adjustType(DOM.createButton());
    firstChild.appendChild(button);
    setStyleName(DEFAULT_STYLE_NAME);
    setEnabled(true);
  }

  /**
   * Creates a button with the given HTML caption.
   * 
   * @param html the HTML caption
   */
  public Button(String html) {
    this();
    setHTML(html);
  }

  /**
   * Creates a button with the given HTML caption and click listener.
   * 
   * @param html the HTML caption
   * @param listener the click listener
   */
  public Button(String html, ClickListener listener) {
    this(html);
    addClickListener(listener);
  }

  public void addClickListener(ClickListener listener) {
    if (clickListeners == null) {
      clickListeners = new ClickListenerCollection();
    }
    clickListeners.add(listener);
  }

  public void addFocusListener(FocusListener listener) {
    if (focusListeners == null) {
      focusListeners = new FocusListenerCollection();
      sinkEvents(Event.FOCUSEVENTS);
    }
    focusListeners.add(listener);
  }

  public void addKeyboardListener(KeyboardListener listener) {
    if (keyboardListeners == null) {
      keyboardListeners = new KeyboardListenerCollection();
      sinkEvents(Event.KEYEVENTS);
    }
    keyboardListeners.add(listener);
  }

  private void addNewStyleName(String styleName) {
    addStyleName(styleName);
    if (!isEnabled()) {
      addStyleName(styleName + "-disabled");
    }
  }

  /**
   * Programmatic equivalent of the user clicking the button.
   */
  public void click() {
    click(getElement());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasHTML#getHTML()
   */
  public String getHTML() {
    return button.getInnerHTML();
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
    return DOM.getElementProperty(button, "name");
  }

  public ButtonStyle getStyle() {
    return style;
  }

  public int getTabIndex() {
    return impl.getTabIndex(getElement());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#getText()
   */
  public String getText() {
    return button.getInnerText();
  }

  /**
   * Determines whether this check box is currently checked.
   * 
   * @return <code>true</code> if the check box is checked
   */
  public boolean isChecked() {
    String propName = isAttached() ? "checked" : "defaultChecked";
    return DOM.getElementPropertyBoolean(button, propName);
  }

  /**
   * Gets whether this widget is enabled.
   * 
   * @return <code>true</code> if the widget is enabled
   */
  public boolean isEnabled() {
    return !DOM.getElementPropertyBoolean(button, "disabled");
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
          final int[] paddings = DOM.getPaddingSizes(button);
          if (button.getAbsoluteLeft() + (button.getOffsetWidth() - paddings[1]) < event.getClientX()) {
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
              if (widget instanceof Button) {
                final Button button = (Button) widget;
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
        if (clickListeners != null) {
          clickListeners.fireClick(this);
        }
        break;
      case Event.ONMOUSEDOWN:
        if (style == ButtonStyle.SPLIT && event.getTarget() == button) {
          final int[] m = DOM.getPaddingSizes(button);
          if (button.getAbsoluteLeft() + (button.getOffsetWidth() - m[1]) < event.getClientX()) {
            addStyleName("mosaic-Split-Button-activeoption");
            return;
          }
        }
        addStyleName("mosaic-Button-active");
        DOM.setCapture(getElement());
        break;
      case Event.ONMOUSEUP:
        if (style == ButtonStyle.SPLIT && event.getTarget() == button) {
          final int[] m = DOM.getPaddingSizes(button);
          if (button.getAbsoluteLeft() + (button.getOffsetWidth() - m[1]) < event.getClientX()) {
            if (menu != null) {
              Region r = DOM.getRegion(getElement());
              menu.setPopupPosition(r.left, r.bottom);
              menu.show();
            }
            removeStyleName("mosaic-Split-Button-activeoption");
            return;
          }
        }
        removeStyleName("mosaic-Button-active");
        DOM.releaseCapture(getElement());
        break;
      case Event.ONMOUSEOVER:
        if (getElement().isOrHasChild(event.getTarget())) {
          addStyleName("mosaic-Button-hover");
          if (DOM.getCaptureElement() == getElement()
              && getElement().isOrHasChild(event.getTarget())) {
            addStyleName("mosaic-Button-active");
          }
          if (style == ButtonStyle.SPLIT) {
            addStyleName("mosaic-Split-Button-hover");
          }
        }
        break;
      case Event.ONMOUSEOUT:
        removeStyleName("mosaic-Button-hover");
        removeStyleName("mosaic-Button-active");
        if (style == ButtonStyle.SPLIT) {
          removeStyleName("mosaic-Split-Button-hover");
        }
        break;
      case Event.ONBLUR:
      case Event.ONFOCUS:
        if (focusListeners != null) {
          focusListeners.fireFocusEvent(this, event);
        }
        break;
      case Event.ONKEYDOWN:
      case Event.ONKEYUP:
      case Event.ONKEYPRESS:
        if (keyboardListeners != null) {
          keyboardListeners.fireKeyboardEvent(this, event);
        }
        break;
    }
  }

  public void removeClickListener(ClickListener listener) {
    if (clickListeners != null) {
      clickListeners.remove(listener);
    }
  }

  public void removeFocusListener(FocusListener listener) {
    if (focusListeners != null) {
      focusListeners.remove(listener);
    }
  }

  public void removeKeyboardListener(KeyboardListener listener) {
    if (keyboardListeners != null) {
      keyboardListeners.remove(listener);
    }
  }

  private void removeOldStyleName(String styleName) {
    removeStyleName(styleName);
    if (!isEnabled()) {
      removeStyleName(styleName + "-disabled");
    }
  }

  public void setAccessKey(char key) {
    DOM.setElementProperty(button, "accessKey", "" + key);
  }

  /**
   * Checks or unchecks this check box.
   * 
   * @param checked <code>true</code> to check the check box
   */
  public void setChecked(boolean checked) {
    DOM.setElementPropertyBoolean(button, "checked", checked);
    DOM.setElementPropertyBoolean(button, "defaultChecked", checked);
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
    DOM.setElementPropertyBoolean(button, "disabled", !enabled);
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

  public void setFocus(boolean focused) {
    if (focused) {
      impl.focus(getElement());
    } else {
      impl.blur(getElement());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasHTML#setHTML(java.lang.String)
   */
  public void setHTML(String html) {
    button.setInnerHTML(html);
  }

  public void setMenu(PopupMenu menu) {
    this.menu = menu;
  }

  public void setName(String name) {
    DOM.setElementProperty(button, "name", name);
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

  public void setTabIndex(int index) {
    impl.setTabIndex(getElement(), index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
   */
  public void setText(String text) {
    button.setInnerText(text);
  }
}
