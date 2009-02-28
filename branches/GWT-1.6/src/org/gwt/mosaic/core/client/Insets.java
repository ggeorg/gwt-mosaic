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
package org.gwt.mosaic.core.client;

import java.io.Serializable;

import org.gwt.mosaic.core.client.util.HashCode;

/**
 * 
 * @author Dmitry A. Durnev
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class Insets implements Cloneable, Serializable {
  private static final long serialVersionUID = -2746796190690789639L;

  public int top;

  public int left;

  public int bottom;

  public int right;

  public Insets(int top, int left, int bottom, int right) {
    setValues(top, left, bottom, right);
  }

  @Override
  public int hashCode() {
    int hashCode = HashCode.EMPTY_HASH_CODE;
    hashCode = HashCode.combine(hashCode, top);
    hashCode = HashCode.combine(hashCode, left);
    hashCode = HashCode.combine(hashCode, bottom);
    hashCode = HashCode.combine(hashCode, right);
    return hashCode;
  }

  public Object clone() {
    return new Insets(top, left, bottom, right);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Insets) {
      Insets i = (Insets) o;
      return ((i.left == left) && (i.bottom == bottom) && (i.right == right) && (i.top == top));
    }
    return false;
  }

  @Override
  public String toString() {
    return (getClass().getName() + "[left=" + left + ",top=" + top +
        ",right=" + right + ",bottom=" + bottom + "]");
  }

  public void set(int top, int left, int bottom, int right) {
    setValues(top, left, bottom, right);
  }

  private void setValues(int top, int left, int bottom, int right) {
    this.top = top;
    this.left = left;
    this.bottom = bottom;
    this.right = right;
  }
}
