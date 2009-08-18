/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.pagebus.client;

/**
 * Callback function for PageBus to invoke for each query result.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public interface QueryCallback {
  /**
   * A query callback handler to handle messages that match the query subject.
   * 
   * @param subject the subject name under which value is stored
   * @param value the value stored under the specified name
   * @param data the {@code data} parameter passed into {@code PageBus#query} 
   * @return if {@code true}, then continue to invoke callbacks and deliver
   *         results, if {@code false}, abort the query
   */
  boolean onResult(String subject, Object value, Object data);
}
