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

/**
 * Event listener interface for {@link SourceRowPagingEvents}.
 */
public interface RowPagingListener {
  /**
   * Fired when the number of pages changes.
   * 
   * @param numPages the new number of pages
   */
  void onNumPagesChanges(int numPages);

  /**
   * Fired when the current page changes, but before it is loaded.
   * 
   * @param page the new page
   */
  void onPageChanged(int page);

  /**
   * Fired when the current page has completely finished loading.
   * 
   * @param page the new page
   */
  void onPageLoaded(int page);

  /**
   * Fired when a failure occurs while loading the page.
   * 
   * @param caught the exception that caused the failure
   */
  void onPagingFailure(Throwable caught);
}
