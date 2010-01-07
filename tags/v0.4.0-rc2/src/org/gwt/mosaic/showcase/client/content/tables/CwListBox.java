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
package org.gwt.mosaic.showcase.client.content.tables;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.showcase.client.content.tables.shared.Person;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.ToolBar;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.InfoPanel.InfoPanelType;
import org.gwt.mosaic.ui.client.ListBox.CellRenderer;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;
import org.gwt.mosaic.ui.client.MessageBox.PromptCallback;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.list.DefaultListModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwListBox extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwListBox(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "ListBox description.";
  }

  @Override
  public String getName() {
    return "ListBox";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final DecoratedTabLayoutPanel tabPanel = new DecoratedTabLayoutPanel();
    tabPanel.setPadding(0);

    tabPanel.add(createDemo1(), "Simple ListBox");
    tabPanel.add(createDemo2(), "Multi Column ListBox");

    return tabPanel;
  }

  /**
   * 
   * @return
   */
  @ShowcaseSource
  private PopupMenu createContextMenu() {
    Command cmd = new Command() {
      public void execute() {
        InfoPanel.show("Menu Button", "You selected a menu item!");
      }
    };

    PopupMenu contextMenu = new PopupMenu();

    contextMenu.addItem("MenuItem 1", cmd);
    contextMenu.addItem("MenuItem 2", cmd);

    contextMenu.addSeparator();

    contextMenu.addItem("MenuItem 3", cmd);
    contextMenu.addItem("MenuItem 4", cmd);

    return contextMenu;
  }

  /**
   * 
   * @return
   */
  @ShowcaseSource
  public Widget createDemo1() {
    final LayoutPanel vBox = new LayoutPanel(
        new BoxLayout(Orientation.VERTICAL));
    vBox.setPadding(0);
    vBox.setWidgetSpacing(0);

    final ListBox<String> listBox = new ListBox<String>();
    listBox.setContextMenu(createContextMenu());

    final DefaultListModel<String> model = (DefaultListModel<String>) listBox.getModel();
    model.add("foo");
    model.add("bar");
    model.add("baz");
    model.add("toto");
    model.add("tintin");

    listBox.addRowSelectionHandler(new RowSelectionHandler() {
      public void onRowSelection(RowSelectionEvent event) {
        int index = listBox.getSelectedIndex();
        if (index != -1) {
          InfoPanel.show("RowSelectionHandler", listBox.getItem(index));
        }
      }
    });

    listBox.addDoubleClickHandler(new DoubleClickHandler() {
      public void onDoubleClick(DoubleClickEvent event) {
        InfoPanel.show(InfoPanelType.HUMANIZED_MESSAGE, "DoubleClickListener",
            listBox.getItem(listBox.getSelectedIndex()));
      }
    });

    final ToolBar toolBar = new ToolBar();
    toolBar.add(new ToolButton("Insert", new ClickHandler() {
      public void onClick(ClickEvent event) {
        MessageBox.prompt("ListBox Insert", "Please enter a new value to add",
            null, new PromptCallback<String>() {
              public void onResult(String input) {
                if (input != null) {
                  final int index = listBox.getSelectedIndex();
                  if (index == -1) {
                    model.add(input);
                  } else {
                    model.add(index, input);
                  }
                }
              }
            });
      }
    }));
    toolBar.add(new ToolButton("Remove", new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (listBox.getSelectedIndex() == -1) {
          MessageBox.alert("ListBox Edit", "No item selected");
          return;
        }
        String item = listBox.getItem(listBox.getSelectedIndex());
        MessageBox.confirm("ListBox Remove",
            "Are you sure you want to permanently delete '" + item
                + "' from the list?", new ConfirmationCallback() {
              public void onResult(boolean result) {
                if (result) {
                  model.remove(listBox.getSelectedIndex());
                }
              }
            });
      };
    }));
    toolBar.add(new ToolButton("Edit", new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (listBox.getSelectedIndex() == -1) {
          MessageBox.alert("ListBox Edit", "No item selected");
          return;
        }
        String item = listBox.getItem(listBox.getSelectedIndex());
        MessageBox.prompt("ListBox Edit", "Please enter a new value for '"
            + item + "'", item, new PromptCallback<String>() {
          public void onResult(String input) {
            if (input != null) {
              final int index = listBox.getSelectedIndex();
              model.set(index, input);
            }
          }
        });
      }
    }));

    vBox.add(toolBar, new BoxLayoutData(FillStyle.HORIZONTAL));
    vBox.add(listBox, new BoxLayoutData(FillStyle.BOTH));

    return vBox;
  }

  /**
   * 
   * @return
   */
  @ShowcaseSource
  public Widget createDemo2() {
    final LayoutPanel vBox = new LayoutPanel(
        new BoxLayout(Orientation.VERTICAL));
    vBox.setPadding(0);
    vBox.setWidgetSpacing(0);

    final ListBox<Person> listBox = new ListBox<Person>(new String[] {
        "Name", "Gender", "Married"});
    listBox.setContextMenu(createContextMenu());
    listBox.setCellRenderer(new CellRenderer<Person>() {
      public void renderCell(ListBox<Person> listBox, int row, int column,
          Person item) {
        switch (column) {
          case 0:
            listBox.setText(row, column, item.getName());
            break;
          case 1:
            listBox.setText(row, column, item.getGender());
            break;
          case 2:
            listBox.setText(row, column, String.valueOf(item.isMarried()));
            break;
          default:
            throw new RuntimeException("Should not happen");
        }
      }
    });

    final DefaultListModel<Person> model = (DefaultListModel<Person>) listBox.getModel();
    model.add(new Person("Rainer Zufall", "male", true));
    model.add(new Person("Marie Darms", "female", false));
    model.add(new Person("Holger Adams", "male", true));
    model.add(new Person("Juliane Adams", "female", true));

    listBox.addRowSelectionHandler(new RowSelectionHandler() {
      public void onRowSelection(RowSelectionEvent event) {
        int index = listBox.getSelectedIndex();
        if (index != -1) {
          InfoPanel.show("RowSelectionHandler", listBox.getItem(
              listBox.getSelectedIndex()).getName());
        }
      }
    });

    listBox.addDoubleClickHandler(new DoubleClickHandler() {
      public void onDoubleClick(DoubleClickEvent event) {
        InfoPanel.show(InfoPanelType.HUMANIZED_MESSAGE, "DoubleClickListener",
            listBox.getItem(listBox.getSelectedIndex()).getName());
      }
    });

    final ToolBar toolBar = new ToolBar();
    toolBar.add(new ToolButton("Insert", new ClickHandler() {
      public void onClick(ClickEvent event) {
        MessageBox.prompt("ListBox Insert",
            "Please enter a new value to add, should be a 'Married Male'",
            null, new PromptCallback<String>() {
              public void onResult(String input) {
                if (input != null) {
                  final int index = listBox.getSelectedIndex();
                  if (index == -1) {
                    model.add(new Person(input, "male", true));
                  } else {
                    model.add(index, new Person(input, "male", true));
                  }
                }
              }
            });
      }
    }));
    toolBar.add(new ToolButton("Remove", new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (listBox.getSelectedIndex() == -1) {
          MessageBox.alert("ListBox Edit", "No item selected");
          return;
        }
        Person item = listBox.getItem(listBox.getSelectedIndex());
        MessageBox.confirm("ListBox Remove",
            "Are you sure you want to permanently delete '" + item.getName()
                + "' from the list?", new ConfirmationCallback() {
              public void onResult(boolean result) {
                if (result) {
                  model.remove(listBox.getSelectedIndex());
                }
              }
            });
      };
    }));
    toolBar.add(new ToolButton("Edit", new ClickHandler() {
      public void onClick(ClickEvent event) {
        if (listBox.getSelectedIndex() == -1) {
          MessageBox.alert("ListBox Edit", "No item selected");
          return;
        }
        final Person item = listBox.getItem(listBox.getSelectedIndex());
        MessageBox.prompt("ListBox Edit", "Please enter a new name for '"
            + item.getName() + "'", item.getName(),
            new PromptCallback<String>() {
              public void onResult(String input) {
                if (input != null) {
                  final int index = listBox.getSelectedIndex();
                  item.setName(input);
                  model.set(index, item);
                }
              }
            });
      }
    }));

    vBox.add(toolBar, new BoxLayoutData(FillStyle.HORIZONTAL));
    vBox.add(listBox, new BoxLayoutData(FillStyle.BOTH));

    return vBox;
  }

}
