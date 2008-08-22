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
package org.mosaic.ui.client.event;

import org.mosaic.core.client.model.ModelChangeEvent;

public class TableColumnModelEvent extends ModelChangeEvent {
  private static final long serialVersionUID = -3254470094177500005L;

  public TableColumnModelEvent(Object source, int from, int to) {
    super(source, from, to);
  }

  public TableColumnModelEvent(ModelChangeEvent event) {
    super(event.getSource(), event.getFromIndex(), event.getToIndex());
  }

}
