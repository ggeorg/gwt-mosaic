/*
 * Copyright 2008 Georgios J. Georgopoulos
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
import org.gwt.mosaic.actions.client.ActionListener;
import org.gwt.mosaic.actions.client.ActionRegistry;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.Showcase;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ToolBar;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.MessageBox.PromptCallback;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file;
 */
public class CwActions extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwActions(CwConstants constants) {
    super(constants);

    actionListener = new ActionListener() {
      public void handleAction(Action action) {
	  Showcase.notifyTrayEvent("Action", action.getId());
      }
    };
  }
  
  @Override
  public String getDescription() {
    return "Actions description";
  }

  @Override
  public String getName() {
    return "Actions";
  }

  /**
   * An action listener.
   */
  @ShowcaseData
  private final ActionListener actionListener;

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
    
    // Action registration
    ActionRegistry.register(new Action("action-1", "Action 1 Text"));
    ActionRegistry.register(new Action("action-2", "Action 2 Text"));

    // Add an action listener to each action
    ActionRegistry.get("action-1").addActionListener(actionListener);
    ActionRegistry.get("action-2").addActionListener(actionListener);

    // Create the UI
    final LayoutPanel toolBox = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
    toolBox.setPadding(0);
    toolBox.setWidgetSpacing(0);
    toolBox.add(createMenuBar(), new BoxLayoutData(FillStyle.HORIZONTAL));
    toolBox.add(createToolBar(), new BoxLayoutData(FillStyle.HORIZONTAL));

    layoutPanel.add(toolBox, new BoxLayoutData(FillStyle.BOTH, true));

    HorizontalPanel hPanel = new HorizontalPanel();
    hPanel.setSpacing(5);
    hPanel.add(new Button("Action-1 Change Text", new ClickListener() {
      public void onClick(Widget sender) {
        final Action action = ActionRegistry.get("action-1");
        MessageBox.prompt(Window.getTitle(), "New Text:", action.getText(),
            new PromptCallback<String>() {
              public void onResult(String input) {
                if (input != null) {
                  action.setText(input);
                  toolBox.layout();
                }
              }
            });
      }
    }));
    hPanel.add(new Button("Action-2 Change Text", new ClickListener() {
      public void onClick(Widget sender) {
        final Action action = ActionRegistry.get("action-2");
        MessageBox.prompt(Window.getTitle(), "New Text:", action.getText(),
            new PromptCallback<String>() {
              public void onResult(String input) {
                if (input != null) {
                  action.setText(input);
                  toolBox.layout();
                }
              }
            });
      }
    }));
    
    layoutPanel.add(hPanel);
    
    return layoutPanel;
  }

  /**
   * Create the menu bar.
   */
  @ShowcaseSource
  private MenuBar createMenuBar() {
    // Create a menu bar
    MenuBar menu = new MenuBar();
    menu.setAnimationEnabled(true);

    // Create the 'Action' menu
    MenuBar helpMenu = new MenuBar(true);
    menu.addItem(new MenuItem("Actions", helpMenu));

    helpMenu.addItem((MenuItem) ActionRegistry.get("action-1").createWidget(
        MenuItem.class));

    helpMenu.addItem((MenuItem) ActionRegistry.get("action-2").createWidget(
        MenuItem.class));

    // Return the menu
    menu.ensureDebugId("mosaicMenuBar");
    return menu;
  }

  /**
   * Create the tool bar.
   */
  @ShowcaseSource
  private Widget createToolBar() {
    final ToolBar toolBar = new ToolBar();

    toolBar.add((ToolButton) ActionRegistry.get("action-1").createWidget(
        ToolButton.class));

    toolBar.add((ToolButton) ActionRegistry.get("action-2").createWidget(
        ToolButton.class));
    
    // Return the menu
    toolBar.ensureDebugId("mosaicToolBar");
    return toolBar;
  }
  
  @Override
  public boolean hasStyle() {
    return false;
  }

}
