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
package org.gwt.mosaic.showcase.client.content.panels;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.Showcase;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.layout.BaseLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.google.gwt.widgetideas.client.ResizableWidgetCollection;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwTabPanel extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwTabPanel(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Divide content over multiple tabs.";
  }

  @Override
  public String getName() {
    return "TabPanel";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));

    // Create a tab panel
    final DecoratedTabPanel tabPanel = new DecoratedTabPanel();
    tabPanel.setAnimationEnabled(true);

    // Add a home tab
    String[] tabTitles = {"Home", "GWT Mosaic Logo", "More Info", "Border Layout"};
    HTML homeText = new HTML("Click one of the tabs to see more content.");
    tabPanel.add(homeText, tabTitles[0]);

    // Add a tab with an image
    VerticalPanel vPanel = new VerticalPanel();
    vPanel.add(Showcase.IMAGES.gwtLogo().createImage());
    tabPanel.add(vPanel, tabTitles[1]);

    // Add a tab
    HTML moreInfo = new HTML("Tabs are highly customizable using CSS.");
    tabPanel.add(moreInfo, tabTitles[2]);

    // Add a tab with layout
    tabPanel.add(createLayoutContent(), tabTitles[3]);

    tabPanel.selectTab(0);
    tabPanel.ensureDebugId("cwTabPanel");

    // We need to layout each selected widget that implements HasLayoutManager
    tabPanel.addTabListener(new TabListener() {
      public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
        return true;
      }

      public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
        if (tabPanel.getWidget(tabIndex) instanceof HasLayoutManager) {
          final HasLayoutManager widget = (HasLayoutManager) tabPanel.getWidget(tabIndex);
          final int[] size = widget.getPreferredSize();
          BaseLayout.setSize((Widget) widget, -1, size[1]);
          widget.layout();
        }
      }
    });

    layoutPanel.add(tabPanel, new BoxLayoutData(FillStyle.HORIZONTAL));

    final CheckBox checkBox = new CheckBox("Resize Animation");
    checkBox.setChecked(tabPanel.isAnimationEnabled());
    checkBox.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        tabPanel.setAnimationEnabled(checkBox.isChecked());
      }
    });
    layoutPanel.add(checkBox);

    // Add to Resizable Collection, so that we run layout each time the tabPanel
    // is resized because of the check box we added bellow the tab panel.
    ResizableWidgetCollection.get().add(new ResizableWidget() {
      public Element getElement() {
        return tabPanel.getDeckPanel().getElement();
      }

      public boolean isAttached() {
        return tabPanel.getDeckPanel().isAttached();
      }

      public void onResize(int width, int height) {
        layoutPanel.layout();
      }
    });

    return layoutPanel;
  }

  /**
   * Create content for layout.
   */
  @ShowcaseSource
  private LayoutPanel createLayoutContent() {
    final LayoutPanel layoutPanel = new LayoutPanel(new BorderLayout());
    layoutPanel.setPadding(0);

    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");
    final Button b5 = new Button("Button 5");

    layoutPanel.add(b1, new BorderLayoutData(Region.NORTH, 10, 200));
    layoutPanel.add(b2, new BorderLayoutData(Region.SOUTH, 10, 200));
    layoutPanel.add(b3, new BorderLayoutData(Region.WEST, 10, 200));
    layoutPanel.add(b4, new BorderLayoutData(Region.EAST, 10, 200));
    layoutPanel.add(b5, new BorderLayoutData(Region.CENTER, true));

    return layoutPanel;
  }

}
