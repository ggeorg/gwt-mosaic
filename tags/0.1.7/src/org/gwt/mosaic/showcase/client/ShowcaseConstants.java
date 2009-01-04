/*
 * Copyright 2008 Google Inc.
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
package org.gwt.mosaic.showcase.client;

import org.gwt.mosaic.showcase.client.content.popups.CwMessageBox;
import org.gwt.mosaic.showcase.client.content.tables.CwPagingScrollTable;
import org.gwt.mosaic.showcase.client.content.tables.CwScrollTable;
import org.gwt.mosaic.showcase.client.content.tables.CwTableLoadingBenchmark;
import org.gwt.mosaic.showcase.client.content.treetables.CwBasicTreeTable;
import org.gwt.mosaic.showcase.client.content.widgets.CwBasicButton;
import org.gwt.mosaic.showcase.client.content.widgets.CwComboBox;
import org.gwt.mosaic.showcase.client.content.widgets.CwCustomButton;
import org.gwt.mosaic.showcase.client.content.widgets.CwDatePicker;
import org.gwt.mosaic.showcase.client.content.widgets.CwMenuBar;
import org.gwt.mosaic.showcase.client.content.widgets.CwToolBar;
import org.gwt.mosaic.showcase.client.content.widgets.CwToolButton;

import com.google.gwt.i18n.client.Constants;

/**
 * The constants used in this Page.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public interface ShowcaseConstants extends Constants,
    ContentWidget.CwConstants, CwBasicButton.CwConstants,
    CwCustomButton.CwConstants, CwToolButton.CwConstants,
    CwComboBox.CwConstants, CwToolBar.CwConstants, CwMenuBar.CwConstants,
    CwScrollTable.CwConstants, CwPagingScrollTable.CwConstants,
    CwTableLoadingBenchmark.CwConstants, CwBasicTreeTable.CwConstants,
    CwDatePicker.CwConstants, CwMessageBox.CwConstants {

  /**
   * The path to source code for examples, raw files, and style definitions.
   */
  String DST_SOURCE = "MosaicSource/";

  /**
   * The destination folder for parsed source code from Mosaic examples.
   */
  String DST_SOURCE_EXAMPLE = DST_SOURCE + "java/";

  /**
   * The destination folder for raw files that are included in entirety.
   */
  String DST_SOURCE_RAW = "raw/";

  /**
   * The destination folder for parsed CSS styles used in Mosaic examples.
   */
  String DST_SOURCE_STYLE = DST_SOURCE + "css/";

  /**
   * Link to GWT MOSAIC homepage.
   */
  String GWT_MOSAIC_HOMEPAGE = "http://code.google.com/p/gwt-mosaic/";

  /**
   * Link to GWT homepage.
   */
  String GWT_HOMEPAGE = "http://code.google.com/webtoolkit/";

  /**
   * Link to GWT examples page.
   */
  String GWT_EXAMPLES = GWT_HOMEPAGE + "examples/";

  /**
   * The available style themes that the user can select.
   */
  String[] STYLE_THEMES = {"standard", "chrome", "dark"};

  /**
   * @return text for the link to more examples
   */
  String mainLinkExamples();

  /**
   * @return text for the link to the GWT homepage
   */
  String mainLinkHomepage();

  /**
   * @return text for the link to the GWT Mosaic homepage
   */
  String mainLinkMosaic();

  /**
   * @return the sub title of the application
   */
  String mainSubTitle();

  /**
   * @return the title of the application
   */
  String mainTitle();
}
