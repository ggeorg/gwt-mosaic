/*
 * Copyright 2008 Google Inc.
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
package org.mosaic.showcase.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mosaic.showcase.client.Application.ApplicationListener;
import org.mosaic.showcase.client.content.layout.BorderLayoutPage;
import org.mosaic.showcase.client.content.layout.BoxLayoutPage;
import org.mosaic.showcase.client.content.layout.LayoutTest1Page;
import org.mosaic.showcase.client.content.layout.LayoutTest2Page;
import org.mosaic.showcase.client.content.layout.LayoutTest3Page;
import org.mosaic.showcase.client.content.layout.MixedLayoutPage;
import org.mosaic.showcase.client.content.layout.NestedBorderLayoutPage;
import org.mosaic.showcase.client.content.other.Actions;
import org.mosaic.showcase.client.content.panels.BottomTabBarsPage;
import org.mosaic.showcase.client.content.panels.DeckLayoutPanelPage;
import org.mosaic.showcase.client.content.panels.TabLayoutPanelPage;
import org.mosaic.showcase.client.content.popups.InfoPanelPage;
import org.mosaic.showcase.client.content.popups.MessageBoxPage;
import org.mosaic.showcase.client.content.popups.WindowPanelPage;
import org.mosaic.showcase.client.content.tables.PagingScrollTablePage;
import org.mosaic.showcase.client.content.tables.ScrollTablePage;
import org.mosaic.showcase.client.content.tables.TableLoadingBenchmarkPage;
import org.mosaic.showcase.client.content.trees.BasicTreePage;
import org.mosaic.showcase.client.content.trees.LazyTreePage;
import org.mosaic.showcase.client.content.trees.VerboseTreePage;
import org.mosaic.showcase.client.content.treetables.BasicTreeTablePage;
import org.mosaic.showcase.client.content.widgets.BasicButtonPage;
import org.mosaic.showcase.client.content.widgets.ComboBoxPage;
import org.mosaic.showcase.client.content.widgets.CustomButtonPage;
import org.mosaic.showcase.client.content.widgets.DatePickerPage;
import org.mosaic.showcase.client.content.widgets.ToolBarPage;
import org.mosaic.showcase.client.content.widgets.ToolButtonPage;

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
 */
public class Showcase implements EntryPoint {

  /**
   * The type passed into the
   * {@link org.mosaic.showcase.generator.ShowcaseGenerator}.
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
  public static final ShowcaseImages images = (ShowcaseImages) GWT.create(ShowcaseImages.class);

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
  private Map<TreeItem, Page> itemWidgets = new HashMap<TreeItem, Page>();

  /**
   * Set the content to the {@link ContentWidget}.
   * 
   * @param content the {@link ContentWidget} to display
   */
  private void displayContentWidget(final Page content) {
    if (content != null) {
      if (!content.isInitialized()) {
        content.init();
      }
      app.setContent(content);
    }
  }

  /**
   * Get the token for a given content widget.
   * 
   * @return the content widget token.
   */
  private String getContentWidgetToken(Page content) {
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
        Page content = itemWidgets.get(item);
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
  }

