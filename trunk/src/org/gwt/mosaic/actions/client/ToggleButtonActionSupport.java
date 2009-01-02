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

import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ToggleButtonActionSupport extends ButtonBaseActionSupport {

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
        Object selected) {
      changeSupport.firePropertyChange(property, oldValue, selected);
    }
  }

  private ToggleButtonBean targetBean;

  public ToggleButtonActionSupport(Action source) {
    this(source, new ToggleButton());

    // Action.NAME
    addBinding(Action.NAME, BeanProperty.<Action, String> create(Action.NAME),
        BeanProperty.<ToggleButtonBean, String> create("text"));
  }

  public ToggleButtonActionSupport(Action source, ToggleButton target) {
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
  public void onClick(Widget sender) {
    super.onClick(sender);
    Boolean newValue = ((ToggleButton) sender).isDown();
    getTargetBean().firePropertyChange("selected", !newValue, newValue);
  }

}
