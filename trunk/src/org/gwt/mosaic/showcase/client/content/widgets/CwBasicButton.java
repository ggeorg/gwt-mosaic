/*
 * Copyright 2008 Google Inc.
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
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".gwt-Button"})
public class CwBasicButton extends ContentWidget implements ClickListener {

  /**
   * The constants used in this <code>ContentWidget</code>.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {
    String mosaicBasicButtonClickMessage();

    String mosaicBasicButtonDescription();

    String mosaicBasicButtonDisabled();

    String mosaicBasicButtonName();

    String mosaicBasicButtonNormal();
  }

  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private CwConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwBasicButton(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }

  @Override
  public String getDescription() {
    return constants.mosaicBasicButtonDescription();
  }

  @Override
  public String getName() {
    return constants.mosaicBasicButtonName();
  }

  /**
   * Fired when the user clicks on a button.
   * 
   * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
   */
  @ShowcaseSource
  public void onClick(Widget sender) {
    InfoPanel.show(((Button) sender).getText(),
        constants.mosaicBasicButtonClickMessage());
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
    layoutPanel.setPadding(0);
    layoutPanel.setWidgetSpacing(20);

    //
    // Basic buttons
    //

    final LayoutPanel hBox1 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox1, new BoxLayoutData(FillStyle.HORIZONTAL));

    // Add a normal button
    Button normalButton = new Button(constants.mosaicBasicButtonNormal(), this);
    normalButton.ensureDebugId("mosaicBasicButton-normal");

    hBox1.add(normalButton);

    // Add a disabled button
    Button disabledButton = new Button(constants.mosaicBasicButtonDisabled(),
        this);
    disabledButton.ensureDebugId("mosaicBasicButton-disabled");
    disabledButton.setEnabled(false);

    hBox1.add(disabledButton);

    //
    // Basic buttons with images
    //

    final LayoutPanel hBox2 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox2, new BoxLayoutData(FillStyle.HORIZONTAL));

    // Add a button with image on right
    Button imageButton1 = new Button(ButtonHelper.createButtonLabel(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation(),
        constants.mosaicBasicButtonNormal(), ButtonLabelType.TEXT_ON_TOP), this);
    imageButton1.ensureDebugId("mosaicBasicButton-normal");

    hBox2.add(imageButton1, new BoxLayoutData(FillStyle.VERTICAL));

    // Add a button with image on top
    Button imageButton2 = new Button(ButtonHelper.createButtonLabel(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation(),
        constants.mosaicBasicButtonNormal(), ButtonLabelType.TEXT_ON_RIGHT),
        this);
    imageButton2.ensureDebugId("mosaicBasicButton-normal");

    hBox2.add(imageButton2, new BoxLayoutData(FillStyle.VERTICAL));

    // Add a button with image on left
    Button imageButton3 = new Button(ButtonHelper.createButtonLabel(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation(),
        constants.mosaicBasicButtonDisabled(), ButtonLabelType.TEXT_ON_BOTTOM),
        this);
    imageButton3.setEnabled(false);
    imageButton3.ensureDebugId("mosaicBasicButton-disabled");

    hBox2.add(imageButton3, new BoxLayoutData(FillStyle.VERTICAL));

    // Add a button with image on left
    Button imageButton4 = new Button(ButtonHelper.createButtonLabel(
        MessageBox.MESSAGEBOX_IMAGES.dialogInformation(),
        constants.mosaicBasicButtonDisabled(), ButtonLabelType.TEXT_ON_LEFT),
        this);
    imageButton4.setEnabled(false);
    imageButton4.ensureDebugId("mosaicBasicButton-disabled");

    hBox2.add(imageButton4, new BoxLayoutData(FillStyle.VERTICAL));

    return layoutPanel;
  }

}
