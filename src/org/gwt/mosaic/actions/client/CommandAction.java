/*
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

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gwt.beansbinding.observablecollections.client.ObservableMap;
import org.gwt.beansbinding.observablecollections.client.ObservableMapListener;

import com.google.gwt.user.client.Command;

/**
 * A base class for implementing the {@link Action} interface.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * @see Action
 */
public class CommandAction extends AbstractMap<String, Object> implements
    Action, HasCommand, ObservableMap<String, Object> {

  private class EntryIterator implements Iterator<Map.Entry<String, Object>> {
    private Iterator<Map.Entry<String, Object>> realIterator;
    private Map.Entry<String, Object> last;

    EntryIterator() {
      realIterator = properties.entrySet().iterator();
    }

    public boolean hasNext() {
      return realIterator.hasNext();
    }

    public Map.Entry<String, Object> next() {
      last = realIterator.next();
      return last;
    }

    public void remove() {
      if (last == null) {
        throw new IllegalStateException();
      }
      Object toRemove = last.getKey();
      last = null;
      CommandAction.this.remove(toRemove);
    }
  }

  private class EntrySet extends AbstractSet<Map.Entry<String, Object>> {
    public void clear() {
      CommandAction.this.clear();
    }

    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<String, Object> e = (Map.Entry<String, Object>) o;
      return containsKey(e.getKey());
    }

    public Iterator<Map.Entry<String, Object>> iterator() {
      return new EntryIterator();
    }

    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
      if (o instanceof Map.Entry) {
        String key = ((Map.Entry<String, Object>) o).getKey();
        if (containsKey(key)) {
          remove(key);
          return true;
        }
      }
      return false;
    }

    public int size() {
      return CommandAction.this.size();
    }
  }

  /**
   * {@code true} is this {@code Action} is enabled; {@code false} otherwise
   * (default is {@code true}.
   */
  protected boolean enabled;

  private final Map<String, Object> properties = new HashMap<String, Object>();

  private List<ObservableMapListener> listeners = new ArrayList<ObservableMapListener>();

  private Set<Map.Entry<String, Object>> entrySet;

  private Command command;

  public CommandAction(Command command) {
    this(null, command);
  }

  public CommandAction(final String name, Command command) {
    super();
    putValue(Action.NAME, name);
    setEnabled(true);
    setCommand(command);
  }

  /**
   * The actionPerformed implementation, simply calls command.execute().
   */
  public final void actionPerformed(ActionEvent e) {
    if (enabled) {
      getCommand().execute(); // kick off the command
    }
  }

  public void addObservableMapListener(ObservableMapListener listener) {
    listeners.add(listener);
  }

  public void clear() {
    // Remove all elements via iterator to trigger notification
    Iterator<String> iterator = keySet().iterator();
    while (iterator.hasNext()) {
      iterator.next();
      iterator.remove();
    }
  }

  public boolean containsKey(Object key) {
    return properties.containsKey(key);
  }

  public boolean containsValue(Object value) {
    return properties.containsValue(value);
  }

  public Set<Map.Entry<String, Object>> entrySet() {
    Set<Map.Entry<String, Object>> es = entrySet;
    return es != null ? es : (entrySet = new EntrySet());
  }

  public Object get(Object key) {
    return properties.get(key);
  }

  public final Command getCommand() {
    return command;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.demo.client.actions.Action#getValue(java.lang.String)
   */
  public Object getValue(final String name) {
    return get(name);
  }

  public boolean isEmpty() {
    return properties.isEmpty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.demo.client.actions.Action#isEnabled()
   */
  public boolean isEnabled() {
    return enabled;
  }

  public Object put(String key, Object value) {
    Object lastValue;
    if (containsKey(key)) {
      lastValue = properties.put(key, value);
      for (ObservableMapListener listener : listeners) {
        listener.mapKeyValueChanged(this, key, lastValue);
      }
    } else {
      lastValue = properties.put(key, value);
      for (ObservableMapListener listener : listeners) {
        listener.mapKeyAdded(this, key);
      }
    }
    return lastValue;
  }

  public void putAll(Map<? extends String, ? extends Object> m) {
    for (String key : m.keySet()) {
      put(key, m.get(key));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.demo.client.actions.Action#putValue(java.lang.String,
   * java.lang.Object)
   */
  public void putValue(final String name, final Object value) {
    put(name, value);
  }

  public Object remove(Object key) {
    if (containsKey(key)) {
      Object value = properties.remove(key);
      for (ObservableMapListener listener : listeners) {
        listener.mapKeyRemoved(this, key, value);
      }
      return value;
    }
    return null;
  }

  public void removeObservableMapListener(ObservableMapListener listener) {
    listeners.remove(listener);
  }

  protected final void setCommand(Command newValue) {
    this.command = newValue;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.demo.client.actions.Action#setEnabled(boolean)
   */
  public void setEnabled(final boolean enabled) {
    boolean oldValue = this.enabled;
    this.enabled = enabled;
    if (oldValue != enabled) {
      put("enabled", Boolean.valueOf(enabled));
    }
  }

  public int size() {
    return properties.size();
  }

}
