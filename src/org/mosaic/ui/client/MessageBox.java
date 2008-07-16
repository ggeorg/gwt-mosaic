package org.mosaic.ui.client;

import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.BorderLayoutData;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MessageBox extends WindowPanel {

  public enum MessageBoxType {
    ALERT, CONFIRM, ERROR, INFO, PLAIN, PROMPT
  };

  /**
   * The caption images to use.
   */
  public static final MessageBoxImages MESSAGEBOX_IMAGES = (MessageBoxImages) GWT.create(MessageBoxImages.class);

  private static final MessageBoxType DEFAULT_TYPE = MessageBoxType.PLAIN;

  public static void alert(String caption, String message, String... args) {
    alert(MessageBoxType.ALERT, caption, message, args);
  }

  public static void error(String caption, String message, String... args) {
    alert(MessageBoxType.ERROR, caption, message, args);
  }

  public static void info(String caption, String message, String... args) {
    alert(MessageBoxType.INFO, caption, message, args);
  }

  private static void alert(MessageBoxType type, String caption, String message,
      String... args) {
    final MessageBox alert = new MessageBox(caption, type);
    alert.setAnimationEnabled(true);
    final int width = Window.getClientWidth();
    alert.setWidth(Math.max(width / 3, 256) + "px");

    BoxLayout box = new BoxLayout(Orientation.VERTICAL);
    // box.setMargin(0);
    LayoutPanel panel = new LayoutPanel(box);
    panel.add(new HTML(message));

    Button button = new Button("OK");
    button.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        alert.hide();
      }
    });
    alert.getButtonPanel().add(button);

    alert.setWidget(panel);
    alert.center();
  }

  public static void confirm(String caption, String message, String... args) {
    final MessageBox confirm = new MessageBox(caption, MessageBoxType.CONFIRM);
    confirm.setAnimationEnabled(true);
    final int width = Window.getClientWidth();
    confirm.setWidth(Math.max(width / 3, 256) + "px");

    BoxLayout box = new BoxLayout(Orientation.VERTICAL);
    // box.setMargin(0);
    LayoutPanel panel = new LayoutPanel(box);
    panel.add(new HTML(message));

    HorizontalPanel hpanel = new HorizontalPanel();

    Button buttonCancel = new Button("Cancel");
    buttonCancel.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        confirm.hide();
      }
    });

    Button buttonOK = new Button("OK");
    buttonOK.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        confirm.hide();
      }
    });

    hpanel.add(buttonCancel);
    hpanel.add(new HTML("&nbsp;"));
    hpanel.add(buttonOK);

    confirm.getButtonPanel().add(hpanel);

    confirm.setWidget(panel);
    confirm.center();
  }

  public static void prompt(String caption, String message, String defaultValue) {
    final MessageBox prompt = new MessageBox(caption, MessageBoxType.PROMPT);
    prompt.setAnimationEnabled(true);
    final int width = Window.getClientWidth();
    prompt.setWidth(Math.max(width / 3, 256) + "px");

    final TextBox input = new TextBox();
    input.setText(defaultValue);

    BoxLayout box = new BoxLayout(Orientation.VERTICAL);
    // box.setMargin(0);
    LayoutPanel panel = new LayoutPanel(box);

    panel.add(new HTML(message), new BoxLayoutData(FillStyle.HORIZONTAL));
    panel.add(input, new BoxLayoutData(FillStyle.HORIZONTAL));

    HorizontalPanel hpanel = new HorizontalPanel();

    Button buttonCancel = new Button("Cancel");
    buttonCancel.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        prompt.hide();
      }
    });

    Button buttonOK = new Button("OK");
    buttonOK.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        prompt.hide();
      }
    });

    hpanel.add(buttonCancel);
    hpanel.add(new HTML("&nbsp;"));
    hpanel.add(buttonOK);

    prompt.getButtonPanel().add(hpanel);

    prompt.setWidget(panel);
    prompt.center();

    prompt.addPopupListener(new PopupListener() {
      public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
        System.out.println(input.getText());
      }
    });
  }

  private Widget widget;

  private HorizontalPanel buttonPanel = new HorizontalPanel();

  public MessageBox() {
    this(null, DEFAULT_TYPE);
  }

  public MessageBox(MessageBoxType type) {
    this(null, type);
  }

  public MessageBox(String text) {
    this(text, DEFAULT_TYPE, false);
  }

  public MessageBox(String text, boolean autoHide) {
    this(text, DEFAULT_TYPE, autoHide);
  }

  public MessageBox(String text, MessageBoxType type) {
    this(text, type, false);
  }

  public MessageBox(String text, MessageBoxType type, boolean autoHide) {
    super(text, false, autoHide, true);

    LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.setLayout(new BorderLayout());

    buttonPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
    layoutPanel.add(buttonPanel, new BorderLayoutData(BorderLayoutRegion.SOUTH));

    if (type == MessageBoxType.ALERT) {
      layoutPanel.add(MESSAGEBOX_IMAGES.dialogWarning().createImage(),
          new BorderLayoutData(BorderLayoutRegion.WEST));
    } else if (type == MessageBoxType.CONFIRM) {
      layoutPanel.add(MESSAGEBOX_IMAGES.dialogQuestion().createImage(),
          new BorderLayoutData(BorderLayoutRegion.WEST));
    } else if (type == MessageBoxType.ERROR) {
      layoutPanel.add(MESSAGEBOX_IMAGES.dialogError().createImage(),
          new BorderLayoutData(BorderLayoutRegion.WEST));
    } else if (type == MessageBoxType.INFO) {
      layoutPanel.add(MESSAGEBOX_IMAGES.dialogInformation().createImage(),
          new BorderLayoutData(BorderLayoutRegion.WEST));
    } else if (type == MessageBoxType.PROMPT) {
      layoutPanel.add(MESSAGEBOX_IMAGES.dialogQuestion().createImage(),
          new BorderLayoutData(BorderLayoutRegion.WEST));
    }
  }

  public HorizontalPanel getButtonPanel() {
    return buttonPanel;
  }

  public String getHTML() {
    if (widget instanceof HasHTML) {
      return ((HasHTML) widget).getHTML();
    } else {
      return null;
    }
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

  public void hide() {
    // TODO beforeClose() event
    super.hide();
  }

  public void setHTML(String html) {
    if (widget instanceof HasHTML) {
      ((HasHTML) widget).setHTML(html);
    } else {
      setWidget(new HTML(html));
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
    final LayoutPanel layoutPanel = getLayoutPanel();
    if (widget != null) {
      layoutPanel.remove(widget);
    }
    widget = w;
    layoutPanel.add(widget);
  }

}
