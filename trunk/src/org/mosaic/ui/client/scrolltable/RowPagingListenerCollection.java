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
package org.mosaic.ui.client.scrolltable;

import java.util.ArrayList;

/**
 * A helper class for implementers of the {@link SourceRowPagingEvents}
 * interface. This subclass of {@link ArrayList} assumes that all objects added
 * to it will be of type {@link RowPagingListener}.
 */
public class RowPagingListenerCollection extends ArrayList<RowPagingListener> {
  private static final long serialVersionUID = -2749626828317431636L;
  
  /**
   * Fired when the number of pages changes.
   * 
   * @param numPages the new number of pages
   */
  public void fireNumPagesChanged(int numPages) {
    for (RowPagingListener listener : this) {
      listener.onNumPagesChanges(numPages);
    }
  }

  /**
   * Fired when the current page changes, but before it is loaded.
   * 
   * @param page the new page
   */
  public void firePageChanged(int page) {
    for (RowPagingListener listener : this) {
      listener.onPageChanged(page);
    }
  }

  /**
   * Fired when the current page has completely finished loading.
   * 
   * @param page the new page
   */
  public void firePageLoaded(int page) {
    for (RowPagingListener listener : this) {
      listener.onPageLoaded(page);
    }
  }

  /**
   * Fired when the current page has completely finished loading.
   * 
   * @param caught the exception that caused the failure
   */
  public void firePagingFailuire(Throwable caught) {
    for (RowPagingListener listener : this) {
      listener.onPagingFailure(caught);
    }
  }
}
