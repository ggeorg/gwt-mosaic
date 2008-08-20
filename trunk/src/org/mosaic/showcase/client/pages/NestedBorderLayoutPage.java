package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.Page;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.BorderLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;

import com.google.gwt.user.client.ui.Button;

/**
 * 
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class NestedBorderLayoutPage extends Page {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public NestedBorderLayoutPage(DemoConstants constants) {
    super(constants);
    // TODO Auto-generated constructor stub
  }

  /**
   * 
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BorderLayout());

    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");

    final LayoutPanel layoutPanel1 = new LayoutPanel(new BorderLayout());

    layoutPanel.add(b1, new BorderLayoutData(BorderLayoutRegion.NORTH, 10, 200));
    layoutPanel.add(b2, new BorderLayoutData(BorderLayoutRegion.SOUTH, 10, 200));
    layoutPanel.add(b3, new BorderLayoutData(BorderLayoutRegion.WEST, 10, 200));
    layoutPanel.add(b4, new BorderLayoutData(BorderLayoutRegion.EAST, 10, 200));
    layoutPanel.add(layoutPanel1, new BorderLayoutData(BorderLayoutRegion.CENTER, true));

    final Button b11 = new Button("Button 11");
    final Button b12 = new Button("Button 12");
    final Button b13 = new Button("Button 13");
    final Button b14 = new Button("Button 14");
    final Button b15 = new Button("Button 15");

    layoutPanel1.add(b11, new BorderLayoutData(BorderLayoutRegion.NORTH));
    layoutPanel1.add(b12, new BorderLayoutData(BorderLayoutRegion.SOUTH));
    layoutPanel1.add(b13, new BorderLayoutData(BorderLayoutRegion.WEST));
    layoutPanel1.add(b14, new BorderLayoutData(BorderLayoutRegion.EAST));
    layoutPanel1.add(b15, new BorderLayoutData(BorderLayoutRegion.CENTER));
  }

  @Override
  public String getName() {
    return "Nested BorderLayout";
  }

}
