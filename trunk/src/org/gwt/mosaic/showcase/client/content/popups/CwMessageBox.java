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

import java.util.Date;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.Showcase;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;
import org.gwt.mosaic.ui.client.MessageBox.MessageBoxType;
import org.gwt.mosaic.ui.client.MessageBox.PromptCallback;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@ShowcaseStyle( {".mosaic-WindowPanel", ".dragdrop-positioner"})
public class CwMessageBox extends ContentWidget {

  /**
   * The constants used in this <code>ContentWidget</code>.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {
    String mosaicMessageBoxName();
    
    String mosaicMessageBoxDescription();
  }

  /**
   * An instance of the constants
   */
  @ShowcaseData
  private CwConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwMessageBox(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }

  @Override
  public String getDescription() {
    return constants.mosaicMessageBoxDescription();
  }

  @Override
  public String getName() {
    return constants.mosaicMessageBoxName();
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel();
    layoutPanel.setPadding(0);

    ScrollPanel scrollPanel = new ScrollPanel();
    layoutPanel.add(scrollPanel, new FillLayoutData(false));

    // Add the button and list to a panel
    VerticalPanel vPanel = new VerticalPanel();
    vPanel.setSpacing(4);

    scrollPanel.add(vPanel);

    //
    // Alert Box
    //

    HTML alertDesc = new HTML(
        "<b>Alert Box</b>"
            + "<br><small>An alert box is often used if you want to make sure information comes through to the user. "
            + "When an alert box pops up, the user will have to click \"OK\" to proceed.</small>");
    vPanel.add(alertDesc);

    Button alertBtn = new Button("Warning");
    alertBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        MessageBox.alert("Warning", "I am a warning box!");
      }
    });

    Button errorBtn = new Button("Error");
    errorBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        MessageBox.error("Error", "I am an error box!");
      }
    });

    Button infoBtn = new Button("Info");
    infoBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        MessageBox.info("Info", "I am an info box!");
      }
    });

    HorizontalPanel hpanel1 = new HorizontalPanel();
    hpanel1.add(alertBtn);
    hpanel1.add(new HTML("&nbsp;"));
    hpanel1.add(errorBtn);
    hpanel1.add(new HTML("&nbsp;"));
    hpanel1.add(infoBtn);

    vPanel.add(hpanel1);

    //
    // Confirmation Box
    //

    HTML confirmDesc = new HTML(
        "<b>Confirmation Box</b>"
            + "<br><small>A confirm box is often used if you want the user to verify or accept something. "
            + "When a confirm box pops up, the user will have to click either \"OK\" or \"Cancel\" to proceed."
            + "If the user clicks \"OK\", the box returns true. If the user clicks \"Cancel\", the box returns false.</small>");
    vPanel.add(confirmDesc);

    Button confirmBtn = new Button("Show Me");
    confirmBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        MessageBox.confirm("Confirmation Box", "I am a confirmation box!",
            new ConfirmationCallback() {
              public void onResult(boolean result) {
        	  Showcase.notifyTrayEvent("Prompt Box", "Result is '" + result + "'");
              }
            });
      }
    });
    vPanel.add(confirmBtn);

    //
    // Prompt Box
    //

    HTML promptDesc = new HTML(
        "<b>Prompt Box</b>"
            + "<br><small>A prompt box is often used if you want the user to input a value. "
            + "When a prompt box pops up, the user will have to click either \"OK\" or \"Cancel\" to proceed after entering an input value. "
            + "If the user clicks \"OK\" the box returns the input value. If the user clicks \"Cancel\" the box returns null.</small>");
    vPanel.add(promptDesc);

    Button promptBtn1 = new Button("Standard");
    promptBtn1.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        MessageBox.prompt("Prompt Box", "Please enter your name", "George",
            new PromptCallback<String>() {
              public void onResult(String input) {
        	  Showcase.notifyTrayEvent("Prompt Box", "Your name is: '" + input + "'");
              }
            });
      }
    });

    Button promptBtn2 = new Button("Multiline");
    promptBtn2.setEnabled(false);

    Button promptBtn3 = new Button("Rich Text");
    promptBtn3.setEnabled(false);

    Button promptBtn4 = new Button("DatePicker");
    promptBtn4.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        MessageBox.prompt("DatePicker Box", new Date(),
            new PromptCallback<Date>() {
              public void onResult(Date input) {
        	  Showcase.notifyTrayEvent("DatePicker Box", "You entered: '" + input + "'");
              }
            });
      }
    });

    Button promptBtn5 = new Button("DateTimePicker");
    promptBtn5.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        MessageBox.prompt("DateTimePicker Box", new Date(), false,
            new PromptCallback<Date>() {
              public void onResult(Date input) {
        	  Showcase.notifyTrayEvent("DateTimePicker Box", "You entered: '" + input
                    + "'");
              }
            });
      }
    });

    HorizontalPanel hpanel2 = new HorizontalPanel();
    hpanel2.add(promptBtn1);
    hpanel2.add(new HTML("&nbsp;"));
    hpanel2.add(promptBtn2);
    hpanel2.add(new HTML("&nbsp;"));
    hpanel2.add(promptBtn3);
    hpanel2.add(new HTML("&nbsp;"));
    hpanel2.add(promptBtn4);
    hpanel2.add(new HTML("&nbsp;"));
    hpanel2.add(promptBtn5);

    vPanel.add(hpanel2);

    //
    // Custom
    //

    HTML customDesc = new HTML("<b>Custom</b>"
        + "<br><small>Some custom MessageBox examples.</small>");
    vPanel.add(customDesc);

    Button customBtn1 = new Button("Login Form");
    customBtn1.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        showLoginForm();
      }
    });

    Button customBtn2 = new Button("RichTextArea Prompt");
    customBtn2.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        richTextAreaPrompt(new PromptCallback<String>() {
          public void onResult(String input) {
              Showcase.notifyTrayEvent("RichTextArea Prompt", input);
          }
        });
      }
    });

    HorizontalPanel hpanel3 = new HorizontalPanel();
    hpanel3.add(customBtn1);
    hpanel3.add(new HTML("&nbsp;"));
    hpanel3.add(customBtn2);

    vPanel.add(hpanel3);

    return layoutPanel;
  }

  /**
   * 
   */
  @ShowcaseSource
  private void showLoginForm() {
    final TextBox username = new TextBox();
    final PasswordTextBox passwd = new PasswordTextBox();

    final MessageBox prompt = new MessageBox(MessageBoxType.PASSWORD,
        "Login Form") {
      @Override
      public void onClose(boolean result) {
        hide();
        if (result) {
            Showcase.notifyTrayEvent("Login Form", "Form submitted!");
        } else {
            Showcase.notifyTrayEvent("Login Form", "You clicked 'Cancel'.");
        }
      }
    };
    prompt.setAnimationEnabled(true);
    final int width = Window.getClientWidth();
    prompt.setWidth(Math.max(width / 3, 256) + "px");

    // Create a panel to hold all of the form widgets
    final LayoutPanel panel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
    panel.setPadding(0);

    // Create a method=POST FormPanel and point it at a service
    final FormPanel form = new FormPanel();
    form.setAction("/myLoginFormHandler");
    form.setMethod(FormPanel.METHOD_POST);

    form.setWidget(panel);

    panel.add(new HTML("User name:"), new BoxLayoutData(FillStyle.HORIZONTAL));
    panel.add(username, new BoxLayoutData(FillStyle.HORIZONTAL));

    panel.add(new HTML("Password:"), new BoxLayoutData(FillStyle.HORIZONTAL));
    panel.add(passwd, new BoxLayoutData(FillStyle.HORIZONTAL));

    // Add a 'submit' button.
    Button buttonSubmit = new Button("Submit");
    buttonSubmit.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        form.submit();
      }
    });
    prompt.getButtonPanel().add(buttonSubmit);

    // Add a 'cancel' button.
    Button buttonCancel = new Button("Cancel");
    buttonCancel.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        prompt.onClose(false);
      }
    });
    prompt.getButtonPanel().add(buttonCancel);

    // Add an event handler to the form.
    form.addFormHandler(new FormHandler() {
      public void onSubmit(FormSubmitEvent event) {
        // validate username/passwd fields
      }

      public void onSubmitComplete(FormSubmitCompleteEvent event) {
        prompt.onClose(true);
      }
    });

    prompt.setWidget(form);
    prompt.center();

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        username.setFocus(true);
      }
    });
  }

  /**
   * 
   */
  @ShowcaseSource
  public static void richTextAreaPrompt(final PromptCallback callback) {
    // Create the text area and toolbar
    final RichTextArea area = new RichTextArea();
    area.ensureDebugId("cwRichText-area");
    area.setSize("100%", "14em");

    DOM.setStyleAttribute(area.getElement(), "background", "white");

    final MessageBox prompt = new MessageBox("RichTextArea Prompt") {
      @Override
      public void onClose(boolean result) {
        hide();
        if (result) {
          callback.onResult(area.getText());
        } else {
          callback.onResult(null);
        }
      }
    };
    prompt.setAnimationEnabled(true);
    // final int width = Window.getClientWidth();
    // prompt.setWidth(Math.max(width / 3, 256) + "px");

    RichTextToolbar toolbar = new RichTextToolbar(area);
    toolbar.ensureDebugId("cwRichText-toolbar");

    LayoutPanel panel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
    panel.setPadding(0);
    panel.setWidgetSpacing(0);
    panel.add(toolbar, new BoxLayoutData(FillStyle.HORIZONTAL));
    panel.add(area, new BoxLayoutData(FillStyle.BOTH));

    Button buttonOK = new Button("OK");
    buttonOK.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        prompt.onClose(true);
      }
    });

    Button buttonCancel = new Button("Cancel");
    buttonCancel.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        prompt.onClose(false);
      }
    });

    prompt.getButtonPanel().add(buttonOK);
    prompt.getButtonPanel().add(buttonCancel);

    prompt.setWidget(panel, 0);
    prompt.center();

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        area.setFocus(true);
      }
    });
  }

}
