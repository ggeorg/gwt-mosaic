package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.TabLayoutPanel;
import org.mosaic.ui.client.TabLayoutPanel.TabBarPosition;
import org.mosaic.ui.client.demo.Annotations.MosaicSource;
import org.mosaic.ui.client.demo.Annotations.MosaicStyle;
import org.mosaic.ui.client.layout.LayoutPanel;

/**
 * 
 */
@MosaicStyle({".mosaic-LayoutPanel"})
public class BottomTabBarsPage extends AbstractLayoutPage {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public BottomTabBarsPage(DemoConstants constants) {
    super(constants);
  }

  /**
   * 
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    final TabLayoutPanel tabPanel = new TabLayoutPanel(TabBarPosition.BOTTOM, true);

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
