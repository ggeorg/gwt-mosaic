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

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * <p>
 * A generic application that includes a title bar, main menu, content area, and
 * some external links at the top.
 * </p>
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.Application { Applied to the entire Application }</li>
 * <li>.Application-top { The top portion of the Application }</li>
 * <li>.Application-title { The title widget }</li>
 * <li>.Application-links { The main external links }</li>
 * <li>.Application-options { The options widget }</li>
 * <li>.Application-menu { The main menu }</li>
 * <li>.Application-content-wrapper { The element around the content }</li>
 * </ul>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class Application extends Viewport implements HasLayoutManager {
  /**
   * Images used in the {@link Application}.
   */
  public interface ApplicationImages extends TreeImages {
    /**
     * An image indicating a leaf.
     * 
     * @return a prototype of this image
     */
    @Resource("noimage.png")
    AbstractImagePrototype treeLeaf();
  }

  /**
   * A listener to handle events from the Application.
   */
  public interface ApplicationListener {
    /**
     * Fired when a menu item is selected.
     * 
     * @param item the item that was selected
     */
    void onMenuItemSelected(com.google.gwt.user.client.ui.TreeItem item);
  }

  /**
   * The base style name.
   */
  public static final String DEFAULT_STYLE_NAME = "Application";

  /**
   * The wrapper around the content.
   */
  private LayoutPanel contentWrapper;

  /**
   * The panel that holds the main links.
   */
  private HorizontalPanel linksPanel;

  /**
   * The {@link ApplicationListener}.
   */
  private ApplicationListener listener = null;

  /**
   * The main menu.
   */
  private Tree mainMenu;

  /**
   * The panel that contains the title widget and links.
   */
  private FlexTable topPanel;

  /**
   * Constructor.
   */
  public Application() {
    super();

    // Setup the main layout widget
    final LayoutPanel layoutPanel = getWidget();
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));

    // Setup the top panel with the title and links
    createTopPanel();
    layoutPanel.add(topPanel, new BoxLayoutData(FillStyle.HORIZONTAL));

    final LayoutPanel bottomPanel = new LayoutPanel(new BorderLayout());
    layoutPanel.add(bottomPanel, new BoxLayoutData(FillStyle.BOTH));

    // Add the main menu
    createMainMenu();

    final CaptionLayoutPanel westPanel = new CaptionLayoutPanel("Select demo");
    westPanel.add(new ScrollPanel(mainMenu));
    westPanel.getHeader().add(Showcase.IMAGES.showcaseDemos().createImage());
    final ImageButton collapseBtn = new ImageButton(
        Caption.IMAGES.toolCollapseLeft());
    westPanel.getHeader().add(collapseBtn, CaptionRegion.RIGHT);

    collapseBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        bottomPanel.setCollapsed(westPanel, !layoutPanel.isCollapsed(westPanel));
        bottomPanel.layout();
      }
    });

    bottomPanel.add(westPanel, new BorderLayoutData(Region.WEST,
        200, 100, 350, true));
    //bottomPanel.setCollapsed(westPanel, true);

    // Add the content wrapper
    contentWrapper = new LayoutPanel(new FillLayout());
    contentWrapper.addStyleName(DEFAULT_STYLE_NAME + "-content-wrapper");
    bottomPanel.add(contentWrapper);
    if (LocaleInfo.getCurrentLocale().isRTL()) {
      // bottomPanel.setCellHorizontalAlignment(contentDecorator,
      // HasHorizontalAlignment.ALIGN_LEFT);
      // contentDecorator.getElement().setAttribute("align", "LEFT");
    } else {
      // bottomPanel.setCellHorizontalAlignment(contentDecorator,
      // HasHorizontalAlignment.ALIGN_RIGHT);
      // contentDecorator.getElement().setAttribute("align", "RIGHT");
    }
    setContent(null);
  }

  /**
   * Add a link to the top of the page.
   * 
   * @param link the widget to add to the mainLinks
   */
  public void addLink(Widget link) {
    if (linksPanel.getWidgetCount() > 0) {
      linksPanel.add(new HTML("&nbsp;|&nbsp;"));
    }
    linksPanel.add(link);
  }

  /**
   * @return the {@link Widget} in the content area
   */
  public Widget getContent() {
    return contentWrapper.getWidget(0);
  }

  /**
   * @return the main menu.
   */
  public Tree getMainMenu() {
    return mainMenu;
  }

  /**
   * @return the {@link Widget} used as the title
   */
  public Widget getTitleWidget() {
    return topPanel.getWidget(0, 0);
  }

  /**
   * Set the {@link Widget} to display in the content area.
   * 
   * @param content the content widget
   */
  public void setContent(Widget content) {
    contentWrapper.clear();
    if (content != null) {
      contentWrapper.add(content);
    }
  }

  /**
   * Set the {@link ApplicationListener}.
   * 
   * @param listener the listener
   */
  public void setListener(ApplicationListener listener) {
    this.listener = listener;
  }

  /**
   * Set the {@link Widget} to use as options, which appear to the right of the
   * title bar.
   * 
   * @param options the options widget
   */
  public void setOptionsWidget(Widget options) {
    topPanel.setWidget(1, 1, options);
  }

  /**
   * Set the {@link Widget} to use as the title bar.
   * 
   * @param title the title widget
   */
  public void setTitleWidget(Widget title) {
    topPanel.setWidget(1, 0, title);
  }

  /**
   * Create the main menu.
   */
  private void createMainMenu() {
    // Setup the main menu
    ApplicationImages treeImages = GWT.create(ApplicationImages.class);
    mainMenu = new Tree(treeImages);
    mainMenu.setAnimationEnabled(true);
    mainMenu.addStyleName(DEFAULT_STYLE_NAME + "-menu");
    mainMenu.addTreeListener(new TreeListener() {
      public void onTreeItemSelected(TreeItem item) {
        if (listener != null) {
          listener.onMenuItemSelected(item);
          contentWrapper.layout();
        }
      }

      public void onTreeItemStateChanged(TreeItem item) {
      }
    });
  }

  /**
   * Create the panel at the top of the page that contains the title and links.
   */
  private void createTopPanel() {
    boolean isRTL = LocaleInfo.getCurrentLocale().isRTL();
    topPanel = new FlexTable();
    topPanel.setCellPadding(0);
    topPanel.setCellSpacing(0);
    topPanel.setStyleName(DEFAULT_STYLE_NAME + "-top");
    FlexCellFormatter formatter = topPanel.getFlexCellFormatter();

    // Setup the links cell
    linksPanel = new HorizontalPanel();
    topPanel.setWidget(0, 0, linksPanel);
    formatter.setStyleName(0, 0, DEFAULT_STYLE_NAME + "-links");
    if (isRTL) {
      formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
    } else {
      formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
    }
    formatter.setColSpan(0, 0, 2);

    // Setup the title cell
    setTitleWidget(null);
    formatter.setStyleName(1, 0, DEFAULT_STYLE_NAME + "-title");

    // Setup the options cell
    setOptionsWidget(null);
    formatter.setStyleName(1, 1, DEFAULT_STYLE_NAME + "-options");
    if (isRTL) {
      formatter.setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
    } else {
      formatter.setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
    }

    // Align the content to the top
    topPanel.getRowFormatter().setVerticalAlign(0,
        HasVerticalAlignment.ALIGN_TOP);
    topPanel.getRowFormatter().setVerticalAlign(1,
        HasVerticalAlignment.ALIGN_TOP);
  }

  @Override
  protected LayoutPanel getWidget() {
    return (LayoutPanel) super.getWidget();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
   */
  public int[] getPreferredSize() {
    return getWidget().getPreferredSize();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayoutManager#layout()
   */
  public void layout() {
    getWidget().layout();
  }
}