  /**
   * Create the main links at the top of the application.
   * 
   * @param constants the constants with text
   */
  private void setupMainLinks(ShowcaseConstants constants) {
    // Link to GWT Mosaic Homepage
    app.addLink(new HTML("<a href=\"" + ShowcaseConstants.GWT_MOSAIC_HOMEPAGE + "\">"
        + constants.mainLinkMosaic() + "</a>"));

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
    setupMainMenuOption(catWidgets, new BasicButtonPage(constants), images.catWidgets());
    setupMainMenuOption(catWidgets, new CustomButtonPage(constants), images.catWidgets());
    setupMainMenuOption(catWidgets, new ToolButtonPage(constants), images.catWidgets());
    setupMainMenuOption(catWidgets, new ComboBoxPage(constants), images.catWidgets());
    setupMainMenuOption(catWidgets, new DatePickerPage(constants), images.catWidgets());
    setupMainMenuOption(catWidgets, new ToolBarPage(constants), images.catWidgets());

    // Popups
    TreeItem catPopups = mainMenu.addItem("Popups");
    setupMainMenuOption(catPopups, new InfoPanelPage(constants), images.catPopups());
    setupMainMenuOption(catPopups, new WindowPanelPage(constants), images.catPopups());
    setupMainMenuOption(catPopups, new MessageBoxPage(constants), images.catPopups());

    // Panels
    TreeItem catPanels = mainMenu.addItem("Layout & Panels");
    setupMainMenuOption(catPanels, new BoxLayoutPage(constants), images.catPanels());
    setupMainMenuOption(catPanels, new BorderLayoutPage(constants), images.catPanels());
    setupMainMenuOption(catPanels, new NestedBorderLayoutPage(constants),
        images.catPanels());
    setupMainMenuOption(catPanels, new MixedLayoutPage(constants), images.catPanels());
    
    TreeItem catLayoutPanels =catPanels.addItem("Panels");
    setupMainMenuOption(catLayoutPanels, new DeckLayoutPanelPage(constants), images.catPanels());
    setupMainMenuOption(catLayoutPanels, new TabLayoutPanelPage(constants), images.catPanels());
    setupMainMenuOption(catLayoutPanels, new BottomTabBarsPage(constants), images.catPanels());

    TreeItem catLayoutTests = catPanels.addItem("Tests");
    setupMainMenuOption(catLayoutTests, new LayoutTest1Page(constants),
        images.catPanels());
    setupMainMenuOption(catLayoutTests, new LayoutTest2Page(constants),
        images.catPanels());
    setupMainMenuOption(catLayoutTests, new LayoutTest3Page(constants),
        images.catPanels());

    // Trees
    TreeItem catTrees = mainMenu.addItem("Trees");
    setupMainMenuOption(catTrees, new BasicTreePage(constants), images.catLists());
    setupMainMenuOption(catTrees, new LazyTreePage(constants), images.catLists());
    setupMainMenuOption(catTrees, new VerboseTreePage(constants), images.catLists());

    // Tables
    TreeItem catTables = mainMenu.addItem("Tables");
    setupMainMenuOption(catTables, new ScrollTablePage(constants), images.catTables());
    setupMainMenuOption(catTables, new PagingScrollTablePage(constants),
        images.catTables());
    setupMainMenuOption(catTables, new TableLoadingBenchmarkPage(constants),
        images.catTables());
    // setupMainMenuOption(catTables, new TablePage(constants),
    // images.catTables());
    
    // TreeTables
    TreeItem catTreeTables = mainMenu.addItem("TreeTables");
    setupMainMenuOption(catTreeTables, new BasicTreeTablePage(constants), images.catLists());
    
    // Other
    TreeItem catOther = mainMenu.addItem("Other Features");
    setupMainMenuOption(catOther, new Actions(constants), images.catOther());

  }

  /**
   * Add an option to the main menu.
   * 
   * @param parent the {@link TreeItem} that is the option
   * @param content the {@link ContentWidget} to display when selected
   * @param image the icon to display next to the {@link TreeItem}
   */
  private void setupMainMenuOption(TreeItem parent, Page content,
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
        Window.open(getHostPageLocation() + "?locale=" + localeName, "_self", "");
      }
    });
    HorizontalPanel localeWrapper = new HorizontalPanel();
    localeWrapper.add(images.locale().createImage());
    localeWrapper.add(localeBox);
    vPanel.add(localeWrapper);

    // Add the option to change the style
    final HorizontalPanel styleWrapper = new HorizontalPanel();
    vPanel.add(styleWrapper);
    for (int i = 0; i < ShowcaseConstants.STYLE_THEMES.length; i++) {
      final ThemeButton button = new ThemeButton(ShowcaseConstants.STYLE_THEMES[i]);
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
    titlePanel.add(images.gwtLogo().createImage());
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
          if (!href.contains(gwtStyleSheet) && !href.contains(gwtMosaicStyleSheet)
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
