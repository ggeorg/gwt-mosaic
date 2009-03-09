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
package org.gwt.mosaic.showcase.client.content.other;

import org.gwt.mosaic.actions.client.Action;
import org.gwt.mosaic.actions.client.CommandAction;
import org.gwt.mosaic.actions.client.RadioButtonBindings;
import org.gwt.mosaic.actions.client.RadioButtonMenuItemBindings;
import org.gwt.mosaic.actions.client.ToolButtonBindings;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.ToolBar;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwRadioButtonActions extends ContentWidget {

  /**
   * 
   */
  @ShowcaseData
  private CommandAction action1, action2, action3;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwRadioButtonActions(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Actions with radio buttons";
  }

  @Override
  public String getName() {
    return "Actions II";
  }

  @Override
  public boolean hasStyle() {
    return false;
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel();

    final LayoutPanel vBox = new LayoutPanel(
        new BoxLayout(Orientation.VERTICAL));
    vBox.setPadding(0);
    vBox.setWidgetSpacing(0);

    action1 = new CommandAction("foo", new Command() {
      public void execute() {
        InfoPanel.show("Action", (String) action1.getValue(Action.NAME));
      }
    });
    action1.putValue(Action.SHORT_DESCRIPTION,
        "A short description for Action #1");
    action1.putValue(Action.SMALL_ICON, CommandAction.ACTION_IMAGES.bomb());

    action2 = new CommandAction("bar", new Command() {
      public void execute() {
        InfoPanel.show("Action", (String) action2.getValue(Action.NAME));
      }
    });
    action2.putValue(Action.SHORT_DESCRIPTION,
        "A short description for Action #2");
    action2.putValue(Action.SMALL_ICON, CommandAction.ACTION_IMAGES.bell());

    action3 = new CommandAction("baz", new Command() {
      public void execute() {
        InfoPanel.show("Action", (String) action3.getValue(Action.NAME));
      }
    });
    action3.putValue(Action.SHORT_DESCRIPTION,
        "A short description for Action #3");
    action3.putValue(Action.SMALL_ICON,
        CommandAction.ACTION_IMAGES.award_star_bronze_1());

    addMenuBar(vBox);
    addToolButton2(vBox);
    addCustomButton(vBox);

    layoutPanel.add(vBox, new FillLayoutData(true));

    return layoutPanel;
  }

  /**
   * 
   * @param panel
   */
  @ShowcaseSource
  private void addToolButton2(final LayoutPanel panel) {
    final ToolBar toolBar = new ToolBar();

    final ToolButtonBindings toolBtnActionSupport1 = new ToolButtonBindings(
        action1);
    toolBtnActionSupport1.getTarget().setStyle(ToolButtonStyle.RADIO);
    toolBtnActionSupport1.bind();

    final ToolButtonBindings toolBtnActionSupport2 = new ToolButtonBindings(
        action2);
    toolBtnActionSupport2.getTarget().setStyle(ToolButtonStyle.RADIO);
    toolBtnActionSupport2.bind();

    final ToolButtonBindings toolBtnActionSupport3 = new ToolButtonBindings(
        action3);
    toolBtnActionSupport3.getTarget().setStyle(ToolButtonStyle.RADIO);
    toolBtnActionSupport3.bind();

    toolBar.add(toolBtnActionSupport1.getTarget());
    toolBar.add(toolBtnActionSupport2.getTarget());
    toolBar.add(toolBtnActionSupport3.getTarget());

    panel.add(toolBar, new BoxLayoutData(FillStyle.HORIZONTAL));
  }

  /**
   * 
   * @param panel
   */
  @ShowcaseSource
  private void addCustomButton(final LayoutPanel panel) {
    final LayoutPanel hBox = new LayoutPanel(new BoxLayout());

    final RadioButtonBindings radioButtonActionSupport1 = new RadioButtonBindings(
        action1, new RadioButton("myRadioGroup"));
    final RadioButtonBindings radioButtonActionSupport2 = new RadioButtonBindings(
        action2, new RadioButton("myRadioGroup"));
    final RadioButtonBindings radioButtonActionSupport3 = new RadioButtonBindings(
        action3, new RadioButton("myRadioGroup"));

    radioButtonActionSupport1.bind();
    radioButtonActionSupport2.bind();
    radioButtonActionSupport3.bind();

    hBox.add(new WidgetWrapper(radioButtonActionSupport1.getTarget()));
    hBox.add(new WidgetWrapper(radioButtonActionSupport2.getTarget()));
    hBox.add(new WidgetWrapper(radioButtonActionSupport3.getTarget()));

    panel.add(hBox);
  }

  /**
   * 
   * @param panel
   */
  @ShowcaseSource
  private void addMenuBar(final LayoutPanel panel) {
    // Create a menu bar
    MenuBar menu = new MenuBar();
    menu.setAnimationEnabled(true);

    // Create the 'Action' menu
    MenuBar actionMenu = new MenuBar(true);
    menu.addItem(new MenuItem("MenuItem", actionMenu));

    RadioButtonMenuItemBindings mi1 = new RadioButtonMenuItemBindings(
        "miRadioGroup", action1);
    actionMenu.addItem(mi1.getTarget());
    RadioButtonMenuItemBindings mi2 = new RadioButtonMenuItemBindings(
        "miRadioGroup", action2);
    actionMenu.addItem(mi2.getTarget());
    RadioButtonMenuItemBindings mi3 = new RadioButtonMenuItemBindings(
        "miRadioGroup", action3);
    actionMenu.addItem(mi3.getTarget());

    mi1.bind();
    mi2.bind();
    mi3.bind();

    // Create the 'Action control' menu
    MenuBar actionCtrlMenu = new MenuBar(true);
    menu.addItem(new MenuItem("Actions Control", actionCtrlMenu));

    actionCtrlMenu.addItem(new MenuItem("Toggle Enabled", new Command() {
      public void execute() {
        action1.setEnabled(!action1.isEnabled());
        action2.setEnabled(!action2.isEnabled());
        action3.setEnabled(!action3.isEnabled());
      }
    }));

    panel.add(menu, new BoxLayoutData(FillStyle.HORIZONTAL));
  }

  @Override
  protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
    GWT.runAsync(new RunAsyncCallback() {

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }

      public void onSuccess() {
        callback.onSuccess(onInitialize());
      }
    });
  }
  
}
