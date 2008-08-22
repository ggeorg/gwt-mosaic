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

import java.util.Vector;

public class ModelChangeListenerCollection extends Vector<ModelChangeListener> {
  private static final long serialVersionUID = 1716253883223087984L;

  public void fireElementAdded(ModelChangeEvent event) {
    for (ModelChangeListener listener : this) {
      listener.elementAdded(event);
    }
  }

  public void fireElementRemoved(ModelChangeEvent event) {
    for (ModelChangeListener listener : this) {
      listener.elementRemoved(event);
    }
  }

  public void fireElementMoved(ModelChangeEvent event) {
    for (ModelChangeListener listener : this) {
      listener.elementMoved(event);
    }
  }

}
