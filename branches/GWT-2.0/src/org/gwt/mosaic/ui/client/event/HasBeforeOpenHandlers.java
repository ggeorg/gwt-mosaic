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

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * A widget that implements this interface is a public source of
 * {@link BeforeOpenEvent} events.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <T> the type being opened
 */
public interface HasBeforeOpenHandlers<T> extends HasHandlers {

  /**
   * Adds a {@link BeforeOpenEvent} handler.
   * 
   * @param handler the handler
   * @return the registration for the event
   */
  HandlerRegistration addBeforeOpenHandler(BeforeOpenHandler<T> handler);
}
