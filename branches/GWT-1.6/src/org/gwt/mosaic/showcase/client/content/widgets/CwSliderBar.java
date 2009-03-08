/*
 * Copyright 2008 Google Inc.
 * 
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidgetCollection;
import com.google.gwt.widgetideas.client.SliderBar;
import com.google.gwt.widgetideas.client.SliderBar.LabelFormatter;

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
    @DefaultStringValue("The SlideBar input Widget lets the user select a value from a range of values via mouse or keyboard events.")
    String mosaicSliderBarDescription();

    @DefaultStringValue("SlideBar")
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
   * A boolean indicating whether or not we should use custom text.
   */
  @ShowcaseData
  private boolean useCustomText = true;

  /**
   * The main slider bat at the top of the page.
   */
  @ShowcaseData
  private SliderBar mainSliderBar = new SliderBar(0.0, 100.0,
      new LabelFormatter() {
        public String formatLabel(SliderBar slider, double value) {
          if (useCustomText) {
            return (int) value + "s";
          } else {
            return (int) (10 * value) / 10.0 + "";
          }
        }
      });

  /**
   * TextBox to display or set current value.
   */
  @ShowcaseData
  private TextBox curBox = new TextBox();

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new ScrollLayoutPanel();
    layoutPanel.setPadding(0);

    // Setup the slider bars
    mainSliderBar.setStepSize(5.0);
    mainSliderBar.setCurrentValue(50.0);
    mainSliderBar.setNumTicks(10);
    mainSliderBar.setNumLabels(5);
    mainSliderBar.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        curBox.setText(mainSliderBar.getCurrentValue() + "");
      }
    });

    // SliderBar implements ResizableWidget
    WidgetWrapper w = new WidgetWrapper(mainSliderBar);
    mainSliderBar.setWidth("100%");
    w.setHeight("100px");
    layoutPanel.add(w, new BoxLayoutData(1.0, 64));

    layoutPanel.add(createControlPanel(), new BoxLayoutData(FillStyle.HORIZONTAL));

    return layoutPanel;
  }

  /**
   * 
   * @return
   */
  @ShowcaseSource
  private Widget createControlPanel() {
    // Place everything in a nice looking grid
    Grid grid = new Grid(9, 3);
    grid.setBorderWidth(1);
    grid.setCellPadding(3);

    // The type of text to display
    final HTML defaultTextLabel = new HTML("custom");

    // Set the current slider position
    curBox.setText("50.0");
    grid.setWidget(0, 1, curBox);
    grid.setHTML(0, 2, "The current value of the knob.");
    grid.setWidget(0, 0, new Button("Set Current Value", new ClickListener() {
      public void onClick(Widget sender) {
        mainSliderBar.setCurrentValue(new Float(curBox.getText()).floatValue());
      }
    }));

    // Set the minimum value
    final TextBox minBox = new TextBox();
    minBox.setText("0.0");
    grid.setWidget(1, 1, minBox);
    grid.setHTML(1, 2, "The lower bounds (minimum) of the range.");
    grid.setWidget(1, 0, new Button("Set Min Value", new ClickListener() {
      public void onClick(Widget sender) {
        mainSliderBar.setMinValue(new Float(minBox.getText()).floatValue());
      }
    }));

    // Set the maximum value
    final TextBox maxBox = new TextBox();
    maxBox.setText("100.0");
    grid.setWidget(2, 1, maxBox);
    grid.setHTML(2, 2, "The upper bounds (maximum) of the range.");
    grid.setWidget(2, 0, new Button("Set Max Value", new ClickListener() {
      public void onClick(Widget sender) {
        mainSliderBar.setMaxValue(new Float(maxBox.getText()).floatValue());
      }
    }));

    // Set the step size
    final TextBox stepSizeBox = new TextBox();
    stepSizeBox.setText("1.0");
    grid.setWidget(3, 1, stepSizeBox);
    grid.setHTML(3, 2, "The increments between each knob position.");
    grid.setWidget(3, 0, new Button("Set Step Size", new ClickListener() {
      public void onClick(Widget sender) {
        mainSliderBar.setStepSize(new Float(stepSizeBox.getText()).floatValue());
      }
    }));

    // Set the number of tick marks
    final TextBox numTicksBox = new TextBox();
    numTicksBox.setText("10");
    grid.setWidget(4, 1, numTicksBox);
    grid.setHTML(4, 2,
        "The vertical black lines along the range of value.  Note that the "
            + "number of ticks is actually one more than the number you "
            + "specify, so setting the number of ticks to one will display a "
            + "tick at each end of the slider.");
    grid.setWidget(4, 0, new Button("Set Num Ticks", new ClickListener() {
      public void onClick(Widget sender) {
        mainSliderBar.setNumTicks(new Integer(numTicksBox.getText()).intValue());
      }
    }));

    // Set the number of labels
    final TextBox numLabelsBox = new TextBox();
    numLabelsBox.setText("5");
    grid.setWidget(5, 1, numLabelsBox);
    grid.setHTML(5, 2, "The labels above the ticks.");
    grid.setWidget(5, 0, new Button("Set Num Labels", new ClickListener() {
      public void onClick(Widget sender) {
        mainSliderBar.setNumLabels(new Integer(numLabelsBox.getText()).intValue());
      }
    }));

    // Create a form to set width of element
    final TextBox widthBox = new TextBox();
    widthBox.setText("50%");
    grid.setWidget(6, 1, widthBox);
    grid.setHTML(6, 2, "Set the width of the slider.  Use this to see how "
        + "resize checking detects the new dimensions and redraws the widget.");
    grid.setWidget(6, 0, new Button("Set Width", new ClickListener() {
      public void onClick(Widget sender) {
        mainSliderBar.setWidth(widthBox.getText());
      }
    }));

    // Add the default text option
    grid.setWidget(7, 1, defaultTextLabel);
    grid.setHTML(7, 2, "Override the format of the labels with a custom"
        + "format.");
    grid.setWidget(7, 0, new Button("Toggle Custom Text", new ClickListener() {
      public void onClick(Widget sender) {
        if (useCustomText) {
          defaultTextLabel.setHTML("default");
          useCustomText = false;
          mainSliderBar.redraw();
        } else {
          defaultTextLabel.setHTML("custom");
          useCustomText = true;
          mainSliderBar.redraw();
        }
      }
    }));

    // Add static resize timer methods
    final HTML resizeCheckLabel = new HTML("enabled");
    grid.setWidget(8, 1, resizeCheckLabel);
    grid.setHTML(8, 2, "When resize checking is enabled, a Timer will "
        + "periodically check if the Widget's dimensions have changed.  If "
        + "they change, the widget will be redrawn.");
    grid.setWidget(8, 0, new Button("Toggle Resize Checking",
        new ClickListener() {
          public void onClick(Widget sender) {
            if (ResizableWidgetCollection.get().isResizeCheckingEnabled()) {
              ResizableWidgetCollection.get().setResizeCheckingEnabled(false);
              resizeCheckLabel.setHTML("disabled");

            } else {
              ResizableWidgetCollection.get().setResizeCheckingEnabled(true);
              resizeCheckLabel.setHTML("enabled");
            }
          }
        }));

    return grid;
  }

  // TODO @Override
  protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
    GWT.runAsync(new RunAsyncCallback() {

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }

      public void onSuccess() {
        callback.onSuccess(onInitialize());
      }
    });
  }

}
