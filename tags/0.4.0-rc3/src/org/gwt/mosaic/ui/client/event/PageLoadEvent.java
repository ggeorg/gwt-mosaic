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
 * Logical event fired when a page has finished loaded.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class PageLoadEvent extends GwtEvent<PageLoadHandler>{
  
  /**
   * Handler type.
   */
  private static Type<PageLoadHandler> TYPE;
  
  /**
   * Fires a page load event on all registered handlers in the handler manager.
   * 
   * @param <S> The event source
   * @param source the source of the handlers
   */
  public static <S extends PageLoadHandler & HasHandlers> void fire(
      S source, int page) {
    if (TYPE != null) {
      PageLoadEvent event = new PageLoadEvent(page);
      source.fireEvent(event);
    }
  }
  
  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<PageLoadHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<PageLoadHandler>();
    }
    return TYPE;
  }

  /**
   * The page that was loaded.
   */
  private int page;

  /**
   * Construct a new {@link PageLoadEvent}.
   * 
   * @param page the page that was loaded
   */
  public PageLoadEvent(int page) {
    this.page = page;
  }

  /**
   * @return the page that has finished loading
   */
  public int getPage() {
    return page;
  }

  @Override
  protected void dispatch(PageLoadHandler handler) {
    handler.onPageLoad(this);
  }

  @Override
  public Type<PageLoadHandler> getAssociatedType() {
    return TYPE;
  }
}
