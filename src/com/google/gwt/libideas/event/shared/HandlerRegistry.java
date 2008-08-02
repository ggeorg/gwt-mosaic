/*
 * Copyright 2007 Google Inc.
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

package com.google.gwt.libideas.event.shared;

import com.google.gwt.core.client.JavaScriptObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Package protected utility interface to allow flexible implementations of our
 * Handler Manager's handler registry.
 */
abstract interface HandlerRegistry {
  void addHandler(AbstractEvent.Key eventKey, EventHandler handler);
  EventHandler getHandler(AbstractEvent.Key eventKey, int index);
  int getHandlerCount(AbstractEvent.Key eventKey);
  void removeHandler(AbstractEvent.Key eventKey, EventHandler handler);

  static class JavaHandlerRegistry extends HashMap<AbstractEvent.Key, ArrayList<EventHandler>>
      implements HandlerRegistry {

    public void addHandler(AbstractEvent.Key eventKey, EventHandler handler) {
      ArrayList<EventHandler> l = super.get(eventKey);
      if (l == null) {
        l = new ArrayList<EventHandler>();
        super.put(eventKey, l);
      }
      l.add(handler);
    }

    public EventHandler getHandler(AbstractEvent.Key eventKey, int index) {
      assert (index < getHandlerCount(eventKey));
      ArrayList<EventHandler> l = super.get(eventKey);
      return l.get(index);
    }

    public int getHandlerCount(AbstractEvent.Key eventKey) {
      ArrayList<EventHandler> l = super.get(eventKey);
      if (l == null) {
        return 0;
      } else {
        return l.size();
      }
    }

    public void removeHandler(AbstractEvent.Key eventKey, EventHandler handler) {
      ArrayList<EventHandler> l = super.get(eventKey);
      if (l != null) {
        l.remove(handler);
      }
    }
  }

  // object.
  static class JSHandlerRegistry implements HandlerRegistry {
    // In this code I pass transcript in everywhere to avoid refactoring problems
    // later. In "real" code transcript should be referred to via JSNI calls.
    private JavaScriptObject transcript = JavaScriptObject.createObject();

    public void addHandler(AbstractEvent.Key eventKey, EventHandler handler) {
      int base = eventKey.hashCode();
      int count = getCount(transcript, base);
      if (count < AbstractEvent.Key.EXPECTED_MAX_HANDLERS_PER_WIDGET) {
        ++count;
        int slot = base + count;
        set(transcript, slot, handler);
        setCount(transcript, base, count);
      } else {
        throw new IllegalStateException("Else case not implemented yet");
      }
    }

    public EventHandler getHandler(AbstractEvent.Key eventKey, int index) {
      assert (index < getHandlerCount(eventKey));
      int findAt = eventKey.hashCode() + index + 1;
      return get(transcript, findAt);
    }

    public int getHandlerCount(AbstractEvent.Key eventKey) {
      int index = eventKey.hashCode();
      return getCount(transcript, index);
    }

    public void removeHandler(AbstractEvent.Key eventKey, EventHandler handler) {
      remove(transcript, eventKey.hashCode(), handler);
    }

    private native EventHandler get(JavaScriptObject transcript, int index) /*-{
      return transcript[index];
    }-*/;

    private native int getCount(JavaScriptObject transcript, int index) /*-{
      var count = transcript[index];
      if(count == null){
        return 0;
      } else{
              return count;
      }
    }-*/;

    private native void remove(JavaScriptObject transcript, int base,
        EventHandler handler)/*-{
      var count = transcript[base];
      var index = index + 1;
      var last = index + count - 1;

       for(;index <= last;index++){
              if(transcript[index].equals(handler)){
                      break;
              }
      }

      for(; index < last; index++){
              transcript[index] = transcript[index+1]
      }
      transcript[last] = null;
      --transcript[base];
    }-*/;

    private native void set(JavaScriptObject transcript, int slot,
        EventHandler handler) /*-{
      transcript[slot] = handler;
    }-*/;

    private native void setCount(JavaScriptObject transcript, int index, int count) /*-{
      transcript[index] = count;
    }-*/;
  }
}
