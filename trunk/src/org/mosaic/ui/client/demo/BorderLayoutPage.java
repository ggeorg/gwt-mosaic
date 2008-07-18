package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.demo.Annotations.MosaicSource;
import org.mosaic.ui.client.demo.Annotations.MosaicStyle;
import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.BorderLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;

import com.google.gwt.user.client.ui.Button;

/**
 * 
 */
@MosaicStyle({".mosaix-LayoutPanel"})
public class BorderLayoutPage extends Page {

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
    
    layoutPanel.add(b1, new BorderLayoutData(BorderLayoutRegion.NORTH));
    layoutPanel.add(b2, new BorderLayoutData(BorderLayoutRegion.SOUTH));
    layoutPanel.add(b3, new BorderLayoutData(BorderLayoutRegion.WEST));
    layoutPanel.add(b4, new BorderLayoutData(BorderLayoutRegion.EAST));
    layoutPanel.add(b5, new BorderLayoutData(BorderLayoutRegion.CENTER, true));
  }
  
}
