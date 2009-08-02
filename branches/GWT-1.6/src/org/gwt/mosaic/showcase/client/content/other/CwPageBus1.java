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

import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.pagebus.client.PageBus;
import org.gwt.mosaic.pagebus.client.SubscriberCallback;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.GridLayout;
import org.gwt.mosaic.ui.client.layout.GridLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class CwPageBus1 extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwPageBus1(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "This example shows how components can exchange events via "
        + "<a href='http://developer.tibco.com/pagebus/' target='_blank'>TIBCO(R) PageBus</a> "
        + "publish/subscribe.";
  }

  @Override
  public String getName() {
    return "PageBus I";
  }

  @Override
  public boolean hasStyle() {
    return false;
  }

  /**
   * 
   */
  @ShowcaseData
  private Map<String, Double> countryMobilePhonesInfo = new HashMap<String, Double>();

  /**
   * 
   */
  @ShowcaseData
  private Map<String, Double> countryInternetInfo = new HashMap<String, Double>();

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new GridLayout(2, 2));
    layoutPanel.setPadding(0);

    layoutPanel.add(newHighGDPCountries(), new GridLayoutData(true));
    layoutPanel.add(newMonitorMessages(), new GridLayoutData(1, 2, true));
    layoutPanel.add(newStatisticsByCountry(), new GridLayoutData(true));

    countryInternetInfo.put("BRZ", 0.13);
    countryInternetInfo.put("CHN", 0.09);
    countryInternetInfo.put("FRA", 0.47);
    countryInternetInfo.put("GER", 0.61);
    countryInternetInfo.put("IND", 0.05);
    countryInternetInfo.put("ITL", 0.50);
    countryInternetInfo.put("JAP", 0.68);
    countryInternetInfo.put("RUS", 0.17);
    countryInternetInfo.put("GBR", 0.61);
    countryInternetInfo.put("USA", 0.68);

    countryMobilePhonesInfo.put("BRZ", 0.45);
    countryMobilePhonesInfo.put("CHN", 0.33);
    countryMobilePhonesInfo.put("FRA", 0.78);
    countryMobilePhonesInfo.put("GER", 0.96);
    countryMobilePhonesInfo.put("IND", 0.06);
    countryMobilePhonesInfo.put("ITL", 1.24);
    countryMobilePhonesInfo.put("JAP", 0.74);
    countryMobilePhonesInfo.put("RUS", 0.85);
    countryMobilePhonesInfo.put("GBR", 1.0);
    countryMobilePhonesInfo.put("USA", 0.96);

    return layoutPanel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget newHighGDPCountries() {
    final CaptionLayoutPanel layoutPanel = new CaptionLayoutPanel(
        "High-GDP Countries");
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.add(new Label("Click the entries below to publish messages."),
        new BoxLayoutData(FillStyle.HORIZONTAL));

    final ListBox listBox = new ListBox();
    listBox.setVisibleItemCount(10);
    listBox.addItem("Brazil", "BRZ");
    listBox.addItem("China", "CHN");
    listBox.addItem("France", "FRA");
    listBox.addItem("Germany", "GER");
    listBox.addItem("India", "IND");
    listBox.addItem("Italy", "ITL");
    listBox.addItem("Japan", "JAP");
    listBox.addItem("Russia", "RUS");
    listBox.addItem("United Kingdom", "GBR");
    listBox.addItem("United States", "USA");

    layoutPanel.add(listBox, new BoxLayoutData(FillStyle.BOTH, true));

    listBox.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        int index = listBox.getSelectedIndex();
        if (index == -1) {
          return;
        }
        PageBus.publish("org.gwt.mosaic.pagebus.ex.country.select",
            new Country(listBox.getValue(index), listBox.getItemText(index)));
      }
    });

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

  /**
   * 
   */
  @ShowcaseSource
  private Widget newStatisticsByCountry() {
    final CaptionLayoutPanel layoutPanel = new CaptionLayoutPanel(
        "Statistics by Country");
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.add(new Label("This component contains 3 subscribers."),
        new BoxLayoutData(FillStyle.HORIZONTAL));

    final Grid grid = new Grid(3, 2);
    grid.getColumnFormatter().setWidth(0, "50%");
    grid.getColumnFormatter().setWidth(1, "50%");
    grid.setHTML(0, 0, "<b>Country:<b>");
    grid.setHTML(1, 0, "<b>Mobile Phones/Person:</b>");
    grid.setHTML(2, 0, "<b>Internet Users/Person:</b>");

    PageBus.subscribe("org.gwt.mosaic.pagebus.ex.country.select",
        new SubscriberCallback() {
          public void onMessage(String subject, Object message,
              Object subscriberData) {
            final Country country = (Country) message;
            grid.setHTML(0, 1, "<b>" + country.getName() + "</b>");
            grid.getCellFormatter().setHorizontalAlignment(0, 1,
                HasHorizontalAlignment.ALIGN_RIGHT);
          }
        });

    PageBus.subscribe("org.gwt.mosaic.pagebus.ex.country.select",
        new SubscriberCallback() {
          public void onMessage(String subject, Object message,
              Object subscriberData) {
            final Country country = (Country) message;
            final double mobilePhonesUsers = countryMobilePhonesInfo.get(country.getCode());
            String bgColor;
            if (mobilePhonesUsers < 0.3) {
              bgColor = "red";
            } else if (mobilePhonesUsers < 0.6) {
              bgColor = "white";
            } else {
              bgColor = "tan";
            }
            grid.setHTML(1, 1, "<b>" + mobilePhonesUsers + "</b>");
            grid.getCellFormatter().setHorizontalAlignment(1, 1,
                HasHorizontalAlignment.ALIGN_RIGHT);

            DOM.setStyleAttribute((Element) grid.getCellFormatter().getElement(
                1, 1).getParentElement(), "background", bgColor);
          }
        });

    PageBus.subscribe("org.gwt.mosaic.pagebus.ex.country.select",
        new SubscriberCallback() {
          public void onMessage(String subject, Object message,
              Object subscriberData) {
            final Country country = (Country) message;
            final double internetUsers = countryInternetInfo.get(country.getCode());
            String bgColor;
            if (internetUsers < 0.3) {
              bgColor = "red";
            } else if (internetUsers < 0.6) {
              bgColor = "white";
            } else {
              bgColor = "tan";
            }
            grid.setHTML(2, 1, "<b>" + internetUsers + "</b>");
            grid.getCellFormatter().setHorizontalAlignment(2, 1,
                HasHorizontalAlignment.ALIGN_RIGHT);

            DOM.setStyleAttribute((Element) grid.getCellFormatter().getElement(
                2, 1).getParentElement(), "background", bgColor);
          }
        });

    layoutPanel.add(grid, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    return layoutPanel;
  }

  class Country {
    private String code;
    private String name;

    public Country(String code, String name) {
      super();
      this.code = code;
      this.name = name;
    }

    /**
     * @return the code
     */
    public String getCode() {
      return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
      this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
      return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
      this.name = name;
    }

    /**
     * @return
     * @see java.lang.String#toString()
     */
    public String toString() {
      return "Country: {Code: " + code + ", Name: " + name + "}";
    }

  }
}
