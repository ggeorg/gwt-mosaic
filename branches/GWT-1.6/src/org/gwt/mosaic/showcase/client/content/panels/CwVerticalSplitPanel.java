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
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalSplitPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwVerticalSplitPanel extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwVerticalSplitPanel(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Give users the freedom to decide how to allocate space using this split panel (a VerticalSplitPanel demo).";
  }

  @Override
  public String getName() {
    return "VerticalSplitPanel";
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
    VerticalSplitPanel hSplit = new VerticalSplitPanel();
    hSplit.ensureDebugId("cwVerticalSplitPanel");
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
    p1.add(new Button("NORTH"), new BorderLayoutData(Region.NORTH));
    p1.add(new Button("SOUTH"), new BorderLayoutData(Region.SOUTH));
    p1.add(new Button("WEST"), new BorderLayoutData(Region.WEST));
    p1.add(new Button("EAST"), new BorderLayoutData(Region.EAST));
    p1.add(new Button("CENTER"));
    hSplit.setTopWidget(p1);

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
    hSplit.setBottomWidget(p2);

    layoutPanel.add(hSplit, new FillLayoutData(true));

    return layoutPanel;
  }

  @Override
  protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
    GWT.runAsync(new RunAsyncCallback() {

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }

      public void onSuccess() {
        callback.onSuccess(onInitialize());
      }
    });
  }
  
}
