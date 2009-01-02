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
import org.gwt.mosaic.actions.client.ButtonActionSupport.ButtonBean;
import org.gwt.mosaic.actions.client.ToggleButtonActionSupport.ToggleButtonBean;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ToolButtonActionSupport extends ActionSupport<ToolButton>
    implements ClickListener {

  public final class ToolButtonBean extends TargetBean {
    private String text;

    public ToolButtonBean(ToolButton target) {
      super(target);
    }

    private String createLabel() {
      AbstractImagePrototype image = this.getImage();
      if (image == null) {
        return text;
      } else {
        return ButtonHelper.createButtonLabel(image, text, labelType);
      }
    }

    public void firePropertyChange(final String property, Object oldValue,
        Object newValue) {
      changeSupport.firePropertyChange(property, oldValue, newValue);
    }

    public Boolean getSelected() {
      return target.isChecked();
    }

    @Override
    public String getText() {
      return this.text;
    }

    @Override
    public Boolean getEnabled() {
      return target.isEnabled();
    }

    @Override
    public void setEnabled(Boolean enabled) {
      enabled = toBoolean(enabled, Boolean.TRUE);
      Boolean oldValue = target.isEnabled();
      target.setEnabled(enabled);
      changeSupport.firePropertyChange("enabled", oldValue, enabled);
    }

    @Override
    public void setImage(AbstractImagePrototype image) {
      super.setImage(image);
      target.setHTML(createLabel());
    }

    public void setSelected(Boolean selected) {
      selected = toBoolean(selected, Boolean.FALSE);
      Boolean oldValue = target.isChecked();
      target.setChecked(selected);
      changeSupport.firePropertyChange("selected", oldValue, selected);
    }

    @Override
    public void setText(String text) {
      String oldValue = this.text;
      this.text = text;
      changeSupport.firePropertyChange("text", oldValue, text);
      target.setHTML(createLabel());
    }
  }

  private ButtonLabelType labelType = ButtonLabelType.TEXT_ON_RIGHT;

  private ToolButtonBean targetBean;

  public ToolButtonActionSupport(Action source) {
    this(source, new ToolButton());
  }

  public ToolButtonActionSupport(Action source, ToolButton target) {
    super(source, target);

    // Action.MNEMONIC_KEY;
    // addBinding(Action.MNEMONIC_KEY,
    // BeanProperty.<Action, Character> create(Action.MNEMONIC_KEY),
    // BeanProperty.<TargetBean, Character> create("accessKey"));

    // Action.NAME
    addBinding(Action.NAME, BeanProperty.<Action, String> create(Action.NAME),
        BeanProperty.<TargetBean, String> create("text"));

    // Action.SHORT_DESCRIPTION
    addBinding(Action.SHORT_DESCRIPTION,
        BeanProperty.<Action, String> create(Action.SHORT_DESCRIPTION),
        BeanProperty.<TargetBean, String> create("title"));

    // Action.SMALL_ICON
    addBinding(Action.SMALL_ICON,
        BeanProperty.<Action, String> create(Action.SMALL_ICON),
        BeanProperty.<ButtonBean, String> create("image"));

    // Action.ACTION_COMMAND_KEY

    // "enabled"
    addBinding("enabled", BeanProperty.<Action, String> create("enabled"),
        BeanProperty.<TargetBean, String> create("enabled"));

    // "selected"
    addBinding("selected", UpdateStrategy.READ_WRITE,
        BeanProperty.<Action, String> create("selected"),
        BeanProperty.<ToggleButtonBean, String> create("selected"));

    // "visible"
    addBinding("visible", BeanProperty.<Action, String> create("visible"),
        BeanProperty.<TargetBean, String> create("visible"));
  }

  public ButtonLabelType getLabelType() {
    return labelType;
  }

  @Override
  protected ToolButtonBean getTargetBean() {
    if (targetBean == null) {
      targetBean = new ToolButtonBean(getTarget());
    }
    return targetBean;
  }

  @Override
  protected void onBind() {
    getTarget().addClickListener(this);
  }

  public void onClick(Widget sender) {
    if (getTarget().getStyle() == ToolButtonStyle.CHECKBOX
        || (getTarget().getStyle() == ToolButtonStyle.RADIO)) {
      Boolean newValue = getTarget().isChecked();
      getTargetBean().firePropertyChange("selected", !newValue, newValue);
    }
    getSource().actionPerformed(new ActionEvent(getSource(), sender));
  }

  @Override
  protected void onUnBind() {
    getTarget().removeClickListener(this);
  }

  public void setLabelType(ButtonLabelType labelType) {
    this.labelType = labelType;
    getTargetBean().setText(getTargetBean().getText());
  }

}
