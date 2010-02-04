/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos
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
/*
 * This is derived work from GWT Incubator project:
 * http://code.google.com/p/google-web-toolkit-incubator/
 * 
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
package org.gwt.mosaic.ui.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Logical event fired when the number of pages changes.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class PageCountChangeEvent extends GwtEvent<PageCountChangeHandler> {

  /**
   * Handler type.
   */
  public static Type<PageCountChangeHandler> TYPE;

  /**
   * Fires a highlight event on all registered handlers in the handler manager.
   * 
   * @param <S> the event source
   * @param source
   * @param oldPageCount
   * @param newPageCount
   */
  public static <S extends HasPageCountChangeHandlers & HasHandlers> void fire(
      S source, int oldPageCount, int newPageCount) {
    if (TYPE != null) {
      PageCountChangeEvent event = new PageCountChangeEvent(oldPageCount,
          newPageCount);
      source.fireEvent(event);
    }
  }
  
  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<PageCountChangeHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<PageCountChangeHandler>();
    }
    return TYPE;
  }

  /**
   * The new page count.
   */
  private int newPageCount;

  /**
   * The previous page count.
   */
  private int oldPageCount;

  /**
   * Construct a new {@link PageCountChangeEvent}.
   * 
   * @param oldPageCount the previous page
   * @param newPageCount the page that was requested
   */
  public PageCountChangeEvent(int oldPageCount, int newPageCount) {
    this.oldPageCount = oldPageCount;
    this.newPageCount = newPageCount;
  }

  /**
   * @return the new page count
   */
  public int getNewPageCount() {
    return newPageCount;
  }

  /**
   * @return the old page count
   */
  public int getOldPageCount() {
    return oldPageCount;
  }

  @Override
  protected void dispatch(PageCountChangeHandler handler) {
    handler.onPageCountChange(this);
  }

  @Override
  public final Type<PageCountChangeHandler> getAssociatedType() {
    return TYPE;
  }
}
