/*
 * Copyright 2008 Google Inc. Copyright 2008 Georgios J. Georgopoulos
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwt.mosaic.showcase.client.content.widgets;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.Showcase;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@ShowcaseStyle( {".gwt-CustomButton", ".gwt-PushButton", ".gwt-ToggleButton"})
public class CwCustomButton extends ContentWidget implements ClickListener {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {
    String mosaicCustomButtonClicked();

    String mosaicCustomButtonDescription();

    String mosaicCustomButtonDown();

    String mosaicCustomButtonName();

    String mosaicCustomButtonPush();

    String mosaicCustomButtonToggle();
  }

  /**
   * An instance of the constants
   */
  @ShowcaseData
  private CwConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwCustomButton(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }

  @Override
  public String getDescription() {
    return constants.mosaicCustomButtonDescription();
  }

  @Override
  public String getName() {
    return constants.mosaicCustomButtonName();
  }

  /**
   * Fired when the user clicks on a button.
   * 
   * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
   */
  @ShowcaseSource
  public void onClick(Widget sender) {
    if (sender instanceof PushButton) {
      Showcase.notifyTrayEvent(constants.mosaicCustomButtonName(),
          constants.mosaicCustomButtonClicked());
    } else {
      ToggleButton btn = (ToggleButton) sender;
      Showcase.notifyTrayEvent(constants.mosaicCustomButtonName(),
          constants.mosaicCustomButtonDown()
              + new Boolean(btn.isDown()).toString());
    }
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));

    //
    // push buttons
    //

    final CaptionLayoutPanel pushBtnPanel = new CaptionLayoutPanel(
        constants.mosaicCustomButtonPush());
    pushBtnPanel.setLayout(new BoxLayout());
    pushBtnPanel.setPadding(5);

    // Add a normal PushButton
    final PushButton normalPushButton = new PushButton(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation().createImage(),
        MessageBox.MESSAGEBOX_IMAGES.dialogQuestion().createImage(), this);
    normalPushButton.ensureDebugId("mosaicCustomButton-push-normal");

    pushBtnPanel.add(new WidgetWrapper(normalPushButton));

    // Add a disabled PushButton
    final PushButton disabledPushButton = new PushButton(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation().createImage(),
        MessageBox.MESSAGEBOX_IMAGES.dialogQuestion().createImage(), this);
    disabledPushButton.ensureDebugId("mosaicCustomButton-push-disabled");
    disabledPushButton.setEnabled(false);

    pushBtnPanel.add(new WidgetWrapper(disabledPushButton));

    layoutPanel.add(pushBtnPanel, new BoxLayoutData(FillStyle.BOTH, true));

    //
    // toggle buttons
    //

    final CaptionLayoutPanel toggleBtnPanel = new CaptionLayoutPanel(constants.mosaicCustomButtonToggle());
    toggleBtnPanel.setLayout(new BoxLayout());
    toggleBtnPanel.setPadding(5);

    // Add a normal ToggleButton
    final ToggleButton normalToggleButton = new ToggleButton(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation().createImage(),
        MessageBox.MESSAGEBOX_IMAGES.dialogQuestion().createImage(), this);
    normalToggleButton.ensureDebugId("mosaicCustomButton-toggle-normal");

    toggleBtnPanel.add(new WidgetWrapper(normalToggleButton));

    // Add a disabled PushButton
    ToggleButton disabledToggleButton = new ToggleButton(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation().createImage(),
        MessageBox.MESSAGEBOX_IMAGES.dialogQuestion().createImage(), this);
    disabledToggleButton.ensureDebugId("mosaicCustomButton-toggle-disabled");
    disabledToggleButton.setEnabled(false);

    toggleBtnPanel.add(new WidgetWrapper(disabledToggleButton));

    layoutPanel.add(toggleBtnPanel, new BoxLayoutData(FillStyle.BOTH, true));

    return layoutPanel;
  }

}
