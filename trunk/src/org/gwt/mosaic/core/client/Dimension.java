/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgopoulos J. Georgios.
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

import org.gwt.mosaic.core.client.util.HashCode;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class Dimension implements Serializable {
  private static final long serialVersionUID = -5761695848507043940L;

  public int width;
  public int height;

  public Dimension(Dimension d) {
    this(d.width, d.height);
  }

  public Dimension() {
    this(0, 0);
  }

  public Dimension(int width, int height) {
    setSize(width, height);
  }

  @Override
  public int hashCode() {
    HashCode hash = new HashCode();
    hash.append(width);
    hash.append(height);
    return hash.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof Dimension) {
      Dimension d = (Dimension) obj;
      return (d.width == width && d.height == height);
    }
    return false;
  }

  @Override
  public String toString() {
    return getClass().getName() + "[width=" + width + ",height=" + height + "]";
  }

  public void setSize(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public void setSize(Dimension d) {
    setSize(d.width, d.height);
  }

  public Dimension getSize() {
    return new Dimension(width, height);
  }
}
