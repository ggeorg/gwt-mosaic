/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopoulos
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
package org.gwt.mosaic.showcase.client.content.validation.basic;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwSimpleDomainValidationExample extends ContentWidget {

  private TextBox orderNoTextBox;
  private TextBox orderDateBox;
  private TextBox deliveryDateBox;
  private TextArea deliveryNotesArea;
  private Button okButton;
  private Button closeButton;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwSimpleDomainValidationExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Simple Domain Validation description";
  }

  @Override
  public String getName() {
    return "Simple Domain Validation";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // init();
    initWidgets();
    updateView();
    return null;// buildPanelWithReportInBottom();
  }

  private void updateView(){
    
  }

  /**
   * Creates and initializes the UI components.
   */
  protected void initWidgets() {
    orderNoTextBox = new TextBox();
    orderDateBox = new TextBox();
    deliveryDateBox = new TextBox();
    deliveryNotesArea = new TextArea();

    okButton = new Button();
    closeButton = new Button();
  }

}
