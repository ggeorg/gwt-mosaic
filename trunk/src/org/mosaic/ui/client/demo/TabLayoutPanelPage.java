package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.mosaic.ui.client.TabLayoutPanel;
import org.mosaic.ui.client.demo.Annotations.MosaicSource;
import org.mosaic.ui.client.demo.Annotations.MosaicStyle;
import org.mosaic.ui.client.layout.LayoutPanel;

/**
 * 
 */
@MosaicStyle({""})
public class TabLayoutPanelPage extends AbstractLayoutPage {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public TabLayoutPanelPage(DemoConstants constants) {
    super(constants);
    // TODO Auto-generated constructor stub
  }

  /**
   * 
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {

    final TabLayoutPanel tabPanel = new DecoratedTabLayoutPanel(true);

    LayoutPanel panel1 = new LayoutPanel();
    populate1(panel1);

    LayoutPanel panel2 = new LayoutPanel();
    populate2(panel2);

    LayoutPanel panel3 = new LayoutPanel();
    populate3(panel3);

    LayoutPanel panel4 = new LayoutPanel();
    populate4(panel4);

    tabPanel.add("BoxLayout", panel1);
    tabPanel.add("BorderLayout", panel2);
    tabPanel.add("Nested BorderLayout", panel3);
    tabPanel.add("Mixed Layout", panel4);

    layoutPanel.add(tabPanel);
    layoutPanel.setPadding(5);
  }

}
