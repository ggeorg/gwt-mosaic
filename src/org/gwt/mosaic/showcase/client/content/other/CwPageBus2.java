/*
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
package org.gwt.mosaic.showcase.client.content.other;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.pagebus.client.PageBus;
import org.gwt.mosaic.pagebus.client.QueryCallback;
import org.gwt.mosaic.pagebus.client.SubscriberCallback;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.HTMLLabel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.TextLabel;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.GridLayout;
import org.gwt.mosaic.ui.client.layout.GridLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwPageBus2 extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwPageBus2(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates components sharing information using "
        + "<a href='http://developer.tibco.com/pagebus/' target='_blank'>TIBCO(R) PageBus</a> "
        + "store/query. ";
  }

  @Override
  public String getName() {
    return "Store/Query";
  }

  @Override
  public boolean hasStyle() {
    return false;
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout());
    layoutPanel.setPadding(0);

    final LayoutPanel vPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
    vPanel.setPadding(0);
    layoutPanel.add(vPanel, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(newMonitorMessages(), new BoxLayoutData(FillStyle.BOTH,
        true));

    vPanel.add(newStoreValues(), new BoxLayoutData(FillStyle.HORIZONTAL, true));
    vPanel.add(newQuerier(), new BoxLayoutData(FillStyle.BOTH, true));

    return layoutPanel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newStoreValues() {
    final CaptionLayoutPanel layoutPanel = new CaptionLayoutPanel(
        "Store Values");
    final FormLayout formLayout = new FormLayout(
        "pref,3dlu,pref:grow,3dlu,pref:grow",
        "pref,3dlu,pref,3dlu,pref,3dlu,pref");
    layoutPanel.setLayout(formLayout);
    // layoutPanel.setPadding(0);

    formLayout.setColumnGroups(new int[][] {{3, 5}});

    layoutPanel.add(new TextLabel("Subject:"), CellConstraints.xy(1, 1));

    final TextBox subjectTextBox = new TextBox();
    subjectTextBox.setText("a.b.c");
    layoutPanel.add(subjectTextBox, CellConstraints.xyw(3, 1, 3));

    layoutPanel.add(new TextLabel("Value:"), CellConstraints.xy(1, 3));

    final TextBox valueTextBox = new TextBox();
    layoutPanel.add(valueTextBox, CellConstraints.xyw(3, 3, 3));

    final HTMLLabel statusHTML = new HTMLLabel();

    layoutPanel.add(new Button("Store Value", new ClickHandler() {
      public void onClick(ClickEvent event) {
        try {
          final String subject = subjectTextBox.getText();
          final String value = valueTextBox.getText();
          PageBus.store(subject, value);
          statusHTML.setHTML("Successfully stored \"" + value + "\" under \""
              + subject + "\"");
        } catch (Exception e) {
          statusHTML.setHTML("<font color='red'>PageBus.store threw an exception: "
              + e.getMessage() + "</font>");
        }

        WidgetHelper.invalidate(statusHTML);
        WidgetHelper.getParent(layoutPanel).layout();
      }
    }), CellConstraints.xy(3, 5));

    layoutPanel.add(new Button("Clear Value", new ClickHandler() {
      public void onClick(ClickEvent event) {
        try {
          final String subject = subjectTextBox.getText();
          PageBus.store(subject, null);
          statusHTML.setHTML("Successfully cleared \"" + subject + "\"");
        } catch (Exception e) {
          statusHTML.setHTML("<font color='red'>PageBus.store threw an exception: "
              + e.getMessage() + "</font>");
        }

        WidgetHelper.invalidate(statusHTML);
        WidgetHelper.getParent(layoutPanel).layout();
      }
    }), CellConstraints.xy(5, 5));

    layoutPanel.add(statusHTML, CellConstraints.xyw(1, 7, 3));

    return layoutPanel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newQuerier() {
    final CaptionLayoutPanel layoutPanel = new CaptionLayoutPanel("Querier");
    layoutPanel.setLayout(new FormLayout("pref:grow,3dlu,pref",
        "pref,1dlu,pref,3dlu,fill:pref:grow"));
    // layoutPanel.setPadding(0);

    final ScrollPanel scrollPanel = new ScrollPanel();
    DOM.setStyleAttribute(scrollPanel.getElement(), "background", "white");
    final FlowPanel flowPanel = new FlowPanel();
    scrollPanel.add(flowPanel);

    layoutPanel.add(new TextLabel("Specify a subject to query:"),
        CellConstraints.xyw(1, 1, 3));

    final TextBox queryTextBox = new TextBox();
    queryTextBox.setText("a.b.**");
    layoutPanel.add(queryTextBox, CellConstraints.xy(1, 3));
    layoutPanel.add(new Button("Perform a Query", new ClickHandler() {
      public void onClick(ClickEvent event) {
        flowPanel.clear();
        PageBus.query(queryTextBox.getText(), new QueryCallback() {
          public boolean onResult(String subject, Object value, Object data) {
            if ("com.tibco.pagebus.query.done".equals(subject)) {
              flowPanel.add(new HTML(subject + ": <i>QUERY COMPLETED</i>"));
            } else {
              flowPanel.add(new HTML(subject + ": " + value));
            }
            return true;
          }
        });
      }
    }), CellConstraints.xy(3, 3));

    final LayoutPanel decorator = new LayoutPanel();
    decorator.add(scrollPanel, new FillLayoutData(true));
    layoutPanel.add(decorator, CellConstraints.xyw(1, 5, 3));

    return layoutPanel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newMonitorMessages() {
    final CaptionLayoutPanel layoutPanel = new CaptionLayoutPanel(
        "Monitor Messages");
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.add(
        new Label("This component subscribes to the subject '**'."),
        new BoxLayoutData(FillStyle.HORIZONTAL));

    final FlowPanel flowPanel = new FlowPanel();

    final ImageButton clearImgBtn = new ImageButton(
        Caption.IMAGES.windowClose());
    clearImgBtn.setTitle("Clear Monitor Messages");
    clearImgBtn.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        flowPanel.clear();
      }
    });
    layoutPanel.getHeader().add(clearImgBtn, CaptionRegion.RIGHT);

    final ScrollPanel scrollPanel = new ScrollPanel();
    DOM.setStyleAttribute(scrollPanel.getElement(), "background", "white");
    scrollPanel.add(flowPanel);

    PageBus.subscribe("**", new SubscriberCallback() {
      public void onMessage(String subject, Object message,
          Object subscriberData) {
        flowPanel.add(new HTML("<b>Subject:</b> " + subject, false));
        flowPanel.add(new HTML("<b>Message:</b> " + message, false));
        flowPanel.add(new HTML("<br>"));
      }
    });

    layoutPanel.add(scrollPanel, new BoxLayoutData(FillStyle.BOTH, true));

    return layoutPanel;
  }

}
