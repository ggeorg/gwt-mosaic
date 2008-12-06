package org.gwt.mosaic.beansbinding.client.adapters;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;
import org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase;
import org.gwt.mosaic.ui.client.ListBox;

public final class ListBoxAdapterProvider implements BeanAdapterProvider {
  
  public final class Adapter extends BeanAdapterBase {
    private ListBox listBox;
    //private Handler handler;
    private Object cachedElementOrElements;
    
    private Adapter(ListBox listBox, String property) {
      super(property);
      this.listBox = listBox;
    }
    
    public Object getSelectedElement() {
      return null;
    }
    
  }

  public BeanAdapter createAdapter(Object source, String property) {
    // TODO Auto-generated method stub
    return null;
  }

  public Class<?> getAdapterClass(Class<?> type) {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean providesAdapter(Class<?> type, String property) {
    // TODO Auto-generated method stub
    return false;
  }

}
