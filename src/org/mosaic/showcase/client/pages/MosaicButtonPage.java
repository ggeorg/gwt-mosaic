package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.pages.Annotations.MosaicData;
import org.mosaic.showcase.client.pages.Annotations.MosaicSource;
import org.mosaic.showcase.client.pages.Annotations.MosaicStyle;
import org.mosaic.ui.client.InfoPanel;
import org.mosaic.ui.client.MosaicButton;
import org.mosaic.ui.client.PopupMenu;
import org.mosaic.ui.client.MosaicButton.ButtonStyle;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@MosaicStyle( {".gwt-Button", ".mosaic-Button", ".mosaic-Menu-Button", ".mosaic-Split-Button"})
public class MosaicButtonPage extends Page implements ClickListener {

  /**
   * The constants used in this Page.
   */
  @MosaicSource
  public static interface DemoConstants extends Constants, Page.DemoConstants {

  }

  /**
   * An instance of the constants.
   */
  @MosaicData
  private DemoConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public MosaicButtonPage(DemoConstants constants) {
    super(constants);
    this.constants = constants;
  }

  /**
   * 
   * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
   */
  @MosaicSource
  public void onClick(Widget sender) {
    InfoPanel.show("Mosaic Button", "Clicked!");
  }

  /**
   * Load this example.
   */
  @MosaicSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    final BoxLayout vLayout = new BoxLayout(Orientation.VERTICAL);
    layoutPanel.setLayout(vLayout);

    //
    // Push buttons
    //

    layoutPanel.add(new HTML("Push buttons"));

    final LayoutPanel hBox1 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox1, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    // Add a push button
    MosaicButton pushButton = new MosaicButton("Hello");
    pushButton.addClickListener(this);
    pushButton.ensureDebugId("mosaicPushButton-normal");

    hBox1.add(pushButton);

    // Add a disabled push button
    MosaicButton disabledPushButton = new MosaicButton("Hello");
    disabledPushButton.setEnabled(false);
    pushButton.ensureDebugId("mosaicPushButton-disabled");

    hBox1.add(disabledPushButton);

    //
    // Menu buttons
    //

    layoutPanel.add(new HTML("Menu buttons"));

    final LayoutPanel hBox2 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox2, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    // Add a menu button
    MosaicButton menuButton = new MosaicButton("Hello");
    menuButton.setStyle(ButtonStyle.MENU);
    menuButton.addClickListener(this);
    menuButton.ensureDebugId("mosaicMenuButton-normal");

    // Make a command that we will execute from all menu items.
    Command cmd1 = new Command() {
      public void execute() {
        InfoPanel.show("Menu Button", "You selected a menu item!");
      }
    };

    PopupMenu menuBtnMenu = new PopupMenu();
    menuBtnMenu.addItem("Item 1", cmd1);
    menuBtnMenu.addItem("Item 2", cmd1);
    menuBtnMenu.addSeparator();
    menuBtnMenu.addItem("Item 3", cmd1);
    menuBtnMenu.addItem("Item 4", cmd1);

    menuButton.setMenu(menuBtnMenu);

    hBox2.add(menuButton);

    // Add a disabled menu button
    MosaicButton disabledMenuButton = new MosaicButton("Hello");
    disabledMenuButton.setStyle(ButtonStyle.MENU);
    disabledMenuButton.setEnabled(false);
    disabledMenuButton.ensureDebugId("mosaicMenuButton-disabled");

    hBox2.add(disabledMenuButton);

    //
    // Split buttons
    //

    layoutPanel.add(new HTML("Split buttons"));

    final LayoutPanel hBox3 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox3, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    // Add a menu button
    MosaicButton splitButton = new MosaicButton("Hello");
    splitButton.setStyle(ButtonStyle.SPLIT);
    splitButton.addClickListener(this);
    splitButton.ensureDebugId("mosaicSplitButton-normal");

