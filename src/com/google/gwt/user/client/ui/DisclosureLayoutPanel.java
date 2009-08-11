package com.google.gwt.user.client.ui;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DisclosurePanelImages;
import com.google.gwt.user.client.ui.Image;

public class DisclosureLayoutPanel extends CaptionLayoutPanel {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-DisclosureLayoutPanel";

  private static DisclosurePanelImages createDefaultImages() {
    // if (LocaleInfo.getCurrentLocale().isRTL()) {
    // return GWT.create(DisclosurePanelImagesRTL.class);
    // }
    return GWT.create(DisclosurePanelImages.class);
  }

  private Image image = new Image();

  /**
   * Create an empty {@code DisclosurePanel} that is is initially closed.
   */
  public DisclosureLayoutPanel() {
    this(null);
  }

  /**
   * Creates a DisclosurePanel with the specified header text, an initial
   * open/close state and a bundle of images to be used in the default header
   * widget.
   * 
   * @param images a bundle that provides disclosure panel specific images
   * @param headerText the text to be displayed in the header
   * @param isOpen the initial open/close state of the content panel
   */
  public DisclosureLayoutPanel(final DisclosurePanelImages images,
      final String headerText, final boolean isOpen) {
    super(headerText);
    setCollapsed(!isOpen);
    getHeader().add(image);

    if (isOpen) {
      images.disclosurePanelOpen().applyTo(image);
    } else {
      images.disclosurePanelClosed().applyTo(image);
    }

    getHeader().addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        setCollapsed(!isCollapsed(), images);
      }
    });
    
    DOM.setStyleAttribute(getHeader().getElement(), "cursor", "pointer");

    setStyleName(DEFAULT_STYLENAME);
  }

  private void setCollapsed(boolean collapsed, DisclosurePanelImages images) {
    setCollapsed(!isCollapsed());
    if (isCollapsed()) {
      images.disclosurePanelClosed().applyTo(image);
    } else {
      images.disclosurePanelOpen().applyTo(image);
    }
    HasLayoutManager lm = WidgetHelper.getParent(DisclosureLayoutPanel.this);
    if (lm != null) {
      lm.layout();
    } else {
      layout();
    }
  }

  /**
   * Creates a DisclosurePanel that will be initially closed using the specified
   * text in the header.
   * 
   * @param headerText the text to be displayed in the header
   */
  public DisclosureLayoutPanel(String headerText) {
    this(createDefaultImages(), headerText, false);
  }

  /**
   * Creates a DisclosurePanel with the specified header text and an initial
   * open/close state.
   * 
   * @param headerText the text to be displayed in the header
   * @param isOpen the initial open/close state of the content panel
   */
  public DisclosureLayoutPanel(String headerText, boolean isOpen) {
    this(createDefaultImages(), headerText, isOpen);
  }
}
