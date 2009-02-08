package org.gwt.mosaic.actions.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

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