    // Make a command that we will execute from all menu items.
    Command cmd2 = new Command() {
      public void execute() {
        InfoPanel.show("Split Button", "You selected a menu item!");
      }
    };

    PopupMenu splitBtnMenu = new PopupMenu();
    splitBtnMenu.addItem("Item 1", cmd2);
    splitBtnMenu.addItem("Item 2", cmd2);
    splitBtnMenu.addSeparator();
    splitBtnMenu.addItem("Item 3", cmd2);
    splitBtnMenu.addItem("Item 4", cmd2);

    splitButton.setMenu(splitBtnMenu);

    hBox3.add(splitButton);

    // Add a disabled menu button
    MosaicButton disabledSplitButton = new MosaicButton("Hello");
    disabledSplitButton.setStyle(ButtonStyle.SPLIT);
    disabledSplitButton.setEnabled(false);
    disabledSplitButton.ensureDebugId("mosaicSplitButton-disabled");

    hBox3.add(disabledSplitButton);

    //
    // Checkbox buttons
    //

    layoutPanel.add(new HTML("Checkbox buttons"));

    final LayoutPanel hBox4 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox4, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    // Add a checkbox button
    MosaicButton checkButton1 = new MosaicButton("Hello");
    checkButton1.setStyle(ButtonStyle.CHECKBOX);
    checkButton1.addClickListener(this);
    checkButton1.ensureDebugId("mosaicCheckboxButton-normal");

    hBox4.add(checkButton1);

    // Add a second checkbox button
    MosaicButton checkButton2 = new MosaicButton("Hello");
    checkButton2.setStyle(ButtonStyle.CHECKBOX);
    checkButton2.addClickListener(this);
    checkButton2.ensureDebugId("mosaicCheckboxButton-normal");

    hBox4.add(checkButton2);

    // Add a third checkbox button
    MosaicButton checkButton3 = new MosaicButton("Hello");
    checkButton3.setStyle(ButtonStyle.CHECKBOX);
    checkButton3.addClickListener(this);
    checkButton3.ensureDebugId("mosaicCheckboxButton-normal");

    hBox4.add(checkButton3);

    // Add a third checkbox button
    MosaicButton checkButton4 = new MosaicButton("Hello");
    checkButton4.setStyle(ButtonStyle.CHECKBOX);
    checkButton4.setEnabled(false);
    checkButton4.ensureDebugId("mosaicCheckboxButton-normal");

    hBox4.add(checkButton4);
    
    //
    // Radio buttons
    //

    layoutPanel.add(new HTML("Radio buttons"));

    final LayoutPanel hBox5 = new LayoutPanel(new BoxLayout());
    layoutPanel.add(hBox5, new BoxLayoutData(FillStyle.HORIZONTAL, true));

    // Add a checkbox button
    MosaicButton radioButton1 = new MosaicButton("Hello");
    radioButton1.setStyle(ButtonStyle.RADIO);
    radioButton1.addClickListener(this);
    radioButton1.ensureDebugId("mosaicRadioButton-normal");

    hBox5.add(radioButton1);

    // Add a second checkbox button
    MosaicButton radioButton2 = new MosaicButton("Hello");
    radioButton2.setStyle(ButtonStyle.RADIO);
    radioButton2.addClickListener(this);
    radioButton2.ensureDebugId("mosaicRadioButton-normal");

    hBox5.add(radioButton2);

    // Add a third checkbox button
    MosaicButton radioButton3 = new MosaicButton("Hello");
    radioButton3.setStyle(ButtonStyle.RADIO);
    radioButton3.addClickListener(this);
    radioButton3.ensureDebugId("mosaicRadioButton-normal");

    hBox5.add(radioButton3);

    // Add a third checkbox button
    MosaicButton radioButton4 = new MosaicButton("Hello");
    radioButton4.setStyle(ButtonStyle.RADIO);
    radioButton4.setEnabled(false);
    radioButton4.ensureDebugId("mosaicRadioButton-normal");

    hBox5.add(radioButton4);
  }

}
