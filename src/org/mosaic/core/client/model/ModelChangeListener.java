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

import java.util.EventListener;

public interface ModelChangeListener extends EventListener {

  /** Tells listeners that an element was added to the model. */
  public void elementAdded(ModelChangeEvent e);
  
  /** Tells listeners that an element was removed from the model. */
  public void elementRemoved(ModelChangeEvent e);
  
  /** Tells listeners that an element was repositioned. */
  public void elementMoved(ModelChangeEvent e);

}
