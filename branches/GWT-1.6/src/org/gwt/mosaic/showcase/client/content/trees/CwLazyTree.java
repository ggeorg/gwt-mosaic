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
package org.gwt.mosaic.showcase.client.content.trees;

import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.FastTree;
import com.google.gwt.widgetideas.client.FastTreeItem;
import com.google.gwt.widgetideas.client.HasFastTreeItems;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {"gwt-FastTree"})
public class CwLazyTree extends CwBasicTree {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwLazyTree(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Lazy GWT Incubator FastTree demo";
  }

  @Override
  public String getName() {
    return "Lazy Tree";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel();

    final FastTree t = new FastTree();
    lazyCreateChild(t, 0, 50);

    final ScrollPanel panel = new ScrollPanel();
    layoutPanel.add(panel);
    panel.add(t);

    return layoutPanel;
  }

  /**
   * 
   * @param parent
   * @param index
   * @param children
   */
  @ShowcaseSource
  private void lazyCreateChild(final HasFastTreeItems parent, final int index,
      final int children) {
    final FastTreeItem item = new FastTreeItem("child" + index + " ("
        + children + " children)") {
      
      private Timer t = new Timer() {
        public void run() {
          lazyCreateChilds();
        }
      };

      private void lazyCreateChilds() {
        try {
          for (int i = 0; i < children; i++) {
            lazyCreateChild(this, i, children + (i * 10));
          }
        } finally {
          removeStyleName("gwt-FastTreeItem-loading");
        }
      }

      public void ensureChildren() {
        addStyleName("gwt-FastTreeItem-loading");
        t.schedule(3333);
      }
    };
    item.becomeInteriorNode();
    parent.addItem(item);
  }

}
