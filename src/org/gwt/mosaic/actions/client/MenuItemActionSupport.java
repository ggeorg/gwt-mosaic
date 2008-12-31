/*
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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class MenuItemActionSupport extends ActionSupport<MenuItem> {

  public final class MenuItemBean extends TargetBean {
    private boolean enabled = true;
    private Command command = null;

    public MenuItemBean(MenuItem target, Command command) {
      super(target);
      this.command = command;
    }

    @Override
    public Boolean isEnabled() {
      return enabled;
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
        getTarget().setCommand(command);
        getTarget().removeStyleDependentName("disabled");
      }
      changeSupport.firePropertyChange("enabled", oldValue, enabled);
    }
  }

  private MenuItemBean targetBean;

  public MenuItemActionSupport(Action source) {
    this(source, new MenuItem("", (Command) null));
  }

  public MenuItemActionSupport(Action source, MenuItem target) {
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
      Command command = getSource() instanceof HasCommand
          ? ((HasCommand) getSource()).getCommand() : getTarget().getCommand();
      targetBean = new MenuItemBean(getTarget(), command);
    }
    return targetBean;
  }

  @Override
  protected void onBind() {
    if (getSource() instanceof HasCommand && getTargetBean().isEnabled()) {
      getTarget().setCommand(((HasCommand) getSource()).getCommand());
    }
  }

  @Override
  protected void onUnBind() {
    getTarget().setCommand(null);
  }

}
