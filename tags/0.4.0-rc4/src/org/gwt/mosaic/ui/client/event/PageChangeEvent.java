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
 * Logical event fired immediately when a new page is requested.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class PageChangeEvent extends GwtEvent<PageChangeHandler> {
  
  /**
   * Handler type.
   */
  private static Type<PageChangeHandler> TYPE;
  
  /**
   * Fires a page change event on all registered handlers in the handler manager.
   * 
   * @param <S> The event source
   * @param source the source of the handlers
   */
  public static <S extends PageChangeHandler & HasHandlers> void fire(
      S source, int oldValue, int newValue) {
    if (TYPE != null) {
      PageChangeEvent event = new PageChangeEvent(oldValue, newValue);
      source.fireEvent(event);
    }
  }
  
  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<PageChangeHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<PageChangeHandler>();
    }
    return TYPE;
  }
  
  /**
   * The new page.
   */
  private int newPage;

  /**
   * The previous page.
   */
  private int oldPage;

  /**
   * Construct a new {@link PageChangeEvent}.
   * 
   * @param oldPage the previous page
   * @param newPage the page that was requested
   */
  public PageChangeEvent(int oldPage, int newPage) {
    this.oldPage = oldPage;
    this.newPage = newPage;
  }

  /**
   * @return the new page that was requested
   */
  public int getNewPage() {
    return newPage;
  }

  /**
   * @return the old page
   */
  public int getOldPage() {
    return oldPage;
  }

  @Override
  protected void dispatch(PageChangeHandler handler) {
    handler.onPageChange(this);
  }

  @Override
  public Type<PageChangeHandler> getAssociatedType() {
    return TYPE;
  }

}
