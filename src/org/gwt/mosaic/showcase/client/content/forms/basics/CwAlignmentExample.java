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
package org.gwt.mosaic.showcase.client.content.forms.basics;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates the different FormLayout alignments.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwAlignmentExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwAlignmentExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates the FormLayout alignment options.";
  }

  @Override
  public String getName() {
    return "Alignments";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    //final LayoutPanel layoutPanel = new LayoutPanel();

    final DecoratedTabLayoutPanel tabPanel = new DecoratedTabLayoutPanel();
    tabPanel.add(buildHorizontalButtons(), "Horizontal");
    tabPanel.add(buildVerticalButtons(), "Vertical");

    return tabPanel;
  }
  
  /**
   * 
   */
  @ShowcaseSource
  private Widget buildHorizontalButtons() {
    FormLayout layout = new FormLayout(
        "left:pref, 15px, center:pref, 15px, right:pref, 15px, fill:pref, 15px, pref",
        "pref, 12px, pref, 4px, pref, 4px, pref, 4px, pref, 4px, pref");
    LayoutPanel panel = new LayoutPanel(layout);

    // Add components to the panel.
    panel.add(newLabel("Left"), new CellConstraints().xy(1, 1));
    panel.add(new Button("Name"), new CellConstraints().xy(1, 3));
    panel.add(new Button("Phone"), new CellConstraints().xy(1, 5));
    panel.add(new Button("Fax"), new CellConstraints().xy(1, 7));
    panel.add(new Button("Email"), new CellConstraints().xy(1, 9));
    panel.add(new Button("Address"), new CellConstraints().xy(1, 11));

    panel.add(newLabel("Center"), new CellConstraints().xy(3, 1));
    panel.add(new Button("Name"), new CellConstraints().xy(3, 3));
    panel.add(new Button("Phone"), new CellConstraints().xy(3, 5));
    panel.add(new Button("Fax"), new CellConstraints().xy(3, 7));
    panel.add(new Button("Email"), new CellConstraints().xy(3, 9));
    panel.add(new Button("Address"), new CellConstraints().xy(3, 11));

    panel.add(newLabel("Right"), new CellConstraints().xy(5, 1));
    panel.add(new Button("Name"), new CellConstraints().xy(5, 3));
    panel.add(new Button("Phone"), new CellConstraints().xy(5, 5));
    panel.add(new Button("Fax"), new CellConstraints().xy(5, 7));
    panel.add(new Button("Email"), new CellConstraints().xy(5, 9));
    panel.add(new Button("Address"), new CellConstraints().xy(5, 11));

    panel.add(newLabel("Fill"),
        new CellConstraints().xy(7, 1, "center, center"));
    panel.add(new Button("Name"), new CellConstraints().xy(7, 3));
    panel.add(new Button("Phone"), new CellConstraints().xy(7, 5));
    panel.add(new Button("Fax"), new CellConstraints().xy(7, 7));
    panel.add(new Button("Email"), new CellConstraints().xy(7, 9));
    panel.add(new Button("Address"), new CellConstraints().xy(7, 11));

    panel.add(newLabel("Default"), new CellConstraints().xy(9, 1,
        "center, center"));
    panel.add(new Button("Name"), new CellConstraints().xy(9, 3));
    panel.add(new Button("Phone"), new CellConstraints().xy(9, 5));
    panel.add(new Button("Fax"), new CellConstraints().xy(9, 7));
    panel.add(new Button("Email"), new CellConstraints().xy(9, 9));
    panel.add(new Button("Address"), new CellConstraints().xy(9, 11));

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget buildVerticalButtons() {
    FormLayout layout = new FormLayout("pref, 8dlu, pref, 4dlu, pref",
        "top:pref, 9dlu, center:pref, 9dlu, bottom:pref, 9dlu, fill:pref, 9dlu, pref");

    // Create a panel that uses the layout.
    LayoutPanel panel = new LayoutPanel(layout);

    // Add components to the panel.
    panel.add(newLabel("Top"), new CellConstraints().xy(1, 1));
    panel.add(createSmallButton(), new CellConstraints().xy(3, 1));
    panel.add(createMediumButton(), new CellConstraints().xy(5, 1));

    panel.add(newLabel("Center"), new CellConstraints().xy(1, 3));
    panel.add(createSmallButton(), new CellConstraints().xy(3, 3));
    panel.add(createMediumButton(), new CellConstraints().xy(5, 3));

    panel.add(newLabel("Bottom"), new CellConstraints().xy(1, 5));
    panel.add(createSmallButton(), new CellConstraints().xy(3, 5));
    panel.add(createMediumButton(), new CellConstraints().xy(5, 5));

    panel.add(newLabel("Fill"), new CellConstraints().xy(1, 7));
    panel.add(createSmallButton(), new CellConstraints().xy(3, 7));
    panel.add(createMediumButton(), new CellConstraints().xy(5, 7));

    panel.add(newLabel("Default"), new CellConstraints().xy(1, 9));
    panel.add(createSmallButton(), new CellConstraints().xy(3, 9));
    panel.add(createMediumButton(), new CellConstraints().xy(5, 9));

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newLabel(String string) {
    final Label label = new Label(string);
    DOM.setStyleAttribute(label.getElement(), "display", "inline");
    DOM.setStyleAttribute(label.getElement(), "overflow", "hidden");
    return label;
  }
  
  /**
   * 
   */
  @ShowcaseSource
  private Button createSmallButton() {
    return new Button("<html>One</html>");
  }

  /**
   * 
   */
  @ShowcaseSource
  private Button createMediumButton() {
    return new Button("<html>One<br>Two</html>");
  }

}
