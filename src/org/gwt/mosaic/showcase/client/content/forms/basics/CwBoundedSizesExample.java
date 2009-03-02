/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopoulos.
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
/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * o Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * o Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * o Neither the name of JGoodies Karsten Lentzsch nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.gwt.mosaic.showcase.client.content.forms.basics;

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates the basic FormLayout sizes: constant, minimum, preferred.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwBoundedSizesExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwBoundedSizesExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates how to specify lower and upper bounds for column and row sizes. "
        + "This is useful to give components or related panels a more stable layout. "
        + "The first two tabs below jump, whereas the remaining tabs are stable.";
  }

  @Override
  public String getName() {
    return "Bounded Sizes";
  }
  
  @Override
  public boolean hasStyle() {
    return false;
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    // final LayoutPanel layoutPanel = new LayoutPanel();

    final DecoratedTabLayoutPanel tabPanel = new DecoratedTabLayoutPanel();
    tabPanel.add(buildJumping1Panel(), "Jumping 1");
    tabPanel.add(buildJumping2Panel(), "Jumping 2");
    tabPanel.add(buildStable1Panel(), "Stable 1");
    tabPanel.add(buildStable2Panel(), "Stable 2");

    return tabPanel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget buildJumping1Panel() {
    FormLayout layout = new FormLayout(
        "pref, 3dlu, [35dlu,min], 2dlu, min, 2dlu, min, 2dlu, min, ",
        EDITOR_ROW_SPEC);
    return buildEditorGeneralPanel(layout);
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget buildJumping2Panel() {
    FormLayout layout = new FormLayout(
        "pref, 3dlu, [35dlu,min], 2dlu, min, 2dlu, min, 2dlu, min, ",
        EDITOR_ROW_SPEC);
    return buildEditorTransportPanel(layout);
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget buildStable1Panel() {
    FormLayout layout = new FormLayout(
        "[50dlu,pref], 3dlu, [35dlu,min], 2dlu, min, 2dlu, min, 2dlu, min, ",
        EDITOR_ROW_SPEC);
    return buildEditorGeneralPanel(layout);
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget buildStable2Panel() {
    FormLayout layout = new FormLayout(
        "[50dlu,pref], 3dlu, [35dlu,min], 2dlu, min, 2dlu, min, 2dlu, min, ",
        EDITOR_ROW_SPEC);
    return buildEditorTransportPanel(layout);
  }

  /**
   * 
   */
  @ShowcaseData
  private static final String EDITOR_ROW_SPEC = "3*(p, 3dlu), p";

  /**
   * Builds and returns the editor's general tab for the given layout.
   * 
   * @param layout the layout to be used
   * @return the editor's general tab
   */
  @ShowcaseSource
  private Widget buildEditorGeneralPanel(FormLayout layout) {
    layout.setColumnGroups(new int[][] {{3, 5, 7, 9}});
    PanelBuilder builder = new PanelBuilder(layout);

    //builder.setDefaultDialogBorder();

    builder.addLabel("File number:", CellConstraints.xy(1, 1));
    builder.add(new TextBox(), CellConstraints.xyw(3, 1, 7));
    builder.addLabel("RFQ number:", CellConstraints.xy(1, 3));
    builder.add(new TextBox(), CellConstraints.xyw(3, 3, 7));
    builder.addLabel("Entry date:", CellConstraints.xy(1, 5));
    builder.add(new TextBox(), CellConstraints.xy(3, 5));
    builder.addLabel("Sales Person:", CellConstraints.xy(1, 7));
    builder.add(new TextBox(), CellConstraints.xyw(3, 7, 7));

    return builder.getPanel();
  }

  /**
   * Builds and answer the editor's transport tab for the given layout.
   * 
   * @param layout the layout to be used
   * @return the editor's transport panel
   */
  @ShowcaseSource
  private Widget buildEditorTransportPanel(FormLayout layout) {
    layout.setColumnGroups(new int[][] {{3, 5, 7, 9}});
    PanelBuilder builder = new PanelBuilder(layout);

    // builder.setDefaultDialogBorder();

    builder.addLabel("Shipper:", CellConstraints.xy(1, 1));
    builder.add(new TextBox(), CellConstraints.xy(3, 1));
    builder.add(new TextBox(), CellConstraints.xyw(5, 1, 5));
    builder.addLabel("Consignee:", CellConstraints.xy(1, 3));
    builder.add(new TextBox(), CellConstraints.xy(3, 3));
    builder.add(new TextBox(), CellConstraints.xyw(5, 3, 5));
    builder.addLabel("Departure:", CellConstraints.xy(1, 5));
    builder.add(new TextBox(), CellConstraints.xy(3, 5));
    builder.add(new TextBox(), CellConstraints.xyw(5, 5, 5));
    builder.addLabel("Destination:", CellConstraints.xy(1, 7));
    builder.add(new TextBox(), CellConstraints.xy(3, 7));
    builder.add(new TextBox(), CellConstraints.xyw(5, 7, 5));

    return builder.getPanel();
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
