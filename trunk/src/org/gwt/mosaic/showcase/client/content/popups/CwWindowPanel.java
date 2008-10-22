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

import java.util.Date;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.Showcase;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.WindowPanel.WindowState;
import org.gwt.mosaic.ui.client.WindowPanel.WindowStateListener;
import org.gwt.mosaic.ui.client.datepicker.DateComboBox;
import org.gwt.mosaic.ui.client.infopanel.TrayInfoPanelNotifier;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {
    ".mosaic-Caption", ".mosaic-TitledLayoutPanel", ".mosaic-WindowPanel",
    ".dragdrop-positioner", ".dragdrop-draggable", ".dragdrop-handle",
    ".dragdrop-movable-panel"})
public class CwWindowPanel extends ContentWidget implements ClickListener {

  private WindowPanel basic;

  private WindowPanel sized;

  private WindowPanel fixed;

  private WindowPanel modal;

  private WindowPanel zIndex;

  private WindowPanel layout;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwWindowPanel(CwConstants constants) {
    super(constants);
  }

  /**
   * 
   * @param windowPanel
   */
  @ShowcaseSource
  private void addMaximizeButton(final WindowPanel windowPanel,
      CaptionRegion captionRegion) {
    final ImageButton maximizeBtn = new ImageButton(
        Caption.IMAGES.windowMaximize());
    maximizeBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (windowPanel.getWindowState() == WindowState.MAXIMIZED) {
          windowPanel.setWindowState(WindowState.NORMAL);
        } else {
          windowPanel.setWindowState(WindowState.MAXIMIZED);
        }
      }
    });
    windowPanel.addWindowStateListener(new WindowStateListener() {
      public void onWindowStateChange(WindowPanel sender) {
        if (sender.getWindowState() == WindowState.MAXIMIZED) {
          maximizeBtn.setImage(Caption.IMAGES.windowRestore().createImage());
        } else {
          maximizeBtn.setImage(Caption.IMAGES.windowMaximize().createImage());
        }

      }
    });
    windowPanel.getHeader().add(maximizeBtn, captionRegion);
  }

  /**
   * 
   * @param windowPanel
   */
  @ShowcaseSource
  private void addMinimizeButton(final WindowPanel windowPanel,
      CaptionRegion captionRegion) {
    final ImageButton minimizeBtn = new ImageButton(
        Caption.IMAGES.windowMinimize());
    minimizeBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        windowPanel.setWindowState(WindowState.MINIMIZED);
      }
    });
    windowPanel.getHeader().add(minimizeBtn, captionRegion);
  }

  /**
   * The 'basic' window panel.
   */
  @ShowcaseSource
  private void createBasicWindowPanel() {
    basic = new WindowPanel("Basic");
    basic.setAnimationEnabled(true);
    basic.setWidget(new HTML("Hello World!"));

    basic.getHeader().add(Showcase.IMAGES.window().createImage());

    addMaximizeButton(basic, CaptionRegion.RIGHT);
    addMinimizeButton(basic, CaptionRegion.RIGHT);

    basic.addWindowCloseListener(new WindowCloseListener() {
      public void onWindowClosed() {
        basic = null;
      }

      public String onWindowClosing() {
        return null;
      }
    });
  }

  /**
   * The 'fixed' window panel.
   */
  @ShowcaseSource
  private void createFixedWindowPanel() {
    fixed = new WindowPanel("Fixed");
    fixed.setResizable(false);
    fixed.setAnimationEnabled(true);
    Image img = new Image("MeteoraGreece.JPG");
    fixed.setWidget(img);

    fixed.getHeader().add(Showcase.IMAGES.window().createImage());

    addMinimizeButton(fixed, CaptionRegion.RIGHT);

    fixed.addWindowCloseListener(new WindowCloseListener() {
      public void onWindowClosed() {
        fixed = null;
      }

      public String onWindowClosing() {
        return null;
      }
    });
  }

  /**
   * Create content for layout.
   */
  @ShowcaseSource
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

  /**
   * The 'layout' window panel.
   */
  @ShowcaseSource
  private void createLayoutWindowPanel() {
    layout = new WindowPanel("Layout");
    layout.setAnimationEnabled(true);
    LayoutPanel panel = new LayoutPanel();
    layout.setWidget(panel);
    createLayoutContent(panel);

    layout.getHeader().add(Showcase.IMAGES.window().createImage());

    addMaximizeButton(layout, CaptionRegion.RIGHT);
    addMinimizeButton(layout, CaptionRegion.RIGHT);

    layout.addWindowCloseListener(new WindowCloseListener() {
      public void onWindowClosed() {
        layout = null;
      }

      public String onWindowClosing() {
        return null;
      }
    });
  }

  /**
   * The 'modal' window panel.
   */
  @ShowcaseSource
  private void createModalWindowPanel() {
    modal = new WindowPanel("Modal");
    modal.setResizable(false);
    modal.setAnimationEnabled(true);
    LayoutPanel upload = new LayoutPanel();
    modal.setWidget(upload);
    createUploadFileContent(upload);

    modal.getHeader().add(Showcase.IMAGES.window().createImage());

    modal.addWindowCloseListener(new WindowCloseListener() {
      public void onWindowClosed() {
        modal = null;
      }

      public String onWindowClosing() {
        return null;
      }
    });
  }

  /**
   * The 'sized' window panel.
   */
  @ShowcaseSource
  private void createSizedWindowPanel() {
    sized = new WindowPanel("Sized");
    sized.setAnimationEnabled(true);
    sized.setSize("512px", "384px");
    final Frame frame = new Frame("http://www.google.com");
    DOM.setStyleAttribute(frame.getElement(), "border", "none");
    sized.setWidget(frame);

    sized.getHeader().add(Showcase.IMAGES.window().createImage());

    addMaximizeButton(sized, CaptionRegion.RIGHT);
    addMinimizeButton(sized, CaptionRegion.RIGHT);

    final ImageButton refreshBtn = new ImageButton(Caption.IMAGES.toolRefresh());
    refreshBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        frame.setUrl(frame.getUrl());
      }
    });
    sized.getHeader().add(refreshBtn, CaptionRegion.RIGHT);

    sized.addWindowCloseListener(new WindowCloseListener() {
      public void onWindowClosed() {
        sized = null;
      }

      public String onWindowClosing() {
        return null;
      }
    });
  }

  /**
   * Create content for upload.
   */
  @ShowcaseSource
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

  /**
   * Create content for z-index test.
   */
  @ShowcaseSource
  private void createZIndexTestContent(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.setPadding(5);

    // Make a command that we will execute from all menu items.
    Command cmd1 = new Command() {
      public void execute() {
        TrayInfoPanelNotifier.notifyTrayEvent("Menu Button",
            "You selected a menu item!");
      }
    };

    // Create a menu bar
    MenuBar menu = new MenuBar();
    menu.setAnimationEnabled(true);

    MenuBar menu1 = new MenuBar(true);
    menu1.setAnimationEnabled(true);
    menu.addItem(new MenuItem("Menu 1", menu1));
    menu1.addItem("Item 1", cmd1);
    menu1.addItem("Item 2", cmd1);
    menu1.addSeparator();
    menu1.addItem("Item 3", cmd1);
    menu1.addItem("Item 4", cmd1);

    MenuBar menu2 = new MenuBar(true);
    menu2.setAnimationEnabled(true);
    menu.addItem(new MenuItem("Menu 2", menu2));
    menu2.addItem("Item 1", cmd1);
    menu2.addItem("Item 2", cmd1);
    menu2.addSeparator();
    menu2.addItem("Item 3", cmd1);
    menu2.addItem("Item 4", cmd1);

    layoutPanel.add(menu, new BoxLayoutData(FillStyle.HORIZONTAL));

    // Create a tool button
    ToolButton menuButton = new ToolButton("Menu Button", this);
    menuButton.setStyle(ToolButtonStyle.MENU);
    menuButton.ensureDebugId("mosaicMenuButton-normal");

    PopupMenu menuBtnMenu = new PopupMenu();
    menuBtnMenu.addItem("Item 1", cmd1);
    menuBtnMenu.addItem("Item 2", cmd1);

    menuButton.setMenu(menuBtnMenu);

    layoutPanel.add(menuButton, new BoxLayoutData(FillStyle.HORIZONTAL));

    // Create a date combo box
    final DateComboBox dateComboBox = new DateComboBox();
    dateComboBox.showDate(new Date());
    layoutPanel.add(dateComboBox, new BoxLayoutData(FillStyle.HORIZONTAL));
  }

  /**
   * The 'zIndex' window panel.
   */
  @ShowcaseSource
  private void createZIndexWindowPanel() {
    zIndex = new WindowPanel("z-index Test");
    zIndex.setResizable(false);
    zIndex.setAnimationEnabled(true);
    final LayoutPanel zIndexContent = new LayoutPanel();
    zIndex.setWidget(zIndexContent);
    createZIndexTestContent(zIndexContent);

    zIndex.getHeader().add(Showcase.IMAGES.window().createImage());

    addMaximizeButton(zIndex, CaptionRegion.RIGHT);
    addMinimizeButton(zIndex, CaptionRegion.RIGHT);

    zIndex.addWindowCloseListener(new WindowCloseListener() {
      public void onWindowClosed() {
        zIndex = null;
      }

      public String onWindowClosing() {
        return null;
      }
    });
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
   * Fired when the user clicks on a button.
   * 
   * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
   */
  @ShowcaseSource
  public void onClick(Widget sender) {
    final Button btn = (Button) sender;
    TrayInfoPanelNotifier.notifyTrayEvent(btn.getText(), "Clicked!");
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout());

    Button btn1 = new Button("Basic");
    btn1.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (basic == null) {
          createBasicWindowPanel();
        }
        if (basic.getWindowState() == WindowState.MINIMIZED) {
          basic.setWindowState(WindowState.NORMAL);
        } else {
          basic.center();
        }
      }
    });
    layoutPanel.add(btn1);

    Button btn2 = new Button("Layout");
    btn2.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (layout == null) {
          createLayoutWindowPanel();
        }
        layout.center();
      }
    });
    layoutPanel.add(btn2);

    Button btn3 = new Button("Sized");
    btn3.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (sized == null) {
          createSizedWindowPanel();
        }
        sized.center();
      }
    });
    layoutPanel.add(btn3);

    Button btn4 = new Button("Fixed");
    btn4.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (fixed == null) {
          createFixedWindowPanel();
        }
        fixed.center();
      }
    });
    layoutPanel.add(btn4);

    Button btn5 = new Button("Modal");
    btn5.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (modal == null) {
          createModalWindowPanel();
        }
        modal.showModal();
      }
    });
    layoutPanel.add(btn5);

    Button btn6 = new Button("z-index Test");
    btn6.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (zIndex == null) {
          createZIndexWindowPanel();
        }
        zIndex.center();
      }
    });
    layoutPanel.add(btn6);

    return layoutPanel;
  }

}