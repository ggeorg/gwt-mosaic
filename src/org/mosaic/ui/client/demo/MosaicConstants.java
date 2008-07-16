package org.mosaic.ui.client.demo;

public interface MosaicConstants {

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
   * The available style themes that the user can select.
   */
  String[] STYLE_THEMES = {"standard" /*, "chrome", "dark"*/};

}
