package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.demo.Annotations.MosaicSource;
import org.mosaic.ui.client.demo.Annotations.MosaicStyle;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Button;

/**
 * 
 */
@MosaicStyle({".mosaic-LayoutPanel"})
public class LayoutTest1Page extends Page {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public LayoutTest1Page(DemoConstants constants) {
    super(constants);
    // TODO Auto-generated constructor stub
  }

  /**
   * 
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.setWidgetSpacing(5);

    final Button b11 = new Button("Button 11");
    final Button b12 = new Button("Button 12");
    final Button b13 = new Button("Button 13");
    final Button b14 = new Button("Button 14");

    BoxLayout boxLayout1 = new BoxLayout(); // default is horizontal
    final LayoutPanel layoutPanel1 = new LayoutPanel(boxLayout1);

    layoutPanel1.add(b11, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel1.add(b12, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel1.add(b13, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel1.add(b14, new BoxLayoutData(FillStyle.VERTICAL));

    final Button b21 = new Button("Button 21");
    final Button b22 = new Button("Button 22");
    final Button b23 = new Button("Button 23");
    final Button b24 = new Button("Button 24");

    BoxLayout boxLayout2 = new BoxLayout(); // default is horizontal
    boxLayout2.setLeftToRight(false);
    final LayoutPanel layoutPanel2 = new LayoutPanel(boxLayout2);

    layoutPanel2.add(b21, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel2.add(b22, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel2.add(b23, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel2.add(b24, new BoxLayoutData(FillStyle.VERTICAL));

    layoutPanel.add(layoutPanel1, new BoxLayoutData(FillStyle.BOTH, true));
    layoutPanel.add(layoutPanel2, new BoxLayoutData(FillStyle.BOTH, true));
  }

}
