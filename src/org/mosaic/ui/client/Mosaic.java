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

import java.util.ArrayList;
import java.util.List;

import org.mosaic.core.client.DOM;
import org.mosaic.ui.client.demo.BasicButtonPage;
import org.mosaic.ui.client.demo.BorderLayoutPage;
import org.mosaic.ui.client.demo.BottomTabBarsPage;
import org.mosaic.ui.client.demo.BoxLayoutPage;
import org.mosaic.ui.client.demo.PagingScrollTablePage;
import org.mosaic.ui.client.demo.TableLoadingBenchmarkPage;
import org.mosaic.ui.client.demo.ComboBoxPage;
import org.mosaic.ui.client.demo.CustomButtonPage;
import org.mosaic.ui.client.demo.DatePickerPage;
import org.mosaic.ui.client.demo.DeckLayoutPanelPage;
import org.mosaic.ui.client.demo.DemoConstants;
import org.mosaic.ui.client.demo.InfoPanelPage;
import org.mosaic.ui.client.demo.LayoutTest1Page;
import org.mosaic.ui.client.demo.LayoutTest2Page;
import org.mosaic.ui.client.demo.MessageBoxPage;
import org.mosaic.ui.client.demo.MixedLayoutPage;
import org.mosaic.ui.client.demo.MosaicButtonPage;
import org.mosaic.ui.client.demo.MosaicConstants;
import org.mosaic.ui.client.demo.NestedBorderLayoutPage;
import org.mosaic.ui.client.demo.Page;
import org.mosaic.ui.client.demo.ScrollTablePage;
import org.mosaic.ui.client.demo.TabLayoutPanelPage;
import org.mosaic.ui.client.demo.TablePage;
import org.mosaic.ui.client.demo.WindowPanelPage;
import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.BorderLayoutData;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.FillLayout;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Mosaic implements EntryPoint {

  /**
   * The type passed into the {@link org.mosaic.ui.generator.MosaicGenerator}.
   */
  private static final class GeneratorInfo {
  }

  /**
   * Images used in the demo.
   */
  public interface MosaicTreeImages extends TreeImages {

    /**
     * An image indicating a leaf.
     * 
     * @return a prototype of this image
     */
    @Resource("noimage.png")
    AbstractImagePrototype treeLeaf();
  }

  private static class TreeBuilder {

    public static void buildTree(Tree tree, Element ul) {
      final int size = DOM.getChildCount(ul);
      for (int i = 0; i < size; i++) {
        final Element li = DOM.getChild(ul, i);
        final TreeItem treeItem = createTreeItem(li);
        tree.addItem(treeItem);
        final int childCount = DOM.getChildCount(li);
        for (int j = 0; j < childCount; j++) {
          Element subList = DOM.getChild(li, j);
          buildTree(treeItem, subList);
        }
      }
    }

    public static void buildTree(TreeItem parent, Element ul) {
      final int size = DOM.getChildCount(ul);
      for (int i = 0; i < size; i++) {
        final Element li = DOM.getChild(ul, i);
        final TreeItem treeItem = createTreeItem(li);
        parent.addItem(treeItem);
        final int childCount = DOM.getChildCount(li);
        for (int j = 0; j < childCount; j++) {
          Element subList = DOM.getChild(li, j);
          buildTree(treeItem, subList);
        }
      }
    }

    private static TreeItem createTreeItem(Element li) {
      final String id = DOM.getElementAttribute(li, "id");
      final String title = DOM.getElementAttribute(li, "title");
      final TreeItem treeItem = new TreeItem(title);
      if (id != null && id.length() > 0) {
        treeItem.getElement().setId(id);
      }
      return treeItem;
    }
  }

  /**
   * The base style name.
   */
  public static final String DEFAULT_STYLE_NAME = "Mosaic";

  /**
   * The current style theme.
   */
  public static String CUR_THEME = MosaicConstants.STYLE_THEMES[0];

  /**
   * The main menu.
   */
  private Tree mainMenu;

  /**
   * 
   */
  LayoutPanel centerPanel;

  /**
   * 
   */
  private List<Page> pages = new ArrayList<Page>();

  /**
   * 
   */
  private TreeItem current;

  /**
   * 
   */
  private Page currentPage;

  public Mosaic() {
    // Create the constants
    DemoConstants constants = GWT.create(DemoConstants.class);
    
    // Widgets
    pages.add(new BasicButtonPage(constants));
    pages.add(new CustomButtonPage(constants));
    pages.add(new MosaicButtonPage(constants));
    pages.add(new ComboBoxPage(constants));
    pages.add(new DatePickerPage(constants));

    // Widgets/Table
    pages.add(new ScrollTablePage(constants));
    pages.add(new PagingScrollTablePage(constants));
    pages.add(new TableLoadingBenchmarkPage(constants));
    pages.add(new TablePage(constants));

    // Widgets/Tabs
    pages.add(new TabLayoutPanelPage(constants));
    pages.add(new BottomTabBarsPage(constants));

    // Widgets/Windows
    pages.add(new InfoPanelPage(constants));
    pages.add(new WindowPanelPage(constants));
    pages.add(new MessageBoxPage(constants));

    // Layout pages
    pages.add(new BoxLayoutPage(constants));
    pages.add(new BorderLayoutPage(constants));
    pages.add(new NestedBorderLayoutPage(constants));
    pages.add(new MixedLayoutPage(constants));
    
    // Layout/Panels
    pages.add(new DeckLayoutPanelPage(constants));
    
    // Layout/Advanced Layout
    pages.add(new LayoutTest1Page(constants));
    pages.add(new LayoutTest2Page(constants));
  }

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    try {
      // Generate the source code and css for the examples
      GWT.create(GeneratorInfo.class);

      LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));

      HorizontalPanel header = new HorizontalPanel();
      header.setStyleName("header");
      Label label = new Label("Mosaic Showcase of Features");
      label.setStyleName("title");
      header.add(label);

      // Setup the main menu
      TreeImages treeImages = GWT.create(MosaicTreeImages.class);
      mainMenu = new Tree(treeImages);
      mainMenu.setAnimationEnabled(true);
      // mainMenu.addStyleName(DEFAULT_STYLE_NAME + "-menu");
      mainMenu.addTreeListener(new TreeListener() {
        public void onTreeItemSelected(TreeItem item) {
          show(item);
        }

        public void onTreeItemStateChanged(TreeItem item) {
          // Nothing to do here!
        }
      });

      ScrollPanel westPanel = new ScrollPanel(mainMenu);

      Element toc = DOM.getElementById("toc");
      TreeBuilder.buildTree(mainMenu, toc);

      // Setup center panel
      centerPanel = new LayoutPanel(new FillLayout());

      LayoutPanel main = new LayoutPanel(new BorderLayout());
      main.add(westPanel, new BorderLayoutData(BorderLayoutRegion.WEST, 200.0, 100, 350, true));
      main.add(centerPanel);

      layoutPanel.add(header, new BoxLayoutData(FillStyle.HORIZONTAL));
      layoutPanel.add(main, new BoxLayoutData(FillStyle.BOTH));

      Viewport.get().initWidget(layoutPanel, false);
    } catch (Throwable t) {
      Window.alert(t.getMessage());
    }
  }

  private void show(TreeItem item) {
    if (current == item) {
      return;
    }
    current = item;
    currentPage = null;
    String pageId = item.getElement().getId();
    for (Page page : pages) {
      if (page.getId().equals(pageId)) {
        currentPage = page;
        break;
      }
    }

    // calculate item path

    if (currentPage == null) {
      centerPanel.clear();
      centerPanel.layout();
      return;
    }

    if (!currentPage.isInitialized()) {
      currentPage.init();
    }

    centerPanel.clear();
    centerPanel.add(currentPage);
    centerPanel.layout();
  }
}
