/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.ui.client;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <T> listener type
 * @deprecated will be removed in GWT 2.0 with the handler listeners themselves
 */
@Deprecated
public abstract class ListenerWrapper<T> extends
    com.google.gwt.user.client.ui.ListenerWrapper<T> {

  protected ListenerWrapper(T listener) {
    super(listener);
  }

  public static class WrappedDoubleClickListener extends
      ListenerWrapper<DoubleClickListener> implements DoubleClickHandler {

    protected WrappedDoubleClickListener(DoubleClickListener listener) {
      super(listener);
    }

    @Deprecated
    public static WrappedDoubleClickListener add(HasDoubleClickHandlers source,
        DoubleClickListener listener) {
      WrappedDoubleClickListener rtn = new WrappedDoubleClickListener(listener);
      source.addDoubleClickHandler(rtn);
      return rtn;
    }

    @Deprecated
    public static void remove(Widget eventSource, DoubleClickListener listener) {
      baseRemove(eventSource, listener, DoubleClickEvent.getType());
    }

    public void onDoubleClick(DoubleClickEvent event) {
      getListener().onDoubleClick(getSource(event));
    }

  }
}
