/*
 * Copyright 2008 Georgios J. Georgopoulos
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
package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.Page;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.ComboBox;
import org.mosaic.ui.client.datepicker.DateBox;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Example file.
 */
@ShowcaseStyle( {".gwt-ComboBox"})
public class ComboBoxPage extends Page {
  
  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface DemoConstants extends Constants, Page.DemoConstants {
    
  }
  
  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private DemoConstants constants;

  public ComboBoxPage(DemoConstants constants) {
    super(constants);
    this.constants = constants;
  }

  /**
   * Load this example.
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    final BoxLayout vLayout = new BoxLayout(Orientation.VERTICAL);
    layoutPanel.setLayout(vLayout);
    
    ComboBox comboBox1 = new DateBox();
    comboBox1.ensureDebugId("mosaicAbstractComboBox-normal");
    
    ComboBox comboBox2 = new DateBox();
    comboBox2.ensureDebugId("mosaicAbstractComboBox-normal");
    
    ComboBox comboBox3 = new DateBox();
    comboBox3.ensureDebugId("mosaicAbstractComboBox-normal");
    
    ComboBox comboBox4 = new DateBox();
    comboBox4.ensureDebugId("mosaicAbstractComboBox-normal");
    
    layoutPanel.add(comboBox1, new BoxLayoutData());
    layoutPanel.add(comboBox2, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(comboBox3, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel.add(comboBox4, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(new SimplePanel(), new BoxLayoutData(FillStyle.BOTH));
  }

  @Override
  public String getName() {
    return "ComboBox";
  }

}
