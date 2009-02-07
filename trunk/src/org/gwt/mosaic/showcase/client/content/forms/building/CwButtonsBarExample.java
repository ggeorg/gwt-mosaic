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

import org.gwt.mosaic.forms.client.builder.DefaultFormBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates how to build button bars using a ButtonBarBuilder.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwButtonsBarExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwButtonsBarExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates how to build button bars that comply with style guides. "
        + "The ButtonBarBuilder ensures minimum widths, uses logical gaps for related and unrelated buttons, "
        + "and allows to add gridded or ungridded buttons - with default or narrow margins.";
  }

  @Override
  public String getName() {
    return "Button Bars";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    final DecoratedTabLayoutPanel tabPanel = new DecoratedTabLayoutPanel();
    tabPanel.add(buildButtonBar1Panel(), "No Builder");
    // tabPanel.add(buildButtonBar2Panel(), "Builder");
    // tabPanel.add(buildButtonBar3Panel(), "Related");
    // tabPanel.add(buildButtonBar4Panel(), "Unrelated ");
    // tabPanel.add(buildButtonMixedBar1Panel(), "Mix");
    // tabPanel.add(buildButtonMixedBar2Panel(), "Mix Narrow");
    return tabPanel;
  }

  /**
   * No Builder.
   */
  @ShowcaseSource
  private Widget buildButtonBar1Panel() {
    final LayoutPanel buttonBar = new LayoutPanel(new FormLayout(
        "0:grow, p, 4px, p", "p"));
    buttonBar.add(new Button("Yes"), new CellConstraints(2, 1));
    buttonBar.add(new Button("No"), new CellConstraints(4, 1));

    return wrap(buttonBar,
        "This bar has been built without a ButtonBarBuilder:\n"
            + " o buttons have no minimum widths,\n"
            + " o the button order is fixed left-to-right,\n"
            + " o gaps may be inconsistent between team members.");
  }

  // Helper Code ************************************************************

  private static Widget wrap(Widget buttonBar, String text) {
    TextArea textArea = new TextArea();
    textArea.setText(text);
    // textArea.setMargin(new Insets(6, 10, 4, 6));
    // Non-editable but shall use the editable background.
    textArea.setReadOnly(true);
    // textArea.putClientProperty("JTextArea.infoBackground", Boolean.TRUE);
    // Component textPane = new JScrollPane(textArea);

    FormLayout layout = new FormLayout("fill:100dlu:grow",
        "fill:56dlu:grow, 4dlu, p");
    LayoutPanel panel = new LayoutPanel(layout);
    CellConstraints cc = new CellConstraints();
    // panel.setBorder(Borders.DIALOG_BORDER);
    panel.add(textArea, cc.xy(1, 1));
    panel.add(buttonBar, cc.xy(1, 3));
    return panel;
  }

}
