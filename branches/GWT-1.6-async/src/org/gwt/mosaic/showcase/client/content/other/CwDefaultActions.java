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

import org.gwt.mosaic.actions.client.MenuItemBindings;
import org.gwt.mosaic.actions.client.application.ExitAction;
import org.gwt.mosaic.actions.client.application.ExitAction.ExitCommand;
import org.gwt.mosaic.actions.client.edit.CopyAction;
import org.gwt.mosaic.actions.client.edit.CutAction;
import org.gwt.mosaic.actions.client.edit.DeleteSelectedAction;
import org.gwt.mosaic.actions.client.edit.DeselectAllAction;
import org.gwt.mosaic.actions.client.edit.PasteAction;
import org.gwt.mosaic.actions.client.edit.SelectAllAction;
import org.gwt.mosaic.actions.client.edit.CopyAction.CopyCommand;
import org.gwt.mosaic.actions.client.edit.CutAction.CutCommand;
import org.gwt.mosaic.actions.client.edit.DeleteSelectedAction.DeleteSelectedCommand;
import org.gwt.mosaic.actions.client.edit.DeselectAllAction.DeselectAllCommand;
import org.gwt.mosaic.actions.client.edit.PasteAction.PasteCommand;
import org.gwt.mosaic.actions.client.edit.SelectAllAction.SelectAllCommand;
import org.gwt.mosaic.actions.client.file.CloseAction;
import org.gwt.mosaic.actions.client.file.CloseAllAction;
import org.gwt.mosaic.actions.client.file.OpenAction;
import org.gwt.mosaic.actions.client.file.CloseAction.CloseCommand;
import org.gwt.mosaic.actions.client.file.CloseAllAction.CloseAllCommand;
import org.gwt.mosaic.actions.client.file.OpenAction.OpenCommand;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwDefaultActions extends ContentWidget {

  /**
   * 
   */
  @ShowcaseData
  private CutAction cut = new CutAction(new CutCommand() {
    public void execute() {
      InfoPanel.show("CutCommand", "Cut ...");
    }
  });
  /**
   * 
   */
  @ShowcaseData
  private CopyAction copy = new CopyAction(new CopyCommand() {
    public void execute() {
      InfoPanel.show("CopyCommand", "Copy ...");
    }
  });
  /**
   * 
   */
  @ShowcaseData
  private PasteAction paste = new PasteAction(new PasteCommand() {
    public void execute() {
      InfoPanel.show("PasteCommand", "Paste ...");
    }
  });
  /**
   * 
   */
  @ShowcaseData
  private DeleteSelectedAction deleteSelected = new DeleteSelectedAction(
      new DeleteSelectedCommand() {
        public void execute() {
          InfoPanel.show("DeleteSelectedCommand", "Delete selected ...");
        }
      });
  /**
   * 
   */
  @ShowcaseData
  private DeselectAllAction deselectAll = new DeselectAllAction(
      new DeselectAllCommand() {
        public void execute() {
          InfoPanel.show("DeselectAllCommand", "Deselect selected ...");
        }
      });
  /**
   * 
   */
  @ShowcaseData
  private SelectAllAction selectAll = new SelectAllAction(
      new SelectAllCommand() {
        public void execute() {
          InfoPanel.show("SelectAllCommand", "All all ...");
        }
      });

  /**
   * 
   */
  @ShowcaseData
  private OpenAction open = new OpenAction(new OpenCommand() {
    public void execute() {
      InfoPanel.show("OpenCommand", "Open ...");
    }
  });
  /**
   * 
   */
  @ShowcaseData
  private CloseAction close = new CloseAction(new CloseCommand() {
    public void execute() {
      InfoPanel.show("CloseCommand", "Close ...");
    }
  });
  /**
   * 
   */
  @ShowcaseData
  private CloseAllAction closeAll = new CloseAllAction(new CloseAllCommand() {
    public void execute() {
      InfoPanel.show("CloseAllCommand", "Close all ...");
    }
  });
  /**
   * 
   */
  @ShowcaseData
  private ExitAction exit = new ExitAction(new ExitCommand() {
    public void execute() {
      InfoPanel.show("ExitCommand", "Exit ...");
    }
  });

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwDefaultActions(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Default Actions demo";
  }

  @Override
  public String getName() {
    return "Actions III";
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

    MenuBar m = new MenuBar();
    MenuBar file = new MenuBar(true);
    MenuBar edit = new MenuBar(true);
    m.addItem("File", file);
    m.addItem("Edit", edit);

    MenuItemBindings miCut = new MenuItemBindings(cut);
    MenuItemBindings miCopy = new MenuItemBindings(copy);
    MenuItemBindings miPaste = new MenuItemBindings(paste);
    MenuItemBindings miSelectAll = new MenuItemBindings(selectAll);
    MenuItemBindings miDeleteSelected = new MenuItemBindings(deleteSelected);
    MenuItemBindings miDeselectAll = new MenuItemBindings(deselectAll);

    miCut.bind();
    miCopy.bind();
    miPaste.bind();
    miSelectAll.bind();
    miDeleteSelected.bind();
    miDeselectAll.bind();

    edit.addItem(miSelectAll.getTarget());
    edit.addItem(miDeleteSelected.getTarget());
    edit.addSeparator();
    edit.addItem(miCut.getTarget());
    edit.addItem(miCopy.getTarget());
    edit.addItem(miPaste.getTarget());
    edit.addSeparator();
    edit.addItem(miDeselectAll.getTarget());

    MenuItemBindings miOpen = new MenuItemBindings(open);
    MenuItemBindings miClose = new MenuItemBindings(close);
    MenuItemBindings miCloseAll = new MenuItemBindings(closeAll);
    MenuItemBindings miExit = new MenuItemBindings(exit);

    miOpen.bind();
    miClose.bind();
    miCloseAll.bind();
    miExit.bind();

    file.addItem(miOpen.getTarget());
    file.addSeparator();
    file.addItem(miClose.getTarget());
    file.addItem(miCloseAll.getTarget());
    file.addSeparator();
    file.addItem(miExit.getTarget());

    layoutPanel.add(m, new BoxLayoutData(FillStyle.HORIZONTAL));

    return layoutPanel;
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
