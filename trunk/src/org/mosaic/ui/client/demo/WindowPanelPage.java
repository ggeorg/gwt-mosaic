package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.MessageBox;
import org.mosaic.ui.client.WindowPanel;
import org.mosaic.ui.client.MessageBox.MessageBoxType;
import org.mosaic.ui.client.demo.Annotations.MosaicSource;
import org.mosaic.ui.client.demo.Annotations.MosaicStyle;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 */
@MosaicStyle({".dragdrop-positioner"})
public class WindowPanelPage extends Page {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public WindowPanelPage(DemoConstants constants) {
    super(constants);
    // TODO Auto-generated constructor stub
  }

  /**
   * 
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BoxLayout());

    final WindowPanel basic = new WindowPanel("Basic");
    basic.setAnimationEnabled(true);
    // basic.setWidth("150px");

    Button btn1 = new Button("Basic");
    btn1.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        basic.center();
      }
    });

    Button btn2 = new Button("Resizable");
    btn2.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        // TODO Auto-generated method stub

      }
    });

    Button btn3 = new Button("Modal");
    btn3.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        MessageBox messageBox = new MessageBox("Alert", MessageBoxType.ALERT);
        messageBox.setWidth(256 + "px");
        messageBox.setAnimationEnabled(true);
        messageBox.setText("Hello dear George!");
        messageBox.center();
      }
    });

    layoutPanel.add(btn1);
    layoutPanel.add(btn2);
    layoutPanel.add(btn3);
  }

}
