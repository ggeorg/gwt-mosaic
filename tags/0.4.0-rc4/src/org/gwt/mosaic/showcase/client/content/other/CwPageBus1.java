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
    return "Publish/Subscribe";
  }

  @Override
  public boolean hasStyle() {
    return false;
  }

  /**
   * 
   */
  @ShowcaseData
  private Map<String, Country> countries = new HashMap<String, Country>();

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new GridLayout(2, 2));
    layoutPanel.setPadding(0);
    
    Country country = new Country("BRZ", "Brazil", 0.13, 0.45);
    countries.put(country.getCode(), country);
    country = new Country("CHN", "China", 0.09, 0.33);
    countries.put(country.getCode(), country);
    country = new Country("FRA", "France", 0.47, 0.78);
    countries.put(country.getCode(), country);
    country = new Country("GER", "Germany", 0.61, 0.96);
    countries.put(country.getCode(), country);
    country = new Country("IND", "India", 0.05, 0.06);
    countries.put(country.getCode(), country);
    country = new Country("ITL", "Italy", 0.50, 1.24);
    countries.put(country.getCode(), country);
    country = new Country("JAP", "Japan", 0.68, 0.74);
    countries.put(country.getCode(), country);
    country = new Country("RUS", "Russia", 0.17, 0.85);
    countries.put(country.getCode(), country);
    country = new Country("GBR", "United Kingdom", 0.61, 1.0);
    countries.put(country.getCode(), country);
    country = new Country("USA", "United States", 0.68, 0.96);
    countries.put(country.getCode(), country);

    layoutPanel.add(newHighGDPCountries(), new GridLayoutData(true));
    layoutPanel.add(newMonitorMessages(), new GridLayoutData(1, 2, true));
    layoutPanel.add(newStatisticsByCountry(), new GridLayoutData(true));

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
    for (Country country : countries.values()) {
      listBox.addItem(country.getName(), country.getCode());
    }

    layoutPanel.add(listBox, new BoxLayoutData(FillStyle.BOTH, true));

    listBox.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        int index = listBox.getSelectedIndex();
        if (index == -1) {
          return;
        }
        PageBus.publish("org.gwt.mosaic.pagebus.ex.country.select",
            countries.get(listBox.getValue(index)));
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
        
        scrollPanel.scrollToBottom();
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
            final double mobilePhonesUsers = country.getMobilePhones();
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
            final double internetUsers = country.getInternet();
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

  static class Country {
    private String code;
    private String name;
    private double internet;
    private double mobilePhones;

    public Country(String code, String name, double internet,
        double mobilePhones) {
      super();
      this.code = code;
      this.name = name;
      this.internet = internet;
      this.mobilePhones = mobilePhones;
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
     * @return the internet
     */
    public double getInternet() {
      return internet;
    }

    /**
     * @param internet the internet to set
     */
    public void setInternet(double internet) {
      this.internet = internet;
    }

    /**
     * @return the mobilePhones
     */
    public double getMobilePhones() {
      return mobilePhones;
    }

    /**
     * @param mobilePhones the mobilePhones to set
     */
    public void setMobilePhones(double mobilePhones) {
      this.mobilePhones = mobilePhones;
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
