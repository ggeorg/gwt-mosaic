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

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ButtonActionSupport extends ActionSupport<Button> implements
    ClickListener {

  public final class ButtonBean extends TargetBean {
    public ButtonBean(Button target) {
      super(target);
    }
  }

  private TargetBean targetBean;

  public ButtonActionSupport(Action source, Button target) {
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

    // Action.ACTION_COMMAND_KEY

    // "enabled"
    addBinding("enabled", BeanProperty.<Action, String> create("enabled"),
        BeanProperty.<TargetBean, String> create("enabled"));

    // "visible"
    addBinding("visible", BeanProperty.<Action, String> create("visible"),
        BeanProperty.<TargetBean, String> create("visible"));
  }

  @Override
  protected TargetBean getTargetBean() {
    if (targetBean == null) {
      targetBean = new TargetBean(getTarget());
    }
    return targetBean;
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
