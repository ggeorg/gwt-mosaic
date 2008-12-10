package org.gwt.mosaic.beansbinding.client.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;
import org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase;
import org.gwt.mosaic.ui.client.ListBox;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Widget;

public final class ListBoxAdapterProvider implements BeanAdapterProvider {

  private static final String SELECTED_ELEMENT_P = "selectedElement";
  private static final String SELECTED_ELEMENTS_P = "selectedElements";

  public final class Adapter extends BeanAdapterBase {
    private ListBox<?> listBox;
    private Handler handler;
    private Object cachedElementOrElements;

    private Adapter(ListBox<?> listBox, String property) {
      super(property);
      this.listBox = listBox;
    }

    private boolean isPlural() {
      return property == SELECTED_ELEMENTS_P;
    }

    public Object getSelectedElement() {
      return ListBoxAdapterProvider.getSelectedElement(listBox);
    }

    public List<?> getSelectedElements() {
      return ListBoxAdapterProvider.getSelectedElements(listBox);
    }

    @Override
    protected void listeningStarted() {
      handler = new Handler();
      cachedElementOrElements = isPlural() ? getSelectedElements()
          : getSelectedElement();
      listBox.addChangeListener(handler);
    }

    @Override
    protected void listeningStopped() {
      listBox.removeChangeListener(handler);
      cachedElementOrElements = null;
      handler = null;
    }

    private class Handler implements ChangeListener {
      public void onChange(Widget sender) {
        Object oldElementOrElements = cachedElementOrElements;
        cachedElementOrElements = isPlural() ? getSelectedElements()
            : getSelectedElement();
        firePropertyChange(oldElementOrElements, cachedElementOrElements);
      }
    }

  }

  private static Object getSelectedElement(ListBox<?> listBox) {
    assert listBox != null;

    int index = listBox.getSelectedIndex();

    if (index == -1) {
      return null;
    }

    return listBox.getItem(index);
  }

  private static List<Object> getSelectedElements(ListBox<?> listBox) {
    assert listBox != null;

    List<Object> elements = new ArrayList<Object>();

    Set<Integer> selection = listBox.getSelectedIndices();

    if (selection == null || selection.size() > 0) {
      return elements;
    }

    for (int i = 0, n = selection.size(); i < n; ++i) {
      elements.add(listBox.getItem(i));
    }

    return elements;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#createAdapter(java.lang.Object,
   *      java.lang.String)
   */
  public BeanAdapter createAdapter(Object source, String property) {
    if (!providesAdapter(source.getClass(), property)) {
      throw new IllegalArgumentException();
    }
    return new Adapter((ListBox<?>) source, property);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#getAdapterClass(java.lang.Class)
   */
  public Class<?> getAdapterClass(Class<?> type) {
    return (type == ListBox.class) ? ListBoxAdapterProvider.Adapter.class
        : null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.beansbinding.core.client.ext.BeanAdapterProvider#providesAdapter(java.lang.Class,
   *      java.lang.String)
   */
  public boolean providesAdapter(Class<?> type, String property) {
    if (type != ListBox.class) {
      return false;
    }

    property = property.intern();

    return property == SELECTED_ELEMENT_P || property == SELECTED_ELEMENTS_P;
  }

}
