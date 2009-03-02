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
package org.gwt.mosaic.showcase.client.content.forms.building;

import org.gwt.mosaic.forms.client.builder.DefaultFormBuilder;
import org.gwt.mosaic.forms.client.debug.FormDebugPanel;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Demonstrates how to find bugs in the layout using the {@link FormDebugPanel}
 * and the {@link FormDebugUtils}.
 * <p>
 * The example also demonstrates efficient panel building with the
 * DefaultFormBuilder. The builder has been configured to use a leading indent
 * column.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwFormDebugExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwFormDebugExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Exhibits layout details using the FormDebugPanel and the FormDebugUtils. "
        + "The example also demonstrates efficient panel building with the DefaultFormBuilder. "
        + "The builder has been configured to use a leading indent column.";
  }

  @Override
  public String getName() {
    return "Debug a Form";
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
    FormLayout layout = new FormLayout(
        "12dlu, pref, 3dlu, [45dlu,min], 2dlu, min, 2dlu, min, 2dlu, min, ", "");
    layout.setColumnGroups(new int[][] {{4, 6, 8, 10}});

    DefaultFormBuilder builder = new DefaultFormBuilder(layout, new FormDebugPanel());
    // builder.setDefaultDialogBorder();
    builder.setLeadingColumnOffset(1);

    builder.appendSeparator("General");
    builder.append("File Number:", newTextBox(true), 7);
    builder.append("RFQ Number:", newTextBox(true), 7);
    builder.append("BL/MBL:", newTextBox(true), newTextBox(true));
    builder.nextLine();

    builder.appendSeparator("Addresses");
    builder.append("Customer:", newTextBox(true), newTextBox(false), 5);
    builder.append("Shipper:", newTextBox(true), newTextBox(false), 5);
    builder.append("Consignee:", newTextBox(true), newTextBox(false), 5);

    builder.appendSeparator("Transport");
    builder.append("Departure:", newTextBox(true), newTextBox(false), 5);
    builder.append("Destination:", newTextBox(true), newTextBox(false), 5);
    builder.append("Delivery date:", newTextBox(true));
    builder.nextLine();

    return builder.getPanel();
  }

  /**
   * Creates and returns a text box.
   */
  @ShowcaseSource
  private TextBox newTextBox(boolean enabled) {
    final TextBox textBox = new TextBox();
    textBox.setEnabled(enabled);
    return textBox;
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
