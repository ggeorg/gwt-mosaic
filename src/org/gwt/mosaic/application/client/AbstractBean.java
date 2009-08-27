/*
 * Copyright (C) 2009 Georgios J. Georgopoulos, All rights reserved.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.gwt.mosaic.application.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.gwt.beansbinding.core.client.util.HasPropertyChangeSupport;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public abstract class AbstractBean implements HasPropertyChangeSupport {

  private transient PropertyChangeSupport support;

  /**
   * Add a {@code PropertyChangeListener} to the listener list. The listener is
   * registered for all properties and TODO its {@code propertyChange} method
   * will run on the event dispatching thread.
   * <p>
   * If {@code listener} is {@code null}, no exception is thrown and no action
   * is taken.
   * 
   * @param listener the {@code PropertyChangeListener} to be added
   * @see #removePropertyChangeListener(PropertyChangeListener)
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }

    if (support == null) {
      support = new PropertyChangeSupport(this);
    }

    support.addPropertyChangeListener(listener);
  }

  /**
   * Add a {@code PropertyChangeListener} for a specific property. The listener
   * will be invoked only when a call on {@code
   * AbstractBean#firePropertyChange(Object, Object)} names that specific
   * property.
   * <p>
   * The same listener object may be added more than once. For each property,
   * the listener will be invoked the number of times it was added for that
   * property.
   * <p>
   * If {@code property} or {@code listener} is {@code null}, no exception is
   * thrown and no action is taken.
   * 
   * @param property the name of the property to listen on
   * @param listener the {@code PropertyChangeListener} to be added
   */
  public void addPropertyChangeListener(String property,
      PropertyChangeListener listener) {
    if (listener == null || property == null) {
      return;
    }

    if (support == null) {
      support = new PropertyChangeSupport(this);
    }

    support.addPropertyChangeListener(property, listener);
  }

  /**
   * Called whenever the value of a bound property is set.
   * <p>
   * If {@code oldValue} is not equal to {@code newValue}, invoke the {@code
   * propertyChange} method on all of the {@code PropertyChangeListeners} added
   * so far.
   * 
   * @param property
   * @param oldValue
   * @param newValue
   */
  protected void firePropertyChange(String property, Object oldValue,
      Object newValue) {
    if (support == null || property == null
        || (oldValue != null && newValue != null && oldValue.equals(newValue))) {
      return;
    }

    support.firePropertyChange(property, oldValue, newValue);
  }

  /**
   * Fire an existing {@code PropertyChangeEvent}.
   * <p>
   * If the event's {@code oldValue} property is not equal to {@code newValue},
   * invoke the {@code propertyChange} method on all of the {@code
   * PropertyChangeListeners} added so far.
   * 
   * @param event
   */
  protected void firePropertyChange(PropertyChangeEvent event) {
    if (support == null || event == null) {
      return;
    }

    support.firePropertyChange(event);
  }

  /**
   * An array of all the {@code PropertyChangeListeners} added so far.
   * 
   * @return all of the {@code PropertyChangeListeners} added so far.
   */
  public PropertyChangeListener[] getPropertyChangeListeners() {
    if (support == null) {
      return new PropertyChangeListener[0];
    }

    return support.getPropertyChangeListeners();
  }

  /**
   * Returns an array of all the listeners which have been associated with the
   * named property.
   * 
   * @param property the name of the property being listened to
   * @return all of the {@code PropertyChangeListeners} associated with the
   *         named property. If no such listeners have been added, or if {@code
   *         propertyName} is {@code null}, an empty array is returned.
   */
  public PropertyChangeListener[] getPropertyChangeListeners(String property) {
    if (support == null || property == null) {
      return new PropertyChangeListener[0];
    }

    return support.getPropertyChangeListeners(property);
  }

  /**
   * Remove a {@code PropertyChangeListener} from the listener list.
   * <p>
   * If the {@code listener} is {@code null}, no exception is thrown and no
   * action is taken.
   * 
   * @param listener the {@code PropertyChangeListener} to be removed.
   * @see #addPropertyChangeListener(PropertyChangeListener)
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    if (listener == null || support == null) {
      return;
    }

    support.removePropertyChangeListener(listener);
  }

  /**
   * Remove a {@code PropertyChangeListener} for a specific property. If {@code
   * listener} was added more than once to the same event source for the
   * specified property, it will be notified one less time after being removed.
   * <p>
   * If {@code property} is {@code null}, no exception is thrown and no action
   * is taken.
   * <p>
   * If {@code listener} is {@code null}, or was never added for the specified
   * property, no exception is thrown and no action is taken.
   * 
   * @param property the name of the property that was listened on
   * @param listener the {@code PropertyChangeListener} to be removed
   */
  public synchronized void removePropertyChangeListener(String property,
      PropertyChangeListener listener) {
    if (listener == null || support == null || property == null) {
      return;
    }

    support.removePropertyChangeListener(property, listener);
  }
}
