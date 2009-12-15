/*
 * Copyright (c) 2008-2009 GWT Mosaic Daniele Renda.
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
package org.gwt.mosaic.contrib.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Daniele
 */
public class MessageTextBox extends Composite {

  private TextBox textBox = new TextBox();
  private Image displayIcon = new Image("exclamation.gif");
  private DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);

  public MessageTextBox() {
    HorizontalPanel layoutPanel = new HorizontalPanel();

    displayIcon.setVisible(false);

    textBox.setWidth("99%");
    layoutPanel.add(textBox);
    layoutPanel.add(displayIcon);

    initWidget(layoutPanel);
  }

  public void showMessage(String text) {
    addMessageListener(text);
    displayIcon.setVisible(true);
    textBox.addStyleName("textbox-error");

  }

  private void addMessageListener(final String text) {
    displayIcon.addMouseListener(new MouseListener() {

      public void onMouseDown(Widget sender, int x, int y) {
      }

      public void onMouseEnter(Widget sender) {
        int left = displayIcon.getAbsoluteLeft() + 10;
        int top = displayIcon.getAbsoluteTop() + 10;
        // Create a basic popup widget
        simplePopup.ensureDebugId("cwBasicPopup-simplePopup");
        // simplePopup.setWidth("150px");
        simplePopup.setWidget(new HTML(text));

        simplePopup.setPopupPosition(left, top);
        // Show the popup
        simplePopup.show();
      }

      public void onMouseLeave(Widget sender) {
        if (simplePopup != null)
          simplePopup.hide();
      }

      public void onMouseMove(Widget sender, int x, int y) {
      }

      public void onMouseUp(Widget sender, int x, int y) {
      }
    });
  }

  /**
   * @return the textBox
   */
  public TextBox getTextBox() {
    return textBox;
  }

  /**
   * @param textBox the textBox to set
   */
  public void setTextBox(TextBox textBox) {
    this.textBox = textBox;
  }
}
