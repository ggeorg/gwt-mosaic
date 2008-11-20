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

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.FastTree;
import com.google.gwt.widgetideas.client.FastTreeItem;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle({".gwt-FastTree"})
public class CwBasicTree extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwBasicTree(CwConstants constants) {
    super(constants);
  }
  
  @Override
  public String getDescription() {
    return "Basic GWT Incubator FastTree demo";
  }

  @Override
  public String getName() {
    return "Basic Tree";
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
    final FastTreeItem a = t.addItem("A root tree item");
    a.addItem("A child");
    final FastTreeItem aXb = a.addItem("Another child");
    aXb.addItem("a grand child");
    final FastTreeItem widgetBranch = a.addItem(new CheckBox("A checkbox child"));
    final FastTreeItem textBoxParent = widgetBranch.addItem("A TextBox parent");
    textBoxParent.addItem(new TextBox());
    textBoxParent.addItem("and another one...");
    textBoxParent.addItem(new TextArea());
    
    final ListBox lb = new ListBox();
    for (int i = 0; i < 100; i++) {
      lb.addItem(i + "");
    }
    widgetBranch.addItem("A ListBox parent").addItem(lb);
    
    final ScrollPanel panel = new ScrollPanel();
    layoutPanel.add(panel);
    layoutPanel.setPadding(0);
    panel.add(t);
    
    return layoutPanel;
  }

}
