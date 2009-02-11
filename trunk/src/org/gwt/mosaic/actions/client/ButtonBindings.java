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
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;

/**
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ButtonBindings extends ButtonBaseBindings {

  public final class ButtonBean extends ButtonBaseBean {
    private String text;

    public ButtonBean(Button target) {
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

    @Override
    public String getText() {
      return this.text;
    }

    @Override
    public void setImage(AbstractImagePrototype image) {
      super.setImage(image);
      target.setHTML(createLabel());
    }

    @Override
    public void setText(String text) {
      String oldValue = this.text;
      this.text = text;
      invalidate();
      changeSupport.firePropertyChange("text", oldValue, text);
      target.setHTML(createLabel());
    }
  }

  private ButtonLabelType labelType = ButtonLabelType.TEXT_ON_RIGHT;

  private ButtonBean targetBean;

  public ButtonBindings(Action source) {
    this(source, new Button());
  }

  public ButtonBindings(Action source, Button target) {
    super(source, target);

    // Action.NAME
    addBinding(Action.NAME, BeanProperty.<Action, String> create(Action.NAME),
        BeanProperty.<ButtonBaseBean, String> create("text"));

    // Action.SMALL_ICON
    addBinding(Action.SMALL_ICON,
        BeanProperty.<Action, String> create(Action.SMALL_ICON),
        BeanProperty.<ButtonBean, String> create("image"));
  }

  public ButtonLabelType getLabelType() {
    return labelType;
  }

  @Override
  protected ButtonBean getTargetBean() {
    if (targetBean == null) {
      targetBean = new ButtonBean((Button) getTarget());
    }
    return targetBean;
  }

  public void setLabelType(ButtonLabelType labelType) {
    this.labelType = labelType;
    getTargetBean().setText(getTargetBean().getText());
  }
}
