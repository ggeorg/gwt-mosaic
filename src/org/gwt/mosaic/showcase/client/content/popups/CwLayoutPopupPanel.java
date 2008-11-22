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
package org.gwt.mosaic.showcase.client.content.popups;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.DecoratedLayoutPopupPanel;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.LayoutPopupPanel;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {
    ".mosaic-Caption", ".mosaic-TitledLayoutPanel", ".mosaic-WindowPanel",
    ".dragdrop-positioner", ".dragdrop-draggable", ".dragdrop-handle",
    ".dragdrop-movable-panel"})
public class CwLayoutPopupPanel extends ContentWidget implements ClickListener {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwLayoutPopupPanel(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Layout PopupPanel description";
  }

  @Override
  public String getName() {
    return "Layout PopupPanel";
  }

  /**
   * Fired when the user clicks on a button.
   * 
   * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
   */
  @ShowcaseSource
  public void onClick(Widget sender) {
    final Button btn = (Button) sender;
    InfoPanel.show(btn.getText(), "Clicked!");
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    ScrollLayoutPanel vBox = new ScrollLayoutPanel();

    LayoutPanel layoutPanel1 = new LayoutPanel(new BoxLayout());
    vBox.add(layoutPanel1);

    layoutPanel1.add(new Button("Plain", new ClickListener() {
      public void onClick(Widget sender) {
        LayoutPopupPanel popup = new LayoutPopupPanel(true);
        popup.add(createContent1());
        popup.center();
      }
    }));
    
    layoutPanel1.add(new Button("Plain 512x?", new ClickListener() {
      public void onClick(Widget sender) {
        LayoutPopupPanel popup = new LayoutPopupPanel(true);
        popup.setWidth("512px");
        popup.add(createContent1());
        popup.center();
      }
    }));
    
    layoutPanel1.add(new Button("Plain ?x384", new ClickListener() {
      public void onClick(Widget sender) {
        LayoutPopupPanel popup = new LayoutPopupPanel(true);
        popup.setHeight("384px");
        popup.add(createContent1());
        popup.center();
      }
    }));

    layoutPanel1.add(new Button("Plain 512x384", new ClickListener() {
      public void onClick(Widget sender) {
        final LayoutPopupPanel popup = new LayoutPopupPanel(true);
        popup.setSize("512px", "384px");
        popup.add(createContent2(popup));
        popup.center();
      }
    }));
    
    // --------------
    
    LayoutPanel layoutPanel2 = new LayoutPanel(new BoxLayout());
    vBox.add(layoutPanel2);
    
    layoutPanel2.add(new Button("Decorated", new ClickListener() {
      public void onClick(Widget sender) {
        DecoratedLayoutPopupPanel popup = new DecoratedLayoutPopupPanel(true);
        popup.add(createContent1());
        popup.center();
      }
    }));
    
    layoutPanel2.add(new Button("Decorated 512x?", new ClickListener() {
      public void onClick(Widget sender) {
        DecoratedLayoutPopupPanel popup = new DecoratedLayoutPopupPanel(true);
        popup.setWidth("512px");
        popup.add(createContent1());
        popup.center();
      }
    }));
    
    layoutPanel2.add(new Button("Decorated ?x384", new ClickListener() {
      public void onClick(Widget sender) {
        DecoratedLayoutPopupPanel popup = new DecoratedLayoutPopupPanel(true);
        popup.setHeight("384px");
        popup.add(createContent1());
        popup.center();
      }
    }));

    layoutPanel2.add(new Button("Decorated 512x384", new ClickListener() {
      public void onClick(Widget sender) {
        final DecoratedLayoutPopupPanel popup = new DecoratedLayoutPopupPanel(true);
        popup.setSize("512px", "384px");
        popup.add(createContent2(popup));
        popup.center();
      }
    }));
    
    // ==============
    
    LayoutPanel layoutPanel3 = new LayoutPanel(new BoxLayout());
    vBox.add(layoutPanel3);

    layoutPanel3.add(new Button("WindowPanel", new ClickListener() {
      public void onClick(Widget sender) {
        WindowPanel popup = new WindowPanel("WindowPanel");
        popup.add(createContent1());
        popup.center();
      }
    }));
    
    layoutPanel3.add(new Button("WindowPanel 512x?", new ClickListener() {
      public void onClick(Widget sender) {
        WindowPanel popup = new WindowPanel("WindowPanel 512x?");
        popup.setWidth("512px");
        popup.add(createContent1());
        popup.center();
      }
    }));
    
    layoutPanel3.add(new Button("WindowPanel ?x384", new ClickListener() {
      public void onClick(Widget sender) {
        WindowPanel popup = new WindowPanel("WindowPanel ?x384");
        popup.setHeight("384px");
        popup.add(createContent1());
        popup.center();
      }
    }));

    layoutPanel3.add(new Button("WindowPanel 512x384", new ClickListener() {
      public void onClick(Widget sender) {
        WindowPanel popup = new WindowPanel("WindowPanel 512x384");
        popup.setSize("512px", "384px");
        popup.add(createContent2(popup));
        popup.center();
      }
    }));

    return vBox;
  }

  private Widget createContent1() {
    final LayoutPanel panel = new LayoutPanel(new BorderLayout());
    panel.setPadding(0);

    panel.add(new Button("North"), new BorderLayoutData(
        BorderLayoutRegion.NORTH));
    panel.add(new Button("South"), new BorderLayoutData(
        BorderLayoutRegion.SOUTH));
    panel.add(new Button("West"), new BorderLayoutData(BorderLayoutRegion.WEST));
    panel.add(new Button("East"), new BorderLayoutData(BorderLayoutRegion.EAST));
    panel.add(new Button("Center"));

    return panel;
  }
  
  private ClickListener cl1, cl2;

  private Widget createContent2(final PopupPanel popup) {
    final LayoutPanel panel = new LayoutPanel(new BorderLayout());
    panel.setPadding(0);

    cl1 = new ClickListener() {
      public void onClick(Widget sender) {
        popup.setPixelSize(256, 192);
        ((HasLayoutManager) popup).layout();
        popup.center();
        ((Button) sender).removeClickListener(this);
        ((Button) sender).addClickListener(cl2);
      }
    };

    cl2 = new ClickListener() {
      public void onClick(Widget sender) {
        popup.setPixelSize(512, 384);
        ((HasLayoutManager) popup).layout();
        popup.center();
        ((Button) sender).removeClickListener(this);
        ((Button) sender).addClickListener(cl1);
      }
    };

    panel.add(new Button("North"), new BorderLayoutData(BorderLayoutRegion.NORTH));
    panel.add(new Button("South"), new BorderLayoutData(BorderLayoutRegion.SOUTH));
    panel.add(new Button("West"), new BorderLayoutData(BorderLayoutRegion.WEST));
    panel.add(new Button("East"), new BorderLayoutData(BorderLayoutRegion.EAST));
    panel.add(new Button("Click Me!", cl1));
    
    return panel;
  }

}