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

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.impl.PopupImpl;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Strict;

/**
 * 
 * @author johan.rydberg(at)gmail.com
 */
public class SheetPanel extends SimplePanel {

  private static final int ANIMATION_DURATION = 350;

  class SlideDownAnimation extends Animation {

    boolean showing;

    int offsetHeight;
    int offsetWidth;

    public void setState(boolean showing) {
      cancel();

      this.showing = showing;
      if (showing) {
	DOM.setStyleAttribute(getElement(), "position", "absolute");
	// We put it somewhere far far away, so that it doesn't flicker.
	getElement().getStyle().setPropertyPx("top", -20000);
	RootPanel.get().add(SheetPanel.this);
	impl.onShow(getElement());
      }

      DeferredCommand.addCommand(new Command() {
	public void execute() {
	  run(ANIMATION_DURATION);
	}
      });
    }

    @Override
    protected void onComplete() {
      if (!showing) {
	removeStyleName(resources.sheetPanelCss().open());
	RootPanel.get().remove(SheetPanel.this);
	impl.onHide(getElement());
      } else {
	addStyleName(resources.sheetPanelCss().open());
      }
      DOM.setStyleAttribute(getElement(), "overflow", "visible");
      getElement().getStyle().setPropertyPx("top", topPosition);
    }

    @Override
    protected void onStart() {
      offsetHeight = getOffsetHeight();
      offsetWidth = getOffsetWidth();
      DOM.setStyleAttribute(getElement(), "overflow", "hidden");
      super.onStart();
    }

    @Override
    protected void onUpdate(double progress) {
      if (!showing) {
	progress = 1.0 - progress;
      }

      int top = topPosition - (offsetHeight - (int) (progress * offsetHeight));
      getElement().getStyle().setPropertyPx("top", top);
    }

  }

  private SlideDownAnimation animation = new SlideDownAnimation();

  private static final PopupImpl impl = GWT.create(PopupImpl.class);

  private int leftPosition = -1;

  private int topPosition = -1;

  /**
   * Css stylenames.
   */
  public interface Css extends CssResource {
    String sheet();

    String open();

  }

  /**
   * Resources for {@link SheetPanel}.
   */
  public interface Resources extends ClientBundle {
    @Source("SheetPanel.css")
    @Strict
    Css sheetPanelCss();
  }

  private String onLoadHeight = null;

  private String onLoadWidth = null;

  private boolean showing;

  private String desiredHeight;

  private String desiredWidth;

  private Resources resources;

  /**
   * Creates an empty decorated popup panel using the specified style names.
   * 
   */
  public SheetPanel(Resources resources) {
    super();
    this.resources = resources;
    super.getContainerElement().appendChild(impl.createElement());
    setStyleName(resources.sheetPanelCss().sheet());
  }

  /**
   * Determines whether or not this popup is showing.
   * 
   * @return <code>true</code> if the popup is showing
   * @see #show()
   * @see #hide()
   */
  public boolean isShowing() {
    return showing;
  }

  /**
   * Hides the popup and detaches it from the page. This has no effect if it is
   * not currently showing.
   */
  public void hide() {
    if (!isShowing()) {
      return;
    }
    setState(false);
    //CloseEvent.fire(this, this, autoClosed);
  }

  /**
   * Sets the height of the panel's child widget. If the panel's child widget
   * has not been set, the height passed in will be cached and used to set the
   * height immediately after the child widget is set.
   * 
   * <p>
   * Note that subclasses may have a different behavior. A subclass may decide
   * not to change the height of the child widget. It may instead decide to
   * change the height of an internal panel widget, which contains the child
   * widget.
   * </p>
   * 
   * @param height the object's new height, in CSS units (e.g. "10px", "1em")
   */
  @Override
  public void setHeight(String height) {
    desiredHeight = height;
    maybeUpdateSize();
    // If the user cleared the size, revert to not trying to control children.
    if (height.length() == 0) {
      desiredHeight = null;
    }
  }

  /**
   * Sets the popup's position relative to the browser's client area. The
   * popup's position may be set before calling {@link #show()}.
   * 
   * @param left the left position, in pixels
   * @param top the top position, in pixels
   */
  public void setPopupPosition(int left, int top) {
    // Save the position of the popup
    leftPosition = left;
    topPosition = top;

    // Account for the difference between absolute position and the
    // body's positioning context.
    left -= Document.get().getBodyOffsetLeft();
    top -= Document.get().getBodyOffsetTop();

    // Set the popup's position manually, allowing setPopupPosition() to be
    // called before show() is called (so a popup can be positioned without it
    // 'jumping' on the screen).
    Element elem = getElement();
    elem.getStyle().setPropertyPx("left", left);
    elem.getStyle().setPropertyPx("top", top);
  }
  
