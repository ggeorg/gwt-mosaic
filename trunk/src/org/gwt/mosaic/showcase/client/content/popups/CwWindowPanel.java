/*
 * Copyright 2008 Georgios J. Georgopoulos.
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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.Showcase;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 */
@ShowcaseStyle( {
    ".mosaic-Caption", ".mosaic-TitledLayoutPanel", ".mosaic-WindowPanel",
    ".dragdrop-positioner", ".dragdrop-draggable", ".dragdrop-handle",
    ".dragdrop-movable-panel"})
public class CwWindowPanel extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwWindowPanel(CwConstants constants) {
    super(constants);
    // TODO Auto-generated constructor stub
  }

  @Override
  public String getDescription() {
    return "WindowPanel description";
  }

  @Override
  public String getName() {
    return "WindowPanel";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout());

    final WindowPanel basic = new WindowPanel("Basic");
    basic.setAnimationEnabled(true);
    basic.setWidget(new HTML("Hello World!"));
    
    basic.getHeader().add(Showcase.IMAGES.window().createImage());

    final WindowPanel layout = new WindowPanel("Layout");
    layout.setAnimationEnabled(true);
    LayoutPanel panel = new LayoutPanel();
    layout.setWidget(panel);
    createLayoutContent(panel);
    
    layout.getHeader().add(Showcase.IMAGES.window().createImage());

    final WindowPanel sized = new WindowPanel("Sized");
    sized.setAnimationEnabled(true);
    sized.setSize("512px", "385px");
    final Frame frame = new Frame("http://www.google.com");
    DOM.setStyleAttribute(frame.getElement(), "border", "none");
    sized.setWidget(frame);
    
    sized.getHeader().add(Showcase.IMAGES.window().createImage());
    
    final ImageButton refreshBtn = new ImageButton(Caption.IMAGES.toolRefresh());
    refreshBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        frame.setUrl(frame.getUrl());
      }
    });
    sized.getHeader().add(refreshBtn, CaptionRegion.RIGHT);

    final WindowPanel fixed = new WindowPanel("Fixed", false, false);
    fixed.setAnimationEnabled(true);
    Image img = new Image("MeteoraGreece.JPG");
    fixed.setWidget(img);
    
    fixed.getHeader().add(Showcase.IMAGES.window().createImage());

    final WindowPanel modal = new WindowPanel("Modal", false, true);
    modal.setAnimationEnabled(true);
    LayoutPanel upload = new LayoutPanel();
    modal.setWidget(upload);
    createUploadFileContent(upload);
    
    modal.getHeader().add(Showcase.IMAGES.window().createImage());

    Button btn1 = new Button("Basic");
    btn1.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        basic.center();
      }
    });
    layoutPanel.add(btn1);

    Button btn2 = new Button("Layout");
    btn2.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        layout.center();
      }
    });
    layoutPanel.add(btn2);

    Button btn3 = new Button("Sized");
    btn3.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        sized.center();
      }
    });
    layoutPanel.add(btn3);

    Button btn4 = new Button("Fixed");
    btn4.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        fixed.center();
      }
    });
    layoutPanel.add(btn4);

    Button btn5 = new Button("Modal");
    btn5.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        modal.center();
      }
    });
    layoutPanel.add(btn5);

    return layoutPanel;
  }

  private void createLayoutContent(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BorderLayout());
    layoutPanel.setPadding(5);

    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");
    final Button b5 = new Button("Button 5");

    layoutPanel.add(b1, new BorderLayoutData(BorderLayoutRegion.NORTH, 10, 200));
    layoutPanel.add(b2, new BorderLayoutData(BorderLayoutRegion.SOUTH, 10, 200));
    layoutPanel.add(b3, new BorderLayoutData(BorderLayoutRegion.WEST, 10, 200));
    layoutPanel.add(b4, new BorderLayoutData(BorderLayoutRegion.EAST, 10, 200));
    layoutPanel.add(b5, new BorderLayoutData(BorderLayoutRegion.CENTER, true));
  }

  private void createUploadFileContent(LayoutPanel layoutPanel) {
    // Create a vertical panel to align the content
    VerticalPanel vPanel = new VerticalPanel();

    // Add a label
    vPanel.add(new HTML("Select a file"));

    // Add a file upload widget
    final FileUpload fileUpload = new FileUpload();
    fileUpload.ensureDebugId("cwFileUpload");
    vPanel.add(fileUpload);

    // Add a button to upload the file
    Button uploadButton = new Button("Upload File");
    uploadButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        String filename = fileUpload.getFilename();
        if (filename.length() == 0) {
          MessageBox.alert("Upload File", "You must select a file to upload");
        } else {
          MessageBox.alert("Upload File", "File uploaded!");
        }
      }
    });
    vPanel.add(new HTML("<br>"));
    vPanel.add(uploadButton);

    layoutPanel.add(vPanel);
    layoutPanel.setPadding(5);
  }

}