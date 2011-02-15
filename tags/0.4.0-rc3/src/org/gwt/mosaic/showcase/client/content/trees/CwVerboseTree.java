/*
 * Copyright 2008 Google Inc.
 * 
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos
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
package org.gwt.mosaic.showcase.client.content.trees;

import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.event.BeforeCloseEvent;
import org.gwt.mosaic.ui.client.event.BeforeCloseHandler;
import org.gwt.mosaic.ui.client.event.BeforeOpenEvent;
import org.gwt.mosaic.ui.client.event.BeforeOpenHandler;
import org.gwt.mosaic.ui.client.tree.FastTree;
import org.gwt.mosaic.ui.client.tree.FastTreeItem;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".gwt-FastTree"})
public class CwVerboseTree extends CwBasicTree {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwVerboseTree(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Verbose GWT Incubator FastTree demo";
  }

  @Override
  public String getName() {
    return "Verbose Tree";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    FastTree tree = new FastTree();
    verboseTreeItem(tree.getTreeRoot(), 10);

    tree.addOpenHandler(new OpenHandler<FastTreeItem>() {
      public void onOpen(OpenEvent<FastTreeItem> event) {
        InfoPanel.show(event.getTarget().getHTML(), "Item "
            + event.getTarget().getHTML() + " is open");
      }
    });

    tree.addCloseHandler(new CloseHandler<FastTreeItem>() {
      public void onClose(CloseEvent<FastTreeItem> event) {
        InfoPanel.show(event.getTarget().getHTML(), "Item"
            + event.getTarget().getHTML() + " is closed");
      }
    });

    tree.addBeforeOpenHandler(new BeforeOpenHandler<FastTreeItem>() {
      public void onBeforeOpen(BeforeOpenEvent<FastTreeItem> event) {
        if (Window.confirm("Would you like to change the name of the item before opening it?")) {
          event.getTarget().setHTML("Name changed before open.");
        }
      }
    });

    tree.addBeforeCloseHandler(new BeforeCloseHandler<FastTreeItem>() {
      public void onBeforeClose(BeforeCloseEvent<FastTreeItem> event) {
        if (Window.confirm("Would you like to change the name of the item before closing it?")) {
          event.getTarget().setHTML("Name changed before close.");
        }
      }
    });

    final ScrollPanel panel = new ScrollPanel();
    panel.add(tree);

    return panel;
  }

  private void verboseTreeItem(FastTreeItem parent, int children) {
    for (int i = 0; i < children; i++) {
      final int index = i;

      FastTreeItem item = new FastTreeItem();
      item.setText("item " + index);
      parent.addItem(item);
      verboseTreeItem(item, children - (i + 1));
    }
  }

}