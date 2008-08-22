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

public class PropertyChangeListenerCollection extends Vector<PropertyChangeListener> {
  private static final long serialVersionUID = 5152119467561494763L;

  public void firePropertyChangeEvent(Object source, String propertyName,
      Object oldValue, Object newValue) {
    PropertyChangeEvent event = new PropertyChangeEvent(source, propertyName, oldValue,
        newValue);
    firePropertyChangeEvent(event);
  }

  public void firePropertyChangeEvent(PropertyChangeEvent event) {
    for (PropertyChangeListener listener : this) {
      listener.onPropertyChange(event);
    }
  }
}
