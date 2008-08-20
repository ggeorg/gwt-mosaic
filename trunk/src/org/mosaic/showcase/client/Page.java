/*
 * Copyright 2008 Google Inc.
 * Copyright 2008 Georgios J. Georgopoulos
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
package org.mosaic.showcase.client;

import java.util.HashMap;
import java.util.Map;

import org.mosaic.ui.client.DecoratedTabLayoutPanel;
import org.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;

/**
 * A widget used to show the examples in the content panel. It includes a tag
 * bar with options to view the example, view the source, or view the css style
 * rules.
 * <p>
 * This widget uses a lazy initialization mechanism so that the content is not
 * rendered until the onInitialize method is called, which happens the first
 * time the widget is added to the page. The data in the source and css tabs are
 * loaded using RPC call to the server.
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.mosaic-sc-Page { Applied to the entire widget }</li>
 * <li>.sc-ContentWidget-tabBar { Applied to the TabBar }</li>
 * <li>.sc-ContentWidget-deckPanel { Applied to the DeckPanel }</li>
 * <li>.sc-ContentWidget-name { Applied to the name }</li>
 * <li>.sc-ContentWidget-description { Applied to the description }</li>
 * </ul>
 */
public abstract class Page extends LayoutPanel {

  /**
   * The constants used in this Page.
   */
  public static interface DemoConstants extends Constants {
    String mosaicPageExample();

    String mosaicPageSource();

    String mosaicPageStyle();
  }

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLE_NAME = "mosaic-sc-Page";

  /**
   * The static loading image displayed when loading CSS or source code.
   */
  private static Image loadingImage;

  /**
   * An instance of the constants
   */
  private DemoConstants constants;

  /**
   * A boolean indicating whether or not this widget has been initialized.
   */
  private boolean initialized = false;

  /**
   * The tab panel with the contents.
   */
  private DecoratedTabLayoutPanel tabPanel;

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
   * The widget used to display css style.
   */
  private HTML styleWidget = null;

  /**
   * A mapping of themes to style definitions.
   */
  private Map<String, String> styleDefs = null;

  public Page(DemoConstants constants) {
    this.constants = constants;
    setStyleName(DEFAULT_STYLE_NAME);
  }

  public final String getId() {
    final String s = this.getClass().getName();
    return s.substring(s.lastIndexOf('.') + 1, s.length());
  }

  public final void init() {
    if (initialized) {
      return;
    }
    initialized = true;

    tabPanel = new DecoratedTabLayoutPanel(true);
    tabPanel.setPadding(5);
    add(tabPanel);

    LayoutPanel panel1 = new LayoutPanel();
    LayoutPanel panel2 = new LayoutPanel();
    LayoutPanel panel3 = new LayoutPanel();

    tabPanel.add(constants.mosaicPageExample(), panel1);
    tabPanel.add(constants.mosaicPageSource(), panel2);
    tabPanel.add(constants.mosaicPageStyle(), panel3);

    // Add source code tab
    sourceWidget = new HTML();
    sourceWidget.setStyleName(DEFAULT_STYLE_NAME + "-source");
    panel2.add(sourceWidget);

    // Add style tab
    styleDefs = new HashMap<String, String>();
    styleWidget = new HTML();
    styleWidget.setStyleName(DEFAULT_STYLE_NAME + "-style");
    panel3.add(styleWidget);

    tabPanel.addTabListener(new TabListener() {
      public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
        return true;
      }

      public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
        // Load the source code
        if (!sourceLoaded) {
          sourceLoaded = true;
          String className = Page.this.getClass().getName();
          className = className.substring(className.lastIndexOf(".") + 1);
          requestSourceContents(ShowcaseConstants.DST_SOURCE_EXAMPLE + className
              + ".html", sourceWidget, null);
        }

        // Load the style definitions
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
          String className = Page.this.getClass().getName();
          className = className.substring(className.lastIndexOf(".") + 1);
          requestSourceContents(srcPath + "/" + className + ".html", styleWidget,
              callback);
        }
      }
    });

    onPageLoad(panel1);
  }

  public final boolean isInitialized() {
    return initialized;
  }

  protected abstract void onPageLoad(LayoutPanel layoutPanel);

  protected void requestSourceContents(String url, final HTML target,
      final RequestCallback callback) {
    // Show the loading image
    if (loadingImage == null) {
      loadingImage = new Image("images/loading.gif");
    }

    // Request the contents of the file
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
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

  /**
   * Get the name of this example to use as a title.
   * 
   * @return a name for this example
   */
  public abstract String getName();

}
