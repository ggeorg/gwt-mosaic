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

import org.gwt.mosaic.forms.client.builder.ButtonBarBuilder;
import org.gwt.mosaic.forms.client.builder.DefaultFormBuilder;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates how to build button bars with a fixed button order or with a
 * button order that honors the platform's style.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwButtonOrderExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwButtonOrderExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates how to build button bars with a fixed button order or with a button order that honors the platform's style.";
  }

  @Override
  public String getName() {
    return "Button Order";
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
    FormLayout layout = new FormLayout("right:pref:grow, 4dlu, pref");
    DefaultFormBuilder rowBuilder = new DefaultFormBuilder(layout);
    // rowBuilder.setDefaultDialogBorder();

    rowBuilder.appendSeparator("Left to Right");
    rowBuilder.append("Ordered:",
        buildButtonSequence(ButtonBarBuilder.createLeftToRightBuilder()));
    rowBuilder.append("Fixed:",
        buildIndividualButtons(ButtonBarBuilder.createLeftToRightBuilder()));

    rowBuilder.appendSeparator("Right to Left");
    rowBuilder.append("Ordered:",
        buildButtonSequence(createRightToLeftBuilder()));
    rowBuilder.append("Fixed:",
        buildIndividualButtons(createRightToLeftBuilder()));

    rowBuilder.appendSeparator("Platform Default Order");
    rowBuilder.append("Ordered:", buildButtonSequence(new ButtonBarBuilder()));
    rowBuilder.append("Fixed:", buildIndividualButtons(new ButtonBarBuilder()));

    return rowBuilder.getPanel();
  }

  /**
   * Builds and returns a button bar honoring the builder's button order.
   * 
   * @param builder the builder used to build the bar
   * @return a button bar that honors the builder's button order
   */
  @ShowcaseSource
  private Widget buildButtonSequence(ButtonBarBuilder builder) {
    builder.addGriddedButtons(new Button[] {
        new Button("One"), new Button("Two"), new Button("Three")});
    return builder.getPanel();
  }

  /**
   * Builds and returns a button bar ignoring the builder's button order.
   * Instead a fixed left to right order is used.
   * 
   * @param builder the builder used to build the bar
   * @return a button bar with a fixed left to right button order
   */
  @ShowcaseSource
  private Widget buildIndividualButtons(ButtonBarBuilder builder) {
    builder.addGridded(new Button("One"));
    builder.addRelatedGap();
    builder.addGridded(new Button("Two"));
    builder.addRelatedGap();
    builder.addGridded(new Button("Three"));
    return builder.getPanel();
  }

  /**
   * Creates and returns a button bar builder with a fixed right-to-left button
   * order. Unlike the factory method
   * {@link ButtonBarBuilder#createLeftToRightBuilder()} this method is useful
   * for demonstration purposes only.
   * 
   * @return a ButtonBarBuilder with right-to-left button order
   */
  @ShowcaseSource
  private static ButtonBarBuilder createRightToLeftBuilder() {
    ButtonBarBuilder builder = new ButtonBarBuilder();
    builder.setLeftToRightButtonOrder(false);
    return builder;
  }

}
