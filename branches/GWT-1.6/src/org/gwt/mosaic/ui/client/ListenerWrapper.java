/*
 * Copyright 2008 Cameron Braid.
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

import org.gwt.mosaic.ui.client.event.CollapseEvent;
import org.gwt.mosaic.ui.client.event.CollapseHandler;
import org.gwt.mosaic.ui.client.event.HasCollapseHandlers;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.BaseListenerWrapper;


/**
 * Legacy listener support hierarchy for <code>org.gwt.mosaic.ui.client</code>.
 * Gathers the bulk of the legacy glue code in one place, for easy deletion when
 * Listener methods are deleted.
 * 
 * @see com.google.gwt.user.ListenerWrapper
 * @param <T>
 *            listener type
 */
@Deprecated
public abstract class ListenerWrapper<T> extends BaseListenerWrapper<T> {

  /**
   * Wrapper for a {@link CollapsedListener}.
   * 
   * @deprecated will be removed in GWT 2.0 along with the listeners being
   *             wrapped
   */
  @Deprecated
  public static class WrappedCollapsedListener extends
      ListenerWrapper<CollapsedListener> implements CollapseHandler {

    /**
     * Adds the wrapped listener.
     * 
     * @param source the event source
     * @param listener the listener
     * @return the wrapped listener
     * 
     * @deprecated will be removed in GWT 2.0 along with the listener classes
     */
    @Deprecated
    public static WrappedCollapsedListener add(HasCollapseHandlers source,
    		CollapsedListener listener) {
      WrappedCollapsedListener rtn = new WrappedCollapsedListener(listener);
      source.addCollapseHandler(rtn);
      return rtn;
    }

    /**
     * Removes the wrapped listener.
     * 
     * @param eventSource the event source from which to remove the wrapped
     *          listener
     * @param listener the listener to remove
     * @deprecated will be removed in GWT 2.0 along with the listener classes
     */
    @Deprecated
    public static void remove(HandlerManager handlerManager, CollapsedListener listener) {
      baseRemove(handlerManager, listener, ChangeEvent.getType());
    }

    WrappedCollapsedListener(CollapsedListener listener) {
      super(listener);
    }

    public void onCollapseChanged(CollapseEvent event) {
      getListener().onCollapsedChange(getSource(event));
    }
  }

  protected ListenerWrapper(T listener) {
    super(listener);
  }

}
