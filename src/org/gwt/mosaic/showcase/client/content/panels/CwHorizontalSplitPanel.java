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
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.GridLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwHorizontalSplitPanel extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwHorizontalSplitPanel(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Give users the freedom to decide how to allocate space using this split panel.";
  }

  @Override
  public String getName() {
    return "GWT Horizontal Split Panel";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel();

    // Create a Horizontal Split Panel
    HorizontalSplitPanel hSplit = new HorizontalSplitPanel();
    hSplit.ensureDebugId("cwHorizontalSplitPanel");
    hSplit.setSplitPosition("50%");

    // Add some content
    LayoutPanel p1 = new LayoutPanel(new BorderLayout()) {
      @Override
      protected void onLoad() {
        super.onLoad();
        DeferredCommand.addCommand(new Command() {
          public void execute() {
            setSize("100%", "100%");
          }
        });
      }
    };
    p1.setPadding(0);
    p1.add(new Button("NORTH"), new BorderLayoutData(BorderLayoutRegion.NORTH));
    p1.add(new Button("SOUTH"), new BorderLayoutData(BorderLayoutRegion.SOUTH));
    p1.add(new Button("WETS"), new BorderLayoutData(BorderLayoutRegion.WEST));
    p1.add(new Button("EAST"), new BorderLayoutData(BorderLayoutRegion.EAST));
    p1.add(new Button("CENTER"));
    hSplit.setLeftWidget(p1);

    LayoutPanel p2 = new LayoutPanel(new GridLayout(2, 2)) {
      @Override
      protected void onLoad() {
        super.onLoad();
        DeferredCommand.addCommand(new Command() {
          public void execute() {
            setSize("100%", "100%");
          }
        });
      }
    };
    p2.setPadding(0);
    p2.add(new Button("(0,0)"));
    p2.add(new Button("(0,1)"));
    p2.add(new Button("(1,0)"));
    p2.add(new Button("(1,1)"));
    hSplit.setRightWidget(p2);

    layoutPanel.add(hSplit, new FillLayoutData(true));

    return layoutPanel;
  }

}
