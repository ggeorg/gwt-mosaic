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
package org.gwt.mosaic.showcase.client.content.forms;

import org.gwt.mosaic.forms.client.builder.FormLayoutPanel;
import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.datepicker.DateComboBox;
import org.gwt.mosaic.ui.client.layout.GridLayout;
import org.gwt.mosaic.ui.client.layout.GridLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwComplexForm extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwComplexForm(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Complex Form description.";
  }

  @Override
  public String getName() {
    return "Complex Form";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    final DecoratedTabLayoutPanel tabPanel = new DecoratedTabLayoutPanel();

    final LayoutPanel form1 = new ScrollLayoutPanel();
    tabPanel.add(form1, "Form #1");

    final LayoutPanel form2 = new ScrollLayoutPanel();
    tabPanel.add(form2, "Form #2");

    // lazy form creation loading
    tabPanel.addTabListener(new TabListener() {
      public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
        return true;
      }

      public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
        if (tabIndex == 0) {
          if (form1.getWidgetCount() == 0) {
            form1.add(createForm1());
            DeferredCommand.addCommand(new Command() {
              public void execute() {
                form1.layout();
              }
            });
          }
        } else if (tabIndex == 1) {
          if (form2.getWidgetCount() == 0) {
            form2.add(createForm2());
            DeferredCommand.addCommand(new Command() {
              public void execute() {
                form2.layout();
              }
            });
          }
        }
      }
    });

    tabPanel.selectTab(0);

    return tabPanel;
  }

  /**
   * Example form #1.
   * 
   * @return the form widget
   */
  @ShowcaseSource
  private Widget createForm1() {
    // Create a layout panel to align the widgets
    final LayoutPanel formLayout = new LayoutPanel(new GridLayout(7, 7)) {
      protected void onLoad() {
        super.onLoad();
        layout();
      }
    };

    // Create and configure a Builder
    final PanelBuilder builder = new PanelBuilder(new FormLayoutPanel() {
      public void add(Widget widget, GridLayoutData layoutData) {
        formLayout.add(widget, layoutData);
      }
    });
    builder.setHorizontalAlignment(PanelBuilder.ALIGN_RIGHT);
    builder.setDebug(false);

    // Fill the grid with components; the builder can create frequently used
    // components, e.g. separators and labels.

    // Add a titled separator to cell (1, 1) that spans 7 columns.
    builder.addSeparator("General", 7);

    builder.addLabel("Company");
    builder.add(new TextBox(), 6);
    builder.addLabel("Contact");
    builder.add(new TextBox(), 6);

    builder.addSeparator("Propeller", 7);

    builder.addLabel("PTI [kW]");
    builder.add(new TextBox(), 2);
    builder.addGap();
    builder.addLabel("Power [kW]");
    builder.add(new TextBox(), 2);

    builder.addLabel("R [mm]");
    builder.add(new TextBox(), 2);
    builder.addGap();
    builder.addLabel("D [mm]");
    builder.add(new TextBox(), 2);

    return formLayout;
  }

  /**
   * Example form #2.
   * 
   * @return the form widget
   */
  @ShowcaseSource
  private Widget createForm2() {
    // Create a layout panel to align the widgets
    final LayoutPanel formLayout = new LayoutPanel(new GridLayout(9, 10)) {
      protected void onLoad() {
        super.onLoad();
        layout();
      }
    };

    // Create and configure a Builder
    final PanelBuilder builder = new PanelBuilder(new FormLayoutPanel() {
      public void add(Widget widget, GridLayoutData layoutData) {
        formLayout.add(widget, layoutData);
      }
    });
    builder.setHorizontalAlignment(PanelBuilder.ALIGN_LEFT);
    builder.setDebug(false);

    // Fill the grid with components; the builder can create frequently used
    // components, e.g. separators and labels.

    // row #1

    builder.addSeparator("Description", 9);

    // row #2

    builder.add(new TextArea(), new GridLayoutData(7, 2));
    builder.add(new CheckBox("Done"), new GridLayoutData(2, 2));

    // row #3

    builder.addSeparator("Date / Time", 4);
    builder.addGap();
    builder.addSeparator("Attributes", 4);

    // row #4

    DateComboBox startDate = new DateComboBox();
    startDate.setDateFormat(DateTimeFormat.getFormat("dd-MM-yyyy"));

    builder.addLabel("Start Date:", 2);
    builder.add(startDate, 2);
    builder.addGap();
    builder.add(new CheckBox("Confidential"), 4);

    // row #5

    DateComboBox deadline = new DateComboBox();
    deadline.setDateFormat(DateTimeFormat.getFormat("dd-MM-yyyy"));

    builder.addLabel("Deadline:", 2);
    builder.add(deadline, 2);
    builder.addGap();
    builder.add(new CheckBox("Not related to work"), 4);

    // row #6

    ListBox duration = new ListBox();
    duration.addItem("00:15");
    duration.addItem("00:30");
    duration.addItem("01:00");

    builder.addLabel("Duration:", 2);
    builder.add(duration, 2);
    builder.addGap();
    builder.addSeparator("Priority", 4);

    // row #7

    builder.addGap(5);

    ListBox priority = new ListBox();
    priority.addItem("Without Priority");
    priority.addItem("Urgent");

    builder.add(priority, 4);

    // row #8

    builder.addSeparator("Completion status", 9);

    // row #9

    builder.addLabel("Percentage Complete", 7);

    ListBox listCompletion = new ListBox();
    listCompletion.addItem("0%", "0");
    listCompletion.addItem("10%", "10");

    builder.add(listCompletion, 2);

    return formLayout;
  }
}
