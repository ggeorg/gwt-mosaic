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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates the FormLayout growing options: none, default, weighted.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwGrowingExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwGrowingExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates the FormLayout growing options: none, default, weighted.";
  }

  @Override
  public String getName() {
    return "Growing";
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
    tabPanel.add(newAllGrowing(), "All");
    tabPanel.add(newHalfGrowing(), "Half");
    tabPanel.add(newPercentGrowing(), "Percent");
    tabPanel.add(newPercent2Growing(), "Percent 2");
    tabPanel.add(newVertical1Growing(), "Vertical 1");
    tabPanel.add(newVertical2Growing(), "Vertical 2");

    return tabPanel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newAllGrowing() {
    FormLayout layout = new FormLayout("pref, 6px, pref:grow",
        "pref, 12px, pref");

    LayoutPanel panel = new ScrollLayoutPanel(layout);

    panel.add(newLabel("Fixed"), CellConstraints.xy(1, 1));
    panel.add(newLabel("Gets all extra space"), CellConstraints.xy(3, 1));

    panel.add(createTextBox(5), CellConstraints.xy(1, 3));
    panel.add(createTextBox(5), CellConstraints.xy(3, 3));

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newHalfGrowing() {
    FormLayout layout = new FormLayout("pref, 6px, 0:grow, 6px, 0:grow",
        "pref, 12px, pref");

    LayoutPanel panel = new ScrollLayoutPanel(layout);

    panel.add(newLabel("Fixed"), CellConstraints.xy(1, 1));
    panel.add(newLabel("Gets half of extra space"), CellConstraints.xy(3, 1));
    panel.add(newLabel("gets half of extra space"), CellConstraints.xy(5, 1));

    panel.add(createTextBox(5), CellConstraints.xy(1, 3));
    panel.add(createTextBox(5), CellConstraints.xy(3, 3));
    panel.add(createTextBox(5), CellConstraints.xy(5, 3));

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newPercentGrowing() {
    FormLayout layout = new FormLayout(
        "pref, 6px, 0:grow(0.25), 6px, 0:grow(0.75)", "pref, 12px, pref");

    LayoutPanel panel = new ScrollLayoutPanel(layout);

    panel.add(newLabel("Fixed"), CellConstraints.xy(1, 1));
    panel.add(newLabel("Gets 25% of extra space"), CellConstraints.xy(3, 1));
    panel.add(newLabel("Gets 75% of extra space"), CellConstraints.xy(5, 1));

    panel.add(createTextBox(5), CellConstraints.xy(1, 3));
    panel.add(createTextBox(5), CellConstraints.xy(3, 3));
    panel.add(createTextBox(5), CellConstraints.xy(5, 3));

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newPercent2Growing() {
    FormLayout layout = new FormLayout("pref:grow(0.33), 6px, pref:grow(0.67)",
        "pref, 12px, pref");

    LayoutPanel panel = new ScrollLayoutPanel(layout);

    panel.add(newLabel("Gets 33% of the space"), CellConstraints.xy(1, 1));
    panel.add(newLabel("Gets 67% of the space"), CellConstraints.xy(3, 1));

    panel.add(createTextBox(5), CellConstraints.xy(1, 3));
    panel.add(createTextBox(5), CellConstraints.xy(3, 3));

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newVertical1Growing() {
    FormLayout layout = new FormLayout("pref, 12px, pref",
        "pref, 6px, fill:0:grow(0.25), 6px, fill:0:grow(0.75)");

    LayoutPanel panel = new ScrollLayoutPanel(layout);

    panel.add(newLabel("Fixed"), CellConstraints.xy(1, 1));
    panel.add(newLabel("Gets 25% of extra space"), CellConstraints.xy(1, 3));
    panel.add(newLabel("Gets 75% of extra space"), CellConstraints.xy(1, 5));

    panel.add(createTextArea(4, 30), CellConstraints.xy(3, 1));
    panel.add(createTextArea(4, 30), CellConstraints.xy(3, 3));
    panel.add(createTextArea(4, 30), CellConstraints.xy(3, 5));

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newVertical2Growing() {
    FormLayout layout = new FormLayout("pref, 12px, pref",
        "fill:0:grow(0.25), 6px, fill:0:grow(0.75)");

    LayoutPanel panel = new ScrollLayoutPanel(layout);

    panel.add(newLabel("Gets 25% of extra space"), CellConstraints.xy(1, 1));
    panel.add(newLabel("Gets 75% of extra space"), CellConstraints.xy(1, 3));

    panel.add(createTextArea(4, 30), CellConstraints.xy(3, 1));
    panel.add(createTextArea(4, 30), CellConstraints.xy(3, 3));

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget createTextBox(int cols) {
    TextBox textBox = new TextBox();
    textBox.setWidth(1 + "em");
    return textBox;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget createTextArea(int rows, int cols) {
    TextArea textArea = new TextArea();
    textArea.setVisibleLines(rows);
    textArea.setCharacterWidth(cols);
    return textArea;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newLabel(String string) {
    final Label label = new Label(string);
    DOM.setStyleAttribute(label.getElement(), "overflow", "hidden");
    return new WidgetWrapper(label, HasAlignment.ALIGN_LEFT,
        HasAlignment.ALIGN_MIDDLE);
  }
  
}
