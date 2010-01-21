/*
 * Copyright 2008 Google Inc.
 * 
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos.
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
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.ProgressBar;
import org.gwt.mosaic.ui.client.ProgressBar.TextFormatter;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
@ShowcaseStyle( {".gwt-Button"})
public class CwProgressBar extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwProgressBar(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "The ProgressBar Widget displays progress over a range of values.";
  }

  @Override
  public String getName() {
    return "ProgressBar";
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
    layoutPanel.setPadding(0);

    final ProgressBar progressBar1 = new ProgressBar();

    final Timer progressBar1Timer = new Timer() {
      @Override
      public void run() {
        progressBar1.setProgress(progressBar1.getProgress() + 10);

        if (progressBar1.getPercent() < 1.0) {
          schedule(333);
        }
      }
    };

    layoutPanel.add(new HTML("<b>Default TextFormater:</b>"), new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(progressBar1);
    layoutPanel.add(new Button("Show", new ClickHandler() {
      public void onClick(ClickEvent event) {
        progressBar1Timer.cancel();
        progressBar1.setProgress(0);
        progressBar1Timer.schedule(1);
      }
    }));

    final ProgressBar progressBar2 = new ProgressBar(0, 50, 0,
        new TextFormatter() {
          @Override
          protected String getText(ProgressBar bar, double curProgress) {
            return Math.round(curProgress) + " / "
                + Math.round(bar.getMaxProgress());
          }
        });

    final Timer progressBar2Timer = new Timer() {
      @Override
      public void run() {
        progressBar2.setProgress(progressBar2.getProgress() + 1);

        if (progressBar2.getPercent() < 1.0) {
          schedule(333);
        }
      }
    };

    layoutPanel.add(new HTML("<b>Custom TextFormater:</b>"), new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(progressBar2, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(new Button("Show", new ClickHandler() {
      public void onClick(ClickEvent event) {
        progressBar2Timer.cancel();
        progressBar2.setProgress(0);
        progressBar2Timer.schedule(1);
      }
    }));

    return layoutPanel;
  }
}
