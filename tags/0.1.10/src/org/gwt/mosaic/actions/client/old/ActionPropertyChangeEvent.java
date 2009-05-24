/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopolos.
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
package org.gwt.mosaic.actions.client.old;

import java.util.EventObject;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * @deprecated
 */
public class ActionPropertyChangeEvent extends EventObject {
  private static final long serialVersionUID = 8263415801874185516L;

  private final int property;

  private final Object oldValue, newValue;

  public ActionPropertyChangeEvent(Action source, int property, Object oldValue,
      Object newValue) {
    super(source);
    this.property = property;
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  @Override
  public Action getSource() {
    return (Action) super.getSource();
  }
  
  public int getProperty() {
    return property;
  }
  
  public Object getOldValue() {
    return oldValue;
  }
  
  public Object getNewValue() {
    return newValue;
  }

}
