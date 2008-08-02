package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.pages.Annotations.MosaicData;
import org.mosaic.showcase.client.pages.Annotations.MosaicSource;
import org.mosaic.showcase.client.pages.Annotations.MosaicStyle;
import org.mosaic.ui.client.MessageBox;
import org.mosaic.ui.client.layout.FillLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 */
@MosaicStyle({".mosaic-WindowPanel", ".dragdrop-positioner"})
public class MessageBoxPage extends Page {

  @MosaicSource
  public static interface DemoConstants extends Constants, Page.DemoConstants {

  }

  /**
   * An instance of the constants
   */
  @MosaicData
  private DemoConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public MessageBoxPage(DemoConstants constants) {
    super(constants);
    this.constants = constants;
  }

  /**
   * 
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    ScrollPanel scrollPanel = new ScrollPanel();
    layoutPanel.add(scrollPanel, new FillLayoutData(false));

    // Add the button and list to a panel
    VerticalPanel vPanel = new VerticalPanel();
    vPanel.setSpacing(8);

    scrollPanel.add(vPanel);

    HTML alertDesc = new HTML(
        "<b>Alert Box</b>"
            + "<br><small>An alert box is often used if you want to make sure information comes through to the user. "
            + "When an alert box pops up, the user will have to click \"OK\" to proceed.</small>");
    vPanel.add(alertDesc);

    Button alertBtn = new Button("Warning");
    alertBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        MessageBox.alert("Warning", "I am a warning box!");
      }
    });

    Button errorBtn = new Button("Error");
    errorBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        MessageBox.error("Error", "I am an error box!");
      }
    });

    Button infoBtn = new Button("Info");
    infoBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        MessageBox.info("Info", "I am an info box!");
      }
    });

    HorizontalPanel hpanel = new HorizontalPanel();
    hpanel.add(alertBtn);
    hpanel.add(new HTML("&nbsp;"));
    hpanel.add(errorBtn);
    hpanel.add(new HTML("&nbsp;"));
    hpanel.add(infoBtn);

    vPanel.add(hpanel);

    HTML confirmDesc = new HTML(
        "<b>Confirmation Box</b>"
            + "<br><small>A confirm box is often used if you want the user to verify or accept something. "
            + "When a confirm box pops up, the user will have to click either \"OK\" or \"Cancel\" to proceed."
            + "If the user clicks \"OK\", the box returns true. If the user clicks \"Cancel\", the box returns false.</small>");
    vPanel.add(confirmDesc);

    Button confirmBtn = new Button("Show Me");
    confirmBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        MessageBox.confirm("Confirm Box", "I am a confirm box!");
      }
    });
    vPanel.add(confirmBtn);

    HTML promptDesc = new HTML(
        "<b>Prompt Box</b>"
            + "<br><small>A prompt box is often used if you want the user to input a value before entering a page. "
            + "When a prompt box pops up, the user will have to click either \"OK\" or \"Cancel\" to proceed after entering an input value. "
            + "If the user clicks \"OK\" the box returns the input value. If the user clicks \"Cancel\" the box returns null.</small>");
    vPanel.add(promptDesc);

    Button promptBtn = new Button("Show Me");
    promptBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        MessageBox.prompt("Prompt Box", "Please enter your name", "George");
      }
    });
    vPanel.add(promptBtn);

  }
}
