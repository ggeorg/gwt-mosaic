/*
 * Copyright 2008 Georgios J. Georgopoulos.
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
package org.mosaic.core.client.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class ModelElement extends Object implements Serializable {
  private static final long serialVersionUID = 1669338912163953854L;

  private PropertyChangeListenerCollection propertyChangeListeners;

  private final Map<String, Object> properties = new HashMap<String, Object>();

  private ModelChangeListenerCollection modelChangeListeners;

  private Vector<ModelElement> childElements;

  protected void addElement(ModelElement modelElement) {
    if (childElements == null) {
      childElements = new Vector<ModelElement>();
    }
    childElements.addElement(modelElement);
    if (this instanceof PropertyChangeListener) {
      modelElement.addPropertyChangeListener((PropertyChangeListener) this);
    }

    // Post childAdded event notification
    fireElementAdded(new ModelChangeEvent(this, 0, getElementCount() - 1));
  }

  public void addModelChangeListener(final ModelChangeListener listener) {
    if (modelChangeListeners == null) {
      modelChangeListeners = new ModelChangeListenerCollection();
    }
    if (modelChangeListeners.indexOf(listener) == -1) {
      modelChangeListeners.add(listener);
    }
  }

  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    if (propertyChangeListeners == null) {
      propertyChangeListeners = new PropertyChangeListenerCollection();
    }
    if (propertyChangeListeners.indexOf(listener) == -1) {
      propertyChangeListeners.add(listener);
    }
  }

  public void fireElementAdded(ModelChangeEvent event) {
    if (modelChangeListeners != null) {
      modelChangeListeners.fireElementAdded(event);
    }
  }

  public void fireElementMoved(ModelChangeEvent event) {
    if (modelChangeListeners != null) {
      modelChangeListeners.fireElementMoved(event);
    }
  }

  public void fireElementRemoved(ModelChangeEvent event) {
    if (modelChangeListeners != null) {
      modelChangeListeners.fireElementRemoved(event);
    }
  }

  protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    if (propertyChangeListeners != null) {
      propertyChangeListeners.firePropertyChangeEvent(this, propertyName, oldValue,
          newValue);
    }
  }

  protected int getElementCount() {
    return childElements.size();
  }

  public Iterator<ModelElement> getElements() {
    final Iterator<ModelElement> iter = childElements.iterator();

    return new Iterator<ModelElement>() {
      public boolean hasNext() {
        return iter.hasNext();
      }

      public ModelElement next() {
        return iter.next();
      }

      public void remove() {
        throw new RuntimeException("remove() is not supported");
      }
    };
  }

  protected Object getProperty(final String propertyName) {
    return properties.get(propertyName);
  }

  protected void moveElement(int index, int newIndex) {
    if ((index < 0) || (index >= getElementCount()) || (newIndex < 0)
        || (newIndex >= getElementCount())) {
      throw new IllegalArgumentException("moveElement() - Index out of range");
    }

    if (index == newIndex) {
      // TODO fireElementMoved(new ModelChangeEvent(this, index, newIndex));
      return;
    }
    
    final ModelElement element = childElements.elementAt(index);
    childElements.removeElementAt(index);
    childElements.insertElementAt(element, newIndex);

    fireElementMoved(new ModelChangeEvent(this, index, newIndex));
  }

  protected void removeElement(ModelElement modelElement) {
    if (childElements != null) {
      final int index = childElements.indexOf(modelElement);
      if (index != -1) {
        if (this instanceof PropertyChangeListener) {
          modelElement.removePropertyChangeListener((PropertyChangeListener) this);
        }
        childElements.remove(index);

        // Post columnAdded event notification.
        fireElementRemoved(new ModelChangeEvent(this, index, 0));
      }
    }
  }

  public void removeModelListener(final ModelChangeListener listener) {
    if (modelChangeListeners != null) {
      modelChangeListeners.remove(listener);
    }
  }

  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    if (propertyChangeListeners != null) {
      propertyChangeListeners.remove(listener);
    }
  }

  protected void setProperty(final String propertyName, final Object value) {
    Object oldValue = properties.get(propertyName);
    properties.put(propertyName, value);
    firePropertyChange(propertyName, oldValue, value);
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(getClass().getName() + " " + properties.toString());
    return sb.toString();
  }

  protected int getElementIndex(ModelElement modelElement) {
    return childElements.indexOf(modelElement);
  }

  public ModelElement getElement(int index) {
    return childElements.get(index);
  }

}
