/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopolos.
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
package org.gwt.mosaic.actions.client;

import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class ToggleButtonBindings extends ButtonBaseBindings {

  public final class ToggleButtonBean extends ButtonBaseBean {
    public ToggleButtonBean(ToggleButton target) {
      super(target);
    }

    public Boolean getSelected() {
      return ((ToggleButton) target).isDown();
    }

    public void setSelected(Boolean selected) {
      selected = toBoolean(selected, Boolean.FALSE);
      Boolean oldValue = ((ToggleButton) target).isDown();
      ((ToggleButton) target).setDown(selected);
      changeSupport.firePropertyChange("selected", oldValue, selected);
    }

    public void firePropertyChange(final String property, Object oldValue,
        Object newValue) {
      changeSupport.firePropertyChange(property, oldValue, newValue);
    }
  }

  private ToggleButtonBean targetBean;

  public ToggleButtonBindings(Action source) {
    this(source, new ToggleButton());

    // Action.NAME
    addBinding(Action.NAME, BeanProperty.<Action, String> create(Action.NAME),
        BeanProperty.<ToggleButtonBean, String> create("text"));
  }

  public ToggleButtonBindings(Action source, ToggleButton target) {
    super(source, target);

    // "selected"
    addBinding("selected", UpdateStrategy.READ_WRITE,
        BeanProperty.<Action, String> create("selected"),
        BeanProperty.<ToggleButtonBean, String> create("selected"));
  }

  @Override
  protected ToggleButtonBean getTargetBean() {
    if (targetBean == null) {
      targetBean = new ToggleButtonBean((ToggleButton) getTarget());
    }
    return targetBean;
  }

  @Override
  public void onClick(ClickEvent event) {
    Boolean newValue = ((ToggleButton) event.getSource()).isDown();
    getTargetBean().firePropertyChange("selected", !newValue, newValue);
    super.onClick(event);
  }

}
