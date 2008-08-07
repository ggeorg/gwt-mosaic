package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.pages.Annotations.MosaicSource;
import org.mosaic.showcase.client.pages.Annotations.MosaicStyle;
import org.mosaic.ui.client.WindowPanel;
import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.BorderLayoutData;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 */
@MosaicStyle( {
    ".mosaic-Caption", ".mosaic-TitledLayoutPanel", ".mosaic-WindowPanel",
    ".dragdrop-positioner", ".dragdrop-draggable", ".dragdrop-handle"})
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
    basic.setSize("320px", "256px");
    LayoutPanel panel = new LayoutPanel();
    basic.setWidget(panel);
    createContent(panel);

    Button btn1 = new Button("Basic");
    btn1.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        basic.center();
      }
    });

    layoutPanel.add(btn1);
  }

  private void createContent(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BorderLayout());
    layoutPanel.setPadding(5);

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