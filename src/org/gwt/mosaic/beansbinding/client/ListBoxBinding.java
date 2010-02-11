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
import org.gwt.beansbinding.core.client.Property;
import org.gwt.beansbinding.core.client.PropertyStateEvent;
import org.gwt.beansbinding.core.client.PropertyStateListener;
import org.gwt.beansbinding.core.client.util.Parameters;
import org.gwt.beansbinding.ui.client.impl.AbstractColumnBinding;
import org.gwt.beansbinding.ui.client.impl.ListBindingManager;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.ListBox.CellRenderer;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.gwt.mosaic.ui.client.list.ListColumn;
import org.gwt.mosaic.ui.client.list.ListDataEvent;
import org.gwt.mosaic.ui.client.list.ListDataListener;
import org.gwt.mosaic.ui.client.list.ListHeader;
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
@SuppressWarnings("unchecked")
public final class ListBoxBinding<E, SS, TS> extends
    AutoBinding<SS, List<E>, TS, List> {

  private Property<TS, ? extends ListBox<E>> listBoxP;
  private ElementsProperty<TS> elementsP;
  private Handler handler = new Handler();
  private ListBox<E> listBox;
  private BindingListModel model;
  private boolean editable = true;
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

    Parameters.checkNotNull(targetListBoxProperty, "targetListBoxProperty");

    listBoxP = targetListBoxProperty;
    elementsP = (ElementsProperty<TS>) getTargetProperty();
  }

  @Override
  protected void bindImpl() {
    elementsP.setAccessible(isListBoxAccessible());
    listBoxP.addPropertyStateListener(getTargetObject(), handler);
    elementsP.addPropertyStateListener(null, handler);
    super.bindImpl();
  }

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
        && (listBoxP.getValue(getTargetObject()) != null);
  }

  private boolean isListBoxAccessible(Object value) {
    return (value != null) && (value != PropertyStateEvent.UNREADABLE);
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

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  public boolean isEditable() {
    return editable;
  }

  /**
   * Creates a {@code ColumnBinding} and adds it to the list of {@code
   * ColumnBindings} maintained by this {@code ListBoxBinding}.
   * 
   * @param columnProperty the property with which to derive each list value
   *          from its corresponding object in the source {@code List}
   * @return the {@code ColumnBinding}
   */
  public ColumnBinding addColumnBinding(Property<E, ?> columnProperty) {
    return addColumnBinding(columnProperty, null);
  }

  /**
   * Creates a named {@code ColumnBinding} and adds it to the end of the list of
   * {@code ColumnBindings} maintained by this {@code ListBoxBinding}.
   * <p>
   * The list of {@code ColumnBindings} dictates the columns to be displayed in
   * the {@code ListBoxs}, with a {@code ColumnBinding's} order in the list
   * determining its index.
   * 
   * @param columnProperty the property with which to derive cell values from
   *          the elements of the source {@code List}
   * @param name a name for the column binding
   * @return the {@code ColumnBinding}
   * @throws IllegalArgumentException if {@code columnProperty} is {@code null}
   */
  public ColumnBinding addColumnBinding(Property<E, ?> columnProperty,
      String name) {
    throwIfBound();

    Parameters.checkNotNull(columnProperty, "columnProperty");

    if ((name == null) && (ListBoxBinding.this.getName() != null)) {
      name = ListBoxBinding.this.getName() + ".COLUMN_BINDING";
    }

    ColumnBinding binding = new ColumnBinding(columnBindings.size(),
        columnProperty, name);
    columnBindings.add(binding);

    return binding;
  }

  /**
   * Creates a {@code ColumnBinding} and inserts it at the given index into the
   * list of {@code ColumnBindings} maintained by this {@code ListBoxBinding}.
   * <p>
   * The list of {@code ColumnBindings} dictates the columns to be displayed in
   * the {@link ListBox}, with a {@code ColumnBinding's} order in the list
   * determining its index.
   * 
   * @param index the index at which to insert the {@code ColumnBinding}
   * @param columnProperty the property with which to derive cell values from
   *          the elements of the source {@code List}
   * @return the {@code ColumnBinding}
   * @throws IllegalArgumentException if {@code columnProperty} is {@code null}
   */
  public ColumnBinding addColumnBinding(int index, Property<E, ?> columnProperty) {
    return addColumnBinding(index, columnProperty, null);
  }

  /**
   * Creates a {@code ColumnBinding} and inserts it at the given index into the
   * list of {@code ColumnBindings} maintained by this {@code ListBoxBinding}.
   * <p>
   * The list of {@code ColumnBindings} dictates the columns to be displayed in
   * the {@code ListBox}, with a {@code ColumnBinding's} order in the list
   * determining its table model index.
   * 
   * @param index the index at which to insert the {@code ColumnBinding}
   * @param columnProperty the property with which to derive cell values from
   *          the elements of the source {@code List}
   * @param name a name for the {@code ColumnBinding}
   * @return the {@code ColumnBinding}
   * @throws IllegalArgumentException if {@code columnProperty} is {@code null}
   */
  public ColumnBinding addColumnBinding(int index,
      Property<E, ?> columnProperty, String name) {
    throwIfBound();

    Parameters.checkNotNull(columnProperty, "columnProperty");

    if ((name == null) && (ListBoxBinding.this.getName() != null)) {
      name = ListBoxBinding.this.getName() + ".COLUMN_BINDING";
    }

    ColumnBinding binding = new ColumnBinding(index, columnProperty, name);
    columnBindings.add(index, binding);
    adjustIndices(index + 1, true);

    return binding;
  }

  /**
   * Removes the given {@code ColumnBinding} from the list maintained by this
   * {@code ListBoxBinding}.
   * <p>
   * The list of {@code ColumnBindings} dictates the columns to be displayed in
   * the {@code ListBoxs}, with a {@code ColumnBinding's} order in the list
   * determining its index.
   * 
   * @param binding the {@code ColumnBinding} to remove
   */
  public boolean removeColumnBinding(ColumnBinding binding) {
    throwIfBound();

    boolean retVal = columnBindings.remove(binding);

    if (retVal) {
      adjustIndices(binding.getColumn(), false);
    }

    return retVal;
  }

  /**
   * Removes the {@code ColumnBinding} with the given index from the list
   * maintained by this {@code ListBoxBinding}.
   * <p>
   * The list of {@code ColumnBindings} dictates the columns to be displayed in
   * the {@code ListBoxs}, with a {@code ColumnBinding's} order in the list
   * determining its index.
   * 
   * @param index the index of the {@code ColumnBinding} to remove
   */
  public ColumnBinding removeColumnBinding(int index) {
    throwIfBound();

    ColumnBinding retVal = columnBindings.remove(index);

    if (retVal != null) {
      adjustIndices(index, false);
    }

    return retVal;
  }

  /**
   * Returns the {@code ColumnBinding} with the given index in the list
   * maintained by this {@code ListBoxBinding}.
   * <p>
   * The list of {@code ColumnBindings} dictates the columns to be displayed in
   * the {@code ListBoxs}, with a {@code ColumnBinding's} order in the list
   * determining its index.
   * 
   * @param index the index of the {@code ColumnBinding} to return
   * @return the {@code ColumnBinding} at the given index
   */
  public ColumnBinding getColumnBinding(int index) {
    return columnBindings.get(index);
  }

  /**
   * Returns an unmodifiable copy of the list of {@code ColumnBindings}
   * maintained by this {@code ListBoxBinding}.
   * <p>
   * The list of {@code ColumnBindings} dictates the columns to be displayed in
   * the {@code ListBox}, with a {@code ColumnBinding's} order in the list
   * determining its index.
   * 
   * @return the list of {@code ColumnBindings}
   */
  public List<ColumnBinding> getColumnBindings() {
    // XXX return Collections.unmodifiableList(columnBindings);
    return columnBindings;
  }

  private void adjustIndices(int start, boolean up) {
    int size = columnBindings.size();

    for (int i = start; i < size; i++) {
      ColumnBinding cb = columnBindings.get(i);
      cb.adjustColumn(cb.getColumn() + (up ? 1 : (-1)));
    }
  }

  // -----------------------------------------------------------------------
  private final class ColumnProperty extends Property {

    private ColumnBinding binding;

    public Class<? extends Object> getWriteType(Object source) {
      return (binding.columnClass == null) ? Object.class : binding.columnClass;
    }

    public Object getValue(Object source) {
      if (binding.isBound()) {
        return binding.editingObject;
      }

      throw new UnsupportedOperationException();
    }

    public void setValue(Object source, Object value) {
      throw new UnsupportedOperationException();
    }

    public boolean isReadable(Object source) {
      return binding.isBound();
    }

    public boolean isWriteable(Object source) {
      return true;
    }

    public void addPropertyStateListener(Object source,
        PropertyStateListener listener) {
    }

    public void removePropertyStateListener(Object source,
        PropertyStateListener listener) {
    }

    public PropertyStateListener[] getPropertyStateListeners(Object source) {
      return new PropertyStateListener[0];
    }
  }

  // -----------------------------------------------------------------------
  public final class ColumnBinding extends AbstractColumnBinding {

    private Class<?> columnClass;
    private boolean editable = true;
    private String columnName;
    private Object editingObject;

    private ColumnBinding(int column, Property<E, ?> columnProperty, String name) {
      super(column, columnProperty, new ColumnProperty(), name);
      ((ColumnProperty) getTargetProperty()).binding = this;
    }

    private void adjustColumn(int newCol) {
      setColumn(newCol);
    }

    /**
     * Sets a name for the column represented by this {@code ColumnBinding}.
     * This is used to initialize the table's column header name. If {@code
     * null} is specified, the {@code toString()} value of the {@code
     * ColumnBinding's} source property is used.
     * 
     * @param name the name
     * @return the {@code ColumnBinding} itself, to allow for method chaining
     */
    public ColumnBinding setColumnName(String name) {
      ListBoxBinding.this.throwIfBound();
      this.columnName = name;

      return this;
    }

    /**
     * Sets the column class to be used by {@code ListBox} to determine the
     * renderer and editor for the column represented by this {@code
     * ColumnBinding}.
     * 
     * @param columnClass the column class
     * @return the {@code ColumnBinding} itself, to allow for method chaining
     */
    public ColumnBinding setColumnClass(Class<?> columnClass) {
      ListBoxBinding.this.throwIfBound();
      this.columnClass = columnClass;

      return this;
    }

    /**
     * Returns the column class to be used by {@code ListBox} to determine the
     * renderer and editor for the column represented by this {@code
     * ColumnBinding}.
     * 
     * @see #setColumnClass
     */
    public Class<?> getColumnClass() {
      return (columnClass == null) ? Object.class : columnClass;
    }

    /**
     * Returns the name for the column represented by this {@code ColumnBinding}
     * . This is used to initialize the table's column header name. If no name
     * has been specified, or if it has been set to {@code null}, the {@code
     * toString()} value of the {@code ColumnBinding's} source property is
     * returned.
     * 
     * @return the name for the column
     * @see #setColumnName
     */
    public String getColumnName() {
      return (columnName == null) ? getSourceProperty().toString() : columnName;
    }

    public ColumnBinding setEditable(boolean editable) {
      this.editable = editable;

      return this;
    }

    public boolean isEditable() {
      return editable;
    }
  }

  // -----------------------------------------------------------------------
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

          ListHeader header = new ListHeader();
          for (ColumnBinding columnBinding : ListBoxBinding.this.getColumnBindings()) {
            header.add(new ListColumn(columnBinding.getColumnName()));
          }
          listBox.setHeader(header);

          listBox.setCellRenderer(new CellRenderer<E>() {
            public void renderCell(ListBox<E> listBox, int row, int column,
                E item) {
              listBox.setText(row, column,
                  model.valueAt(row, column).toString());
            }
          });
        }

        model.setElements((List) pse.getNewValue(), true);
      }
    }
  }

  // -----------------------------------------------------------------------
  private final class BindingListModel extends ListBindingManager implements
      ListModel {

    private final List<ListDataListener> listeners;

    public BindingListModel() {
      // (ggeorg)listeners = new CopyOnWriteArrayList<TableModelListener>();
      listeners = new ArrayList<ListDataListener>();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#getColBindings()
     */
    @Override
    protected AbstractColumnBinding[] getColBindings() {
      AbstractColumnBinding[] bindings = new AbstractColumnBinding[columnBindings.size()];
      bindings = columnBindings.toArray(bindings);
      return bindings;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#allChanged()
     */
    @Override
    protected void allChanged() {
      contentsChanged(0, size());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#valueChanged(int,
     *      int)
     */
    @Override
    protected void valueChanged(int row, int column) {
      contentsChanged(row, row);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#added(int,
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

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#removed(int,
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

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.beansbinding.ui.client.impl.ListBindingManager#changed(int)
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

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.mosaic.ui.client.list.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index) {
      return getElement(index);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.mosaic.ui.client.list.ListModel#addListDataListener(org.gwt.mosaic.ui.client.list.ListDataListener)
     */
    public void addListDataListener(ListDataListener l) {
      listeners.add(l);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.mosaic.ui.client.list.ListModel#removeListDataListener(org.gwt.mosaic.ui.client.list.ListDataListener)
     */
    public void removeListDataListener(ListDataListener l) {
      listeners.remove(l);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.gwt.mosaic.ui.client.list.ListModel#getSize()
     */
    public int getSize() {
      return size();
    }
  }

}
