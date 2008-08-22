package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.mosaic.ui.client.TabLayoutPanel;
import org.mosaic.ui.client.layout.LayoutPanel;

/**
 * 
 */
@ShowcaseStyle({""})
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
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    final TabLayoutPanel tabPanel = new DecoratedTabLayoutPanel(true);
    tabPanel.setPadding(5);

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
  }

  @Override
  public String getName() {
    return "TabLayoutPanel";
  }

}
