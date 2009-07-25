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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ButtonBase;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public abstract class ButtonBaseBindings extends ActionBindings<ButtonBase>
    implements ClickHandler {

  public class ButtonBaseBean extends TargetBean {
    public ButtonBaseBean(ButtonBase target) {
      super(target);
    }
  }

  public ButtonBaseBindings(Action source, ButtonBase target) {
    super(source, target);

    // Action.MNEMONIC_KEY;
    // addBinding(Action.MNEMONIC_KEY,
    // BeanProperty.<Action, Character> create(Action.MNEMONIC_KEY),
    // BeanProperty.<ButtonBaseBean, Character> create("accessKey"));

    // Action.SHORT_DESCRIPTION
    addBinding(Action.SHORT_DESCRIPTION,
        BeanProperty.<Action, String> create(Action.SHORT_DESCRIPTION),
        BeanProperty.<ButtonBaseBean, String> create("title"));

    // Action.NAME

    // Action.SMALL_ICON

    // Action.ACTION_COMMAND_KEY

    // "enabled"
    addBinding("enabled", BeanProperty.<Action, String> create("enabled"),
        BeanProperty.<ButtonBaseBean, String> create("enabled"));

    // "visible"
    addBinding("visible", BeanProperty.<Action, String> create("visible"),
        BeanProperty.<ButtonBaseBean, String> create("visible"));
  }

  private HandlerRegistration handlerReg = null;

  @Override
  protected void onBind() {
    handlerReg = getTarget().addClickHandler(this);
  }

  @Override
  public void onUnBind() {
    if (handlerReg != null) {
      handlerReg.removeHandler();
    }
  }

  public void onClick(ClickEvent event) {
    getSource().actionPerformed(new ActionEvent(getSource(), event.getSource()));
  }
}
