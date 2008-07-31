/*
 * Copyright 2008 Georgios J. Georgopoulos.
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
package org.mosaic.core.client.model;

import java.util.EventObject;

public class ModelChangeEvent extends EventObject {
  private static final long serialVersionUID = 1417961656197328010L;

  protected final int fromIndex;

  protected final int toIndex;

  public ModelChangeEvent(Object source) {
    this(source, -1, -1);
  }

  public ModelChangeEvent(Object source, int from, int to) {
    super(source);
    fromIndex = from;
    toIndex = to;
  }

  public int getFromIndex() {
    return fromIndex;
  }

  public int getToIndex() {
    return toIndex;
  }

}
