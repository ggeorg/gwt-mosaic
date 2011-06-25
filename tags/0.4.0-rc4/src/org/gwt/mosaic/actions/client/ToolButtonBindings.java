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
import org.gwt.mosaic.actions.client.ButtonBindings.ButtonBean;
import org.gwt.mosaic.actions.client.ToggleButtonBindings.ToggleButtonBean;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ToolButtonBindings extends ActionBindings<ToolButton> implements
    ClickHandler {

  public final class ToolButtonBean extends TargetBean {
    private String text;

    public ToolButtonBean(ToolButton target) {
      super(target);
    }

    private String createLabel() {
      ImageResource image = this.getImage();
      if (image == null) {
        return text;
      } else {
        return ButtonHelper.createButtonLabel(
            AbstractImagePrototype.create(image), text, labelType);
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
    public void setImage(ImageResource image) {
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
      invalidate(); // its a dummy call, just for good practice
      changeSupport.firePropertyChange("text", oldValue, text);
      target.setHTML(createLabel());
    }
  }

  private ButtonLabelType labelType = ButtonLabelType.TEXT_ON_RIGHT;

  private ToolButtonBean targetBean;

  public ToolButtonBindings(Action source) {
    this(source, new ToolButton());
  }

  public ToolButtonBindings(Action source, ToolButton target) {
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

  private HandlerRegistration handlerReg = null;

  @Override
  protected void onBind() {
    handlerReg = getTarget().addClickHandler(this);
  }

  @Override
  protected void onUnBind() {
    if (handlerReg != null) {
      handlerReg.removeHandler();
    }
  }

  public void setLabelType(ButtonLabelType labelType) {
    this.labelType = labelType;
    getTargetBean().setText(getTargetBean().getText());
  }

  public void onClick(ClickEvent event) {
    if (getTarget().getStyle() == ToolButtonStyle.CHECKBOX) {
      Boolean newValue = getTarget().isChecked();
      getTargetBean().firePropertyChange("selected", !newValue, newValue);
    } else if (getTarget().getStyle() == ToolButtonStyle.RADIO) {
      Boolean newValue = getTarget().isChecked();
      // XXX workaround to update BeanProperty {
      getTarget().setChecked(!newValue);
      getTargetBean().firePropertyChange("selected", newValue, !newValue);
      getTarget().setChecked(newValue);
      // XXX } end of workaround
      getTargetBean().firePropertyChange("selected", !newValue, newValue);
    }
    getSource().actionPerformed(new ActionEvent(getSource(), event.getSource()));
  }

}