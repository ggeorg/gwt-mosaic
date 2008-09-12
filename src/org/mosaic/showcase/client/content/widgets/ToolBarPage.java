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
import org.mosaic.ui.client.MessageBox;
import org.mosaic.ui.client.PopupMenu;
import org.mosaic.ui.client.ToolBar;
import org.mosaic.ui.client.ToolButton;
import org.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 */
@ShowcaseStyle( {".gwt-MenuBar", ".mosaic-ToolBar"})
public class ToolBarPage extends Page implements ClickListener {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface DemoConstants extends Constants, Page.DemoConstants {
    String mosaicMenuBarDescription();

    String mosaicMenuBarEditCategory();

    String[] mosaicMenuBarEditOptions();

    String mosaicMenuBarFileCategory();

    String[] mosaicMenuBarFileOptions();

    String[] mosaicMenuBarFileRecents();

    String[] mosaicMenuBarGWTOptions();

    String mosaicMenuBarHelpCategory();

    String[] mosaicMenuBarHelpOptions();

    String mosaicMenuBarName();

    String[] mosaicMenuBarPrompts();
  }

  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private DemoConstants constants;

  public ToolBarPage(DemoConstants constants) {
    super(constants);
    this.constants = constants;
  }

  /**
   * 
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    LayoutPanel toolBox = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
    toolBox.setPadding(0);
    toolBox.setWidgetSpacing(0);
    toolBox.add(createMenuBar(), new BoxLayoutData(FillStyle.HORIZONTAL));
    toolBox.add(createToolBar1(), new BoxLayoutData(FillStyle.HORIZONTAL));
    toolBox.add(createToolBar2(), new BoxLayoutData(FillStyle.HORIZONTAL));

    layoutPanel.setLayout(new BoxLayout());
    layoutPanel.add(toolBox, new BoxLayoutData(FillStyle.BOTH, true));
  }

  /**
   * Create the menu bar.
   */
  @ShowcaseSource
  private MenuBar createMenuBar() {
    // Create a command that will execute on menu item selection
    Command menuCommand = new Command() {
      private int curPhrase = 0;
      private final String[] phrases = constants.mosaicMenuBarPrompts();

      public void execute() {
        MessageBox.alert(Window.getTitle(), phrases[curPhrase]);
        curPhrase = (curPhrase + 1) % phrases.length;
      }
    };

    // Create a menu bar
    MenuBar menu = new MenuBar();
    menu.setAnimationEnabled(true);

    // Create a sub menu of recent documents
    MenuBar recentDocsMenu = new MenuBar(true);
    String[] recentDocs = constants.mosaicMenuBarFileRecents();
    for (int i = 0; i < recentDocs.length; i++) {
      recentDocsMenu.addItem(recentDocs[i], menuCommand);
    }

    // Create the file menu
    MenuBar fileMenu = new MenuBar(true);
    fileMenu.setAnimationEnabled(true);
    menu.addItem(new MenuItem(constants.mosaicMenuBarFileCategory(), fileMenu));
    String[] fileOptions = constants.mosaicMenuBarFileOptions();
    for (int i = 0; i < fileOptions.length; i++) {
      if (i == 3) {
        fileMenu.addSeparator();
        fileMenu.addItem(fileOptions[i], recentDocsMenu);
        fileMenu.addSeparator();
      } else {
        fileMenu.addItem(fileOptions[i], menuCommand);
      }
    }

    // Create the edit menu
    MenuBar editMenu = new MenuBar(true);
    menu.addItem(new MenuItem(constants.mosaicMenuBarEditCategory(), editMenu));
    String[] editOptions = constants.mosaicMenuBarEditOptions();
    for (int i = 0; i < editOptions.length; i++) {
      editMenu.addItem(editOptions[i], menuCommand);
    }

    // Create the GWT menu
    MenuBar gwtMenu = new MenuBar(true);
    menu.addItem(new MenuItem("GWT", true, gwtMenu));
    String[] gwtOptions = constants.mosaicMenuBarGWTOptions();
    for (int i = 0; i < gwtOptions.length; i++) {
      gwtMenu.addItem(gwtOptions[i], menuCommand);
    }

    // Create the help menu
    MenuBar helpMenu = new MenuBar(true);
    menu.addSeparator();
    menu.addItem(new MenuItem(constants.mosaicMenuBarHelpCategory(), helpMenu));
    String[] helpOptions = constants.mosaicMenuBarHelpOptions();
    for (int i = 0; i < helpOptions.length; i++) {
      helpMenu.addItem(helpOptions[i], menuCommand);
    }

    // Return the menu
    menu.ensureDebugId("mosaicMenuBar");
    return menu;
  }

