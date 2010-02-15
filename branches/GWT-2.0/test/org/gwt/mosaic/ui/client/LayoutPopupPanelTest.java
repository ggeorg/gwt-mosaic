/*
 * Copyright (c) 2010 GWT Mosaic Georgios J. Georgopoulos.
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

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Tests for {@link LayoutPopupPanel}
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
@DoNotRunWith(Platform.HtmlUnit)
public class LayoutPopupPanelTest extends GWTTestCase {

  private static class Adder implements HasWidgetsTester.WidgetAdder {
    public void addChild(HasWidgets container, Widget child) {
      ((LayoutPopupPanel) container).setWidget(child);
    }
  }

  /**
   * Expose otherwise private or protected methods.
   */
  private static class TestablePopupPanel extends LayoutPopupPanel {
    private int onLoadCount;

    public void assertOnLoadCount(int expected) {
      assertEquals(expected, onLoadCount);
    }

    @Override
    public com.google.gwt.user.client.Element getContainerElement() {
      return super.getContainerElement();
    }

    @Override
    public com.google.gwt.dom.client.Element getGlassElement() {
      return super.getGlassElement();
    }

    @Override
    public void onLoad() {
      super.onLoad();
      onLoadCount++;
    }
  }

  @Override
  public String getModuleName() {
    return "org.gwt.mosaic.UI";
  }

  /**
   * Test the basic accessors.
   */
  public void testAccessors() {
    LayoutPopupPanel popup = createPopupPanel();

    // Animation enabled
    assertFalse(popup.isAnimationEnabled());
    popup.setAnimationEnabled(true);
    assertTrue(popup.isAnimationEnabled());

    // Modal
    popup.setModal(true);
    assertTrue(popup.isModal());
    popup.setModal(false);
    assertFalse(popup.isModal());

    // AutoHide enabled
    popup.setAutoHideEnabled(true);
    assertTrue(popup.isAutoHideEnabled());
    popup.setAutoHideEnabled(false);
    assertFalse(popup.isAutoHideEnabled());

    // PreviewAllNativeEvents enabled
    popup.setPreviewingAllNativeEvents(true);
    assertTrue(popup.isPreviewingAllNativeEvents());
    popup.setPreviewingAllNativeEvents(false);
    assertFalse(popup.isPreviewingAllNativeEvents());

    // setVisible
    assertTrue(popup.isVisible());
    popup.setVisible(false);
    assertFalse(popup.isVisible());
    popup.setVisible(true);
    assertTrue(popup.isVisible());

    // isShowing
    assertFalse(popup.isShowing());
    popup.show();
    assertTrue(popup.isShowing());
    popup.hide();
    assertFalse(popup.isShowing());
  }

  /**
   * Test the basic {@code HasWidget} tests.
   */
  public void testAttachDetachOrder() {
    HasWidgetsTester.testAll(createPopupPanel(), new Adder(), false);
  }

  /**
   * @return a new {@code LayoutPopupPanel}
   */
  protected TestablePopupPanel createPopupPanel() {
    return new TestablePopupPanel();
  }

  public void testAutoHidePartner() {
    final LayoutPopupPanel popup = createPopupPanel();

    // Add a partner
    DivElement partner0 = Document.get().createDivElement();
    popup.addAutoHidePartner(partner0);
    popup.addAutoHidePartner(Document.get().createDivElement());

    // Remove a partner
    popup.removeAutoHidePartner(partner0);
  }

  public void testAutoHideOnHistoryEvent() {
    final LayoutPopupPanel popup = createPopupPanel();
    popup.show();
    assertTrue(popup.isShowing());

    // When autoHideOnHistoryEvent is disabled, the popup remains visible.
    History.newItem("popupToken0");
    assertTrue(popup.isShowing());

    // When autoHideOnHistoryEvent is enabled, the popup is hidden.
    popup.setAutoHideOnHistoryEventsEnabled(true);
    History.newItem("popupToken1");
    assertFalse(popup.isShowing());
  }

  /**
   * Tests that a large PopupPanel is not positioned off the top or left edges
   * of the browser window, making part of the panel unreachable.
   */
  @DoNotRunWith(Platform.HtmlUnit)
  public void testCenterLargePopup() {
    final LayoutPopupPanel popup = createPopupPanel();
    popup.setHeight("4096px");
    popup.setWidth("4096px");
    popup.setWidget(new Label("foo"));
    popup.center();
    assertEquals(0, popup.getAbsoluteTop());
    assertEquals(0, popup.getAbsoluteLeft());
  }

  /**
   * GWT Issue 2463: If a {@link PopupPanel} contains a dependent
   * {@link PopupPanel} that is hidden or shown in the onDetach or onAttach
   * method, we could run into conflicts with the animations. The
   * {@link MenuBar} exhibits this behavior because, when we detach a
   * {@link MenuBar} from the page, it closes all of its sub menus, each located
   * in a different {@link PopupPanel}.
   */
  public void testDependantPopupPanel() {
    // Create the dependent popup
    final LayoutPopupPanel dependantPopup = createPopupPanel();
    dependantPopup.setAnimationEnabled(true);

    // Create the primary popup
    final LayoutPopupPanel primaryPopup = new LayoutPopupPanel(false, false) {
      @Override
      protected void onAttach() {
        dependantPopup.show();
        super.onAttach();
      }

      @Override
      protected void onDetach() {
        dependantPopup.hide();
        super.onDetach();
      }
    };
    primaryPopup.setAnimationEnabled(true);

    testDependantPopupPanel(primaryPopup);
  }

  /**
   * @see #testDependantPopupPanel()
   */
  protected void testDependantPopupPanel(final LayoutPopupPanel primaryPopup) {
    // Show the popup
    primaryPopup.show();

    // Hide the popup
    new Timer() {
      @Override
      public void run() {
        primaryPopup.hide();
      }
    }.schedule(1000);

    delayTestFinish(5000);
    // Give time for any errors to occur
    new Timer() {
      @Override
      public void run() {
        finishTest();
      }
    }.schedule(2000);
  }

  public void testGlassPanelDisabled() {
    // Verify that the glass is disabled by default
    final TestablePopupPanel popup = createPopupPanel();
    assertFalse(popup.isGlassEnabled());
    assertNull(popup.getGlassElement());

    // Verify the glass panel is never created
    popup.show();
    assertNull(popup.getGlassElement());
    popup.hide();
  }

  public void testGlassDisabledWhileShowing() {
    // Show the popup and glass panel
    final TestablePopupPanel popup = createPopupPanel();
    popup.setGlassEnabled(true);
    com.google.gwt.dom.client.Element glass = popup.getGlassElement();
    popup.show();

    // Disable the glass panel and hide the popup
    popup.setGlassEnabled(false);
    assertTrue(isAttached(glass));
    popup.hide();
    assertFalse(isAttached(glass));

    // Show the popup and verify that glass is no longer used
    popup.show();
    assertFalse(isAttached(glass));
    popup.hide();
  }

  public void testGlassEnabled() {
    // Verify that the glass is disabled by default
    final TestablePopupPanel popup = createPopupPanel();
    assertFalse(popup.isGlassEnabled());
    assertNull(popup.getGlassElement());

    // Enable the glass panel and verify it is created
    popup.setGlassEnabled(true);
    Element glass = popup.getGlassElement();
    assertNotNull(glass);
    assertFalse(isAttached(glass));

    // Show the popup and verify the glass panel is added
    popup.show();
    assertTrue(isAttached(glass));

    // Hide the popup and verify the glass panel is removed
    popup.hide();
    assertFalse(isAttached(glass));
  }

  public void testGlassEnabledWhileShowing() {
    // Verify that the glass is disabled by default
    final TestablePopupPanel popup = createPopupPanel();
    assertFalse(popup.isGlassEnabled());
    assertNull(popup.getGlassElement());

    // Show the popup and enable the glass panel
    popup.show();
    popup.setGlassEnabled(true);
    Element glass = popup.getGlassElement();
    assertNotNull(glass);
    assertFalse(isAttached(glass));

    // Hide the popup and verify the glass panel is removed
    popup.hide();
    assertFalse(isAttached(glass));

    // Show the popup and verify the glas is now used
    popup.show();
    assertTrue(isAttached(glass));
    popup.hide();
  }

  /**
   * Test that the onLoad method is only called once when showing the popup.
   */
  public void testOnLoad() {
    final TestablePopupPanel popup = createPopupPanel();

    // show() without animation
    {
      popup.setAnimationEnabled(false);
      popup.show();
      popup.assertOnLoadCount(1);
      popup.hide();
    }

    // show() with animation
    {
      popup.setAnimationEnabled(true);
      popup.show();
      popup.assertOnLoadCount(2);
      popup.hide();
    }

    // center() without animation
    {
      popup.setAnimationEnabled(false);
      popup.center();
      popup.assertOnLoadCount(3);
      popup.hide();
    }

    // center() with animation
    {
      popup.setAnimationEnabled(true);
      popup.center();
      popup.assertOnLoadCount(4);
      popup.hide();
    }

    // pack() without animation
    {
      popup.setAnimationEnabled(false);
      popup.pack();
      popup.assertOnLoadCount(3);
      popup.hide();
    }

    // pack() with animation
    {
      popup.setAnimationEnabled(true);
      popup.pack();
      popup.assertOnLoadCount(4);
      popup.hide();
    }
  }

  @DoNotRunWith(Platform.HtmlUnit)
  public void testPopup() {
    // Get rid of window margins so we can test absolute position.
    Window.setMargin("0px");

    final LayoutPopupPanel popup = createPopupPanel();
    popup.setAnimationEnabled(false);
    Label lbl = new Label("foo");

    // Make sure that setting the popup's size & position works _before_
    // setting its widget.
    popup.setSize("384px", "128px");
    popup.setPopupPosition(128, 64);
    popup.setWidget(lbl);
    popup.show();

    // DecoratorPanel adds width and height because it wraps the content in a
    // 3x3 table.
    assertTrue(popup.getOffsetWidth() >= 384);
    assertTrue(popup.getOffsetHeight() >= 128);
    assertEquals(128, popup.getPopupLeft());
    assertEquals(64, popup.getPopupTop());

    // Make sure that the popup returns to the correct position
    // after hiding and showing it.
    popup.hide();
    popup.show();
    assertEquals(128, popup.getPopupLeft());
    assertEquals(64, popup.getPopupTop());

    // Make sure that setting the popup's size & position works _after_
    // setting its widget (and that clearing its size properly resizes it to
    // its widget's size).
    popup.setSize("", "");
    popup.setPopupPosition(16, 16);

    // DecoratorPanel adds width and height because it wraps the content in a
    // 3x3 table.
    assertTrue(popup.getOffsetWidth() >= lbl.getOffsetWidth());
    assertTrue(popup.getOffsetWidth() >= lbl.getOffsetHeight());
    assertEquals(16, popup.getAbsoluteLeft());
    assertEquals(16, popup.getAbsoluteTop());

    // Ensure that hiding the popup fires the appropriate events.
    delayTestFinish(1000);
    popup.addCloseHandler(new CloseHandler<PopupPanel>() {
      public void onClose(CloseEvent<PopupPanel> event) {
        finishTest();
      }
    });
    popup.hide();
  }

  public void testSeparateContainers() {
    TestablePopupPanel p1 = createPopupPanel();
    TestablePopupPanel p2 = createPopupPanel();
    assertTrue(p1.getContainerElement() != null);
    assertTrue(p2.getContainerElement() != null);
    assertFalse(p1.getContainerElement() == p2.getContainerElement());
  }

  /**
   * GWT Issue 2481: Try to set the contents of the popup while the popup is
   * attached. When we hide the popup, this should not leave the popup in an
   * invalid attach state.
   */
  public void testSetWidgetWhileAttached() {
    final LayoutPopupPanel popup = createPopupPanel();
    popup.show();
    popup.setWidget(new Label("test"));
    popup.hide();
  }
  
  /**
   * Test the showing a popup while it is hiding will not result in an illegal
   * state.
   */
  public void testShowWhileHiding() {
    final LayoutPopupPanel popup = createPopupPanel();

    // Show the popup
    popup.setAnimationEnabled(false);
    popup.show();
    assertTrue(popup.isShowing());

    // Start hiding the popup
    popup.setAnimationEnabled(true);
    popup.hide();
    assertFalse(popup.isShowing());

    // Show the popup while its hiding
    popup.show();
    assertTrue(popup.isShowing());
  }
  
  @Override
  protected void gwtTearDown() throws Exception {
    RootPanel.get().clear();
  }

  private boolean isAttached(Element elem) {
    return Document.get().getBody().isOrHasChild(elem);
  }
}
