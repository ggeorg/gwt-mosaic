package org.gwt.mosaic.beansbinding.client.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;
import org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase;
import org.gwt.mosaic.ui.client.Table;

import com.google.gwt.gen2.event.shared.HandlerRegistration;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;

public class TableAdapterProvider implements BeanAdapterProvider {

  public final class Adapter extends BeanAdapterBase {
    private class Handler implements RowSelectionHandler {
      public void onRowSelection(RowSelectionEvent event) {
        Object oldElementOrElements = cachedElementOrElements;
        cachedElementOrElements = isPlural() ? getSelectedElements()
            : getSelectedElement();
        firePropertyChange(oldElementOrElements, cachedElementOrElements);
      }
    }
    private Table<?> table;
    private Handler handler;

    private Object cachedElementOrElements;

    private HandlerRegistration handlerRegistration;

    protected Adapter(Table<?> table, String property) {
      super(property);
      this.table = table;
    }

    public Object getSelectedElement() {
      return TableAdapterProvider.getSelectedElement(table);
    }

    public List<?> getSelectedElements() {
      return TableAdapterProvider.getSelectedElements(table);
    }

    private boolean isPlural() {
      return property == SELECTED_ELEMENTS_P;
    }

    @Override
    protected void listeningStarted() {
      handler = new Handler();
      cachedElementOrElements = isPlural() ? getSelectedElements()
          : getSelectedElement();
      handlerRegistration = table.addRowSelectionHandler(handler);
    }

    @Override
    protected void listeningStopped() {
      if (handlerRegistration != null) {
        handlerRegistration.removeHandler();
      }
      cachedElementOrElements = null;
      handler = null;
    }

  }
  
  private static final String SELECTED_ELEMENT_P = "selectedElement";
  private static final String SELECTED_ELEMENTS_P = "selectedElements";

  private static Object getSelectedElement(Table<?> table) {
    assert table != null;

    int index = table.getSelectedIndex();

    if (index == -1) {
      return null;
    }

    return table.getRowValue(index);
  }

  private static List<Object> getSelectedElements(Table<?> table) {
    assert table != null;

    List<Object> elements = new ArrayList<Object>();

    Set<Integer> selection = table.getSelectedIndices();

    if (selection == null || selection.size() == 0) {
      return elements;
    }

    for (Integer i : selection) {
      elements.add(table.getRowValue(i));
    }

    return elements;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#createAdapter(
   * java.lang.Object, java.lang.String)
   */
  public BeanAdapter createAdapter(Object source, String property) {
    if (!providesAdapter(source.getClass(), property)) {
      throw new IllegalArgumentException();
    }
    return new Adapter((Table<?>) source, property);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#getAdapterClass
   * (java.lang.Class)
   */
  public Class<?> getAdapterClass(Class<?> type) {
    return (type == Table.class) ? TableAdapterProvider.Adapter.class : null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#providesAdapter
   * (java.lang.Class, java.lang.String)
   */
  public boolean providesAdapter(Class<?> type, String property) {
    if (type != Table.class) {
      return false;
    }

    property = property.intern();

    return property == SELECTED_ELEMENT_P || property == SELECTED_ELEMENTS_P;
  }

}
