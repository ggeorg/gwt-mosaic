package gwt.mosaic.core.client;

public abstract class Options {
  
  /**
   * Class representing the dimensions of an object in pixels.
   */
  public static final class Dimensions {
    public Integer width;
    public Integer height;

    public Dimensions() {
    }

    public Dimensions(int width, int height) {
      this.width = width;
      this.height = height;
    }

    public Dimensions(Dimensions dimensions) {
      if (dimensions == null) {
        throw new IllegalArgumentException("dimensions is null");
      }
      this.width = dimensions.width;
      this.height = dimensions.height;
    }

    @Override
    public boolean equals(Object object) {
      boolean equals = false;

      if (object instanceof Dimensions) {
        Dimensions dimensions = (Dimensions) object;
        equals = (width == dimensions.width && height == dimensions.height);
      }

      return equals;
    }

    @Override
    public int hashCode() {
      return 31 * width + height;
    }

    @Override
    public String toString() {
      return getClass().getName() + " [" + width + "x" + height + "]";
    }

  }

  /**
   * Class representing the bounds of an object in pixels.
   */
  public static final class Bounds {

    public Integer left;
    public Integer top;
    public Integer width;
    public Integer height;

    public Bounds() {
    }

    public Bounds(Integer left, Integer top, Integer width, Integer height) {
      this.left = left;
      this.top = top;
      this.width = width;
      this.height = height;
    }

    public Bounds(Bounds bounds) {
      if (bounds == null) {
        throw new IllegalArgumentException("bounds is null.");
      }
      this.left = bounds.left;
      this.top = bounds.top;
      this.width = bounds.width;
      this.height = bounds.height;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + top;
      result = prime * result + left;
      result = prime * result + width;
      result = prime * result + height;
      return result;
    }

    @Override
    public String toString() {
      return getClass().getName() + " [" + top + ", " + left + ", " + width + ", " + height + "]";
    }
  }

}
