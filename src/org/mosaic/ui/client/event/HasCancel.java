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
package org.mosaic.ui.client.event;

/**
 * Does this type support the cancellation contract?
 */
public interface HasCancel {

  /**
   * Cancels the current event.
   */
  public void cancelEvent();

  /**
   * Is the current event canceled?
   * 
   * @return is the event canceled?
   */
  public boolean isCanceled();

}
