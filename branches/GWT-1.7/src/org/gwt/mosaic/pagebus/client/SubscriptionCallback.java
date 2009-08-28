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
 * Callback function for PageBus to invoke when a message is published on a
 * subscribed subject.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public interface SubscriptionCallback {
  /**
   * A subscriber message handler.
   * 
   * NOTE: {@code PageBus#subscribe}, {@code PageBus#unsubscribe} and {@code
   * PageBus#publish} can be safely called from within the callback function.
   * 
   * NOTE: Callbacks <em>should not</em> throw exceptions. If callback does
   * throw an exception, PageBus will handle it gracefully but as a rule,
   * callbacks should catch and handle their own exceptions. Exceptions should
   * <em>never</em> be used as a mechanism for communicating between subscriber
   * and publisher.
   * 
   * @param subject reference to the string subject on which the message was
   *          published
   * @param message reference to the message that was published (do _NOT_ modify
   *          the data, as all other subscribers receive the same reference)
   * @param subscriberData reference to the subscriberData parameter that was
   *          specified by the subscriber in {@code PageBus#subscribe}
   */
  void execute(String subject, Object message, Object subscriberData);
}
