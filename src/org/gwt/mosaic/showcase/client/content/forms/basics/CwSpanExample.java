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
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
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
 * Demonstrates how components can span multiple columns and rows.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwSpanExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwSpanExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates how components can span multiple columns and rows.";
  }

  @Override
  public String getName() {
    return "Span";
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
    tabPanel.add(newColumnSpan(), "Column Span");
    tabPanel.add(newRowSpan(), "Row Span");

    return tabPanel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newColumnSpan() {
    FormLayout layout = new FormLayout("pref, 8px, 100px, 4px, 200px",
        "pref, 6px, pref, 6px, pref, 6px, pref");

    LayoutPanel panel = new ScrollLayoutPanel(layout);

    panel.add(newLabel("Name:"), CellConstraints.xy(1, 1));
    panel.add(new TextBox(), CellConstraints.xyw(3, 1, 3));

    panel.add(newLabel("Phone:"), CellConstraints.xy(1, 3));
    panel.add(new TextBox(), CellConstraints.xyw(3, 3, 3));

    panel.add(newLabel("ZIP, City:"), CellConstraints.xy(1, 5));
    panel.add(new TextBox(), CellConstraints.xy(3, 5));
    panel.add(new TextBox(), CellConstraints.xy(5, 5));

    panel.add(newLabel("Country:"), CellConstraints.xy(1, 7));
    panel.add(new TextBox(), CellConstraints.xyw(3, 7, 3));

    return panel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newRowSpan() {
    FormLayout layout = new FormLayout("200px, 25px, 200px",
        "2*(pref, 2px, pref, 9px), pref, 2px, pref");

    LayoutPanel panel = new ScrollLayoutPanel(layout);

    panel.add(newLabel("Name:"), CellConstraints.xy(1, 1));
    panel.add(new TextBox(), CellConstraints.xy(1, 3));

    panel.add(newLabel("Phone:"), CellConstraints.xy(1, 5));
    panel.add(new TextBox(), CellConstraints.xy(1, 7));

    panel.add(newLabel("Fax:"), CellConstraints.xy(1, 9));
    panel.add(new TextBox(), CellConstraints.xy(1, 11));

    panel.add(newLabel("Notes:"), CellConstraints.xy(3, 1));
    panel.add(new TextArea(), CellConstraints.xywh(3, 3, 1, 9));

    return panel;
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
