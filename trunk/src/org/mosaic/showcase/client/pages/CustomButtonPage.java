package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.pages.Annotations.MosaicData;
import org.mosaic.showcase.client.pages.Annotations.MosaicSource;
import org.mosaic.showcase.client.pages.Annotations.MosaicStyle;
import org.mosaic.ui.client.Caption;
import org.mosaic.ui.client.DecoratedButton;
import org.mosaic.ui.client.InfoPanel;
import org.mosaic.ui.client.MessageBox;
import org.mosaic.ui.client.ToolButton;
import org.mosaic.ui.client.WidgetWrapper;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@MosaicStyle( {".gwt-CustomButton", ".gwt-PushButton", ".gwt-ToggleButton"})
public class CustomButtonPage extends Page implements ClickListener {

  /**
   * The constants used in this Page.
   */
  @MosaicSource
  public static interface DemoConstants extends Constants, Page.DemoConstants {
    String mosaicCustomButtonDescription();

    String mosaicCustomButtonPush();

    String mosaicCustomButtonToggle();
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
  public CustomButtonPage(DemoConstants constants) {
    super(constants);
    this.constants = constants;
  }

  /**
   * Load this example.
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    final BoxLayout vLayout = new BoxLayout(Orientation.VERTICAL);
    layoutPanel.setLayout(vLayout);

    layoutPanel.add(new HTML(constants.mosaicCustomButtonDescription()));

    //
    // push buttons
    //

    final LayoutPanel pushBox = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
	pushBox.setPadding(0);
    pushBox.setWidgetSpacing(0);

    final Caption pushCaption = new Caption(constants.mosaicCustomButtonPush());
    final LayoutPanel pushBody = new LayoutPanel(new BoxLayout());

    pushBox.add(pushCaption, new BoxLayoutData(FillStyle.HORIZONTAL));
    pushBox.add(pushBody, new BoxLayoutData(FillStyle.BOTH));

    // Add a normal PushButton
    final PushButton normalPushButton = new PushButton(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation().createImage(),
        MessageBox.MESSAGEBOX_IMAGES.dialogQuestion().createImage(), this);
    normalPushButton.ensureDebugId("mosaicCustomButton-push-normal");

    pushBody.add(new WidgetWrapper(normalPushButton), new BoxLayoutData(FillStyle.BOTH, true));
    
    // ???
    //final DecoratedButton decButton = new DecoratedButton("Hello");
    //pushBody.add(decButton, new BoxLayoutData(FillStyle.BOTH));
    final ToolButton toolButton = new ToolButton("Hello");
    pushBody.add(toolButton, new BoxLayoutData(FillStyle.BOTH));
    
    // Add a disabled PushButton
    final PushButton disabledPushButton = new PushButton(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation().createImage(),
        MessageBox.MESSAGEBOX_IMAGES.dialogQuestion().createImage(), this);
    disabledPushButton.ensureDebugId("mosaicCustomButton-push-disabled");
    disabledPushButton.setEnabled(false);

    pushBody.add(new WidgetWrapper(disabledPushButton), new BoxLayoutData(FillStyle.BOTH, true));

    layoutPanel.add(pushBox, new BoxLayoutData(FillStyle.BOTH, true));

    //
    // toggle buttons
    //

    final LayoutPanel toggleBox = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
	toggleBox.setPadding(0);
    toggleBox.setWidgetSpacing(0);

    final Caption toggleCaption = new Caption(constants.mosaicCustomButtonToggle());
    final LayoutPanel toggleBody = new LayoutPanel(new BoxLayout());

    toggleBox.add(toggleCaption, new BoxLayoutData(FillStyle.HORIZONTAL));
    toggleBox.add(toggleBody, new BoxLayoutData(FillStyle.BOTH));

    // Add a normal ToggleButton
    final ToggleButton normalToggleButton = new ToggleButton(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation().createImage(),
        MessageBox.MESSAGEBOX_IMAGES.dialogQuestion().createImage(), this);
    normalToggleButton.ensureDebugId("mosaicCustomButton-toggle-normal");

    toggleBody.add(new WidgetWrapper(normalToggleButton));

    // Add a disabled PushButton
    ToggleButton disabledToggleButton = new ToggleButton(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation().createImage(),
        MessageBox.MESSAGEBOX_IMAGES.dialogQuestion().createImage(), this);
    disabledToggleButton.ensureDebugId("mosaicCustomButton-toggle-disabled");
    disabledToggleButton.setEnabled(false);

    toggleBody.add(new WidgetWrapper(disabledToggleButton));

    layoutPanel.add(toggleBox, new BoxLayoutData(FillStyle.BOTH, true));
  }

  /**
   * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
   */
  @MosaicSource
  public void onClick(Widget sender) {
    if (sender instanceof PushButton) {
      InfoPanel.show("Custom Button", "Clicked!");
    } else {
      ToggleButton btn = (ToggleButton) sender;
      InfoPanel.show("Custom Button", "Down = " + new Boolean(btn.isDown()).toString());
    }
  }

}
