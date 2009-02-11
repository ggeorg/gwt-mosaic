/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopolos.
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
package org.gwt.mosaic.actions.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 *
 */
public class ActionMap {

  private ActionMap parent;

  private HashMap<Object, Action> table;

  public ActionMap() {
    // Nothing to do here!
  }

  public Object[] allKeys() {
    Object[] keys = keys();
    if (parent == null) {
      return keys;
    }
    Object[] parentKeys = parent.allKeys();
    if (keys.length == 0) {
      return parentKeys;
    }
    if (parentKeys.length == 0) {
      return keys;
    }
    HashSet<Object> keySet = new HashSet<Object>(Arrays.asList(keys));
    keySet.addAll(Arrays.asList(parentKeys));
    return keySet.toArray(new Object[keySet.size()]);
  }

  public void clear() {
    if (table != null) {
      table.clear();
    }
  }

  public Action get(Object key) {
    Action action = null;
    if (table != null) {
      action = table.get(key);
    }
    if (action == null && getParent() != null) {
      action = getParent().get(key);
    }
    return action;
  }

  public ActionMap getParent() {
    return parent;
  }

  public Object[] keys() {
    if (table == null) {
      return new Object[0];
    }
    return table.keySet().toArray(new Object[table.size()]);
  }

  public void put(Object key, Action action) {
    if (action != null) {
      if (table == null) {
        table = new HashMap<Object, Action>();
      }
      table.put(key, action);
    } else {
      remove(key);
    }
  }

  public void remove(Object key) {
    if (table != null) {
      table.remove(key);
    }
  }

  public void setParent(final ActionMap parent) {
    this.parent = parent;
  }

  public int size() {
    return (table != null) ? table.size() : 0;
  }
}
