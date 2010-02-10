/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.showcase.client.content.other;

import org.gwt.mosaic.actions.client.Action;
import org.gwt.mosaic.actions.client.ButtonBindings;
import org.gwt.mosaic.actions.client.CheckBoxBindings;
import org.gwt.mosaic.actions.client.CheckBoxMenuItemBindings;
import org.gwt.mosaic.actions.client.CommandAction;
import org.gwt.mosaic.actions.client.MenuItemBindings;
import org.gwt.mosaic.actions.client.PushButtonBindings;
import org.gwt.mosaic.actions.client.RadioButtonBindings;
import org.gwt.mosaic.actions.client.ToggleButtonBindings;
import org.gwt.mosaic.actions.client.ToolButtonBindings;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwActions extends ContentWidget {

  /**
   * 
   */
  @ShowcaseData
  private CommandAction action;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwActions(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Actions description";
  }

  @Override
  public String getName() {
    return "Actions I";
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
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
    layoutPanel.setPadding(0);

    final LayoutPanel vBox = new ScrollLayoutPanel();
    vBox.setWidgetSpacing(0);

    action = new CommandAction("Hello!", new Command() {
      public void execute() {
        InfoPanel.show("Action", (String) action.getValue(Action.NAME));
      }
    });
    action.setEnabled(false);
    action.putValue(Action.SHORT_DESCRIPTION, "A short description");
    action.putValue(Action.SMALL_ICON, CommandAction.ACTION_IMAGES.bell());

    addMenuBar(layoutPanel);
    vBox.add(new Label("GWT Button"), new BoxLayoutData(FillStyle.HORIZONTAL));
    addButton(vBox);
    vBox.add(new Label("GWT ToolButton (PUSH)"), new BoxLayoutData(
        FillStyle.HORIZONTAL));
    addToolButton1(vBox);
    vBox.add(new Label("GWT ToolButton (CHECKBOX)"), new BoxLayoutData(
        FillStyle.HORIZONTAL));
    addToolButton2(vBox);
    vBox.add(new Label("GWT ToolButton (SPLIT)"), new BoxLayoutData(
        FillStyle.HORIZONTAL));
    addToolButton3(vBox);
    vBox.add(new Label("GWT ToolButton (MENU)"), new BoxLayoutData(
        FillStyle.HORIZONTAL));
    addToolButton4(vBox);
    vBox.add(new Label("GWT CustomButton"), new BoxLayoutData(
        FillStyle.HORIZONTAL));
    addCustomButton(vBox);

    layoutPanel.add(vBox, new BoxLayoutData(FillStyle.BOTH));

    return layoutPanel;
  }

  /**
   * 
   * @param panel
   */
  @ShowcaseSource
  private void addButton(final LayoutPanel panel) {
    final LayoutPanel hBox = new LayoutPanel(new BoxLayout());

    final ButtonBindings btnActionSupport1 = new ButtonBindings(action);
    btnActionSupport1.bind();

    final ButtonBindings btnActionSupport2 = new ButtonBindings(action);
    btnActionSupport2.setLabelType(ButtonLabelType.TEXT_ON_BOTTOM);
    btnActionSupport2.bind();

    final ButtonBindings btnActionSupport3 = new ButtonBindings(action);
    btnActionSupport3.setLabelType(ButtonLabelType.TEXT_ON_LEFT);
    btnActionSupport3.bind();

    final ButtonBindings btnActionSupport4 = new ButtonBindings(action);
    btnActionSupport4.setLabelType(ButtonLabelType.TEXT_ON_TOP);
    btnActionSupport4.bind();

    final ButtonBindings btnActionSupport5 = new ButtonBindings(action);
    btnActionSupport5.setLabelType(ButtonLabelType.TEXT_ONLY);
    btnActionSupport5.bind();

    final ButtonBindings btnActionSupport6 = new ButtonBindings(action);
    btnActionSupport6.setLabelType(ButtonLabelType.NO_TEXT);
    btnActionSupport6.bind();

    hBox.add(btnActionSupport1.getTarget());
    hBox.add(btnActionSupport2.getTarget());
    hBox.add(btnActionSupport3.getTarget());
    hBox.add(btnActionSupport4.getTarget());
    hBox.add(btnActionSupport5.getTarget());
    hBox.add(btnActionSupport6.getTarget());

    panel.add(hBox, new BoxLayoutData(FillStyle.HORIZONTAL, true));
  }

  /**
   * 
   * @param panel
   */
  @ShowcaseSource
  private void addToolButton1(final LayoutPanel panel) {
    final LayoutPanel hBox = new LayoutPanel(new BoxLayout());

    final ToolButtonBindings toolBtnActionSupport1 = new ToolButtonBindings(
        action);
    toolBtnActionSupport1.bind();

    final ToolButtonBindings toolBtnActionSupport2 = new ToolButtonBindings(
        action);
    toolBtnActionSupport2.bind();
    toolBtnActionSupport2.setLabelType(ButtonLabelType.TEXT_ON_BOTTOM);

    final ToolButtonBindings toolBtnActionSupport3 = new ToolButtonBindings(
        action);
    toolBtnActionSupport3.bind();
    toolBtnActionSupport3.setLabelType(ButtonLabelType.TEXT_ON_LEFT);

    final ToolButtonBindings toolBtnActionSupport4 = new ToolButtonBindings(
        action);
    toolBtnActionSupport4.bind();
    toolBtnActionSupport4.setLabelType(ButtonLabelType.TEXT_ON_TOP);

    final ToolButtonBindings toolBtnActionSupport5 = new ToolButtonBindings(
        action);
    toolBtnActionSupport5.bind();
    toolBtnActionSupport5.setLabelType(ButtonLabelType.TEXT_ONLY);

    final ToolButtonBindings toolBtnActionSupport6 = new ToolButtonBindings(
        action);
    toolBtnActionSupport6.bind();
    toolBtnActionSupport6.setLabelType(ButtonLabelType.NO_TEXT);

    hBox.add(toolBtnActionSupport1.getTarget());
    hBox.add(toolBtnActionSupport2.getTarget());
    hBox.add(toolBtnActionSupport3.getTarget());
    hBox.add(toolBtnActionSupport4.getTarget());
    hBox.add(toolBtnActionSupport5.getTarget());
    hBox.add(toolBtnActionSupport6.getTarget());

    panel.add(hBox, new BoxLayoutData(FillStyle.HORIZONTAL, true));
  }

  /**
   * 
   * @param panel
   */
  @ShowcaseSource
  private void addToolButton2(final LayoutPanel panel) {
    final LayoutPanel hBox = new LayoutPanel(new BoxLayout());

    final ToolButtonBindings toolBtnActionSupport1 = new ToolButtonBindings(
        action);
    toolBtnActionSupport1.getTarget().setStyle(ToolButtonStyle.CHECKBOX);
    toolBtnActionSupport1.bind();

    final ToolButtonBindings toolBtnActionSupport2 = new ToolButtonBindings(
        action);
    toolBtnActionSupport2.setLabelType(ButtonLabelType.TEXT_ON_BOTTOM);
    toolBtnActionSupport2.getTarget().setStyle(ToolButtonStyle.CHECKBOX);
    toolBtnActionSupport2.bind();

    final ToolButtonBindings toolBtnActionSupport3 = new ToolButtonBindings(
        action);
    toolBtnActionSupport3.setLabelType(ButtonLabelType.TEXT_ON_LEFT);
    toolBtnActionSupport3.getTarget().setStyle(ToolButtonStyle.CHECKBOX);
    toolBtnActionSupport3.bind();

    final ToolButtonBindings toolBtnActionSupport4 = new ToolButtonBindings(
        action);
    toolBtnActionSupport4.setLabelType(ButtonLabelType.TEXT_ON_TOP);
    toolBtnActionSupport4.getTarget().setStyle(ToolButtonStyle.CHECKBOX);
    toolBtnActionSupport4.bind();

    final ToolButtonBindings toolBtnActionSupport5 = new ToolButtonBindings(
        action);
    toolBtnActionSupport5.setLabelType(ButtonLabelType.TEXT_ONLY);
    toolBtnActionSupport5.getTarget().setStyle(ToolButtonStyle.CHECKBOX);
    toolBtnActionSupport5.bind();

    final ToolButtonBindings toolBtnActionSupport6 = new ToolButtonBindings(
        action);
    toolBtnActionSupport6.setLabelType(ButtonLabelType.NO_TEXT);
    toolBtnActionSupport6.getTarget().setStyle(ToolButtonStyle.CHECKBOX);
    toolBtnActionSupport6.bind();

    hBox.add(toolBtnActionSupport1.getTarget());
    hBox.add(toolBtnActionSupport2.getTarget());
    hBox.add(toolBtnActionSupport3.getTarget());
    hBox.add(toolBtnActionSupport4.getTarget());
    hBox.add(toolBtnActionSupport5.getTarget());
    hBox.add(toolBtnActionSupport6.getTarget());

    panel.add(hBox, new BoxLayoutData(FillStyle.HORIZONTAL, true));
  }

  /**
   * 
   * @param panel
   */
  @ShowcaseSource
  private void addToolButton3(final LayoutPanel panel) {
    final LayoutPanel hBox = new LayoutPanel(new BoxLayout());

    // Make a command that we will execute from all menu items.
    Command cmd1 = new Command() {
      public void execute() {
        InfoPanel.show("Menu Button", "You selected a menu item!");
      }
    };

    PopupMenu btnMenu = new PopupMenu();
    btnMenu.addItem("Item 1", cmd1);
    btnMenu.addItem("Item 2", cmd1);

    final ToolButtonBindings toolBtnActionSupport1 = new ToolButtonBindings(
        action);
    ToolButton btn1 = toolBtnActionSupport1.getTarget();
    btn1.setStyle(ToolButtonStyle.SPLIT);
    btn1.setMenu(btnMenu);
    toolBtnActionSupport1.bind();

    final ToolButtonBindings toolBtnActionSupport2 = new ToolButtonBindings(
        action);
    toolBtnActionSupport2.setLabelType(ButtonLabelType.TEXT_ON_BOTTOM);
    ToolButton btn2 = toolBtnActionSupport2.getTarget();
    btn2.setStyle(ToolButtonStyle.SPLIT);
    btn2.setMenu(btnMenu);
    toolBtnActionSupport2.bind();

    final ToolButtonBindings toolBtnActionSupport3 = new ToolButtonBindings(
        action);
    toolBtnActionSupport3.setLabelType(ButtonLabelType.TEXT_ON_LEFT);
    ToolButton btn3 = toolBtnActionSupport3.getTarget();
    btn3.setStyle(ToolButtonStyle.SPLIT);
    btn3.setMenu(btnMenu);
    toolBtnActionSupport3.bind();

    final ToolButtonBindings toolBtnActionSupport4 = new ToolButtonBindings(
        action);
    toolBtnActionSupport4.setLabelType(ButtonLabelType.TEXT_ON_TOP);
    ToolButton btn4 = toolBtnActionSupport4.getTarget();
    btn4.setStyle(ToolButtonStyle.SPLIT);
    btn4.setMenu(btnMenu);
    toolBtnActionSupport4.bind();

    final ToolButtonBindings toolBtnActionSupport5 = new ToolButtonBindings(
        action);
    toolBtnActionSupport5.setLabelType(ButtonLabelType.TEXT_ONLY);
    ToolButton btn5 = toolBtnActionSupport5.getTarget();
    btn5.setStyle(ToolButtonStyle.SPLIT);
    btn5.setMenu(btnMenu);
    toolBtnActionSupport5.bind();

    final ToolButtonBindings toolBtnActionSupport6 = new ToolButtonBindings(
        action);
    toolBtnActionSupport6.setLabelType(ButtonLabelType.NO_TEXT);
    ToolButton btn6 = toolBtnActionSupport6.getTarget();
    btn6.setStyle(ToolButtonStyle.SPLIT);
    btn6.setMenu(btnMenu);
    toolBtnActionSupport6.bind();

    hBox.add(btn1);
    hBox.add(btn2);
    hBox.add(btn3);
    hBox.add(btn4);
    hBox.add(btn5);
    hBox.add(btn6);

    panel.add(hBox, new BoxLayoutData(FillStyle.HORIZONTAL, true));
  }

  /**
   * 
   * @param panel
   */
  @ShowcaseSource
  private void addToolButton4(final LayoutPanel panel) {
    final LayoutPanel hBox = new LayoutPanel(new BoxLayout());

    // Make a command that we will execute from all menu items.
    Command cmd1 = new Command() {
      public void execute() {
        InfoPanel.show("Menu Button", "You selected a menu item!");
      }
    };

    PopupMenu btnMenu = new PopupMenu();
    btnMenu.addItem("Item 1", cmd1);
    btnMenu.addItem("Item 2", cmd1);

    final ToolButtonBindings toolBtnActionSupport1 = new ToolButtonBindings(
        action);
    ToolButton btn1 = toolBtnActionSupport1.getTarget();
    btn1.setStyle(ToolButtonStyle.MENU);
    btn1.setMenu(btnMenu);
    toolBtnActionSupport1.bind();

    final ToolButtonBindings toolBtnActionSupport2 = new ToolButtonBindings(
        action);
    toolBtnActionSupport2.setLabelType(ButtonLabelType.TEXT_ON_BOTTOM);
    ToolButton btn2 = toolBtnActionSupport2.getTarget();
    btn2.setStyle(ToolButtonStyle.MENU);
    btn2.setMenu(btnMenu);
    toolBtnActionSupport2.bind();

    final ToolButtonBindings toolBtnActionSupport3 = new ToolButtonBindings(
        action);
    toolBtnActionSupport3.setLabelType(ButtonLabelType.TEXT_ON_LEFT);
    ToolButton btn3 = toolBtnActionSupport3.getTarget();
    btn3.setStyle(ToolButtonStyle.MENU);
    btn3.setMenu(btnMenu);
    toolBtnActionSupport3.bind();

    final ToolButtonBindings toolBtnActionSupport4 = new ToolButtonBindings(
        action);
    toolBtnActionSupport4.setLabelType(ButtonLabelType.TEXT_ON_TOP);
    ToolButton btn4 = toolBtnActionSupport4.getTarget();
    btn4.setStyle(ToolButtonStyle.MENU);
    btn4.setMenu(btnMenu);
    toolBtnActionSupport4.bind();

    final ToolButtonBindings toolBtnActionSupport5 = new ToolButtonBindings(
        action);
    toolBtnActionSupport5.setLabelType(ButtonLabelType.TEXT_ONLY);
    ToolButton btn5 = toolBtnActionSupport5.getTarget();
    btn5.setStyle(ToolButtonStyle.MENU);
    btn5.setMenu(btnMenu);
    toolBtnActionSupport5.bind();

    final ToolButtonBindings toolBtnActionSupport6 = new ToolButtonBindings(
        action);
    toolBtnActionSupport6.setLabelType(ButtonLabelType.NO_TEXT);
    ToolButton btn6 = toolBtnActionSupport6.getTarget();
    btn6.setStyle(ToolButtonStyle.MENU);
    btn6.setMenu(btnMenu);
    toolBtnActionSupport6.bind();

    hBox.add(btn1);
    hBox.add(btn2);
    hBox.add(btn3);
    hBox.add(btn4);
    hBox.add(btn5);
    hBox.add(btn6);

    panel.add(hBox, new BoxLayoutData(FillStyle.HORIZONTAL, true));
  }

  /**
   * 
   * @param panel
   */
  @ShowcaseSource
  private void addCustomButton(final LayoutPanel panel) {
    final LayoutPanel hBox = new LayoutPanel(new BoxLayout());

    final PushButtonBindings pushBtnActionSupport = new PushButtonBindings(
        action, new PushButton(
            MessageBox.MESSAGEBOX_IMAGES.dialogInformation().createImage(),
            MessageBox.MESSAGEBOX_IMAGES.dialogQuestion().createImage()));
    pushBtnActionSupport.bind();

    final ToggleButtonBindings toggleBtnActionSupport = new ToggleButtonBindings(
        action, new ToggleButton(
            MessageBox.MESSAGEBOX_IMAGES.dialogInformation().createImage(),
            MessageBox.MESSAGEBOX_IMAGES.dialogQuestion().createImage()));
    toggleBtnActionSupport.bind();

    final CheckBoxBindings checkBoxActionSupport = new CheckBoxBindings(action);
    checkBoxActionSupport.bind();

    final RadioButtonBindings radioButtonActionSupport = new RadioButtonBindings(
        action, new RadioButton("RadioButtonName"));
    radioButtonActionSupport.bind();

    hBox.add(new WidgetWrapper(pushBtnActionSupport.getTarget()));
    hBox.add(new WidgetWrapper(toggleBtnActionSupport.getTarget()));
    hBox.add(new WidgetWrapper(checkBoxActionSupport.getTarget()));
    hBox.add(new WidgetWrapper(radioButtonActionSupport.getTarget()));

    panel.add(hBox, new BoxLayoutData(FillStyle.HORIZONTAL, true));
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

    final MenuItemBindings actionBinding1 = new MenuItemBindings(action);
    actionMenu.addItem(actionBinding1.getTarget());
    actionBinding1.bind();

    actionMenu.addSeparator();

    final CheckBoxMenuItemBindings actionBinding2 = new CheckBoxMenuItemBindings(
        action);
    actionMenu.addItem(actionBinding2.getTarget());
    actionBinding2.bind();

    // Create the 'Action control' menu
    MenuBar actionCtrlMenu = new MenuBar(true);
    menu.addItem(new MenuItem("Actions Control", actionCtrlMenu));

    actionCtrlMenu.addItem(new MenuItem("Change Text", new Command() {
      public void execute() {
        String newName = Window.prompt("New Text:",
            (String) action.getValue(Action.NAME));
        action.putValue(Action.NAME, newName);

        DeferredCommand.addCommand(new Command() {
          public void execute() {
            panel.layout();
          }
        });
      }
    }));

    actionCtrlMenu.addItem(new MenuItem("Change Tooltip", new Command() {
      public void execute() {
        String newName = Window.prompt("New Text:",
            (String) action.getValue(Action.SHORT_DESCRIPTION));
        action.putValue(Action.SHORT_DESCRIPTION, newName);
      }
    }));

    actionCtrlMenu.addSeparator();

    actionCtrlMenu.addItem(new MenuItem("Toggle Enabled", new Command() {
      public void execute() {
        action.setEnabled(!action.isEnabled());
      }
    }));

    actionCtrlMenu.addItem(new MenuItem("Toggle Selected", new Command() {
      public void execute() {
        Boolean selected = (Boolean) action.getValue("selected");
        if (selected == null) {
          selected = Boolean.FALSE;
        }
        action.putValue("selected", !selected);
      }
    }));

    actionCtrlMenu.addItem(new MenuItem("Toggle Icon", new Command() {
      public void execute() {
        ImageResource icon = (ImageResource) action.getValue(Action.SMALL_ICON);
        if (CommandAction.ACTION_IMAGES.bell().equals(icon)) {
          icon = CommandAction.ACTION_IMAGES.award_star_bronze_1();
        } else {
          icon = CommandAction.ACTION_IMAGES.bell();
        }
        action.putValue(Action.SMALL_ICON, icon);
      }
    }));

    panel.add(menu, new BoxLayoutData(FillStyle.HORIZONTAL));
  }
}
