/*
 * Copyright 2006-2008 Google Inc.
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
package org.gwt.mosaic.ui.client.table;

import java.util.EventObject;

/**
 * {@code TableColumnModelEvent} instances are used to notify
 * {@link TableColumnListener TableColumnListeners} that a
 * {@link TableColumnModel} has changed.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class TableColumnModelEvent extends EventObject {
  private static final long serialVersionUID = -5629295241594529834L;

  /** The <em>from<em> index. */
  protected int fromIndex;

  /** The <em>to</em> index. */
  protected int toIndex;

  /**
   * Constructor.
   * 
   * @param source the {@link TableColumnModel} that originated the event
   * @param from specifies the <em>from</em> index
   * @param to specifies the <em>to</em> index
   */
  public TableColumnModelEvent(final TableColumnModel source, final int from,
      final int to) {
    super(source);
    fromIndex = from;
    toIndex = to;
  }

  /**
   * Returns the <em>from</em> index.
   * 
   * @return the <em>from</em> index.
   */
  public int getFromIndex() {
    return fromIndex;
  }

  /**
   * Returns the <em>to</em> index.
   * 
   * @return the <em>to</em> index.
   */
  public int getToIndex() {
    return toIndex;
  }
}