  @Override
  public void setWidget(Widget w) {
    super.setWidget(w);
      maybeUpdateSize();
  }
  
  /**
   * Sets the width of the panel's child widget. If the panel's child widget has
   * not been set, the width passed in will be cached and used to set the width
   * immediately after the child widget is set.
   * 
   * <p>
   * Note that subclasses may have a different behavior. A subclass may decide
   * not to change the width of the child widget. It may instead decide to
   * change the width of an internal panel widget, which contains the child
   * widget.
   * </p>
   * 
   * @param width the object's new width, in CSS units (e.g. "10px", "1em")
   */
  @Override
  public void setWidth(String width) {
    desiredWidth = width;
    maybeUpdateSize();
    // If the user cleared the size, revert to not trying to control children.
    if (width.length() == 0) {
      desiredWidth = null;
    }
  }

  /**
   * Sets whether this object is visible. This method just sets the
   * <code>visibility</code> style attribute. You need to call {@link #show()}
   * to actually attached/detach the {@link PopupPanel} to the page.
   * 
   * @param visible <code>true</code> to show the object, <code>false</code> to
   *          hide it
   * @see #show()
   * @see #hide()
   */
  @Override
  public void setVisible(boolean visible) {
    // We use visibility here instead of UIObject's default of display
    // Because the panel is absolutely positioned, this will not create
    // "holes" in displayed contents and it allows normal layout passes
    // to occur so the size of the PopupPanel can be reliably determined.
    DOM.setStyleAttribute(getElement(), "visibility", visible ? "visible"
        : "hidden");

    // If the PopupImpl creates an iframe shim, it's also necessary to hide it
    // as well.
    impl.setVisible(getElement(), visible);
  }

  /**
   * Shows the popup and attach it to the page. It must have a child widget
   * before this method is called.
   */
  public void show() {
    if (showing) {
      return;
    }
    setVisible(false);
    setState(true);
    int left = (Window.getClientWidth() - getOffsetWidth()) / 2;
    getElement().getStyle().setPropertyPx("left", Math.max(Window.getScrollLeft() + left, 0));
    setVisible(true);
  }

  @Override
  protected com.google.gwt.user.client.Element getContainerElement() {
    return impl.getContainerElement(getPopupImplElement()).cast();
  }

  @Override
  protected com.google.gwt.user.client.Element getStyleElement() {
    return impl.getStyleElement(getPopupImplElement()).cast();
  }

  @Override
  protected void onUnload() {
    // Just to be sure, we perform cleanup when the popup is unloaded (i.e.
    // removed from the DOM). This is normally taken care of in hide(), but it
    // can be missed if someone removes the popup directly from the RootPanel.
    if (isShowing()) {
      setState(false);
    }
  }

  /**
   * We control size by setting our child widget's size. However, if we don't
   * currently have a child, we record the size the user wanted so that when we
   * do get a child, we can set it correctly. Until size is explicitly cleared,
   * any child put into the popup will be given that size.
   */
  void maybeUpdateSize() {
    // For subclasses of PopupPanel, we want the default behavior of setWidth
    // and setHeight to change the dimensions of PopupPanel's child widget.
    // We do this because PopupPanel's child widget is the first widget in
    // the hierarchy which provides structure to the panel. DialogBox is
    // an example of this. We want to set the dimensions on DialogBox's
    // FlexTable, which is PopupPanel's child widget. However, it is not
    // DialogBox's child widget. To make sure that we are actually getting
    // PopupPanel's child widget, we have to use super.getWidget().
    Widget w = super.getWidget();
    if (w != null) {
      if (desiredHeight != null) {
        w.setHeight(desiredHeight);
      }
      if (desiredWidth != null) {
        w.setWidth(desiredWidth);
      }
    }
  }

  /**
   * Get the element that {@link PopupImpl} uses. PopupImpl creates an element
   * that goes inside of the outer element, so all methods in PopupImpl are
   * relative to the first child of the outer element, not the outer element
   * itself.
   * 
   * @return the Element that {@link PopupImpl} creates and expects
   */
  private com.google.gwt.user.client.Element getPopupImplElement() {
    return DOM.getFirstChild(super.getContainerElement());
  }

  /**
   * Set the showing state of the popup.
   * 
   * @param showing the new state
   */
  private void setState(boolean showing) {
    animation.setState(showing);
    this.showing = showing;
  }

}
