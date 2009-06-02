/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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

import org.gwt.mosaic.core.client.UserAgent;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.showcase.client.content.layout.AbstractLayoutPage;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-TabLayoutPanel"})
public class CwDynamicTabLayoutPanel extends AbstractLayoutPage {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwDynamicTabLayoutPanel(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Add/remove tabs programmatically. When you add too many tabs, a tab scrolling interface will appear.";
  }

  @Override
  public String getName() {
    return "Advanced Tabs";
  }

  /**
   * A Tab counter.
   */
  @ShowcaseData
  private int tabCounter = 0;

  /**
   * The {@link TabLayoutPanel}.
   */
  @ShowcaseData
  private DecoratedTabLayoutPanel tabPanel;

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout());

    tabPanel = new DecoratedTabLayoutPanel();

    layoutPanel.add(tabPanel, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(createButtonBar(), new BoxLayoutData(FillStyle.VERTICAL));

    return layoutPanel;
  }

  /**
   * 
   * @return
   */
  @ShowcaseSource
  private Widget createButtonBar() {
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));

    layoutPanel.add(new Button("Add", new ClickHandler() {
      public void onClick(ClickEvent event) {
        tabPanel.add(new HTML("<h1>Tab #" + tabCounter + "</h1>"), "Tab #"
            + tabCounter);

        WidgetHelper.getParent(tabPanel).layout();

        if (UserAgent.isGecko()) {
          DeferredCommand.addCommand(layoutCmd);
        }

        ++tabCounter;
      }
    }), new BoxLayoutData(FillStyle.HORIZONTAL));

    layoutPanel.add(new Button("Remove", new ClickHandler() {
      public void onClick(ClickEvent event) {
        int widgetIndex = tabPanel.getSelectedTab();
        if (widgetIndex == -1) {
          return;
        }
        tabPanel.selectTab(widgetIndex - 1);
        tabPanel.remove(widgetIndex);

        WidgetHelper.getParent(tabPanel).layout();

        if (UserAgent.isGecko()) {
          DeferredCommand.addCommand(layoutCmd);
        }
      }
    }), new BoxLayoutData(FillStyle.HORIZONTAL));

    layoutPanel.add(new Button("Remove All", new ClickHandler() {
      public void onClick(ClickEvent event) {
        tabPanel.clear();

        WidgetHelper.getParent(tabPanel).layout();

        if (UserAgent.isGecko()) {
          DeferredCommand.addCommand(layoutCmd);
        }
      }
    }), new BoxLayoutData(FillStyle.HORIZONTAL));

    return layoutPanel;
  }

  /**
   * Optional layout command.
   */
  @ShowcaseData
  private Command layoutCmd = new Command() {
    public void execute() {
      WidgetHelper.invalidate(tabPanel);
      WidgetHelper.getParent(tabPanel).layout();
    }
  };

}
