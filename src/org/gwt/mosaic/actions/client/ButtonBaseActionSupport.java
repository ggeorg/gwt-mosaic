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

import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author georgopoulos.georgios(at)gmail.com
 */
public abstract class ButtonBaseActionSupport extends ActionSupport<ButtonBase>
    implements ClickListener {

  public class ButtonBaseBean extends TargetBean {
    public ButtonBaseBean(ButtonBase target) {
      super(target);
    }
  }

  public ButtonBaseActionSupport(Action source, ButtonBase target) {
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

  @Override
  protected void onBind() {
    getTarget().addClickListener(this);
  }

  public void onClick(Widget sender) {
    getSource().actionPerformed(new ActionEvent(getSource(), sender));
  }

  @Override
  public void onUnBind() {
    getTarget().removeClickListener(this);
  }
}
