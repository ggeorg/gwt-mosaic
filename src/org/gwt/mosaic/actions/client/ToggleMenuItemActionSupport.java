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
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.mosaic.actions.client.ToggleButtonActionSupport.ToggleButtonBean;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ToggleMenuItemActionSupport extends ActionSupport<MenuItem> {

  public final class MenuItemBean extends TargetBean {
    private boolean enabled = true;
    private boolean selected = false;
    private Command command = null;
    private String text;

    public MenuItemBean(MenuItem target, Command command) {
      super(target);
      this.command = command;
      this.text = target.getText();
    }

    private String createLabel() {
      AbstractImagePrototype image = getSelected()
          ? CommandAction.ACTION_IMAGES.menuitem_checkbox()
          : CommandAction.ACTION_IMAGES.noimage();
      return ButtonHelper.createButtonLabel(image, text,
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
        getTarget().setCommand(command);
        getTarget().removeStyleDependentName("disabled");
      }
      changeSupport.firePropertyChange("enabled", oldValue, enabled);
    }

    @Override
    public void setText(String text) {
      String oldValue = this.text;
      this.text = text;
      changeSupport.firePropertyChange("text", oldValue, text);
      target.setHTML(createLabel());
    }

    public Boolean getSelected() {
      return selected;
    }

    public void setSelected(Boolean selected) {
      selected = toBoolean(selected, Boolean.FALSE);
      Boolean oldValue = this.selected;
      this.selected = selected;
      changeSupport.firePropertyChange("selected", oldValue, selected);
      target.setHTML(createLabel());
    }

  }

  private MenuItemBean targetBean;

  public ToggleMenuItemActionSupport(Action source) {
    this(source, new MenuItem("", (Command) null));
  }

  public ToggleMenuItemActionSupport(Action source, MenuItem target) {
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

    // "selected"
    addBinding("selected", UpdateStrategy.READ_WRITE,
        BeanProperty.<Action, String> create("selected"),
        BeanProperty.<ToggleButtonBean, String> create("selected"));

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
    if (getSource() instanceof HasCommand && getTargetBean().getEnabled()) {
      getTarget().setCommand(((HasCommand) getSource()).getCommand());
    }
  }

  @Override
  protected void onUnBind() {
    getTarget().setCommand(null);
  }

}
