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
import org.gwt.mosaic.actions.client.ToggleButtonBindings.ToggleButtonBean;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class CheckBoxBindings extends ButtonBaseBindings {

  public final class CheckBoxBean extends ButtonBaseBean {
    public CheckBoxBean(CheckBox target) {
      super(target);
    }

    public Boolean getSelected() {
      return ((CheckBox) target).getValue();
    }

    public void setSelected(Boolean selected) {
      selected = toBoolean(selected, Boolean.FALSE);
      Boolean oldValue = ((CheckBox) target).getValue();
      ((CheckBox) target).setValue(selected);
      changeSupport.firePropertyChange("selected", oldValue, selected);
    }

    public void firePropertyChange(final String property, Object oldValue,
        Object newValue) {
      changeSupport.firePropertyChange(property, oldValue, newValue);
    }
  }

  private CheckBoxBean targetBean;

  public CheckBoxBindings(Action source) {
    this(source, new CheckBox());
  }

  public CheckBoxBindings(Action source, CheckBox target) {
    super(source, target);

    // Action.NAME
    addBinding(Action.NAME, BeanProperty.<Action, String> create(Action.NAME),
        BeanProperty.<ButtonBaseBean, String> create("text"));

    // "selected"
    addBinding("selected", UpdateStrategy.READ_WRITE,
        BeanProperty.<Action, String> create("selected"),
        BeanProperty.<ToggleButtonBean, String> create("selected"));
  }

  @Override
  protected CheckBoxBean getTargetBean() {
    if (targetBean == null) {
      targetBean = new CheckBoxBean((CheckBox) getTarget());
    }
    return targetBean;
  }

  @Override
  public void onClick(ClickEvent event) {
    Boolean newValue = ((CheckBox) event.getSource()).getValue();
    getTargetBean().firePropertyChange("selected", !newValue, newValue);
    super.onClick(event);
  }

}
