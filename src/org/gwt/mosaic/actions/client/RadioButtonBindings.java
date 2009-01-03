/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author georgopoulos.georgios(at)gmail.com
 */
public class RadioButtonBindings extends ButtonBaseActionSupport {

  public final class RadioButtonBean extends ButtonBaseBean {
    public RadioButtonBean(CheckBox target) {
      super(target);
    }

    public Boolean getSelected() {
      return ((RadioButton) target).isChecked();
    }

    public void setSelected(Boolean selected) {
      selected = toBoolean(selected, Boolean.FALSE);
      Boolean oldValue = ((RadioButton) target).isChecked();
      ((RadioButton) target).setChecked(selected);
      changeSupport.firePropertyChange("selected", oldValue, selected);
    }

    public void firePropertyChange(final String property, Object oldValue,
        Object newValue) {
      changeSupport.firePropertyChange(property, oldValue, newValue);
    }
  }

  private RadioButtonBean targetBean;

  public RadioButtonBindings(Action source, RadioButton target) {
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
  protected RadioButtonBean getTargetBean() {
    if (targetBean == null) {
      targetBean = new RadioButtonBean((RadioButton) getTarget());
    }
    return targetBean;
  }

  @Override
  public void onClick(Widget sender) {
    Boolean newValue = ((RadioButton) sender).isChecked();
    // XXX workaround to update BeanProperty {
    ((RadioButton) sender).setChecked(!newValue);
    getTargetBean().firePropertyChange("selected", newValue, !newValue);
    ((RadioButton) sender).setChecked(newValue);
    // XXX } end of workaround
    getTargetBean().firePropertyChange("selected", !newValue, newValue);
    super.onClick(sender);
  }

}
