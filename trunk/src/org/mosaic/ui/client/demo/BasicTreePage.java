package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.demo.Annotations.MosaicSource;
import org.mosaic.ui.client.demo.Annotations.MosaicStyle;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.tree.FastTree;
import org.mosaic.ui.client.tree.FastTreeItem;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 */
@MosaicStyle({".gwt-FastTree"})
public class BasicTreePage extends Page {

  public BasicTreePage(DemoConstants constants) {
    super(constants);
    FastTree.addDefaultCSS();
  }

  /**
   * 
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {    
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
    
    ScrollPanel panel = new ScrollPanel();
    layoutPanel.add(panel);
    layoutPanel.setPadding(0);
    panel.add(t);
  }

}
