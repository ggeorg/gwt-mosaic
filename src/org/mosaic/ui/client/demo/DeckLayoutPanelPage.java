package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.DeckLayoutPanel;
import org.mosaic.ui.client.demo.Annotations.MosaicSource;
import org.mosaic.ui.client.demo.Annotations.MosaicStyle;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@MosaicStyle({".mosaic-DockLayoutPanel", ".mosaic-LayoutPanel"})
public class DeckLayoutPanelPage extends AbstractLayoutPage {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public DeckLayoutPanelPage(DemoConstants constants) {
    super(constants);
  }

  /**
   * <code>DeckLayoutPanel</code> example code.
   */
  @MosaicSource
  @Override
  protected void onPageLoad(final LayoutPanel layoutPanel) {
    final BoxLayout boxLayout = new BoxLayout(Orientation.VERTICAL);
    boxLayout.setSpacing(5);
    layoutPanel.setLayout(boxLayout);

    // Create a DeckLayoutPanel
    final DeckLayoutPanel deck = new DeckLayoutPanel();
    // deck.setSpacing(4);
    // deck.setMargin(5);

    final LayoutPanel panel1 = new LayoutPanel();
    populate1(panel1);
    deck.add(panel1);

    final LayoutPanel panel2 = new LayoutPanel();
    populate2(panel2);
    deck.add(panel2);

    final LayoutPanel panel3 = new LayoutPanel();
    populate3(panel3);
    deck.add(panel3);

    final LayoutPanel panel4 = new LayoutPanel();
    populate4(panel4);
    deck.add(panel4);

    // Add a drop box with the list types
    final ListBox dropBox = new ListBox(false);
    dropBox.addItem("BoxLayout");
    dropBox.addItem("BorderLayout");
    dropBox.addItem("Nested BorderLayout");
    dropBox.addItem("Mixed Layout");
    dropBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        deck.showWidget(dropBox.getSelectedIndex());
        deck.layout();
      }
    });

    layoutPanel.add(dropBox, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(deck, new BoxLayoutData(FillStyle.BOTH, true));

    deck.showWidget(dropBox.getSelectedIndex());
  }

}
