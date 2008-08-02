package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.pages.Annotations.MosaicSource;
import org.mosaic.showcase.client.pages.Annotations.MosaicStyle;
import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.BorderLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;

import com.google.gwt.user.client.ui.Button;

/**
 * 
 */
@MosaicStyle({".mosaic-LayoutPanel"})
public class BorderLayoutPage extends Page {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public BorderLayoutPage(DemoConstants constants) {
    super(constants);
  }

  /**
   * 
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BorderLayout());
    
    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");
    final Button b5 = new Button("Button 5");
    
    layoutPanel.add(b1, new BorderLayoutData(BorderLayoutRegion.NORTH, 10, 200));
    layoutPanel.add(b2, new BorderLayoutData(BorderLayoutRegion.SOUTH, 10, 200));
    layoutPanel.add(b3, new BorderLayoutData(BorderLayoutRegion.WEST, 10, 200));
    layoutPanel.add(b4, new BorderLayoutData(BorderLayoutRegion.EAST, 10, 200));
    layoutPanel.add(b5, new BorderLayoutData(BorderLayoutRegion.CENTER, true));
  }
  
}
