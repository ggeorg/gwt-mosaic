package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.InfoPanel;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.tree.FastTree;
import org.mosaic.ui.client.tree.FastTreeItem;
import org.mosaic.ui.client.tree.HasFastTreeItems;
import org.mosaic.ui.client.tree.ListeningFastTreeItem;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;

public class VerboseTreePage extends BasicTreePage {

  public VerboseTreePage(DemoConstants constants) {
    super(constants);
  }

  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    FastTree tree = new FastTree();
    verboseTreeItem(tree, 10);

    ScrollPanel panel = new ScrollPanel();
    layoutPanel.add(panel);
    panel.add(tree);
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
