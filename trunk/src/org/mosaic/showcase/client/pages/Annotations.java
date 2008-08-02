package org.mosaic.showcase.client.pages;

/**
 * The annotations used in {@link Showcase}.
 */
public class Annotations {

  /**
   * Indicates that a class variable should be included as source data in the
   * example. All data must have a JavaDoc style comment.
   */
  public @interface MosaicData {
  }

  /**
   * Indicates that a method or inner class should be included as source code in
   * the example. All source must have a JavaDoc style comment.
   */
  public @interface MosaicSource {
  }

  /**
   * Indicates the raw files that be included as raw source in a Showcase
   * example.
   */
  public static @interface MosaicRaw {
    String[] value();
  }

  /**
   * Indicates the prefix of a style attribute used in a Showcase example.
   */
  public static @interface MosaicStyle {
    String[] value();
  }
}
