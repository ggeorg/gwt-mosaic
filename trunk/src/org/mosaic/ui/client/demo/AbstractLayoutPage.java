package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.demo.Annotations.MosaicSource;
import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.BorderLayoutData;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Button;

public abstract class AbstractLayoutPage extends Page {

  /**
   * 
   */
  @MosaicSource
  protected void populate1(LayoutPanel layoutPanel) {
    BoxLayout boxLayout = new BoxLayout(Orientation.VERTICAL);
    boxLayout.setSpacing(5);
    layoutPanel.setLayout(boxLayout);

    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");

    BoxLayout boxLayout1 = new BoxLayout(); // default is horizontal
    boxLayout1.setSpacing(2);
    final LayoutPanel layoutPanel1 = new LayoutPanel(boxLayout1);

    final Button b11 = new Button("Button 11");
    final Button b12 = new Button("Button 12");
    final Button b13 = new Button("Button 13");
    final Button b14 = new Button("Button 14");

    final BoxLayoutData blData = new BoxLayoutData(FillStyle.VERTICAL);
    blData.setWidth(100);
    layoutPanel1.add(b11, blData);
    layoutPanel1.add(b12, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel1.add(b13);
    layoutPanel1.add(b14, new BoxLayoutData(FillStyle.BOTH));

    layoutPanel.add(b1, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(layoutPanel1, new BoxLayoutData(FillStyle.BOTH, true));
    layoutPanel.add(b2, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(b3);
    layoutPanel.add(b4, new BoxLayoutData(FillStyle.HORIZONTAL));
  }
  
  /**
   * 
   */
  @MosaicSource
  protected void populate2(LayoutPanel layoutPanel) {
    final BoxLayout boxLayout = new BoxLayout(Orientation.VERTICAL);
    boxLayout.setSpacing(5);
    layoutPanel.setLayout(boxLayout);
    
    final BoxLayoutData boxLayoutData1 = new BoxLayoutData(FillStyle.VERTICAL);

    final Button b11 = new Button("Button 11");
    final Button b12 = new Button("Button 12");
    final Button b13 = new Button("Button 13");
    final Button b14 = new Button("Button 14");

    BoxLayout boxLayout1 = new BoxLayout(); // default is horizontal
    boxLayout1.setSpacing(2);
    final LayoutPanel layoutPanel1 = new LayoutPanel(boxLayout1);

    layoutPanel1.add(b11, boxLayoutData1);
    layoutPanel1.add(b12, boxLayoutData1);
    layoutPanel1.add(b13, boxLayoutData1);
    layoutPanel1.add(b14, boxLayoutData1);

    final Button b21 = new Button("Button 21");
    final Button b22 = new Button("Button 22");
    final Button b23 = new Button("Button 23");
    final Button b24 = new Button("Button 24");

    BoxLayout boxLayout2 = new BoxLayout(); // default is horizontal
    boxLayout2.setSpacing(2);
    boxLayout2.setLeftToRight(false);
    final LayoutPanel layoutPanel2 = new LayoutPanel(boxLayout2);

    layoutPanel2.add(b21, boxLayoutData1);
    layoutPanel2.add(b22, boxLayoutData1);
    layoutPanel2.add(b23, boxLayoutData1);
    layoutPanel2.add(b24, boxLayoutData1);

    layoutPanel.add(layoutPanel1, new BoxLayoutData(FillStyle.BOTH, true));
    layoutPanel.add(layoutPanel2, new BoxLayoutData(FillStyle.BOTH, true));
  }

  /**
   * 
   */
  @MosaicSource
  protected void populate3(LayoutPanel layoutPanel) {
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

  /**
   * 
   */
  @MosaicSource
  protected void populate4(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BorderLayout());

    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");

    final LayoutPanel layoutPanel1 = new LayoutPanel(new BorderLayout());

    layoutPanel.add(b1, new BorderLayoutData(BorderLayoutRegion.NORTH));
    layoutPanel.add(b2, new BorderLayoutData(BorderLayoutRegion.SOUTH));
    layoutPanel.add(b3, new BorderLayoutData(BorderLayoutRegion.WEST));
    layoutPanel.add(b4, new BorderLayoutData(BorderLayoutRegion.EAST));
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

  /**
   * 
   */
  @MosaicSource
  protected void populate5(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BorderLayout());

    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");

    final LayoutPanel layoutPanel1 = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));

    layoutPanel.add(b1, new BorderLayoutData(BorderLayoutRegion.NORTH));
    layoutPanel.add(b2, new BorderLayoutData(BorderLayoutRegion.SOUTH));
    layoutPanel.add(b3, new BorderLayoutData(BorderLayoutRegion.WEST));
    layoutPanel.add(b4, new BorderLayoutData(BorderLayoutRegion.EAST));
    layoutPanel.add(layoutPanel1, new BorderLayoutData(BorderLayoutRegion.CENTER, true));

    final Button b11 = new Button("Button 11");
    final Button b12 = new Button("Button 12");
    final Button b13 = new Button("Button 13");
    final Button b14 = new Button("Button 14");
    final Button b15 = new Button("Button 15");

    layoutPanel1.add(b11, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel1.add(b12, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel1.add(b13, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel1.add(b14, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel1.add(b15, new BoxLayoutData(FillStyle.HORIZONTAL));
  }

}
