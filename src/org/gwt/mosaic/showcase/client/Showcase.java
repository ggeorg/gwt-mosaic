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
package org.gwt.mosaic.showcase.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.showcase.client.Application.ApplicationListener;
import org.gwt.mosaic.showcase.client.content.layout.CwBorderLayout;
import org.gwt.mosaic.showcase.client.content.layout.CwBorderLayoutTest_Collapsed;
import org.gwt.mosaic.showcase.client.content.layout.CwBoxLayout;
import org.gwt.mosaic.showcase.client.content.layout.CwBoxLayoutTest_Horizontal;
import org.gwt.mosaic.showcase.client.content.layout.CwBoxLayoutTest_Vertical;
import org.gwt.mosaic.showcase.client.content.layout.CwBoxLayoutTest_Histogram;
import org.gwt.mosaic.showcase.client.content.layout.CwCalculatorLayout;
import org.gwt.mosaic.showcase.client.content.layout.CwFillLayout;
import org.gwt.mosaic.showcase.client.content.layout.CwGridLayout;
import org.gwt.mosaic.showcase.client.content.layout.CwBoxLayoutTest1;
import org.gwt.mosaic.showcase.client.content.layout.CwBoxLayoutTest2;
import org.gwt.mosaic.showcase.client.content.layout.CwFillLayoutTest1;
import org.gwt.mosaic.showcase.client.content.layout.CwMixedLayout;
import org.gwt.mosaic.showcase.client.content.layout.CwNestedBorderLayout;
import org.gwt.mosaic.showcase.client.content.other.CwActions;
import org.gwt.mosaic.showcase.client.content.panels.CwBottomTabBars;
import org.gwt.mosaic.showcase.client.content.panels.CwDeckLayoutPanel;
import org.gwt.mosaic.showcase.client.content.panels.CwHorizontalSplitPanel;
import org.gwt.mosaic.showcase.client.content.panels.CwStackLayoutPanel;
import org.gwt.mosaic.showcase.client.content.panels.CwTabLayoutPanel;
import org.gwt.mosaic.showcase.client.content.panels.CwVerticalSplitPanel;
import org.gwt.mosaic.showcase.client.content.popups.CwInfoPanel;
import org.gwt.mosaic.showcase.client.content.popups.CwMessageBox;
import org.gwt.mosaic.showcase.client.content.popups.CwWindowPanel;
import org.gwt.mosaic.showcase.client.content.tables.CwPagingScrollTable;
import org.gwt.mosaic.showcase.client.content.tables.CwScrollTable;
import org.gwt.mosaic.showcase.client.content.tables.CwTableLoadingBenchmark;
import org.gwt.mosaic.showcase.client.content.trees.CwBasicTree;
import org.gwt.mosaic.showcase.client.content.trees.CwLazyTree;
import org.gwt.mosaic.showcase.client.content.trees.CwVerboseTree;
import org.gwt.mosaic.showcase.client.content.treetables.CwBasicTreeTable;
import org.gwt.mosaic.showcase.client.content.treetables.CwLazyTreeTable;
import org.gwt.mosaic.showcase.client.content.widgets.CwBasicButton;
import org.gwt.mosaic.showcase.client.content.widgets.CwComboBox;
import org.gwt.mosaic.showcase.client.content.widgets.CwCustomButton;
import org.gwt.mosaic.showcase.client.content.widgets.CwDatePicker;
import org.gwt.mosaic.showcase.client.content.widgets.CwMenuBar;
import org.gwt.mosaic.showcase.client.content.widgets.CwToolBar;
import org.gwt.mosaic.showcase.client.content.widgets.CwToolButton;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class Showcase implements EntryPoint {

  /**
   * The type passed into the
   * {@link org.gwt.mosaic.showcase.generator.ShowcaseGenerator}.
   */
  private static final class GeneratorInfo {
  }

  private static final class ThemeButton extends ToggleButton {
    private static List<ThemeButton> allButtons = null;

    private String theme;

    public ThemeButton(String theme) {
      super();
      this.theme = theme;
      addStyleName("sc-ThemeButton-" + theme);

      // Add this button to the static list
      if (allButtons == null) {
        allButtons = new ArrayList<ThemeButton>();
        setDown(true);
      }
      allButtons.add(this);
    }

    public String getTheme() {
      return theme;
    }

    @Override
    protected void onClick() {
      if (!isDown()) {
        // Raise all of the other buttons
        for (ThemeButton button : allButtons) {
          if (button != this) {
            button.setDown(false);
          }
        }

        // Fire the click listeners
        super.onClick();
      }
    }
  }

  /**
   * The base style name.
   */
  public static final String DEFAULT_STYLE_NAME = "Mosaic";

  /**
   * The static images used throughout the Showcase.
   */
  public static final ShowcaseImages IMAGES = (ShowcaseImages) GWT.create(ShowcaseImages.class);

  /**
   * The current style theme.
   */
  public static String CUR_THEME = ShowcaseConstants.STYLE_THEMES[0];

  /**
   * Get the URL of the page, without an hash of query string.
   * 
   * @return the location of the page
   */
  private static native String getHostPageLocation()
  /*-{
    var s = $doc.location.href;
  
    // Pull off any hash.
    var i = s.indexOf('#');
    if (i != -1)
      s = s.substring(0, i);
  
    // Pull off any query string.
    i = s.indexOf('?');
    if (i != -1)
      s = s.substring(0, i);
  
    // Ensure a final slash if non-empty.
    return s;
  }-*/;

  /**
   * The {@link Application}.
   */
  private Application app = new Application();

  /**
   * A mapping of history tokens to their associated menu items.
   */
  private Map<String, TreeItem> itemTokens = new HashMap<String, TreeItem>();

  /**
   * A mapping of menu items to the widget display when the item is selected.
   */
  private Map<TreeItem, ContentWidget> itemWidgets = new HashMap<TreeItem, ContentWidget>();

  /**
   * Set the content to the {@link ContentWidget}.
   * 
   * @param content the {@link ContentWidget} to display
   */
  private void displayContentWidget(final ContentWidget content) {
    if (content != null) {
      if (!content.isInitialized()) {
        content.initialize();
      }
      app.setContent(content);
    }
  }

  /**
   * Get the token for a given content widget.
   * 
   * @return the content widget token.
   */
  private String getContentWidgetToken(ContentWidget content) {
    String className = content.getClass().getName();
    className = className.substring(className.lastIndexOf('.') + 1);
    return className;
  }

  /**
   * Get the style name of the reference element defined in the current GWT
   * theme style sheet.
   * 
   * @param prefix the prefix of the reference style name
   * @return the style name
   */
  private String getCurrentReferenceStyleName(String prefix) {
    String gwtRef = prefix + "-Reference-" + CUR_THEME;
    if (LocaleInfo.getCurrentLocale().isRTL()) {
      gwtRef += "-rtl";
    }
    return gwtRef;
  }

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    // Generate the source code and css for the examples
    GWT.create(GeneratorInfo.class);

    // Create the constants
    ShowcaseConstants constants = (ShowcaseConstants) GWT.create(ShowcaseConstants.class);

    // Swap out the style sheets for the RTL versions if needed
    updateStyleSheets();

    // Create the application
    setupTitlePanel(constants);
    setupMainLinks(constants);
    setupOptionsPanel();
    setupMainMenu(constants);

    // Setup a history listener to reselect the associate menu item
    final HistoryListener historyListener = new HistoryListener() {
      public void onHistoryChanged(String historyToken) {
        TreeItem item = itemTokens.get(historyToken);
        if (item == null) {
          item = app.getMainMenu().getItem(0).getChild(0);
        }

        // Select the associated TreeItem
        app.getMainMenu().setSelectedItem(item, false);
        app.getMainMenu().ensureSelectedItemVisible();

        // Show the associated ContentWidget
        displayContentWidget(itemWidgets.get(item));
      }
    };
    History.addHistoryListener(historyListener);

    // Add an listener that sets the content widget when a menu item is selected
    app.setListener(new ApplicationListener() {
      public void onMenuItemSelected(TreeItem item) {
        ContentWidget content = itemWidgets.get(item);
        if (content != null && !content.equals(app.getContent())) {
          History.newItem(getContentWidgetToken(content));
        }
      }
    });

    // Show the initial example
    String initToken = History.getToken();
    if (initToken.length() > 0) {
      historyListener.onHistoryChanged(initToken);
    } else { // Use the first token available
      TreeItem firstItem = app.getMainMenu().getItem(0).getChild(0);
      app.getMainMenu().setSelectedItem(firstItem, false);
      app.getMainMenu().ensureSelectedItemVisible();
      displayContentWidget(itemWidgets.get(firstItem));
    }

    DOM.getElementById("splash").getStyle().setProperty("display", "none");
  }

  /**
   * Create the main links at the top of the application.
   * 
   * @param constants the constants with text
   */
  private void setupMainLinks(ShowcaseConstants constants) {
    // Link to GWT Mosaic Homepage
    app.addLink(new HTML("<a href=\"" + ShowcaseConstants.GWT_MOSAIC_HOMEPAGE
        + "\">" + constants.mainLinkMosaic() + "</a>"));

    // Link to GWT Homepage
    app.addLink(new HTML("<a href=\"" + ShowcaseConstants.GWT_HOMEPAGE + "\">"
        + constants.mainLinkHomepage() + "</a>"));

    // Link to More Examples
    app.addLink(new HTML("<a href=\"" + ShowcaseConstants.GWT_EXAMPLES + "\">"
        + constants.mainLinkExamples() + "</a>"));
  }

  /**
   * Setup all of the options in the main menu.
   * 
   * @param constants the constant values to use
   */
  private void setupMainMenu(ShowcaseConstants constants) {
    Tree mainMenu = app.getMainMenu();

    // Widgets
    TreeItem catWidgets = mainMenu.addItem("Widgets");
    setupMainMenuOption(catWidgets, new CwBasicButton(constants),
        IMAGES.catWidgets());
    setupMainMenuOption(catWidgets, new CwCustomButton(constants),
        IMAGES.catWidgets());
    setupMainMenuOption(catWidgets, new CwToolButton(constants),
        IMAGES.catWidgets());
    setupMainMenuOption(catWidgets, new CwComboBox(constants),
        IMAGES.catWidgets());
    setupMainMenuOption(catWidgets, new CwDatePicker(constants),
        IMAGES.catWidgets());
    setupMainMenuOption(catWidgets, new CwToolBar(constants),
        IMAGES.catWidgets());
    setupMainMenuOption(catWidgets, new CwMenuBar(constants),
        IMAGES.catWidgets());

    // Popups
    TreeItem catPopups = mainMenu.addItem("Popups");
    setupMainMenuOption(catPopups, new CwInfoPanel(constants),
        IMAGES.catPopups());
    setupMainMenuOption(catPopups, new CwWindowPanel(constants),
        IMAGES.catPopups());
    setupMainMenuOption(catPopups, new CwMessageBox(constants),
        IMAGES.catPopups());

    // Panels
    TreeItem catPanels = mainMenu.addItem("Layout & Panels");
    setupMainMenuOption(catPanels, new CwFillLayout(constants),
        IMAGES.catPanels());
    setupMainMenuOption(catPanels, new CwBoxLayout(constants),
        IMAGES.catPanels());
    setupMainMenuOption(catPanels, new CwBorderLayout(constants),
        IMAGES.catPanels());
    setupMainMenuOption(catPanels, new CwGridLayout(constants),
        IMAGES.catPanels());
    setupMainMenuOption(catPanels, new CwNestedBorderLayout(constants),
        IMAGES.catPanels());
    setupMainMenuOption(catPanels, new CwMixedLayout(constants),
        IMAGES.catPanels());

    TreeItem catLayoutPanels = catPanels.addItem("Panels");
    setupMainMenuOption(catLayoutPanels, new CwDeckLayoutPanel(constants),
        IMAGES.catPanels());
    setupMainMenuOption(catLayoutPanels, new CwStackLayoutPanel(constants),
        IMAGES.catPanels());
    setupMainMenuOption(catLayoutPanels, new CwTabLayoutPanel(constants),
        IMAGES.catPanels());
    setupMainMenuOption(catLayoutPanels, new CwBottomTabBars(constants),
        IMAGES.catPanels());

    TreeItem catLayoutTests = catPanels.addItem("Tests & Demos");
    TreeItem catFillLayoutTests = catLayoutTests.addItem("FillLayout");
    setupMainMenuOption(catFillLayoutTests, new CwFillLayoutTest1(constants),
        IMAGES.catPanels());
    TreeItem catBoxLayoutTests = catLayoutTests.addItem("BoxLayout");
    setupMainMenuOption(catBoxLayoutTests, new CwBoxLayoutTest1(constants),
        IMAGES.catPanels());
    setupMainMenuOption(catBoxLayoutTests, new CwBoxLayoutTest2(constants),
        IMAGES.catPanels());
    setupMainMenuOption(catBoxLayoutTests, new CwBoxLayoutTest_Horizontal(
        constants), IMAGES.catPanels());
    setupMainMenuOption(catBoxLayoutTests, new CwBoxLayoutTest_Vertical(
        constants), IMAGES.catPanels());
    setupMainMenuOption(catBoxLayoutTests, new CwBoxLayoutTest_Histogram(
        constants), IMAGES.catPanels());
    TreeItem catBorderLayoutTests = catLayoutTests.addItem("BorderLayout");
    setupMainMenuOption(catBorderLayoutTests, new CwBorderLayoutTest_Collapsed(
        constants), IMAGES.catPanels());
    TreeItem catGridLayoutTests = catLayoutTests.addItem("GridLayout");
    setupMainMenuOption(catGridLayoutTests, new CwCalculatorLayout(constants),
        IMAGES.catPanels());
    TreeItem catGWTPanelsTests = catLayoutTests.addItem("GWT Panels");
    setupMainMenuOption(catGWTPanelsTests, new CwHorizontalSplitPanel(constants),
        IMAGES.catPanels());
    setupMainMenuOption(catGWTPanelsTests, new CwVerticalSplitPanel(constants),
        IMAGES.catPanels());

    // Trees
    TreeItem catTrees = mainMenu.addItem("Trees");
    setupMainMenuOption(catTrees, new CwBasicTree(constants), IMAGES.catLists());
    setupMainMenuOption(catTrees, new CwLazyTree(constants), IMAGES.catLists());
    setupMainMenuOption(catTrees, new CwVerboseTree(constants),
        IMAGES.catLists());

    // Tables
    TreeItem catTables = mainMenu.addItem("Tables");
    setupMainMenuOption(catTables, new CwScrollTable(constants),
        IMAGES.catTables());
    setupMainMenuOption(catTables, new CwPagingScrollTable(constants),
        IMAGES.catTables());
    setupMainMenuOption(catTables, new CwTableLoadingBenchmark(constants),
        IMAGES.catTables());
    // setupMainMenuOption(catTables, new TablePage(constants),
    // images.catTables());

    // TreeTables
    TreeItem catTreeTables = mainMenu.addItem("TreeTables");
    setupMainMenuOption(catTreeTables, new CwBasicTreeTable(constants),
        IMAGES.catLists());
    setupMainMenuOption(catTreeTables, new CwLazyTreeTable(constants),
        IMAGES.catLists());

    // Other
    TreeItem catOther = mainMenu.addItem("Other Features");
    setupMainMenuOption(catOther, new CwActions(constants), IMAGES.catOther());

  }

  /**
   * Add an option to the main menu.
   * 
   * @param parent the {@link TreeItem} that is the option
   * @param content the {@link ContentWidget} to display when selected
   * @param image the icon to display next to the {@link TreeItem}
   */
  private void setupMainMenuOption(TreeItem parent, ContentWidget content,
      AbstractImagePrototype image) {
    // Create the TreeItem
    TreeItem option = parent.addItem(image.getHTML() + " " + content.getName());

    // Map the item to its history token and content widget
    itemWidgets.put(option, content);
    itemTokens.put(getContentWidgetToken(content), option);
  }

  /**
   * Create the options that appear next to the title.
   */
  private void setupOptionsPanel() {
    VerticalPanel vPanel = new VerticalPanel();
    vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    if (LocaleInfo.getCurrentLocale().isRTL()) {
      vPanel.getElement().setAttribute("align", "left");
    } else {
      vPanel.getElement().setAttribute("align", "right");
    }
    app.setOptionsWidget(vPanel);

    // Add the option to change the locale
    final ListBox localeBox = new ListBox();
    String currentLocale = LocaleInfo.getCurrentLocale().getLocaleName();
    if (currentLocale.equals("default")) {
      currentLocale = "en";
    }
    String[] localeNames = LocaleInfo.getAvailableLocaleNames();
    for (String localeName : localeNames) {
      if (!localeName.equals("default")) {
        String nativeName = LocaleInfo.getLocaleNativeDisplayName(localeName);
        localeBox.addItem(nativeName, localeName);
        if (localeName.equals(currentLocale)) {
          localeBox.setSelectedIndex(localeBox.getItemCount() - 1);
        }
      }
    }
    localeBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        String localeName = localeBox.getValue(localeBox.getSelectedIndex());
        Window.open(getHostPageLocation() + "?locale=" + localeName, "_self",
            "");
      }
    });
    HorizontalPanel localeWrapper = new HorizontalPanel();
    localeWrapper.add(IMAGES.locale().createImage());
    localeWrapper.add(localeBox);
    vPanel.add(localeWrapper);

    // Add the option to change the style
    final HorizontalPanel styleWrapper = new HorizontalPanel();
    vPanel.add(styleWrapper);
    for (int i = 0; i < ShowcaseConstants.STYLE_THEMES.length; i++) {
      final ThemeButton button = new ThemeButton(
          ShowcaseConstants.STYLE_THEMES[i]);
      styleWrapper.add(button);
      button.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          // Update the current theme
          CUR_THEME = button.getTheme();

          // Reload the current tab, loading the new theme if necessary
          // TODO TabBar bar = ((TabBar) app.getContentTitle());
          // TODO bar.selectTab(bar.getSelectedTab());

          // Load the new style sheets
          updateStyleSheets();
        }
      });
    }
  }

  /**
   * Create the title bar at the top of the Application.
   * 
   * @param constants the constant values to use
   */
  private void setupTitlePanel(ShowcaseConstants constants) {
    // Get the title from the internationalized constants
    String pageTitle = "<h1>" + constants.mainTitle() + "</h1><h2>"
        + constants.mainSubTitle() + "</h2>";

    // Add the title and some images to the title bar
    HorizontalPanel titlePanel = new HorizontalPanel();
    titlePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    titlePanel.add(IMAGES.gwtLogo().createImage());
    titlePanel.add(new HTML(pageTitle));
    app.setTitleWidget(titlePanel);
  }

  /**
   * Update the style sheets to reflect the current theme and direction.
   */
  private void updateStyleSheets() {
    // Generate the names of the style sheets to include
    String gwtStyleSheet = "gwt/" + CUR_THEME + "/" + CUR_THEME + ".css";
    String gwtMosaicStyleSheet = "gwt/" + CUR_THEME + "/Mosaic.css";
    String showcaseStyleSheet = CUR_THEME + "/Showcase.css";
    if (LocaleInfo.getCurrentLocale().isRTL()) {
      gwtStyleSheet = gwtStyleSheet.replace(".css", "_rtl.css");
      gwtMosaicStyleSheet = gwtMosaicStyleSheet.replace(".css", "_rtl.css");
      showcaseStyleSheet = showcaseStyleSheet.replace(".css", "_rtl.css");
    }

    // Find existing style sheets that need to be removed
    boolean styleSheetsFound = false;
    final HeadElement headElem = StyleSheetLoader.getHeadElement();
    final List<Element> toRemove = new ArrayList<Element>();
    NodeList<Node> children = headElem.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node node = children.getItem(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element elem = Element.as(node);
        if (elem.getTagName().equalsIgnoreCase("link")
            && elem.getPropertyString("rel").equalsIgnoreCase("stylesheet")) {
          styleSheetsFound = true;
          String href = elem.getPropertyString("href");
          // If the correct style sheets are already loaded, then we should have
          // nothing to remove.
          if (!href.contains(gwtStyleSheet)
              && !href.contains(gwtMosaicStyleSheet)
              && !href.contains(showcaseStyleSheet)) {
            toRemove.add(elem);
          }
        }
      }
    }

    // Return if we already have the correct style sheets
    if (styleSheetsFound && toRemove.size() == 0) {
      return;
    }

    // Detach the app while we manipulate the styles to avoid rendering issues
    RootPanel.get().remove(app);

    // Remove the old style sheets
    for (Element elem : toRemove) {
      headElem.removeChild(elem);
    }

    // Load the GWT theme style sheet
    String modulePath = GWT.getModuleBaseURL();
    Command callback = new Command() {
      public void execute() {
        // Different themes use different background colors for the body
        // element, but IE only changes the background of the visible content
        // on the page instead of changing the background color of the entire
        // page. By changing the display style on the body element, we force
        // IE to redraw the background correctly.
        RootPanel.getBodyElement().getStyle().setProperty("display", "none");
        RootPanel.getBodyElement().getStyle().setProperty("display", "");
        RootPanel.get().add(app);
      }
    };

    StyleSheetLoader.loadStyleSheet(modulePath + gwtStyleSheet,
        getCurrentReferenceStyleName("gwt"), null);
    StyleSheetLoader.loadStyleSheet(modulePath + gwtMosaicStyleSheet,
        getCurrentReferenceStyleName("mosaic"), null);

    // Load the showcase specific style sheet after the GWT & Mosaic theme style
    // sheet so that custom styles supercede the theme styles.
    StyleSheetLoader.loadStyleSheet(modulePath + showcaseStyleSheet,
        getCurrentReferenceStyleName("Application"), callback);
  }
}
