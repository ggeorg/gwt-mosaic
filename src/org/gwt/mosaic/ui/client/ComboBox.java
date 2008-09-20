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
package org.gwt.mosaic.ui.client;

import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public abstract class ComboBox extends LayoutComposite implements HasName, HasText {

  private static final String DEFAULT_STYLENAME = "mosaic-ComboBox";

  protected final TextBox input;
  protected final Button button;

  public ComboBox() {
    this(DEFAULT_STYLENAME);
  }

  protected ComboBox(String styleName) {
    super();
    final LayoutPanel layoutPanel = getWidget();
    layoutPanel.setLayout(new BoxLayout(Orientation.HORIZONTAL));
    layoutPanel.setPadding(0);
    layoutPanel.setWidgetSpacing(0);

    input = new TextBox();
    layoutPanel.add(input, new BoxLayoutData(FillStyle.BOTH));

    button = new Button("&nbsp;", new ClickListener() {
      public void onClick(Widget sender) {
        onButtonClick();
      }
    });
    layoutPanel.add(button, new BoxLayoutData(FillStyle.VERTICAL));

    setStyleName(styleName);
  }
  
  protected abstract void onButtonClick();

  public Direction getDirection() {
    return input.getDirection();
  }

  public void setDirection(Direction direction) {
    input.setDirection(direction);
  }

  protected InputElement getInputElement() {
    return getElement().cast();
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
   * @see com.google.gwt.user.client.ui.HasName#setName(java.lang.String)
   */
  public void setName(String name) {
    input.setName(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#getText()
   */
  public String getText() {
    return input.getText();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
   */
  public void setText(String text) {
    input.setText(text);
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

  /**
   * Turns read-only mode on or off.
   * 
   * @param readOnly if <code>true</code>, the widget becomes read-only; if
   *            <code>false</code> the widget becomes editable
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

}
