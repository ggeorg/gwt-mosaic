package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Button;

/**
 * 
 */
@ShowcaseStyle({".mosaic-LayoutPanel"})
public class BoxLayoutPage extends Page {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public BoxLayoutPage(DemoConstants constants) {
    super(constants);
  }

  /**
   * 
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.setWidgetSpacing(5);

    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");

    final LayoutPanel layoutPanel1 = new LayoutPanel(new BoxLayout());
    layoutPanel1.setWidgetSpacing(2);

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

}
