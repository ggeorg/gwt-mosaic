/*
 * Copyright 2008 Google Inc.
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
package org.gwt.mosaic.showcase.client.content.trees;

import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.FastTree;
import com.google.gwt.widgetideas.client.FastTreeItem;
import com.google.gwt.widgetideas.client.HasFastTreeItems;
import com.google.gwt.widgetideas.client.ListeningFastTreeItem;

/**
 * Example file.
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
    return "Lazy FastTree description";
  }

  @Override
  public String getName() {
    return "Lazy FastTree";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel();
    
    FastTree tree = new FastTree();
    verboseTreeItem(tree, 10);

    final ScrollPanel panel = new ScrollPanel();
    layoutPanel.add(panel);
    panel.add(tree);
    
    return layoutPanel;
  }
  
  private void verboseTreeItem(HasFastTreeItems parent, int children) {
    for (int i = 0; i < children; i++) {
      final int index = i;

      FastTreeItem item = new ListeningFastTreeItem("child " + i) {

        public void beforeClose() {
          InfoPanel.show(this.getText(), "Close item" + index);
        }

        public void beforeOpen() {
          InfoPanel.show(this.getText(), "Open item " + index);
        }

        protected boolean beforeSelectionLost() {
          return Window.confirm("Are you sure you want to leave me?");
        }

        protected void ensureChildren() {
          InfoPanel.show(this.getText(), "You are about to open my children for the first time");
        }

        protected void onSelected() {
          InfoPanel.show(this.getText(), "You selected item " + index);
        }
      };
      parent.addItem(item);
      verboseTreeItem(item, children - (i + 1));
    }
  }

}
