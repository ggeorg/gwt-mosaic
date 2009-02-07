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
package org.gwt.mosaic.showcase.client.content.forms.building;

import org.gwt.mosaic.forms.client.builder.ButtonStackBuilder;
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
 * Demonstrates how to build button stacks using the ButtonStackBuilder.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwButtonStacksExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwButtonStacksExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates how to build button stacks that comply with style guides. "
        + "The ButtonStackBuilder ensures minimum widths, "
        + "uses logical gaps for related and unrelated buttons, "
        + "and allows to add gridded or ungridded buttons - with default or narrow margins.";
  }

  @Override
  public String getName() {
    return "Button Stacks";
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
    tabPanel.add(buildButtonStackNoBuilder(), "No Builder");
    tabPanel.add(buildButtonStackWithBuilder(), "Builder");
    tabPanel.add(buildButtonStackRelated(), "Related");
    tabPanel.add(buildButtonStackUnrelated(), "Unrelated");
    tabPanel.add(buildButtonStackMixedDefault(), "Mix");
    tabPanel.add(buildButtonStackMixedNarrow(), "Mix Narrow");
    return tabPanel;
  }

  /**
   * No Builder.
   */
  @ShowcaseSource
  private Widget buildButtonStackNoBuilder() {
    LayoutPanel buttonStack = new LayoutPanel(new FormLayout("p", "p, 4px, p"));
    buttonStack.add(new Button("Yes"), CellConstraints.xy(1, 1));
    buttonStack.add(new Button("No"), CellConstraints.xy(1, 3));
    return wrap(buttonStack,
        "This stack has been built without a ButtonStackBuilder.\n"
            + " o The buttons have no minimum width and\n"
            + " o The gaps use pixel sizes and do not scale with the font\n"
            + " o The gaps may become inconsisten in a team.");
  }

  /**
   * Builder.
   */
  @ShowcaseSource
  private Widget buildButtonStackWithBuilder() {
    ButtonStackBuilder builder = new ButtonStackBuilder();
    builder.addGridded(new Button("Yes"));
    builder.addRelatedGap();
    builder.addGridded(new Button("No"));
    return wrap(builder.getPanel(),
        "This stack has been built with a ButtonStackBuilder.\n"
            + " o The buttons have a minimum width and\n"
            + " o The gap uses a logical size that follows a style guide.");
  }

  /**
   * Related.
   */
  @ShowcaseSource
  private Widget buildButtonStackRelated() {
    ButtonStackBuilder builder = new ButtonStackBuilder();
    builder.addGridded(new Button("Related"));
    builder.addRelatedGap();
    builder.addGridded(new Button("Related"));
    builder.addRelatedGap();
    builder.addGridded(new Button("Related"));
    return wrap(builder.getPanel(),
        "This stack uses the logical gap for related buttons.\n");
  }

  /**
   * Unrelated.
   */
  @ShowcaseSource
  private Widget buildButtonStackUnrelated() {
    ButtonStackBuilder builder = new ButtonStackBuilder();
    builder.addGridded(new Button("Unrelated"));
    builder.addUnrelatedGap();
    builder.addGridded(new Button("Unrelated"));
    builder.addUnrelatedGap();
    builder.addGridded(new Button("Unrelated"));
    return wrap(builder.getPanel(),
        "This stack uses the logical gap for unrelated buttons.\n");
  }

  /**
   * Mix.
   */
  @ShowcaseSource
  private Widget buildButtonStackMixedDefault() {
    ButtonStackBuilder builder = new ButtonStackBuilder();
    builder.addGridded(new Button("OK"));
    builder.addRelatedGap();
    builder.addGridded(new Button("Cancel"));
    builder.addUnrelatedGap();
    builder.addGridded(new Button("Help"));
    builder.addUnrelatedGap();
    builder.addGlue();
    builder.addFixed(new Button("Copy to Clipboard"));

    return wrap(builder.getPanel(),
        "Demonstrates a glue (between Help and Copy),\n"
            + "has related and unrelated buttons and\n"
            + "a button with long label with the default margin.");
  }

  /**
   * Mix Narrow.
   */
  @ShowcaseSource
  private Widget buildButtonStackMixedNarrow() {
    ButtonStackBuilder builder = new ButtonStackBuilder();
    builder.addGridded(new Button("OK"));
    builder.addRelatedGap();
    builder.addGridded(new Button("Cancel"));
    builder.addUnrelatedGap();
    builder.addGridded(new Button("Help"));
    builder.addUnrelatedGap();
    builder.addGlue();
    builder.addGridded(new Button("Copy to Clipboard"));

    return wrap(builder.getPanel(),
        "Demonstrates a glue (between Help and Copy),\n"
            + "has related and unrelated buttons and\n"
            + "a button with long label with a narrow margin.\n\n"
            + "Note that some look&feels do not support\n"
            + "the narrow margin feature, and conversely,\n"
            + "others have only narrow margins.");
  }

  // Helper Code ************************************************************

  /**
   * Helper Code.
   */
  @ShowcaseSource
  private static Widget wrap(Widget buttonBar, String text) {
    TextArea textArea = new TextArea();
    textArea.setText(text);
    // textArea.setMargin(new Insets(6, 10, 4, 6));
    // Non-editable but shall use the editable background.
    textArea.setReadOnly(true);
    // textArea.putClientProperty("JTextArea.infoBackground", Boolean.TRUE);
    // Component textPane = new JScrollPane(textArea);

    FormLayout layout = new FormLayout("fill:100dlu:grow, 6dlu, p",
        "fill:56dlu:grow");
    LayoutPanel panel = new LayoutPanel(layout);
    // panel.setBorder(Borders.DIALOG_BORDER);
    panel.setPadding(0);
    panel.add(textArea, CellConstraints.xy(1, 1));
    panel.add(buttonBar, CellConstraints.xy(3, 1));
    return panel;
  }

}
