/*
 * Copyright (c) 2009 GWT Mosaic Georgopoulos J. Georgios
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
package org.gwt.mosaic.showcase.client.content.forms.factories;

import org.gwt.mosaic.forms.client.builder.ButtonBarBuilder;
import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.factories.ButtonBarFactory;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates the use of Factories as provided by the Forms framework.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwButtonBarFactoryExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwButtonBarFactoryExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Presents the button bars as provided by the ButtonBarFactory.";
  }

  @Override
  public String getName() {
    return "Button Bars";
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
    final DecoratedTabLayoutPanel tabPanel = new DecoratedTabLayoutPanel();
    tabPanel.add(buildButtonBar1Panel(), "Dialog 1");
    tabPanel.add(buildButtonBar2Panel(), "Dialog 2");
    tabPanel.add(buildButtonBar3Panel(), "Dialog 3");
    tabPanel.add(buildAddRemovePropertiesPanel(), "List 1");
    tabPanel.add(buildAddRemovePanel(), "List 2");
    return tabPanel;
  }

  /**
   * Dialog 1.
   */
  @ShowcaseSource
  private Widget buildButtonBar1Panel() {
    FormLayout layout = new FormLayout("default:grow",
        "0:grow, p, 4dlu, p, 4dlu, p, 4dlu, p");

    PanelBuilder builder = new PanelBuilder(layout);
    // builder.setDefaultDialogBorder();

    builder.nextRow();
    builder.add(ButtonBarFactory.buildCloseBar(new Button("Close")));
    builder.nextRow(2);
    builder.add(ButtonBarFactory.buildOKBar(new Button("OK")));
    builder.nextRow(2);
    builder.add(ButtonBarFactory.buildOKCancelBar(new Button("OK"), new Button(
        "Cancel")));
    builder.nextRow(2);
    builder.add(ButtonBarFactory.buildOKCancelApplyBar(new Button("OK"),
        new Button("Cancel"), new Button("Apply")));

    return builder.getPanel();
  }

  /**
   * Dialog 2.
   */
  @ShowcaseSource
  private Widget buildButtonBar2Panel() {
    FormLayout layout = new FormLayout("default:grow",
        "0:grow, p, 4dlu, p, 4dlu, p, 4dlu, p");

    PanelBuilder builder = new PanelBuilder(layout);
    // builder.setDefaultDialogBorder();

    builder.nextRow();
    builder.add(ButtonBarFactory.buildCloseHelpBar(new Button("Close"),
        new Button("Help")));
    builder.nextRow(2);
    builder.add(ButtonBarFactory.buildOKHelpBar(new Button("OK"), new Button(
        "Help")));
    builder.nextRow(2);
    builder.add(ButtonBarFactory.buildOKCancelHelpBar(new Button("OK"),
        new Button("Cancel"), new Button("Help")));
    builder.nextRow(2);
    builder.add(ButtonBarFactory.buildOKCancelApplyHelpBar(new Button("OK"),
        new Button("Cancel"), new Button("Apply"), new Button("Help")));

    return builder.getPanel();
  }

  /**
   * Dialog 3.
   */
  @ShowcaseSource
  private Widget buildButtonBar3Panel() {
    FormLayout layout = new FormLayout("default:grow",
        "0:grow, p, 4dlu, p, 4dlu, p, 4dlu, p");

    PanelBuilder builder = new PanelBuilder(layout);
    // builder.setDefaultDialogBorder();

    builder.nextRow();
    builder.add(ButtonBarFactory.buildHelpCloseBar(new Button("Help"),
        new Button("Close")));
    builder.nextRow(2);
    builder.add(ButtonBarFactory.buildHelpOKBar(new Button("Help"), new Button(
        "OK")));
    builder.nextRow(2);
    builder.add(ButtonBarFactory.buildHelpOKCancelBar(new Button("Help"),
        new Button("OK"), new Button("Cancel")));
    builder.nextRow(2);
    builder.add(ButtonBarFactory.buildHelpOKCancelApplyBar(new Button("Help"),
        new Button("OK"), new Button("Cancel"), new Button("Apply")));

    return builder.getPanel();
  }

  /**
   * List 1..
   */
  @ShowcaseSource
  private Widget buildAddRemovePropertiesPanel() {
    FormLayout layout = new FormLayout("fill:default:grow",
        "fill:p:grow, 4dlu, p, 14dlu, " + "fill:p:grow, 4dlu, p");

    PanelBuilder builder = new PanelBuilder(layout);
    // builder.setDefaultDialogBorder();

    builder.add(new TextArea());
    builder.nextRow(2);
    builder.add(ButtonBarFactory.buildAddRemovePropertiesLeftBar(new Button(
        "Add\u2026"), new Button("Remove"), new Button("Properties\u2026")));
    builder.nextRow(2);

    builder.add(new TextArea());
    builder.nextRow(2);
    builder.add(ButtonBarFactory.buildAddRemovePropertiesRightBar(new Button(
        "Add\u2026"), new Button("Remove"), new Button("Properties\u2026")));
    return builder.getPanel();
  }

  /**
   * List 2.
   */
  @ShowcaseSource
  private Widget buildAddRemovePanel() {
    FormLayout layout = new FormLayout(
        "fill:default:grow, 9dlu, fill:default:grow", "fill:p:grow, 4dlu, p");

    PanelBuilder builder = new PanelBuilder(layout);
    // builder.setDefaultDialogBorder();

    builder.add(new TextArea(), CellConstraints.xy(1, 1));
    builder.add(ButtonBarFactory.buildAddRemoveLeftBar(new Button("Add\u2026"),
        new Button("Remove")), CellConstraints.xy(1, 3));

    builder.add(new TextArea(), CellConstraints.xy(3, 1));
    builder.add(ButtonBarFactory.buildAddRemoveRightBar(
        new Button("Add\u2026"), new Button("Remove")),
        CellConstraints.xy(3, 3));
    return builder.getPanel();
  }

}
