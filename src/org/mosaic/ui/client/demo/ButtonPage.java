package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.demo.Annotations.MosaicSource;
import org.mosaic.ui.client.demo.Annotations.MosaicStyle;
import org.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

@MosaicStyle(".gwt-Button")
public class ButtonPage extends Page {

  /**
   * 
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    FlowPanel flowPanel = new FlowPanel();
    
    flowPanel.add(new Button("Button 1"));
    flowPanel.add(new Button("Button 2"));
    flowPanel.add(new Button("Button 3"));
    
    layoutPanel.add(flowPanel);
  }

}
