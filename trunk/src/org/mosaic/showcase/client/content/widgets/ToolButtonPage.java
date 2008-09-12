/*
 * Copyright 2008 Georgios J. Georgopoulos
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
package org.mosaic.showcase.client.content.widgets;

import org.mosaic.showcase.client.Page;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.InfoPanel;
import org.mosaic.ui.client.PopupMenu;
import org.mosaic.ui.client.ToolButton;
import org.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@ShowcaseStyle( {
    ".gwt-Button", ".mosaic-Button", ".mosaic-Menu-Button", ".mosaic-Split-Button",
    ".mosaic-Checkbox-Button", ".mosaic-Radio-Button"})
public class ToolButtonPage extends Page implements ClickListener {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface DemoConstants extends Constants, Page.DemoConstants {

  }

  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private DemoConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public ToolButtonPage(DemoConstants constants) {
    super(constants);
    this.constants = constants;
  }
  
  /**
   * Load this example.
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
    //layoutPanel.getElement().getStyle().setProperty("overflow", "auto");

    //
    // Push buttons
    //

    layoutPanel.add(new HTML("Push buttons"));

    final LayoutPanel hBox1 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox1, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    addPushButtons(hBox1);

    //
    // Menu buttons
    //

    layoutPanel.add(new HTML("Menu buttons"));

    final LayoutPanel hBox2 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox2, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    addMenuButtons(hBox2);

    //
    // Split buttons
    //

    layoutPanel.add(new HTML("Split buttons"));

    final LayoutPanel hBox3 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox3, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    addSplitButtons(hBox3);

    //
    // Checkbox buttons
    //

    layoutPanel.add(new HTML("Checkbox buttons"));

    final LayoutPanel hBox4 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox4, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    addCheckboxButtons(hBox4);

    //
    // Radio buttons
    //

    layoutPanel.add(new HTML("Radio buttons"));

    final LayoutPanel hBox5 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox5, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    addRadioButtons(hBox5);
  }
  
  /**
   * Push Buttons.
   * 
   * @param layoutPanel
   */
  @ShowcaseSource
  private void addPushButtons(LayoutPanel layoutPanel) {
    // Add a push button
    ToolButton pushButton = new ToolButton("Hello");
    pushButton.addClickListener(this);
    pushButton.ensureDebugId("mosaicPushButton-normal");

    layoutPanel.add(pushButton);

    // Add a disabled push button
    ToolButton disabledPushButton = new ToolButton("Hello");
    disabledPushButton.setEnabled(false);
    pushButton.ensureDebugId("mosaicPushButton-disabled");

    layoutPanel.add(disabledPushButton);
  }
  
  /**
   * Menu Buttons.
   * 
   * @param layoutPanel
   */
  @ShowcaseSource
  private void addMenuButtons(LayoutPanel layoutPanel) {
    // Add a menu button
    ToolButton menuButton = new ToolButton("Hello");
    menuButton.setStyle(ToolButtonStyle.MENU);
    menuButton.addClickListener(this);
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
    menuBtnMenu.addSeparator();
    menuBtnMenu.addItem("Item 3", cmd1);
    menuBtnMenu.addItem("Item 4", cmd1);

    menuButton.setMenu(menuBtnMenu);

    layoutPanel.add(menuButton);

    // Add a disabled menu button
    ToolButton disabledMenuButton = new ToolButton("Hello");
    disabledMenuButton.setStyle(ToolButtonStyle.MENU);
    disabledMenuButton.setEnabled(false);
    disabledMenuButton.ensureDebugId("mosaicMenuButton-disabled");

    layoutPanel.add(disabledMenuButton);
  }
  
  /**
   * Split Buttons.
   * 
   * @param layoutPanel
   */
  @ShowcaseSource
  private void addSplitButtons(LayoutPanel layoutPanel) {
    // Add a menu button
    ToolButton splitButton = new ToolButton("Hello");
    splitButton.setStyle(ToolButtonStyle.SPLIT);
    splitButton.addClickListener(this);
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
    splitBtnMenu.addSeparator();
    splitBtnMenu.addItem("Item 3", cmd2);
    splitBtnMenu.addItem("Item 4", cmd2);

    splitButton.setMenu(splitBtnMenu);

    layoutPanel.add(splitButton);

    // Add a disabled menu button
    ToolButton disabledSplitButton = new ToolButton("Hello");
    disabledSplitButton.setStyle(ToolButtonStyle.SPLIT);
    disabledSplitButton.setEnabled(false);
    disabledSplitButton.ensureDebugId("mosaicSplitButton-disabled");

    layoutPanel.add(disabledSplitButton);
  }

  /**
   * Checkbox Buttons.
   * 
   * @param layoutPanel
   */
  @ShowcaseSource
  private void addCheckboxButtons(LayoutPanel layoutPanel) {
    // Add a checkbox button
    ToolButton checkButton1 = new ToolButton("Hello");
    checkButton1.setStyle(ToolButtonStyle.CHECKBOX);
    checkButton1.addClickListener(this);
    checkButton1.ensureDebugId("mosaicCheckboxButton-normal");

    layoutPanel.add(checkButton1);

    // Add a second checkbox button
    ToolButton checkButton2 = new ToolButton("Hello");
    checkButton2.setStyle(ToolButtonStyle.CHECKBOX);
    checkButton2.addClickListener(this);
    checkButton2.ensureDebugId("mosaicCheckboxButton-normal");

    layoutPanel.add(checkButton2);

    // Add a third checkbox button
    ToolButton checkButton3 = new ToolButton("Hello");
    checkButton3.setStyle(ToolButtonStyle.CHECKBOX);
    checkButton3.addClickListener(this);
    checkButton3.ensureDebugId("mosaicCheckboxButton-normal");

    layoutPanel.add(checkButton3);

    // Add a fourth checkbox button
    ToolButton checkButton4 = new ToolButton("Hello");
    checkButton4.setStyle(ToolButtonStyle.CHECKBOX);
    checkButton4.setEnabled(false);
    checkButton4.ensureDebugId("mosaicCheckboxButton-normal");

    layoutPanel.add(checkButton4);

    // Add a fifth checkbox button
    ToolButton checkButton5 = new ToolButton("Hello");
    checkButton5.setStyle(ToolButtonStyle.CHECKBOX);
    checkButton5.setEnabled(false);
    checkButton5.setChecked(true);
    checkButton5.ensureDebugId("mosaicCheckboxButton-normal");

    layoutPanel.add(checkButton5);
  }
  
  /**
   * Radio Buttons.
   * 
   * @param layoutPanel
   */
  @ShowcaseSource
  private void addRadioButtons(LayoutPanel layoutPanel) {
    // Add a checkbox button
    ToolButton radioButton1 = new ToolButton("Hello");
    radioButton1.setStyle(ToolButtonStyle.RADIO);
    radioButton1.addClickListener(this);
    radioButton1.ensureDebugId("mosaicRadioButton-normal");

    layoutPanel.add(radioButton1);

    // Add a second checkbox button
    ToolButton radioButton2 = new ToolButton("Hello");
    radioButton2.setStyle(ToolButtonStyle.RADIO);
    radioButton2.addClickListener(this);
    radioButton2.ensureDebugId("mosaicRadioButton-normal");

    layoutPanel.add(radioButton2);

    // Add a third checkbox button
    ToolButton radioButton3 = new ToolButton("Hello");
    radioButton3.setStyle(ToolButtonStyle.RADIO);
    radioButton3.addClickListener(this);
    radioButton3.ensureDebugId("mosaicRadioButton-normal");

    layoutPanel.add(radioButton3);

    // Add a fourth checkbox button
    ToolButton radioButton4 = new ToolButton("Hello");
    radioButton4.setStyle(ToolButtonStyle.RADIO);
    radioButton4.setEnabled(false);
    radioButton4.ensureDebugId("mosaicRadioButton-normal");

    layoutPanel.add(radioButton4);

    // Add a fifth checkbox button
    ToolButton radioButton5 = new ToolButton("Hello");
    radioButton5.setStyle(ToolButtonStyle.RADIO);
    radioButton5.setEnabled(false);
    radioButton5.setChecked(true);
    radioButton5.ensureDebugId("mosaicRadioButton-normal");

    layoutPanel.add(radioButton5);
  }

  /**
   * 
   * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
   */
  @ShowcaseSource
  public void onClick(Widget sender) {
    final Button btn = (Button) sender;
    InfoPanel.show(btn.getHTML(), "Clicked!");
  }

  @Override
  public String getName() {
    return "Tool Button";
  }

}
