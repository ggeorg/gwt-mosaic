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

import java.util.List;

import org.gwt.beansbinding.core.client.AutoBinding;
import org.gwt.beansbinding.core.client.ObjectProperty;
import org.gwt.beansbinding.core.client.Property;
import org.gwt.mosaic.ui.client.ListBox;

/**
 * A factory class for creating instances of the custom GWT-Mosaic
 * {@code Binding} implementations provided by this package.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class MosaicBindings {

  private MosaicBindings() {
    // Nothing to do here!
  }

  /**
   * Creates a {@code ListBoxBinding} from direct references to a {@code List}
   * and {@code ListBox}.
   * 
   * @param <E>
   * @param strategy the update strategy
   * @param sourceList the source {@code List}
   * @param targetListBox the target {@code ListBox}
   * @return the {@code ListBoxBinding}
   */
  public static <E> ListBoxBinding<E, List<E>, ListBox<E>> createListBoxBinding(
      AutoBinding.UpdateStrategy strategy, List<E> sourceList,
      ListBox<E> targetListBox) {
    return new ListBoxBinding<E, List<E>, ListBox<E>>(strategy, sourceList,
        ObjectProperty.<List<E>> create(), targetListBox,
        ObjectProperty.<ListBox<E>> create(), null);
  }

  /**
   * Creates a named {@code ListBoxBinding} from direct references to a
   * {@code List} and {@code ListBox}.
   * 
   * @param <E>
   * @param strategy the update strategy
   * @param sourceList the source {@code List}
   * @param targetListBox the target {@code ListBox}
   * @param name
   * @return the {@code ListBoxBinding}
   */
  public static <E> ListBoxBinding<E, List<E>, ListBox<E>> createListBoxBinding(
      AutoBinding.UpdateStrategy strategy, List<E> sourceList,
      ListBox<E> targetListBox, String name) {
    return new ListBoxBinding<E, List<E>, ListBox<E>>(strategy, sourceList,
        ObjectProperty.<List<E>> create(), targetListBox,
        ObjectProperty.<ListBox<E>> create(), name);
  }

  /**
   * Creates {@code ListBoxBinding} from an object and property that
   * resolves to {@code List} and a direct reference to a {@code ListBox}.
   * 
   * @param <E>
   * @param <SS>
   * @param strategy the update strategy
   * @param sourceObject the source object
   * @param sourceListProperty a property on the source object that resolves to
   *          a {@code List}
   * @param targetListBox the target {@code ListBox}
   * @param name
   * @return the {@code ListBoxBinding}
   * @throws IllegalArgumentException if {@code sourceListProperty} is
   *           {@code null}
   */
  public static <E, SS> ListBoxBinding<E, SS, ListBox<E>> createListBoxBinding(
      AutoBinding.UpdateStrategy strategy, SS sourceObject,
      Property<SS, List<E>> sourceListProperty, ListBox<E> targetListBox) {
    return new ListBoxBinding<E, SS, ListBox<E>>(strategy, sourceObject,
        sourceListProperty, targetListBox,
        ObjectProperty.<ListBox<E>> create(), null);
  }

  /**
   * Creates a named {@code ListBoxBinding} from an object and property that
   * resolves to {@code List} and a direct reference to a {@code ListBox}.
   * 
   * @param <E>
   * @param <SS>
   * @param strategy the update strategy
   * @param sourceObject the source object
   * @param sourceListProperty a property on the source object that resolves to
   *          a {@code List}
   * @param targetListBox the target {@code ListBox}
   * @param name
   * @return the {@code ListBoxBinding}
   * @throws IllegalArgumentException if {@code sourceListProperty} is
   *           {@code null}
   */
  public static <E, SS> ListBoxBinding<E, SS, ListBox<E>> createListBoxBinding(
      AutoBinding.UpdateStrategy strategy, SS sourceObject,
      Property<SS, List<E>> sourceListProperty, ListBox<E> targetListBox,
      String name) {
    return new ListBoxBinding<E, SS, ListBox<E>>(strategy, sourceObject,
        sourceListProperty, targetListBox,
        ObjectProperty.<ListBox<E>> create(), name);
  }
}
