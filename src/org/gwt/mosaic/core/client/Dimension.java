/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos
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
package org.gwt.mosaic.core.client;

import java.io.Serializable;

/**
 * Utility class that holds a width and height that represents a dimension of an
 * element.
 * 
 * @author http://harmony.apache.org/
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class Dimension implements Serializable {
  private static final long serialVersionUID = -5761695848507043940L;

  public int width;
  public int height;

  /**
   * Creates a new instance of {@code Dimension}.
   */
  public Dimension() {
    this(0, 0);
  }

  /**
   * Creates a new instance of {@code Dimension} with a predefine dimension.
   * 
   * @param d {@code Dimension} to copy
   */
  public Dimension(Dimension d) {
    this(d.width, d.height);
  }

  /**
   * Creates a new instance of {@code Dimension} with width and height.
   * 
   * @param width the dimension width
   * @param height the dimension height
   */
  public Dimension(int width, int height) {
    setSize(width, height);
  }

  /**
   * Creates a new instance of {@code Dimension} with {@code width=size[0]} and
   * {@code height=size[1]}.
   * 
   * @param size the dimension array ({@code size[0]} is the width and {@code
   *          size[1]} is the height)
   */
  public Dimension(int[] size) {
    setSize(size[0], size[1]);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof Dimension) {
      return (((Dimension) obj).width == width && ((Dimension) obj).height == height);
    }
    return false;
  }

  /**
   * Return the height of the dimension.
   * 
   * @return height of the dimension
   */
  public int getHeight() {
    return height;
  }

  public Dimension getSize() {
    return new Dimension(width, height);
  }

  /**
   * Return the width of the dimension.
   * 
   * @return width of the dimension
   */
  public int getWidth() {
    return width;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 71 * hash + this.width;
    hash = 71 * hash + this.height;
    return hash;
  }

  /**
   * Set the height of the dimension.
   * 
   * @param height the dimension height
   */
  public void setHeight(int height) {
    this.height = height;
  }

  public void setSize(Dimension d) {
    setSize(d.width, d.height);
  }

  public void setSize(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /**
   * Set the width of the dimension
   * 
   * @param width the dimension width
   */
  public void setWidth(int width) {
    this.width = width;
  }

  public int[] toArray() {
    return new int[] {width, height};
  }

  @Override
  public String toString() {
    return getClass().getName() + "[width=" + width + ",height=" + height + "]";
  }
}
