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

import com.google.gwt.user.client.ui.PushButton;

/**
 * @author georgopoulos.georgios(at)gmail.com
 */
public class PushButtonBindings extends ButtonBaseBindings {

  public final class PushButtonBean extends ButtonBaseBean {
    public PushButtonBean(PushButton target) {
      super(target);
    }
  }

  private PushButtonBean targetBean;

  public PushButtonBindings(Action source) {
    this(source, new PushButton());

    // Action.NAME
    addBinding(Action.NAME, BeanProperty.<Action, String> create(Action.NAME),
        BeanProperty.<PushButtonBean, String> create("text"));
  }

  public PushButtonBindings(Action source, PushButton target) {
    super(source, target);
  }

  @Override
  protected PushButtonBean getTargetBean() {
    if (targetBean == null) {
      targetBean = new PushButtonBean((PushButton) getTarget());
    }
    return targetBean;
  }

}
