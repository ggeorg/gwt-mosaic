package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.demo.Annotations.MosaicSource;
import org.mosaic.ui.client.demo.Annotations.MosaicStyle;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.tree.FastTree;
import org.mosaic.ui.client.tree.FastTreeItem;
import org.mosaic.ui.client.tree.HasFastTreeItems;

import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * 
 */
@MosaicStyle( {"gwt-FastTree"})
public class LazyTreePage extends BasicTreePage {

  public LazyTreePage(DemoConstants constants) {
    super(constants);
  }

  /**
   * 
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    final FastTree t = new FastTree();
    lazyCreateChild(t, 0, 50);

    ScrollPanel panel = new ScrollPanel();
    layoutPanel.add(panel);
    panel.add(t);
  }

  /**
   * 
   * @param parent
   * @param index
   * @param children
   */
  @MosaicSource
  private void lazyCreateChild(final HasFastTreeItems parent, final int index,
      final int children) {
    FastTreeItem item = new FastTreeItem("child" + index + " (" + children + " children)") {
      public void ensureChildren() {
        for (int i = 0; i < children; i++) {
          lazyCreateChild(this, i, children + (i * 10));
        }
      }
    };
    item.becomeInteriorNode();
    parent.addItem(item);
  }

}
