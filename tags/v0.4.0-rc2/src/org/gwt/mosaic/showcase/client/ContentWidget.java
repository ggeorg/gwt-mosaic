/*
 * Copyright 2008 Google Inc.
 * 
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
package org.gwt.mosaic.showcase.client;

import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.google.gwt.widgetideas.client.ResizableWidgetCollection;

/**
 * A widget used to show gwt-mosaic examples in the ContentPanel. It includes a
 * tab panel with options to view the example, view the source, or view the css
 * style rules.
 * <p>
 * This widget uses a lazy initialization mechanism so that the content is not
 * rendered until the onInitialize method is called, which happens the first
 * time the {@code Widget} is added to the page. The data in the source and css
 * tabs are loaded using RPC call to the server.
 * 
 * <h3>CSS Style Rules</h3>
 * 
 * <pre>
 * <ul class="css">
 * <li>.sc-ContentWidget { Applied to the entire widget }</li>
 * <li>.sc-ContentWidget-name { Applied to the name }</li>
 * <li>.sc-ContentWidget-description { Applied to the description }</li>
 * </ul>
 * </pre>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public abstract class ContentWidget extends LayoutPanel implements
    SelectionHandler<Integer> {

  /**
   * The constants used in this Content Widget.
   */
  public static interface CwConstants extends Constants {
    String contentWidgetExample();

    String contentWidgetSource();

    String contentWidgetStyle();
  }

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLE_NAME = "sc-ContentWidget";

  /**
   * The static loading image displayed when loading CSS or source code.
   */
  private static String loadingImage;

  /**
   * An instance of the constants
   */
  private CwConstants constants;

  /**
   * The tab panel with the contents.
   */
  private DecoratedTabLayoutPanel tabPanel;

  /**
   * A boolean indicating whether or not this widget has been initialized.
   */
  private boolean initialized = false;

  /**
   * A boolean indicating whether or not the RPC request for the source code has
   * been sent.
   */
  private boolean sourceLoaded = false;

  /**
   * The widget used to display source code.
   */
  private HTML sourceWidget = null;

  /**
   * The widget used to display UiBinder source code.
   */
  private HTML uiBinderSourceWidget = null;

  /**
   * A mapping of themes to style definitions.
   */
  private Map<String, String> styleDefs = null;

  /**
   * The widget used to display css style.
   */
  private HTML styleWidget = null;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public ContentWidget(CwConstants constants) {
    this.constants = constants;
    setStyleName(DEFAULT_STYLE_NAME);
  }

  private String createTabBarCaption(ImageResource image, String text) {
    StringBuffer sb = new StringBuffer();
    sb.append("<table cellspacing='0px' cellpadding='0px' border='0px'><thead><tr>");
    sb.append("<td valign='middle'>");
    sb.append(AbstractImagePrototype.create(image).getHTML());
    sb.append("</td><td valign='middle' style='white-space: nowrap;'>&nbsp;");
    sb.append(text);
    sb.append("</td></tr></thead></table>");
    return sb.toString();
  }

  /**
   * Get the description of this example.
   * 
   * @return a description for this example
   */
  public abstract String getDescription();

  /**
   * Get the name of this example to use as a title.
   * 
   * @return a name for this example
   */
  public abstract String getName();

  /**
   * Returns true if this widget has a source section.
   * 
   * @return true if source tab available
   */
  public boolean hasSource() {
    return true;
  }

  /**
   * Returns true if this widget has a style section.
   * 
   * @return true if style tab available
   */
  public boolean hasStyle() {
    return true;
  }

  /**
   * Returns true if this widget has a UiBinder source section.
   * 
   * @return true if UiBinder source tab available
   */
  public boolean hasUiBinderSource() {
    return false;
  }

  /**
   * Initialize this widget by creating the elements that should be added to the
   * page.
   */
  public final void initialize() {
    if (initialized) {
      return;
    }
    initialized = true;

    tabPanel = new DecoratedTabLayoutPanel(true);
    tabPanel.setPadding(5);
    add(tabPanel);

    // Add a tab handler
    tabPanel.addSelectionHandler(this);

    // Create the container for the main example
    final LayoutPanel vPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
    vPanel.setPadding(0);
    vPanel.setWidgetSpacing(5);
    tabPanel.add(vPanel, createTabBarCaption(Showcase.IMAGES.mediaPlayGreen(),
        constants.contentWidgetExample()), true);

    // Add the name
    HTML nameWidget = new HTML(getName());
    nameWidget.setStyleName(DEFAULT_STYLE_NAME + "-name");
    vPanel.add(nameWidget, new BoxLayoutData(FillStyle.HORIZONTAL));

    // Add the description
    final HTML descWidget = new HTML(getDescription());
    descWidget.setStyleName(DEFAULT_STYLE_NAME + "-description");
    vPanel.add(descWidget, new BoxLayoutData(FillStyle.HORIZONTAL));

    // Monitor word wraps
    ResizableWidgetCollection.get().add(new ResizableWidget() {
      public Element getElement() {
        return descWidget.getElement();
      }

      public boolean isAttached() {
        return descWidget.isAttached();
      }

      public void onResize(int width, int height) {
        WidgetHelper.invalidate(descWidget);
        // WidgetHelper.layout((Widget) WidgetHelper.getParent(descWidget));
      }
    });

    // Add source code tab
    if (hasSource()) {
      // final LayoutPanel panel2 = new LayoutPanel();
      sourceWidget = new HTML();
      sourceWidget.setStyleName(DEFAULT_STYLE_NAME + "-source");
      // panel2.add(sourceWidget);
      tabPanel.add(sourceWidget, createTabBarCaption(Showcase.IMAGES.cup(),
          constants.contentWidgetSource()), true);
    } else {
      sourceLoaded = true;
    }

    // Add style tab
    if (hasStyle()) {
      // final LayoutPanel panel3 = new LayoutPanel();
      styleDefs = new HashMap<String, String>();
      styleWidget = new HTML();
      styleWidget.setStyleName(DEFAULT_STYLE_NAME + "-style");
      // panel3.add(styleWidget);
      tabPanel.add(styleWidget, createTabBarCaption(Showcase.IMAGES.css(),
          constants.contentWidgetStyle()), true);
    }

    // Add UiBinder source code tab
    if (hasUiBinderSource()) {
      // final LayoutPanel panel4 = new LayoutPanel();
      uiBinderSourceWidget = new HTML();
      uiBinderSourceWidget.setStyleName(DEFAULT_STYLE_NAME + "-source");
      // panel4.add(styleWidget);
      tabPanel.add(uiBinderSourceWidget, createTabBarCaption(
          Showcase.IMAGES.catForms(), "UiBinder"), true);
    }

    // Initialize the widget and add it to the page
    final Widget widget = onInitialize();
    if (widget != null) {
      vPanel.add(widget, new BoxLayoutData(FillStyle.BOTH));
    }
    onInitializeComplete();
  }

  public final boolean isInitialized() {
    return initialized;
  }

  /**
   * When the widget is first initialized, this method is called. If it returns
   * a Widget, the widget will be added as the first tab. Return {@code null} to
   * disable the first tab.
   * 
   * @return the widget to add to the first tab
   */
  protected abstract Widget onInitialize();

  /**
   * Called when initialization has completed and the widget has been added to
   * the page.
   */
  protected void onInitializeComplete() {
    // Nothing to do here!
  }

  @Override
  protected void onLoad() {
    // Initialize this widget if we haven't already
    initialize();
  }

  public void onSelection(SelectionEvent<Integer> event) {
    int tabIndex = event.getSelectedItem().intValue();

    // Load the source code
    final String tabHTML = tabPanel.getTabHTML(tabIndex);
    if (!sourceLoaded && tabHTML.contains(constants.contentWidgetSource())) {
      sourceLoaded = true;
      String className = this.getClass().getName();
      className = className.substring(className.lastIndexOf(".") + 1);
      requestSourceContents(ShowcaseConstants.DST_SOURCE_EXAMPLE + className
          + ".html", sourceWidget, null);
    }

    // Load the style definitions
    if (hasStyle() && tabHTML.contains(constants.contentWidgetStyle())) {
      final String theme = Showcase.CUR_THEME;
      if (styleDefs.containsKey(theme)) {
        styleWidget.setHTML(styleDefs.get(theme));
      } else {
        styleDefs.put(theme, "");
        RequestCallback callback = new RequestCallback() {
          public void onError(Request request, Throwable exception) {
            styleDefs.put(theme, "Style not available.");
          }

          public void onResponseReceived(Request request, Response response) {
            styleDefs.put(theme, response.getText());
          }
        };

        String srcPath = ShowcaseConstants.DST_SOURCE_STYLE + theme;
        if (LocaleInfo.getCurrentLocale().isRTL()) {
          srcPath += "_rtl";
        }
        String className = this.getClass().getName();
        className = className.substring(className.lastIndexOf(".") + 1);
        requestSourceContents(srcPath + "/" + className + ".html", styleWidget,
            callback);
      }
    }

    // Load the source code
    if (hasUiBinderSource() && tabHTML.contains("UiBinder")) {
      String className = this.getClass().getName();
      className = className.substring(className.lastIndexOf(".") + 1);
      requestSourceContents(ShowcaseConstants.DST_SOURCE_EXAMPLE + className
          + ".ui.html", uiBinderSourceWidget, null);
    }
  }

  /**
   * Load the contents of a remote file into the specified widget.
   * 
   * @param url the URL of the file relative to the source directory in public
   * @param target the target Widget to place the contents
   * @param callback the callback when the call completes
   */
  protected void requestSourceContents(String url, final HTML target,
      final RequestCallback callback) {
    // Show the loading image
    if (loadingImage == null) {
      loadingImage = "<img src=\"loading.gif\">";
    }
    target.setDirection(HasDirection.Direction.LTR);
    DOM.setStyleAttribute(target.getElement(), "textAlign", "left");
    target.setHTML("&nbsp;&nbsp;" + loadingImage);

    // Request the contents of the file
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
        GWT.getModuleBaseURL() + url);
    RequestCallback realCallback = new RequestCallback() {
      public void onError(Request request, Throwable exception) {
        target.setHTML("Cannot find resource");
        if (callback != null) {
          callback.onError(request, exception);
        }
      }

      public void onResponseReceived(Request request, Response response) {
        target.setHTML(response.getText());
        if (callback != null) {
          callback.onResponseReceived(request, response);
        }
      }

    };
    builder.setCallback(realCallback);

    // Send the request
    Request request = null;
    try {
      request = builder.send();
    } catch (RequestException e) {
      realCallback.onError(request, e);
    }
  }

}
