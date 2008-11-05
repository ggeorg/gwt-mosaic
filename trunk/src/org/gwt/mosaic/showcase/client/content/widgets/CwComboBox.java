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
package org.gwt.mosaic.showcase.client.content.widgets;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.ComboBox;
import org.gwt.mosaic.ui.client.datepicker.DateComboBox;
import org.gwt.mosaic.ui.client.datepicker.DateTimeComboBox;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@ShowcaseStyle( {".gwt-ComboBox"})
public class CwComboBox extends ContentWidget {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {
    String mosaicComboBoxDescription();

    String mosaicComboBoxName();
  }

  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private CwConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwComboBox(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }

  @Override
  public String getDescription() {
    return constants.mosaicComboBoxDescription();
  }

  @Override
  public String getName() {
    return constants.mosaicComboBoxName();
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));

    DateComboBox comboBox1 = new DateComboBox();
    comboBox1.ensureDebugId("mosaicAbstractComboBox-normal");

    DateTimeComboBox comboBox2 = new DateTimeComboBox();
    comboBox2.ensureDebugId("mosaicAbstractComboBox-normal");

    ComboBox comboBox3 = new DateComboBox();
    comboBox3.ensureDebugId("mosaicAbstractComboBox-normal");
    comboBox3.setEnabled(false);

    ComboBox comboBox4 = new DateComboBox();
    comboBox4.ensureDebugId("mosaicAbstractComboBox-normal");

    layoutPanel.add(comboBox1, new BoxLayoutData());
    layoutPanel.add(comboBox2, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(comboBox3, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel.add(comboBox4, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(new SimplePanel(), new BoxLayoutData(FillStyle.BOTH));

    return layoutPanel;
  }

}
