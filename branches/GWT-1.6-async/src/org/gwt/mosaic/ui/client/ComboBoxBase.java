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
package org.gwt.mosaic.ui.client;

import org.gwt.mosaic.core.client.util.DelayedRunnable;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesFocusEvents;
import com.google.gwt.user.client.ui.SourcesKeyboardEvents;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public abstract class ComboBoxBase<T extends Widget> extends LayoutComposite
    implements HasFocus, HasDirection, HasName, HasText, SourcesClickEvents,
    SourcesChangeEvents, SourcesFocusEvents, SourcesKeyboardEvents {

  private static final String DEFAULT_STYLENAME = "mosaic-ComboBox";

  private final TextBox input;
  private final Button button;
  private final DropDownPanel popup;

  private boolean cancelNextClick;

  public ComboBoxBase() {
    this(DEFAULT_STYLENAME);
  }

  protected ComboBoxBase(String styleName) {
    super();
    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.setLayout(new BoxLayout(Orientation.HORIZONTAL));
    layoutPanel.setPadding(0);
    layoutPanel.setWidgetSpacing(0);

    input = new TextBox();
    layoutPanel.add(input, new BoxLayoutData(FillStyle.BOTH));
    input.addKeyboardListener(new KeyboardListener() {
      public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        switch (keyCode) {
          case KEY_ENTER:
          case KEY_TAB:
          case KEY_ESCAPE:
            hidePopup();
            break;
          case KEY_DOWN:
            if (!popup.isAttached()) {
              new DelayedRunnable(1) {
                @Override
                public void run() {
                  showPopup();
                }
              };
            }
            break;
          default:
        }
      }

      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // Nothing to do here!
      }

      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        // Nothing to do here!
      }

    });

    button = new Button();
    button.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (cancelNextClick) {
          cancelNextClick = false;
        } else {
          showPopup();
        }
      }
    });
    layoutPanel.add(button, new BoxLayoutData(FillStyle.VERTICAL));

    popup = new DropDownPanel(this);
    popup.addPopupListener(new PopupListener() {
      public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
        if (!autoClosed) {
          input.setFocus(true);
        }
      }
    });

    setStyleName(styleName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.SourcesChangeEvents#addChangeListener(com.google.gwt.user.client.ui.ChangeListener)
   */
  public void addChangeListener(ChangeListener listener) {
    input.addChangeListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.SourcesClickEvents#addClickListener(com.google.gwt.user.client.ui.ClickListener)
   */
  public void addClickListener(ClickListener listener) {
    input.addClickListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.SourcesFocusEvents#addFocusListener(com.google.gwt.user.client.ui.FocusListener)
   */
  public void addFocusListener(FocusListener listener) {
    input.addFocusListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.SourcesKeyboardEvents#addKeyboardListener(com.google.gwt.user.client.ui.KeyboardListener)
   */
  public void addKeyboardListener(KeyboardListener listener) {
    input.addKeyboardListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.i18n.client.HasDirection#getDirection()
   */
  public Direction getDirection() {
    return input.getDirection();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasName#getName()
   */
  public String getName() {
    return input.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasFocus#getTabIndex()
   */
  public int getTabIndex() {
    return input.getTabIndex();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#getText()
   */
  public String getText() {
    return input.getText();
  }

  protected void hidePopup() {
    hidePopup(false);
  }

  protected void hidePopup(boolean autoHide) {
    if (autoHide) {
      popup.hide(true);
    } else if (onHidePopup()) {
      popup.hide();
    }
  }

  public boolean isAnimationEnabled() {
    return popup.isAnimationEnabled();
  }

  /**
   * Gets whether this widget is enabled.
   * 
   * @return <code>true</code> if the widget is enabled
   */
  public boolean isEnabled() {
    return input.isEnabled() && button.isEnabled();
  }

  protected boolean isPopupVisible() {
    return popup.isAttached();
  }
  
  /**
   * Determines whether or not the widget is read-only.
   * 
   * @return <code>true</code> if the widget is currently read-only,
   *         <code>false</code> if the widget is currently editable
   */
  public boolean isReadOnly() {
    return input.isReadOnly();
  }

  @Override
  public void layout() {
    hidePopup(true);
    super.layout();
  }

  protected abstract boolean onHidePopup();

  protected abstract T onShowPopup();

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.SourcesChangeEvents#removeChangeListener(com.google.gwt.user.client.ui.ChangeListener)
   */
  public void removeChangeListener(ChangeListener listener) {
    input.removeChangeListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.SourcesClickEvents#removeClickListener(com.google.gwt.user.client.ui.ClickListener)
   */
  public void removeClickListener(ClickListener listener) {
    input.removeClickListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.SourcesFocusEvents#removeFocusListener(com.google.gwt.user.client.ui.FocusListener)
   */
  public void removeFocusListener(FocusListener listener) {
    input.removeFocusListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.SourcesKeyboardEvents#removeKeyboardListener(com.google.gwt.user.client.ui.KeyboardListener)
   */
  public void removeKeyboardListener(KeyboardListener listener) {
    input.removeKeyboardListener(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasFocus#setAccessKey(char)
   */
  public void setAccessKey(char key) {
    input.setAccessKey(key);
  }

  public void setAnimationEnabled(boolean enable) {
    popup.setAnimationEnabled(enable);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.i18n.client.HasDirection#setDirection(com.google.gwt.i18n.client.HasDirection.Direction)
   */
  public void setDirection(Direction direction) {
    input.setDirection(direction);
  }

  /**
   * Sets whether this widget is enabled.
   * 
   * @param enabled <code>true</code> to enable the widget, <code>false</code>
   *          to disable it
   */
  public void setEnabled(boolean enabled) {
    input.setEnabled(enabled);
    button.setEnabled(enabled);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasFocus#setFocus(boolean)
   */
  public void setFocus(boolean focused) {
    input.setFocus(focused);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasName#setName(java.lang.String)
   */
  public void setName(String name) {
    input.setName(name);
  }

  /**
   * Turns read-only mode on or off.
   * 
   * @param readOnly if <code>true</code>, the widget becomes read-only; if
   *          <code>false</code> the widget becomes editable
   */
  public void setReadOnly(boolean readOnly) {
    input.setReadOnly(readOnly);
    String readOnlyStyle = "readonly";
    if (readOnly) {
      addStyleDependentName(readOnlyStyle);
    } else {
      removeStyleDependentName(readOnlyStyle);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasFocus#setTabIndex(int)
   */
  public void setTabIndex(int index) {
    input.setTabIndex(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
   */
  public void setText(String text) {
    input.setText(text);
  }

  protected void showPopup() {
    if (popup.isAttached()) {
      hidePopup(true);
    } else {
      final T w = onShowPopup();
      if (w != null) {
        if (w != popup.getWidget()) {
          popup.setWidget(w);
        }
        popup.show();
      }
    }
  }

}
