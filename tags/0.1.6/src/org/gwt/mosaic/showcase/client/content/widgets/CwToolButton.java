/*
 * Copyright 2008 Google Inc.
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
package org.gwt.mosaic.showcase.client.content.widgets;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {
    ".gwt-Button", ".mosaic-Button", ".mosaic-Menu-Button",
    ".mosaic-Split-Button", ".mosaic-Checkbox-Button", ".mosaic-Radio-Button"})
public class CwToolButton extends ContentWidget implements ClickListener {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {

  }

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwToolButton(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Tool Button description";
  }

  @Override
  public String getName() {
    return "Tool Button";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final ScrollLayoutPanel layoutPanel = new ScrollLayoutPanel();

    //
    // Push buttons
    //

    layoutPanel.add(new HTML("Push buttons widgets"));

    final LayoutPanel hBox1 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox1, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    addPushButtons(hBox1);

    //
    // Push buttons with image
    //

    layoutPanel.add(new HTML("Push buttons widgets with image"));

    final LayoutPanel hBox2 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox2, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    addImageButtons(hBox2);

    //
    // Menu & Split buttons
    //

    layoutPanel.add(new HTML("Menu & Split buttons widgets"));

    final LayoutPanel hBox3 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox3, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    addMenuAndSplitButtons(hBox3);

    //
    // Checkbox & Radio buttons
    //

    layoutPanel.add(new HTML("Checkbox & Radio buttons widgets"));

    final LayoutPanel hBox4 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox4, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    addCheckboxAndRadioButtons(hBox4);

    return layoutPanel;
  }

  /**
   * Basic Buttons.
   * 
   * @param layoutPanel
   */
  @ShowcaseSource
  private void addPushButtons(LayoutPanel layoutPanel) {
    // Add a push button
    ToolButton pushButton1 = new ToolButton("Push Button", this);
    pushButton1.ensureDebugId("mosaicPushButton-normal");

    layoutPanel.add(pushButton1);

    // Add a disabled push button
    ToolButton disabledPushButton = new ToolButton("Disabled");
    disabledPushButton.setEnabled(false);
    disabledPushButton.ensureDebugId("mosaicPushButton-disabled");

    layoutPanel.add(disabledPushButton);

  }

  /**
   * Image Buttons.
   * 
   * @param layoutPanel
   */
  @ShowcaseSource
  private void addImageButtons(LayoutPanel layoutPanel) {
    // Add a push button with image
    ToolButton pushButton1 = new ToolButton(ButtonHelper.createButtonLabel(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation(), "Image Button",
        ButtonLabelType.TEXT_ON_TOP), this);
    pushButton1.ensureDebugId("mosaicPushButton-normal");

    layoutPanel.add(pushButton1, new BoxLayoutData(FillStyle.VERTICAL));

    // Add a push button with image
    ToolButton pushButton2 = new ToolButton(ButtonHelper.createButtonLabel(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation(), "Image Button",
        ButtonLabelType.TEXT_ON_RIGHT), this);
    pushButton2.ensureDebugId("mosaicPushButton-normal");

    layoutPanel.add(pushButton2, new BoxLayoutData(FillStyle.VERTICAL));

    // Add a push button with image
    ToolButton pushButton3 = new ToolButton(ButtonHelper.createButtonLabel(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation(), "Image Button",
        ButtonLabelType.TEXT_ON_BOTTOM), this);
    pushButton3.ensureDebugId("mosaicPushButton-normal");

    layoutPanel.add(pushButton3, new BoxLayoutData(FillStyle.VERTICAL));

    // Add a push button with image
    ToolButton pushButton4 = new ToolButton(ButtonHelper.createButtonLabel(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation(), "Image Button",
        ButtonLabelType.TEXT_ON_LEFT), this);
    pushButton4.setEnabled(false);
    pushButton4.ensureDebugId("mosaicPushButton-normal");

    layoutPanel.add(pushButton4, new BoxLayoutData(FillStyle.VERTICAL));

  }

  /**
   * Menu Buttons.
   * 
   * @param layoutPanel
   */
  @ShowcaseSource
  private void addMenuAndSplitButtons(LayoutPanel layoutPanel) {
    // Add a menu button
    ToolButton menuButton = new ToolButton("Menu Button", this);
    menuButton.setStyle(ToolButtonStyle.MENU);
    menuButton.ensureDebugId("mosaicMenuButton-normal");

    // Make a command that we will execute from all menu items.
    Command cmd1 = new Command() {
      public void execute() {
        InfoPanel.show("Menu Button", "You selected a menu item!");
      }
    };

    PopupMenu menuBtnMenu = new PopupMenu();
    menuBtnMenu.addItem("Item 1", cmd1);
    menuBtnMenu.addItem("Item 2", cmd1);

    menuButton.setMenu(menuBtnMenu);

    layoutPanel.add(menuButton);

    // Add a disabled menu button
    ToolButton disabledMenuButton = new ToolButton("Disabled", this);
    disabledMenuButton.setStyle(ToolButtonStyle.MENU);
    disabledMenuButton.setEnabled(false);
    disabledMenuButton.ensureDebugId("mosaicMenuButton-disabled");

    layoutPanel.add(disabledMenuButton);

    // Add a spacer
    layoutPanel.add(new HTML("&nbsp;"));

    // Add a menu button
    ToolButton splitButton = new ToolButton("Split Button", this);
    splitButton.setStyle(ToolButtonStyle.SPLIT);
    splitButton.ensureDebugId("mosaicSplitButton-normal");

    // Make a command that we will execute from all menu items.
    Command cmd2 = new Command() {
      public void execute() {
        InfoPanel.show("Split Button", "You selected a menu item!");
      }
    };

    PopupMenu splitBtnMenu = new PopupMenu();
    splitBtnMenu.addItem("Item 1", cmd2);
    splitBtnMenu.addItem("Item 2", cmd2);

    splitButton.setMenu(splitBtnMenu);

    layoutPanel.add(splitButton);

    // Add a disabled menu button
    ToolButton disabledSplitButton = new ToolButton("Disabled", this);
    disabledSplitButton.setStyle(ToolButtonStyle.SPLIT);
    disabledSplitButton.setEnabled(false);
    disabledSplitButton.ensureDebugId("mosaicSplitButton-disabled");

    layoutPanel.add(disabledSplitButton);
  }

  /**
   * Radio Buttons.
   * 
   * @param layoutPanel
   */
  @ShowcaseSource
  private void addCheckboxAndRadioButtons(LayoutPanel layoutPanel) {
    // Add a checkbox button
    ToolButton checkButton1 = new ToolButton("Checkbox", this);
    checkButton1.setStyle(ToolButtonStyle.CHECKBOX);
    checkButton1.ensureDebugId("mosaicCheckboxButton-normal");

    layoutPanel.add(checkButton1);

    // Add a disabled checkbox button
    ToolButton checkButton2 = new ToolButton("Disabled", this);
    checkButton2.setStyle(ToolButtonStyle.CHECKBOX);
    checkButton2.setEnabled(false);
    checkButton2.setChecked(true);
    checkButton2.ensureDebugId("mosaicCheckboxButton-normal");

    layoutPanel.add(checkButton2);

    // Add a spacer
    layoutPanel.add(new HTML("&nbsp;"));

    // Add a radio button
    ToolButton radioButton1 = new ToolButton("Radio #1", this);
    radioButton1.setStyle(ToolButtonStyle.RADIO);
    radioButton1.ensureDebugId("mosaicRadioButton-normal");

    layoutPanel.add(radioButton1);

    // Add a second radio button
    ToolButton radioButton2 = new ToolButton("Radio #2", this);
    radioButton2.setStyle(ToolButtonStyle.RADIO);
    radioButton2.ensureDebugId("mosaicRadioButton-normal");

    layoutPanel.add(radioButton2);

    // Add a fourth radio button
    ToolButton radioButton4 = new ToolButton("Radio #3", this);
    radioButton4.setStyle(ToolButtonStyle.RADIO);
    radioButton4.setEnabled(false);
    radioButton4.setChecked(true);
    radioButton4.ensureDebugId("mosaicRadioButton-disabled");

    layoutPanel.add(radioButton4);

  }

  /**
   * Fired when the user clicks on a button.
   * 
   * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
   */
  @ShowcaseSource
  public void onClick(Widget sender) {
    final Button btn = (Button) sender;
    InfoPanel.show(btn.getText(), "Clicked!");
  }

}
