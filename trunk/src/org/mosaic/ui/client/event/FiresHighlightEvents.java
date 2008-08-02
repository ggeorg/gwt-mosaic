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
 * Fires Highlight change events.
 * 
 * @param <ValueType> change type
 */
public interface FiresHighlightEvents<ValueType> {
  /**
   * Adds a highlight handler.
   * 
   * @param handler the handler
   */
  void addHighlightHandler(HighlightHandler<ValueType> handler);

  /**
   * Removes a highlight handler.
   * 
   * @param handler the handler
   */
  void removeHighlightHandler(HighlightHandler<ValueType> handler);
}
