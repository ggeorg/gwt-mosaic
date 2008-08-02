package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.pages.Annotations.MosaicData;
import org.mosaic.showcase.client.pages.Annotations.MosaicSource;
import org.mosaic.showcase.client.pages.Annotations.MosaicStyle;
import org.mosaic.ui.client.InfoPanel;
import org.mosaic.ui.client.MessageBox;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@MosaicStyle( {".gwt-Button"})
public class BasicButtonPage extends Page implements ClickListener {

  /**
   * 
   */
  @MosaicSource
  protected enum ButtonLabelType {
    TEXT_ON_TOP, TEXT_ON_RIGHT, TEXT_ON_BOTTOM, TEXT_ON_LEFT
  }

  /**
   * The constants used in this Page.
   */
  @MosaicSource
  public static interface DemoConstants extends Constants, Page.DemoConstants {
    String mosaicBasicButtonClickMessage();

    String mosaicBasicButtonDescription();
    
    String mosaicBasicButtonDescription2();

    String mosaicBasicButtonDisabled();

    String mosaicBasicButtonImage();

    String mosaicBasicButtonName();

    String mosaicBasicButtonNormal();
  }

  /**
   * An instance of the constants.
   */
  @MosaicData
  private DemoConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public BasicButtonPage(DemoConstants constants) {
    super(constants);
    this.constants = constants;
  }

  /**
   * 
   * @param type
   * @return
   */
  @MosaicSource
  protected String createItemLabel(ButtonLabelType type) {
    final HTML html = new HTML(constants.mosaicBasicButtonImage());
    final Image img = MessageBox.MESSAGEBOX_IMAGES.dialogInformation().createImage();
    if (type == ButtonLabelType.TEXT_ON_LEFT || type == ButtonLabelType.TEXT_ON_RIGHT) {
      HorizontalPanel hpanel = new HorizontalPanel();
      if (type == ButtonLabelType.TEXT_ON_LEFT) {
        hpanel.add(html);
        hpanel.add(img);
      } else {
        hpanel.add(img);
        hpanel.add(html);
      }
      hpanel.setCellVerticalAlignment(html, HasVerticalAlignment.ALIGN_MIDDLE);
      hpanel.setCellVerticalAlignment(img, HasVerticalAlignment.ALIGN_MIDDLE);
      return hpanel.toString();
    } else {
      VerticalPanel vpanel = new VerticalPanel();
      if (type == ButtonLabelType.TEXT_ON_TOP) {
        vpanel.add(html);
        vpanel.add(img);
      } else {
        vpanel.add(img);
        vpanel.add(html);
      }
      vpanel.setCellHorizontalAlignment(html, HasHorizontalAlignment.ALIGN_CENTER);
      vpanel.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
      return vpanel.toString();
    }
  }

  /**
   * 
   * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
   */
  @MosaicSource
  public void onClick(Widget sender) {
    InfoPanel.show(constants.mosaicBasicButtonName(),
        constants.mosaicBasicButtonClickMessage());
  }

  /**
   * Load this example.
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    final BoxLayout vLayout = new BoxLayout(Orientation.VERTICAL);
    layoutPanel.setLayout(vLayout);

    //
    // Basic buttons
    //
    
    layoutPanel.add(new HTML(constants.mosaicBasicButtonDescription()));

    final LayoutPanel hBox1 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox1, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    // Add a normal button
    Button normalButton = new Button(constants.mosaicBasicButtonNormal(), this);
    normalButton.ensureDebugId("mosaicBasicButton-normal");

    hBox1.add(normalButton);

    // Add a disabled button
    Button disabledButton = new Button(constants.mosaicBasicButtonDisabled(), this);
    disabledButton.ensureDebugId("mosaicBasicButton-disabled");
    disabledButton.setEnabled(false);

    hBox1.add(disabledButton);

    //
    // Basic buttons with images
    //
    
    layoutPanel.add(new HTML(constants.mosaicBasicButtonDescription2()));

    final LayoutPanel hBox2 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox2, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    // Add a button with image on right
    Button imageButton1 = new Button(createItemLabel(ButtonLabelType.TEXT_ON_TOP), this);
    imageButton1.ensureDebugId("mosaicBasicButton-image");

    hBox2.add(imageButton1, new BoxLayoutData(FillStyle.VERTICAL));

    // Add a button with image on top
    Button imageButton2 = new Button(createItemLabel(ButtonLabelType.TEXT_ON_RIGHT), this);
    imageButton2.ensureDebugId("mosaicBasicButton-image");

    hBox2.add(imageButton2, new BoxLayoutData(FillStyle.VERTICAL));

    // Add a button with image on left
    Button imageButton3 = new Button(createItemLabel(ButtonLabelType.TEXT_ON_BOTTOM),
        this);
    imageButton3.setEnabled(false);
    imageButton3.ensureDebugId("mosaicBasicButton-image");

    hBox2.add(imageButton3, new BoxLayoutData(FillStyle.VERTICAL));

    // Add a button with image on left
    Button imageButton4 = new Button(createItemLabel(ButtonLabelType.TEXT_ON_LEFT), this);
    imageButton4.setEnabled(false);
    imageButton4.ensureDebugId("mosaicBasicButton-image");

    hBox2.add(imageButton4, new BoxLayoutData(FillStyle.VERTICAL));

  }

}
