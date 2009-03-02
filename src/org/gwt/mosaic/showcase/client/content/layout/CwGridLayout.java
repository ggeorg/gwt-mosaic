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
package org.gwt.mosaic.showcase.client.content.layout;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.layout.GridLayout;
import org.gwt.mosaic.ui.client.layout.GridLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwGridLayout extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwGridLayout(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "GridLayout description";
  }

  @Override
  public String getName() {
    return "GridLayout";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new GridLayout(3, 3));

    final Button b11 = new Button("Button 1-1");
    final Button b12 = new Button("Button 1-2");
    final Button b13 = new Button("Button 1-3");

    final Button b21 = new Button("Button 2-1");

    final Button b31 = new Button("Button 3-1");
    GridLayoutData gl = new GridLayoutData(1, 3, true);
    gl.setHorizontalAlignment(GridLayoutData.ALIGN_CENTER);
    gl.setVerticalAlignment(GridLayoutData.ALIGN_MIDDLE);
    layoutPanel.add(b11, gl);
    layoutPanel.add(b12);
    layoutPanel.add(b13, new GridLayoutData(1, 2));
    layoutPanel.add(b21);
    layoutPanel.add(b31, new GridLayoutData(2, 1, true));

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
