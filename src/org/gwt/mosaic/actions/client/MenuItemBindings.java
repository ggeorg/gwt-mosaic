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

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class MenuItemBindings extends ActionBindings<MenuItem> {

  public final class MenuItemBean extends TargetBean {
    private boolean enabled = true;
    private String text;

    public MenuItemBean(MenuItem target) {
      super(target);
      this.text = target.getText();
    }

    private String createLabel() {
      ImageResource image = this.getImage();
      if (image == null) {
        image = CommandAction.ACTION_IMAGES.noimage();
      }
      return ButtonHelper.createButtonLabel(
          AbstractImagePrototype.create(image), text,
          ButtonLabelType.TEXT_ON_RIGHT);
    }

    @Override
    public Boolean getEnabled() {
      return enabled;
    }

    @Override
    public String getText() {
      return this.text;
    }

    @Override
    public void setEnabled(Boolean enabled) {
      enabled = toBoolean(enabled, Boolean.TRUE);
      Boolean oldValue = this.enabled;
      this.enabled = enabled;
      if (!enabled) {
        getTarget().setCommand(null);
        getTarget().addStyleDependentName("disabled");
      } else {
        getTarget().setCommand(menuCmd);
        getTarget().removeStyleDependentName("disabled");
      }
      changeSupport.firePropertyChange("enabled", oldValue, enabled);
    }

    @Override
    public void setImage(ImageResource image) {
      super.setImage(image);
      target.setHTML(createLabel());
    }

    @Override
    public void setText(String text) {
      String oldValue = this.text;
      this.text = text;
      changeSupport.firePropertyChange("text", oldValue, text);
      target.setHTML(createLabel());
    }

  }

  private MenuItemBean targetBean;

  private Command menuCmd = new Command() {
    public void execute() {
      if (getTargetBean().getEnabled()) {
        getSource().actionPerformed(new ActionEvent(getSource(), getTarget()));
      }
    }
  };

  public MenuItemBindings(Action source) {
    this(source, new MenuItem("", (Command) null));
  }

  public MenuItemBindings(Action source, MenuItem target) {
    super(source, target);

    // Action.MNEMONIC_KEY;

    // Action.NAME
    addBinding(Action.NAME, BeanProperty.<Action, String> create(Action.NAME),
        BeanProperty.<MenuItemBean, String> create("text"));

    // Action.SHORT_DESCRIPTION
    addBinding(Action.SHORT_DESCRIPTION,
        BeanProperty.<Action, String> create(Action.SHORT_DESCRIPTION),
        BeanProperty.<MenuItemBean, String> create("title"));

    // Action.SMALL_ICON
    addBinding(Action.SMALL_ICON,
        BeanProperty.<Action, String> create(Action.SMALL_ICON),
        BeanProperty.<MenuItemBean, String> create("image"));

    // Action.ACTION_COMMAND_KEY

    // "enabled"
    addBinding("enabled", BeanProperty.<Action, String> create("enabled"),
        BeanProperty.<MenuItemBean, String> create("enabled"));

    // "visible"
    addBinding("visible", BeanProperty.<Action, String> create("visible"),
        BeanProperty.<MenuItemBean, String> create("visible"));
  }

  @Override
  protected MenuItemBean getTargetBean() {
    if (targetBean == null) {
      targetBean = new MenuItemBean(getTarget());
    }
    return targetBean;
  }

  @Override
  protected void onBind() {
    getTarget().setCommand(menuCmd);
  }

  @Override
  protected void onUnBind() {
    getTarget().setCommand(null);
  }

}
