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
package org.gwt.mosaic.ui.client;

import java.util.Date;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.datepicker.DatePicker;
import org.gwt.mosaic.ui.client.datepicker.DateTimePicker;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public abstract class MessageBox extends WindowPanel {

  public interface ConfirmationCallback {
    void onResult(boolean result);
  }

  public enum MessageBoxType {
    ALERT, CONFIRM, ERROR, INFO, PASSWORD, PLAIN, PROMPT
  }

  public interface PromptCallback<T> {
    void onResult(T input);
  };

  /**
   * The caption images to use.
   */
  public static final MessageBoxImages MESSAGEBOX_IMAGES = (MessageBoxImages) GWT.create(MessageBoxImages.class);

  private static final MessageBoxType DEFAULT_TYPE = MessageBoxType.PLAIN;

  private static void alert(MessageBoxType type, String caption, String message) {
    final MessageBox alert = new MessageBox(type, caption) {
      @Override
      public void onClose(boolean result) {
        hide();
      }
    };
    alert.setAnimationEnabled(true);
    int preferredWidth = Window.getClientWidth();
    preferredWidth = Math.max(preferredWidth / 3, 256);
    alert.setWidth(preferredWidth + "px");

    final Button buttonOK = new Button("OK");
    buttonOK.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        alert.hide();
      }
    });
    alert.getButtonPanel().add(buttonOK);

    alert.setWidget(new HTML(message));
    alert.showModal();

    if (alert.getOffsetWidth() < preferredWidth) {
      alert.setWidth(preferredWidth + "px");
      alert.center();
    }

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        buttonOK.setFocus(true);
      }
    });
  }

  public static void alert(String caption, String message) {
    alert(MessageBoxType.ALERT, caption, message);
  }

  public static void confirm(String caption, String message,
      final ConfirmationCallback callback) {
    final MessageBox confirm = new MessageBox(MessageBoxType.CONFIRM, caption) {
      @Override
      public void onClose(boolean result) {
        hide();
        callback.onResult(result);
      }
    };
    confirm.setAnimationEnabled(true);
    int preferredWidth = Window.getClientWidth();
    preferredWidth = Math.max(preferredWidth / 3, 256);
    confirm.setWidth(preferredWidth + "px");

    final Button buttonOK = new Button("OK");
    buttonOK.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        confirm.onClose(true);
      }
    });

    final Button buttonCancel = new Button("Cancel");
    buttonCancel.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        confirm.onClose(false);
      }
    });

    confirm.getButtonPanel().add(buttonOK);
    confirm.getButtonPanel().add(buttonCancel);

    confirm.setWidget(new HTML(message));
    confirm.showModal();

    if (confirm.getOffsetWidth() < preferredWidth) {
      confirm.setWidth(preferredWidth + "px");
      confirm.center();
    }

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        buttonOK.setFocus(true);
      }
    });
  }

  public static void error(String caption, String message) {
    alert(MessageBoxType.ERROR, caption, message);
  }

  public static void info(String caption, String message) {
    alert(MessageBoxType.INFO, caption, message);
  }

  public static void prompt(String caption, Date defaultValue,
      boolean use24Hours, final PromptCallback<Date> callback) {
    final DateTimePicker dateTimePicker = new DateTimePicker(use24Hours);
    // dateTimePicker.getDatePicker().setSelectedDate(defaultValue, true);
    dateTimePicker.getTimePicker().setDateTime(defaultValue);

    final MessageBox prompt = new MessageBox(caption) {
      @Override
      public void onClose(boolean result) {
        hide();
        if (result) {
          callback.onResult(dateTimePicker.getDate());
        } else {
          callback.onResult(null);
        }
      }
    };
    prompt.setAnimationEnabled(true);
    int preferredWidth = Window.getClientWidth();
    preferredWidth = Math.max(preferredWidth / 3, 256);
    prompt.setWidth(preferredWidth + "px");

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

    prompt.setWidget(dateTimePicker, 0);
    prompt.showModal();
    
    if (prompt.getOffsetWidth() < preferredWidth) {
      prompt.setWidth(preferredWidth + "px");
      prompt.center();
    }
  }

  public static void prompt(String caption, Date defaultValue,
      final PromptCallback<Date> callback) {
    final DatePicker datePicker = new DatePicker();
    datePicker.setSelectedDate(defaultValue);

    final MessageBox prompt = new MessageBox(caption) {
      @Override
      public void onClose(boolean result) {
        hide();
        if (result) {
          callback.onResult(datePicker.getSelectedDate());
        } else {
          callback.onResult(null);
        }
      }
    };
    prompt.setAnimationEnabled(true);
    int preferredWidth = Window.getClientWidth();
    preferredWidth = Math.max(preferredWidth / 3, 256);
    prompt.setWidth(preferredWidth + "px");

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

    prompt.setWidget(datePicker, 0);
    prompt.showModal();
    
    if (prompt.getOffsetWidth() < preferredWidth) {
      prompt.setWidth(preferredWidth + "px");
      prompt.center();
    }
  }

  public static void prompt(String caption, String message,
      String defaultValue, final PromptCallback<String> callback) {
    final TextBox input = new TextBox();
    input.setText(defaultValue);

    final MessageBox prompt = new MessageBox(MessageBoxType.PROMPT, caption) {
      @Override
      public void onClose(boolean result) {
        hide();
        if (result) {
          callback.onResult(input.getText());
        } else {
          callback.onResult(null);
        }
      }
    };
    prompt.setAnimationEnabled(true);
    int preferredWidth = Window.getClientWidth();
    preferredWidth = Math.max(preferredWidth / 3, 256);
    prompt.setWidth(preferredWidth + "px");

    final LayoutPanel panel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
    panel.setPadding(0);
    panel.add(new HTML(message), new BoxLayoutData(FillStyle.HORIZONTAL));
    panel.add(input, new BoxLayoutData(FillStyle.HORIZONTAL));

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

    prompt.setWidget(panel);
    prompt.showModal();
    
    if (prompt.getOffsetWidth() < preferredWidth) {
      prompt.setWidth(preferredWidth + "px");
      prompt.center();
    }

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        input.setFocus(true);
      }
    });
  }

  private Widget widget;

  private LayoutPanel buttonPanel = new LayoutPanel();

  private Image image;

  private WidgetWrapper imageWrapper;

  public MessageBox() {
    this(DEFAULT_TYPE, null);
  }

  public MessageBox(MessageBoxType type) {
    this(type, null);
  }

  public MessageBox(MessageBoxType type, String text) {
    this(type, text, false);
  }

  public MessageBox(MessageBoxType type, String text, boolean autoHide) {
    super(text, false, autoHide);

    final LayoutPanel layoutPanel = new LayoutPanel(new BorderLayout());
    super.setWidget(layoutPanel);
    layoutPanel.setWidgetSpacing(10);
    // if (UserAgent.isGecko()) {
    DOM.setStyleAttribute(layoutPanel.getElement(), "overflow", "auto");
    // }

    final BoxLayout buttonPanelLayout = new BoxLayout(Orientation.HORIZONTAL);
    buttonPanelLayout.setLeftToRight(false);
    buttonPanel.setLayout(buttonPanelLayout);
    buttonPanel.setPadding(5);
    setFooter(buttonPanel);

    if (type == MessageBoxType.ALERT) {
      setImage(MESSAGEBOX_IMAGES.dialogWarning().createImage());
    } else if (type == MessageBoxType.CONFIRM) {
      setImage(MESSAGEBOX_IMAGES.dialogQuestion().createImage());
    } else if (type == MessageBoxType.ERROR) {
      setImage(MESSAGEBOX_IMAGES.dialogError().createImage());
    } else if (type == MessageBoxType.INFO) {
      setImage(MESSAGEBOX_IMAGES.dialogInformation().createImage());
    } else if (type == MessageBoxType.PASSWORD) {
      setImage(MESSAGEBOX_IMAGES.dialogPassword().createImage());
    } else if (type == MessageBoxType.PROMPT) {
      setImage(MESSAGEBOX_IMAGES.dialogQuestion().createImage());
    }

    addStyleName("mosaic-MessageBox");
  }

  public MessageBox(String text) {
    this(DEFAULT_TYPE, text, false);
  }

  public MessageBox(String text, boolean autoHide) {
    this(DEFAULT_TYPE, text, autoHide);
  }

  public LayoutPanel getButtonPanel() {
    return buttonPanel;
  }

  public String getHTML() {
    if (widget instanceof HasHTML) {
      return ((HasHTML) widget).getHTML();
    } else {
      return null;
    }
  }

  public Image getImage() {
    return image;
  }

  public String getText() {
    if (widget instanceof HasText) {
      return ((HasText) widget).getText();
    } else {
      return null;
    }
  }

  public Widget getWidget() {
    return widget;
  }

  public abstract void onClose(boolean result);

  @Override
  public boolean onKeyDownPreview(char key, int modifiers) {
    switch (key) {
      case KeyboardListener.KEY_ESCAPE:
        onClose(false);
        break;
    }
    return super.onKeyDownPreview(key, modifiers);
  }

  public void setHTML(String html) {
    if (widget instanceof HasHTML) {
      ((HasHTML) widget).setHTML(html);
    } else {
      setWidget(new HTML(html));
    }
  }

  public void setImage(Image image) {
    final LayoutPanel layoutPanel = (LayoutPanel) super.getWidget();
    if (this.image != image) {
      if (imageWrapper != null) {
        layoutPanel.remove(imageWrapper);
      }
      this.image = image;
      imageWrapper = new WidgetWrapper(image);
      layoutPanel.add(imageWrapper, new BorderLayoutData(Region.WEST));
    }
  }

  public void setText(final String text) {
    if (widget instanceof HasHTML) {
      ((HasHTML) widget).setText(text);
    } else {
      HTML html = new HTML();
      html.setText(text);
      setWidget(html);
    }
  }

  public void setWidget(Widget w) {
    setWidget(w, -1);
  }

  public void setWidget(Widget w, final int padding) {
    final LayoutPanel layoutPanel = (LayoutPanel) super.getWidget();
    if (padding > -1) {
      layoutPanel.setPadding(padding);
    }
    if (widget != w) {
      if (widget != null) {
        layoutPanel.remove(widget);
      }
      widget = w;
      layoutPanel.add(widget);
    }
  }

}