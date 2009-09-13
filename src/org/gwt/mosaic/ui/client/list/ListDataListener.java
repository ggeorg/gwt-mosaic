/*
 * Copyright 2008 Google Inc.
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
package org.gwt.mosaic.ui.client.list;

import java.util.EventListener;

/**
 * A {@code ListDataListener} can register with a {@link ListModel} and receive
 * notofication of updates to the model.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public interface ListDataListener extends EventListener {

  /**
   * Notifies the listener that the contents of the list have changed in some
   * way. This method will be called if the change cannot be notified via the
   * {@link #intervalAdded(ListDataEvent)} or the
   * {@link #intervalRemoved(ListDataEvent)} methods.
   * 
   * @param event the event
   */
  void contentsChanged(ListDataEvent event);

  /**
   * Notifies the listener that one or more items have been added to the list.
   * The {@code event} argument can supply the indices for the range of items
   * added.
   * 
   * @param event the event
   */
  void intervalAdded(ListDataEvent event);

  /**
   * Notifies the listener that one or more items have been removed from the
   * list. The {@code event} argument can supply the indices for range of items
   * removed.
   * 
   * @param event the event
   */
  void intervalRemoved(ListDataEvent event);

}
