package org.mosaic.ui.client;

import com.google.gwt.user.client.ui.AbstractDecoratedButton;
import com.google.gwt.user.client.ui.HasHTML;

public class DecoratedButton extends AbstractDecoratedButton implements HasHTML {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-DecoratedButton";
  
  /**
   * The default styles applied to each row.
   */
  private static final String[] DEFAULT_ROW_STYLENAMES = {
      "buttonTop", "buttonMiddle", "buttonBottom"};

  protected final ButtonPanel decPanel;

  public DecoratedButton() {
    this(new ButtonPanel(DEFAULT_ROW_STYLENAMES));
    setStyleName(DEFAULT_STYLENAME);
  }

  private DecoratedButton(ButtonPanel decPanel) {
    super(decPanel.getElement());
    this.decPanel = decPanel;
  }

  public DecoratedButton(String html) {
    this();
    setHTML(html);
  }

  public String getHTML() {
    return decPanel.getContainerElement().getInnerHTML();
  }

  public String getText() {
    return decPanel.getContainerElement().getInnerText();
  }

  public void setHTML(String html) {
    decPanel.getContainerElement().setInnerHTML(html);
  }

  public void setText(String text) {
    decPanel.getContainerElement().setInnerHTML(text);
  }

}
