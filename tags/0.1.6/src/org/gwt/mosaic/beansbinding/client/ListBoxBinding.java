/*
 * Copyright 2006-2008 Google Inc.
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
package org.gwt.mosaic.beansbinding.client;

import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.AutoBinding;
import org.gwt.beansbinding.core.client.ObjectProperty;
import org.gwt.beansbinding.core.client.Property;
import org.gwt.beansbinding.core.client.PropertyStateEvent;
import org.gwt.beansbinding.core.client.PropertyStateListener;
import org.gwt.beansbinding.ui.client.impl.AbstractColumnBinding;
import org.gwt.beansbinding.ui.client.impl.ListBindingManager;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.gwt.mosaic.ui.client.list.ListDataEvent;
import org.gwt.mosaic.ui.client.list.ListDataListener;
import org.gwt.mosaic.ui.client.list.ListModel;

/**
 * Binds a {@code List} of objects to act as the elements of a {@link ListBox}.
 * 
 * TODO more
 * 
 * @param <E> the type of elements in the source {@code List}
 * @param <SS> the type of source object (on which the source property resolves
 *          to {@code List})
 * @param <TS> the type of target object (on which the target property resolves
 *          to {@code JList})
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public final class ListBoxBinding<E, SS, TS> extends
    AutoBinding<SS, List<E>, TS, List> {

  private Property<TS, ? extends ListBox<E>> listBoxP;
  private ElementsProperty<TS> elementsP;
  private Handler handler = new Handler();
  private ListBox<E> listBox;
  private BindingListModel model;
  private List<ColumnBinding> columnBindings = new ArrayList<ColumnBinding>();

  /**
   * Constructs an instance of {@code ListBoxBinding}.
   * 
   * @param strategy the update strategy
   * @param sourceObject the source object
   * @param sourceListProperty a property on the source object that resolves to
   *          the {@code List} of elements
   * @param targetObject the target object
   * @param targetListBoxProperty a property on the target object that resolves
   *          to a {@link ListBox}
   * @param name a name for the {@code ListBoxBinding}
   * @throws IllegalArgumentException if the source property or target property
   *           is {@code null}
   */
  protected ListBoxBinding(UpdateStrategy strategy, SS sourceObject,
      Property<SS, List<E>> sourceListProperty, TS targetObject,
      Property<TS, ? extends ListBox<E>> targetListBoxProperty, String name) {
    super(strategy == UpdateStrategy.READ_WRITE ? UpdateStrategy.READ
        : strategy, sourceObject, sourceListProperty, targetObject,
        new ElementsProperty<TS>(), name);

    if (targetListBoxProperty == null) {
      throw new IllegalArgumentException(
          "target ListBox property can't be null");
    }

    listBoxP = targetListBoxProperty;
    elementsP = (ElementsProperty<TS>) getTargetProperty();
    addColumnBinding(null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.beansbinding.core.client.AutoBinding#bindImpl()
   */
  @Override
  protected void bindImpl() {
    elementsP.setAccessible(isListBoxAccessible());
    listBoxP.addPropertyStateListener(getTargetObject(), handler);
    elementsP.addPropertyStateListener(null, handler);
    super.bindImpl();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.beansbinding.core.client.AutoBinding#unbindImpl()
   */
  @Override
  protected void unbindImpl() {
    elementsP.removePropertyStateListener(null, handler);
    listBoxP.removePropertyStateListener(getTargetObject(), handler);
    elementsP.setAccessible(false);
    cleanupForLast();
    super.unbindImpl();
  }

  private boolean isListBoxAccessible() {
    return listBoxP.isReadable(getTargetObject())
        && listBoxP.getValue(getTargetObject()) != null;
  }

  private boolean isListBoxAccessible(Object value) {
    return value != null && value != PropertyStateEvent.UNREADABLE;
  }

  private void cleanupForLast() {
    if (listBox == null) {
      return;
    }

    listBox.setModel(new DefaultListModel<E>());
    listBox = null;
    model.setElements(null, true);
    model = null;
  }

  /**
   * Creates a {@code ColumnBinding} and adds it to the list of
   * {@code ColumnBindings} maintained by this {@code ListBoxBinding}.
   * 
   * @param columnProperty the property with which to derive each list value
   *          from its corresponding object in the source {@code List}
   * @return the {@code ColumnBinding}
   */
  public ColumnBinding addColumnBinding(Property<E, ?> columnProperty) {
    return addDetailBinding(columnProperty, null);
  }

  /**
   * Creates a {@code ColumnBinding} and adds it to the list of
   * {@code ColumnBindings} maintained by this {@code ListBoxBinding}.
   * 
   * @param columnProperty the property with which to derive each list value
   *          from its corresponding object in the source {@code List}
   * @param name
   * @return the {@code ColumnBinding}
   */
  public ColumnBinding addDetailBinding(Property<E, ?> detailProperty,
      String name) {
    throwIfBound();

    if (name == null && ListBoxBinding.this.getName() != null) {
      name = ListBoxBinding.this.getName() + ".COLUMN_BINDING";
    }

    ColumnBinding columnBinding = detailProperty == null ? new ColumnBinding(
        ObjectProperty.<E> create(), name) : new ColumnBinding(detailProperty,
        name);

    columnBindings.add(columnBinding);

    return columnBinding;
  }

  /**
   * Removes the given {@code ColumnBinding} from the list maintained by this
   * {@code ListBoxBinding}.
   * 
   * @param binding the {@code ColumnBinding} to remove
   * @return {@code true} if this list contained the specified element
   * @see #addColumnBinding(Property)
   * @see #addDetailBinding(Property, String)
   */
  public boolean removeColumnBinding(ColumnBinding binding) {
    throwIfBound();
    return columnBindings.remove(binding);
  }

  /**
   * Returns an array of the {@code ColumnBindings} maintained by this
   * {@code ListBoxBinding}.
   * 
   * @return the list of {@code ColumnBindings}
   * @see #addColumnBinding(Property)
   * @see #addDetailBinding(Property, String)
   */
  public ColumnBinding[] getColumnBindings() {
    return (ColumnBinding[]) columnBindings.toArray(new AbstractColumnBinding[columnBindings.size()]);
  }

  private final Property DETAIL_PROPERTY = new Property() {
    public Class<Object> getWriteType(Object source) {
      return Object.class;
    }

    public Object getValue(Object source) {
      throw new UnsupportedOperationException();
    }

    public void setValue(Object source, Object value) {
      throw new UnsupportedOperationException();
    }

    public boolean isReadable(Object source) {
      throw new UnsupportedOperationException();
    }

    public boolean isWriteable(Object source) {
      return true;
    }

    public void addPropertyStateListener(Object source,
        PropertyStateListener listener) {
      throw new UnsupportedOperationException();
    }

    public void removePropertyStateListener(Object source,
        PropertyStateListener listener) {
      throw new UnsupportedOperationException();
    }

    public PropertyStateListener[] getPropertyStateListeners(Object source) {
      throw new UnsupportedOperationException();
    }
  };

  /**
   * {@code ColumnBinding} represents a binding between a property of the
   * elements in the {@code ListBoxBinding's} source {@code List}, and the
   * values shown in the {@code ListBox}.
   * <p>
   * A {@code Converter} may be specified on a {@code ColumnBinding}.
   * Specifying a {@code Validator} is also possible, but doesn't make sense
   * since {@code ListBox} values aren't editable.
   * <p>
   * {@code ColumnBindings} are managed by their {@code ListBoxBinding}. They
   * are not to be explicitly bound, unbound, added to a {@code BindingGroup},
   * or accessed in a way that is not allowed for a managed binding.
   * 
   * @see ListBoxBinding#addColumnBinding(Property)
   */
  public final class ColumnBinding extends AbstractColumnBinding {

    private ColumnBinding(Property<E, ?> detailProperty, String name) {
      super(0, detailProperty, DETAIL_PROPERTY, name);
    }

  }

  private class Handler implements PropertyStateListener {
    public void propertyStateChanged(PropertyStateEvent pse) {
      if (!pse.getValueChanged()) {
        return;
      }

      if (pse.getSourceProperty() == listBoxP) {
        cleanupForLast();

        boolean wasAccessible = isListBoxAccessible(pse.getOldValue());
        boolean isAccessible = isListBoxAccessible(pse.getNewValue());

        if (wasAccessible != isAccessible) {
          elementsP.setAccessible(isAccessible);
        } else if (elementsP.isAccessible()) {
          elementsP.setValueAndIgnore(null, null);
        }
      } else {
        if (((ElementsProperty.ElementsPropertyStateEvent) pse).shouldIgnore()) {
          return;
        }

        if (listBox == null) {
          listBox = listBoxP.getValue(getTargetObject());
          model = new BindingListModel();
          listBox.setModel(model);
        }

        model.setElements((List) pse.getNewValue(), true);
      }
    }
  }

  private final class BindingListModel extends ListBindingManager implements
      ListModel {
    private final List<ListDataListener> listeners;

    public BindingListModel() {
      // (ggeorg)listeners = new CopyOnWriteArrayList<TableModelListener>();
      listeners = new ArrayList<ListDataListener>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.mosaic.beansbinding.client.impl.ListBindingManager#getColBindings()
     */
    @Override
    protected AbstractColumnBinding[] getColBindings() {
      AbstractColumnBinding[] bindings = new AbstractColumnBinding[columnBindings.size()];
      bindings = columnBindings.toArray(bindings);
      return bindings;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.mosaic.beansbinding.client.impl.ListBindingManager#allChanged()
     */
    @Override
    protected void allChanged() {
      contentsChanged(0, size());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.mosaic.beansbinding.client.impl.ListBindingManager#valueChanged(int,
     *      int)
     */
    @Override
    protected void valueChanged(int row, int column) {
      contentsChanged(row, row);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.mosaic.beansbinding.client.impl.ListBindingManager#added(int,
     *      int)
     */
    @Override
    protected void added(int index, int length) {
      assert length > 0; // enforced by ListBindingManager

      ListDataEvent e = new ListDataEvent(this,
          ListDataEvent.Type.INTERVAL_ADDED, index, index + length - 1);

      for (ListDataListener listener : listeners) {
        listener.intervalAdded(e);
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.mosaic.beansbinding.client.impl.ListBindingManager#removed(int,
     *      int)
     */
    @Override
    protected void removed(int index, int length) {
      assert length > 0; // enforced by ListBindingManager

      ListDataEvent e = new ListDataEvent(this,
          ListDataEvent.Type.INTERVAL_REMOVED, index, index + length - 1);
      for (ListDataListener listener : listeners) {
        listener.intervalRemoved(e);
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.mosaic.beansbinding.client.impl.ListBindingManager#changed(int)
     */
    @Override
    protected void changed(int row) {
      contentsChanged(row, row);
    }

    private void contentsChanged(int row0, int row1) {
      ListDataEvent e = new ListDataEvent(this,
          ListDataEvent.Type.CONTENTS_CHANGED, row0, row1);
      for (ListDataListener listener : listeners) {
        listener.contentsChanged(e);
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.mosaic.ui.client.list.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index) {
      System.out.println(valueAt(index, 0));
      System.out.println(getElement(index));
      return getElement(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.mosaic.ui.client.list.ListModel#addListDataListener(org.gwt.mosaic.ui.client.list.ListDataListener)
     */
    public void addListDataListener(ListDataListener l) {
      listeners.add(l);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.mosaic.ui.client.list.ListModel#removeListDataListener(org.gwt.mosaic.ui.client.list.ListDataListener)
     */
    public void removeListDataListener(ListDataListener l) {
      listeners.remove(l);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gwt.mosaic.ui.client.list.ListModel#getSize()
     */
    public int getSize() {
      return size();
    }
  }

}