  /**
   * Create a tool bar.
   */
  @ShowcaseSource
  private Widget createToolBar1() {
    final ToolBar toolBar = new ToolBar();

    // Add a push button
    ToolButton pushButton = new ToolButton("Push 1");
    pushButton.addClickListener(this);
    pushButton.ensureDebugId("mosaicPushButton-normal");

    toolBar.add(pushButton);

    // Add a disabled push button
    ToolButton disabledPushButton = new ToolButton("Push 2");
    disabledPushButton.setEnabled(false);
    pushButton.ensureDebugId("mosaicPushButton-disabled");

    toolBar.add(disabledPushButton);

    toolBar.addSeparator();

    // Add a menu button
    ToolButton menuButton = new ToolButton("Menu 1");
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

    toolBar.add(menuButton);

    // Add a disabled menu button
    ToolButton disabledMenuButton = new ToolButton("Menu 2");
    disabledMenuButton.setStyle(ToolButtonStyle.MENU);
    disabledMenuButton.setEnabled(false);
    disabledMenuButton.ensureDebugId("mosaicMenuButton-disabled");

    toolBar.add(disabledMenuButton);

    toolBar.addSeparator();

    // Add a menu button
    ToolButton splitButton = new ToolButton("Split");
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

    toolBar.add(splitButton);

    return toolBar;
  }

  /**
   * Create a tool bar.
   */
  @ShowcaseSource
  private Widget createToolBar2() {
    final ToolBar toolBar = new ToolBar();

    // Add a checkbox button
    ToolButton checkButton1 = new ToolButton("Checkbox 1");
    checkButton1.setStyle(ToolButtonStyle.CHECKBOX);
    checkButton1.addClickListener(this);
    checkButton1.ensureDebugId("mosaicCheckboxButton-normal");

    toolBar.add(checkButton1);

    // Add a second checkbox button
    ToolButton checkButton2 = new ToolButton("Checkbox 2");
    checkButton2.setStyle(ToolButtonStyle.CHECKBOX);
    checkButton2.addClickListener(this);
    checkButton2.ensureDebugId("mosaicCheckboxButton-normal");

    toolBar.add(checkButton2);

    // Add a third checkbox button
    ToolButton checkButton3 = new ToolButton("Checkbox 3");
    checkButton3.setStyle(ToolButtonStyle.CHECKBOX);
    checkButton3.addClickListener(this);
    checkButton3.ensureDebugId("mosaicCheckboxButton-normal");

    toolBar.add(checkButton3);

    toolBar.addSeparator();

    // Add a checkbox button
    ToolButton radioButton1 = new ToolButton("Radio 1");
    radioButton1.setStyle(ToolButtonStyle.RADIO);
    radioButton1.addClickListener(this);
    radioButton1.ensureDebugId("mosaicRadioButton-normal");

    toolBar.add(radioButton1);

    // Add a second checkbox button
    ToolButton radioButton2 = new ToolButton("Radio 2");
    radioButton2.setStyle(ToolButtonStyle.RADIO);
    radioButton2.addClickListener(this);
    radioButton2.ensureDebugId("mosaicRadioButton-normal");

    toolBar.add(radioButton2);

    // Add a third checkbox button
    ToolButton radioButton3 = new ToolButton("Radio 3");
    radioButton3.setStyle(ToolButtonStyle.RADIO);
    radioButton3.addClickListener(this);
    radioButton3.ensureDebugId("mosaicRadioButton-normal");

    toolBar.add(radioButton3);

    return toolBar;
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
    return "ToolBar";
  }

}
