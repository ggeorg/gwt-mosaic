package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Button;

public class BidiBoxLayoutPage extends Page {

  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
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

}
