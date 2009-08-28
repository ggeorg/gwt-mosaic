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

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The payload message published on {@code
 * com.tibco.pagebus.error.callbackError}. Handlers for such messages might do
 * things like display alert messages, send the browser immediately to an error
 * page, or replace a faulty component with an error box.
 * 
 * Note: When subscribing to {@code com.tibco.pagebus.error.callbackError}, the
 * callback function passed into subscribe must <em>never</em> throw exceptions.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class SubscriptionCallbackError extends JavaScriptObject {

  // Overlay types always have protected, zero-arg ctors
  protected SubscriptionCallbackError() {
    // Nothing to do here!
  }

  /**
   * The PageBus subject associated with the callback invocation that threw the
   * error.
   * 
   * @return the subject associated with the callback invocation that threw the
   *         error
   */
  public final native String getName() 
  /*-{
    return this.name;
  }-*/;

  /**
   * The error message value.
   * 
   * @return the error the error message
   */
  public final native String getError()
  /*-{
    return error;
  }-*/;

}
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

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The payload message published on {@code
 * com.tibco.pagebus.error.callbackError}. Handlers for such messages might do
 * things like display alert messages, send the browser immediately to an error
 * page, or replace a faulty component with an error box.
 * 
 * Note: When subscribing to {@code com.tibco.pagebus.error.callbackError}, the
 * callback function passed into subscribe must <em>never</em> throw exceptions.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class SubscriptionCallbackError extends JavaScriptObject {

  // Overlay types always have protected, zero-arg ctors
  protected SubscriptionCallbackError() {
    // Nothing to do here!
  }

  /**
   * The PageBus subject associated with the callback invocation that threw the
   * error.
   * 
   * @return the subject associated with the callback invocation that threw the
   *         error
   */
  public final native String getName() 
  /*-{
    return this.name;
  }-*/;

  /**
   * The error message value.
   * 
   * @return the error the error message
   */
  public final native String getError()
  /*-{
    return error;
  }-*/;

}
