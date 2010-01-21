/*
 * Copyright 2008 Google Inc.
 * 
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos.
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
import org.gwt.mosaic.ui.client.SliderBar;
import org.gwt.mosaic.ui.client.SliderBar.LabelFormatter;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
@ShowcaseStyle( {".gwt-Button"})
public class CwSliderBar extends ContentWidget {

  /**
   * The constants used in this Content Widget
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {
    @DefaultStringValue("The SliderBar input Widget lets the user select a value from a range of values via mouse or keyboard events.")
    String mosaicSliderBarDescription();

    @DefaultStringValue("SliderBar")
    String mosaicSliderBarName();
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
  public CwSliderBar(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }

  @Override
  public String getDescription() {
    return constants.mosaicSliderBarDescription();
  }

  @Override
  public String getName() {
    return constants.mosaicSliderBarName();
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

    final InlineLabel label1 = new InlineLabel("Value: 0.5");
    final InlineLabel label2 = new InlineLabel("Value: 0.5");
    final InlineLabel label3 = new InlineLabel("Value: 50.0");

    final SliderBar sliderBar1 = new SliderBar();
    sliderBar1.setStepSize(0.1);
    sliderBar1.setCurrentValue(0.5);
    sliderBar1.addValueChangeHandler(new ValueChangeHandler<Double>() {
      public void onValueChange(ValueChangeEvent<Double> event) {
        label1.setText("Value: " + event.getValue());
        layoutPanel.invalidate(label1);
        layoutPanel.layout();
      }
    });

    final SliderBar sliderBar2 = new SliderBar();
    sliderBar2.setStepSize(0.1);
    sliderBar2.setCurrentValue(0.5);
    sliderBar2.setNumTicks(10);
    sliderBar2.setNumLabels(5);
    sliderBar2.addValueChangeHandler(new ValueChangeHandler<Double>() {
      public void onValueChange(ValueChangeEvent<Double> event) {
        label2.setText("Value: " + event.getValue());
        layoutPanel.invalidate(label2);
        layoutPanel.layout();
      }
    });

    final SliderBar sliderBar3 = new SliderBar(0.0, 100.0,
        new LabelFormatter() {
          public String formatLabel(SliderBar slider, double value) {
            return (int) value + "s";
          }
        });
    sliderBar3.setStepSize(5.0);
    sliderBar3.setCurrentValue(50.0);
    sliderBar3.setNumTicks(10);
    sliderBar3.setNumLabels(5);
    sliderBar3.addValueChangeHandler(new ValueChangeHandler<Double>() {
      public void onValueChange(ValueChangeEvent<Double> event) {
        label3.setText("Value: " + event.getValue());
        layoutPanel.invalidate(label3);
        layoutPanel.layout();
      }
    });

    // SliderBar implements ResizableWidget
    layoutPanel.add(sliderBar1);
    layoutPanel.add(label1);
    layoutPanel.add(sliderBar2);
    layoutPanel.add(label2);
    layoutPanel.add(sliderBar3);
    layoutPanel.add(label3);

    return layoutPanel;
  }
}
