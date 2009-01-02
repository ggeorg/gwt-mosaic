/*
 * Copyright 2008 Google Inc.
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.gwt.mosaic.actions.client.Action;
import org.gwt.mosaic.actions.client.ButtonActionSupport;
import org.gwt.mosaic.actions.client.CommandAction;
import org.gwt.mosaic.actions.client.MenuItemActionSupport;
import org.gwt.mosaic.actions.client.PushButtonActionSupport;
import org.gwt.mosaic.actions.client.ToggleButtonActionSupport;
import org.gwt.mosaic.actions.client.ToggleMenuItemActionSupport;
import org.gwt.mosaic.actions.client.ToolButtonActionSupport;
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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwActions2 extends ContentWidget {

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
  public CwActions2(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Actions description";
  }

  @Override
  public String getName() {
    return "Actions";
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
    layoutPanel.setWidgetSpacing(0);
    
    final LayoutPanel vBox = new ScrollLayoutPanel();
    
    action = new CommandAction("Hello Action!", new Command() {
      public void execute() {
        Window.alert("Hello");
      }
    });
    action.setEnabled(false);
    action.putValue(Action.SHORT_DESCRIPTION, "A short description");
    action.putValue(Action.SMALL_ICON, CommandAction.ACTION_IMAGES.action());

    addMenuBar(layoutPanel);
    vBox.add(new HTML("<u><b>GWT Button</b></u>"), new BoxLayoutData(
        FillStyle.HORIZONTAL));
    addButton(vBox);
    vBox.add(new HTML("<u><b>GWT ToolButton (PUSH)</b></u>"),
        new BoxLayoutData(FillStyle.HORIZONTAL));
    addToolButton1(vBox);
    vBox.add(new HTML("<u><b>GWT ToolButton (CHECKBOX)</b></u>"),
        new BoxLayoutData(FillStyle.HORIZONTAL));
    addToolButton2(vBox);
    vBox.add(new HTML("<u><b>GWT ToolButton (SPLIT)</b></u>"),
        new BoxLayoutData(FillStyle.HORIZONTAL));
    addToolButton3(vBox);
    vBox.add(new HTML("<u><b>GWT ToolButton (MENU)</b></u>"),
        new BoxLayoutData(FillStyle.HORIZONTAL));
    addToolButton4(vBox);
    vBox.add(new HTML("<u><b>GWT CustomButton</b></u>"), new BoxLayoutData(
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
    hBox.setPadding(0);

    final ButtonActionSupport btnActionSupport1 = new ButtonActionSupport(
        action);
    btnActionSupport1.bind();
    btnActionSupport1.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        panel.layout();
      }
    });

    final ButtonActionSupport btnActionSupport2 = new ButtonActionSupport(
        action);
    btnActionSupport2.bind();
    btnActionSupport2.setLabelType(ButtonLabelType.TEXT_ON_BOTTOM);
    btnActionSupport2.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        panel.layout();
      }
    });

    final ButtonActionSupport btnActionSupport3 = new ButtonActionSupport(
        action);
    btnActionSupport3.bind();
    btnActionSupport3.setLabelType(ButtonLabelType.TEXT_ON_LEFT);
    btnActionSupport3.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        panel.layout();
      }
    });

    final ButtonActionSupport btnActionSupport4 = new ButtonActionSupport(
        action);
    btnActionSupport4.bind();
    btnActionSupport4.setLabelType(ButtonLabelType.TEXT_ON_TOP);
    btnActionSupport4.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        panel.layout();
      }
    });

    final ButtonActionSupport btnActionSupport5 = new ButtonActionSupport(
        action);
    btnActionSupport5.bind();
    btnActionSupport5.setLabelType(ButtonLabelType.TEXT_ONLY);
    btnActionSupport5.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        panel.layout();
      }
    });

    final ButtonActionSupport btnActionSupport6 = new ButtonActionSupport(
        action);
    btnActionSupport6.bind();
    btnActionSupport6.setLabelType(ButtonLabelType.NO_TEXT);
    btnActionSupport6.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        panel.layout();
      }
    });

    hBox.add(btnActionSupport1.getTarget());
    hBox.add(btnActionSupport2.getTarget());
    hBox.add(btnActionSupport3.getTarget());
    hBox.add(btnActionSupport4.getTarget());
    hBox.add(btnActionSupport5.getTarget());
    hBox.add(btnActionSupport6.getTarget());

    panel.add(hBox);
  }

  private void addCustomButton(final LayoutPanel panel) {
    final LayoutPanel hBox = new LayoutPanel(new BoxLayout());
    hBox.setPadding(0);

    // final PushButtonActionSupport pushBtnActionSupport1 = new
    // PushButtonActionSupport(
    // action);
    // pushBtnActionSupport1.bind();
    // pushBtnActionSupport1.addPropertyChangeListener(new
    // PropertyChangeListener() {
    // public void propertyChange(PropertyChangeEvent evt) {
    // panel.layout();
    // }
    // });

    final PushButtonActionSupport pushBtnActionSupport2 = new PushButtonActionSupport(
        action, new PushButton(
            MessageBox.MESSAGEBOX_IMAGES.dialogInformation().createImage(),
            MessageBox.MESSAGEBOX_IMAGES.dialogQuestion().createImage()));
    pushBtnActionSupport2.bind();

    // final ToggleButtonActionSupport toggleBtnActionSupport1 = new
    // ToggleButtonActionSupport(
    // action);
    // toggleBtnActionSupport1.bind();
    // toggleBtnActionSupport1.addPropertyChangeListener(new
    // PropertyChangeListener() {
    // public void propertyChange(PropertyChangeEvent evt) {
    // panel.layout();
    // }
    // });

    final ToggleButtonActionSupport toggleBtnActionSupport2 = new ToggleButtonActionSupport(
        action, new ToggleButton(
            MessageBox.MESSAGEBOX_IMAGES.dialogInformation().createImage(),
            MessageBox.MESSAGEBOX_IMAGES.dialogQuestion().createImage()));
    toggleBtnActionSupport2.bind();

    // hBox.add(new WidgetWrapper(pushBtnActionSupport1.getTarget()));
    hBox.add(new WidgetWrapper(pushBtnActionSupport2.getTarget()));
    // hBox.add(new WidgetWrapper(toggleBtnActionSupport1.getTarget()));
    hBox.add(new WidgetWrapper(toggleBtnActionSupport2.getTarget()));

    panel.add(hBox);
  }

  private void addToolButton1(final LayoutPanel panel) {
    final LayoutPanel hBox = new LayoutPanel(new BoxLayout());
    hBox.setPadding(0);

    final ToolButtonActionSupport toolBtnActionSupport1 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport1.bind();

    final ToolButtonActionSupport toolBtnActionSupport2 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport2.bind();
    toolBtnActionSupport2.setLabelType(ButtonLabelType.TEXT_ON_BOTTOM);

    final ToolButtonActionSupport toolBtnActionSupport3 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport3.bind();
    toolBtnActionSupport3.setLabelType(ButtonLabelType.TEXT_ON_LEFT);

    final ToolButtonActionSupport toolBtnActionSupport4 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport4.bind();
    toolBtnActionSupport4.setLabelType(ButtonLabelType.TEXT_ON_TOP);

    final ToolButtonActionSupport toolBtnActionSupport5 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport5.bind();
    toolBtnActionSupport5.setLabelType(ButtonLabelType.TEXT_ONLY);

    final ToolButtonActionSupport toolBtnActionSupport6 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport6.bind();
    toolBtnActionSupport6.setLabelType(ButtonLabelType.NO_TEXT);

    hBox.add(toolBtnActionSupport1.getTarget());
    hBox.add(toolBtnActionSupport2.getTarget());
    hBox.add(toolBtnActionSupport3.getTarget());
    hBox.add(toolBtnActionSupport4.getTarget());
    hBox.add(toolBtnActionSupport5.getTarget());
    hBox.add(toolBtnActionSupport6.getTarget());

    panel.add(hBox);
  }

  private void addToolButton2(final LayoutPanel panel) {
    final LayoutPanel hBox = new LayoutPanel(new BoxLayout());
    hBox.setPadding(0);

    final ToolButtonActionSupport toolBtnActionSupport1 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport1.getTarget().setStyle(ToolButtonStyle.CHECKBOX);
    toolBtnActionSupport1.bind();

    final ToolButtonActionSupport toolBtnActionSupport2 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport2.setLabelType(ButtonLabelType.TEXT_ON_BOTTOM);
    toolBtnActionSupport2.getTarget().setStyle(ToolButtonStyle.CHECKBOX);
    toolBtnActionSupport2.bind();

    final ToolButtonActionSupport toolBtnActionSupport3 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport3.setLabelType(ButtonLabelType.TEXT_ON_LEFT);
    toolBtnActionSupport3.getTarget().setStyle(ToolButtonStyle.CHECKBOX);
    toolBtnActionSupport3.bind();

    final ToolButtonActionSupport toolBtnActionSupport4 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport4.setLabelType(ButtonLabelType.TEXT_ON_TOP);
    toolBtnActionSupport4.getTarget().setStyle(ToolButtonStyle.CHECKBOX);
    toolBtnActionSupport4.bind();

    final ToolButtonActionSupport toolBtnActionSupport5 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport5.setLabelType(ButtonLabelType.TEXT_ONLY);
    toolBtnActionSupport5.getTarget().setStyle(ToolButtonStyle.CHECKBOX);
    toolBtnActionSupport5.bind();

    final ToolButtonActionSupport toolBtnActionSupport6 = new ToolButtonActionSupport(
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

    panel.add(hBox);
  }

  private void addToolButton3(final LayoutPanel panel) {
    final LayoutPanel hBox = new LayoutPanel(new BoxLayout());
    hBox.setPadding(0);

    // Make a command that we will execute from all menu items.
    Command cmd1 = new Command() {
      public void execute() {
        InfoPanel.show("Menu Button", "You selected a menu item!");
      }
    };

    PopupMenu btnMenu = new PopupMenu();
    btnMenu.addItem("Item 1", cmd1);
    btnMenu.addItem("Item 2", cmd1);

    final ToolButtonActionSupport toolBtnActionSupport1 = new ToolButtonActionSupport(
        action);
    ToolButton btn1 = toolBtnActionSupport1.getTarget();
    btn1.setStyle(ToolButtonStyle.SPLIT);
    btn1.setMenu(btnMenu);
    toolBtnActionSupport1.bind();

    final ToolButtonActionSupport toolBtnActionSupport2 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport2.setLabelType(ButtonLabelType.TEXT_ON_BOTTOM);
    ToolButton btn2 = toolBtnActionSupport2.getTarget();
    btn2.setStyle(ToolButtonStyle.SPLIT);
    btn2.setMenu(btnMenu);
    toolBtnActionSupport2.bind();

    final ToolButtonActionSupport toolBtnActionSupport3 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport3.setLabelType(ButtonLabelType.TEXT_ON_LEFT);
    ToolButton btn3 = toolBtnActionSupport3.getTarget();
    btn3.setStyle(ToolButtonStyle.SPLIT);
    btn3.setMenu(btnMenu);
    toolBtnActionSupport3.bind();

    final ToolButtonActionSupport toolBtnActionSupport4 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport4.setLabelType(ButtonLabelType.TEXT_ON_TOP);
    ToolButton btn4 = toolBtnActionSupport4.getTarget();
    btn4.setStyle(ToolButtonStyle.SPLIT);
    btn4.setMenu(btnMenu);
    toolBtnActionSupport4.bind();

    final ToolButtonActionSupport toolBtnActionSupport5 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport5.setLabelType(ButtonLabelType.TEXT_ONLY);
    ToolButton btn5 = toolBtnActionSupport5.getTarget();
    btn5.setStyle(ToolButtonStyle.SPLIT);
    btn5.setMenu(btnMenu);
    toolBtnActionSupport5.bind();

    final ToolButtonActionSupport toolBtnActionSupport6 = new ToolButtonActionSupport(
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

    panel.add(hBox);
  }

  private void addToolButton4(final LayoutPanel panel) {
    final LayoutPanel hBox = new LayoutPanel(new BoxLayout());
    hBox.setPadding(0);

    // Make a command that we will execute from all menu items.
    Command cmd1 = new Command() {
      public void execute() {
        InfoPanel.show("Menu Button", "You selected a menu item!");
      }
    };

    PopupMenu btnMenu = new PopupMenu();
    btnMenu.addItem("Item 1", cmd1);
    btnMenu.addItem("Item 2", cmd1);

    final ToolButtonActionSupport toolBtnActionSupport1 = new ToolButtonActionSupport(
        action);
    ToolButton btn1 = toolBtnActionSupport1.getTarget();
    btn1.setStyle(ToolButtonStyle.MENU);
    btn1.setMenu(btnMenu);
    toolBtnActionSupport1.bind();

    final ToolButtonActionSupport toolBtnActionSupport2 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport2.setLabelType(ButtonLabelType.TEXT_ON_BOTTOM);
    ToolButton btn2 = toolBtnActionSupport2.getTarget();
    btn2.setStyle(ToolButtonStyle.MENU);
    btn2.setMenu(btnMenu);
    toolBtnActionSupport2.bind();

    final ToolButtonActionSupport toolBtnActionSupport3 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport3.setLabelType(ButtonLabelType.TEXT_ON_LEFT);
    ToolButton btn3 = toolBtnActionSupport3.getTarget();
    btn3.setStyle(ToolButtonStyle.MENU);
    btn3.setMenu(btnMenu);
    toolBtnActionSupport3.bind();

    final ToolButtonActionSupport toolBtnActionSupport4 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport4.setLabelType(ButtonLabelType.TEXT_ON_TOP);
    ToolButton btn4 = toolBtnActionSupport4.getTarget();
    btn4.setStyle(ToolButtonStyle.MENU);
    btn4.setMenu(btnMenu);
    toolBtnActionSupport4.bind();

    final ToolButtonActionSupport toolBtnActionSupport5 = new ToolButtonActionSupport(
        action);
    toolBtnActionSupport5.setLabelType(ButtonLabelType.TEXT_ONLY);
    ToolButton btn5 = toolBtnActionSupport5.getTarget();
    btn5.setStyle(ToolButtonStyle.MENU);
    btn5.setMenu(btnMenu);
    toolBtnActionSupport5.bind();

    final ToolButtonActionSupport toolBtnActionSupport6 = new ToolButtonActionSupport(
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

    panel.add(hBox);
  }

  private void addMenuBar(final LayoutPanel panel) {
    // Create a menu bar
    MenuBar menu = new MenuBar();
    menu.setAnimationEnabled(true);

    // Create the 'Action' menu
    MenuBar actionMenu = new MenuBar(true);
    menu.addItem(new MenuItem("Actions", actionMenu));

    final MenuItemActionSupport actionBinding1 = new MenuItemActionSupport(
        action);
    actionMenu.addItem(actionBinding1.getTarget());
    actionBinding1.bind();

    actionMenu.addSeparator();

    final ToggleMenuItemActionSupport actionBinding2 = new ToggleMenuItemActionSupport(
        action);
    actionMenu.addItem(actionBinding2.getTarget());
    actionBinding2.bind();

    // Create the 'Action control' menu
    MenuBar actionCtrlMenu = new MenuBar(true);
    menu.addItem(new MenuItem("Actions Control", actionCtrlMenu));

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

    panel.add(menu, new BoxLayoutData(FillStyle.HORIZONTAL));
  }

}
