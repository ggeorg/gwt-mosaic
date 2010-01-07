package org.gwt.mosaic.beansbinding.client;

import org.gwt.beansbinding.core.client.Property;
import org.gwt.beansbinding.core.client.PropertyStateListener;
import org.gwt.beansbinding.ui.client.impl.AbstractColumnBinding;

/**
 * {@code ColumnBinding} represents a binding between a property of the elements
 * in the {@code ListBoxBinding's} source {@code List}, and the values shown in
 * the {@code ListBox}.
 * <p>
 * A {@code Converter} may be specified on a {@code ColumnBinding}. Specifying a
 * {@code Validator} is also possible, but doesn't make sense since {@code
 * ListBox} values aren't editable.
 * <p>
 * {@code ColumnBindings} are managed by their {@code ListBoxBinding}. They are
 * not to be explicitly bound, unbound, added to a {@code BindingGroup}, or
 * accessed in a way that is not allowed for a managed binding.
 * 
 * @see ListBoxBinding#addColumnBinding(Property)
 */
public final class ColumnBinding<E> extends AbstractColumnBinding {

  ColumnBinding(Property<E, ?> detailProperty, String name) {
    super(0, detailProperty, new Property() {
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
    }, name);
  }

}
