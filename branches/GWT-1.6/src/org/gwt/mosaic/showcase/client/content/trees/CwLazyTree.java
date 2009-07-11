/*
 * Copyright 2008 Google Inc.
 * 
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
package org.gwt.mosaic.showcase.client.content.trees;

import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;

import com.google.gwt.gen2.commonevent.shared.BeforeOpenEvent;
import com.google.gwt.gen2.commonevent.shared.BeforeOpenHandler;
import com.google.gwt.gen2.complexpanel.client.FastTree;
import com.google.gwt.gen2.complexpanel.client.FastTreeItem;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

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
    final FastTree t = new FastTree();
    lazyCreateChild(t.getTreeRoot(), 0, 50);

    t.addBeforeOpenHandler(new BeforeOpenHandler<FastTreeItem>() {
      FastTreeItem item;

      private Timer t = new Timer() {
        public void run() {
          lazyCreateChilds();
        }
      };

      private void lazyCreateChilds() {
        try {
          for (int i = 0; i < 50; i++) {
            lazyCreateChild(item, i, 50 + (i * 10));
          }
        } finally {
          item.removeStyleName("gwt-FastTreeItem-loading");
        }
      }

      public void onBeforeOpen(BeforeOpenEvent<FastTreeItem> event) {
        item = (FastTreeItem) event.getTarget();
        if (event.isFirstTime()) {
          item.addStyleName("gwt-FastTreeItem-loading");
          t.schedule(333);
        }
      }
    });

    final ScrollPanel panel = new ScrollPanel();
    panel.add(t);

    return panel;
  }

  /**
   * 
   * @param parent
   * @param index
   * @param children
   */
  @ShowcaseSource
  private void lazyCreateChild(final FastTreeItem parent, final int index,
      final int children) {
    final FastTreeItem item = new FastTreeItem("child" + index + " ("
        + children + " children)");
    item.becomeInteriorNode();
    parent.addItem(item);
  }

}
