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
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".gwt-MenuBar"})
public class CwMenuBar extends ContentWidget {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {
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
  private CwConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwMenuBar(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }

  @Override
  public String getDescription() {
    return "MenuBar description";
  }

  @Override
  public String getName() {
    return "MenuBar";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel();
    layoutPanel.setPadding(5);

    final CaptionLayoutPanel toolBox = new CaptionLayoutPanel(Window.getTitle());
    toolBox.setPadding(0);
    toolBox.setLayout(new BoxLayout(Orientation.VERTICAL));
    toolBox.add(createMenuBar(), new BoxLayoutData(FillStyle.HORIZONTAL));

    layoutPanel.add(toolBox, new FillLayoutData(true));

    return layoutPanel;
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

}
