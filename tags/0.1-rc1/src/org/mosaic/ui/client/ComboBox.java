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

import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.HasLayoutManager;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ComboBox extends Composite implements HasLayoutManager, HasName, HasText {

  private static final String DEFAULT_STYLE_NAME = "mosaic-ComboBox";

  private final TextBox input;
  private final Button button;

  public ComboBox() {
    this(DEFAULT_STYLE_NAME);
  }

  protected ComboBox(String styleName) {
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(Orientation.HORIZONTAL));
    layoutPanel.setPadding(0);
    layoutPanel.setWidgetSpacing(1);

    initWidget(layoutPanel);

    input = new TextBox();
    layoutPanel.add(input, new BoxLayoutData(FillStyle.BOTH));

    button = new Button("&nbsp;", new ClickListener() {
      public void onClick(Widget sender) {
        // TODO Auto-generated method stub
      }
    });
    layoutPanel.add(button, new BoxLayoutData(FillStyle.VERTICAL));

    setStyleName(styleName);
  }

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

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Composite#getWidget()
   */
  @Override
  protected LayoutPanel getWidget() {
    return (LayoutPanel) super.getWidget();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayout#layout()
   */
  public void layout() {
    getWidget().layout();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayout#getPreferredSize()
   */
  public int[] getPreferredSize() {
    return getWidget().getPreferredSize();
  }

}