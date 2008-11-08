/*
 * Copyright 2008 Google Inc.
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
package org.gwt.mosaic.forms.client.builder;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.layout.GridLayoutData;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class PanelBuilder extends AbstractFormBuilder implements
    HasHorizontalAlignment {

  private final FormLayoutPanel layoutPanel;

  private HorizontalAlignmentConstant align = ALIGN_LEFT;

  private boolean debug = false;

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public PanelBuilder(FormLayoutPanel layoutPanel) {
    this.layoutPanel = layoutPanel;
  }

  public Widget add(Widget widget) {
    return add(widget, new GridLayoutData());
  }

  public Widget add(Widget widget, GridLayoutData layoutData) {
    layoutPanel.add(widget, layoutData);
    if (debug) {
      DOM.setStyleAttribute(widget.getElement(), "border", "1px solid #f00");
    }
    return widget;
  }

  public Widget add(Widget widget, int colspan) {
    return add(widget, new GridLayoutData(colspan, 1));
  }

  public Widget addGap() {
    return addLabel(null);
  }

  public Widget addGap(int colspan) {
    return addLabel(null, colspan);
  }

  public Label addLabel(String text) {
    return addLabel(text, false, new GridLayoutData());
  }

  public Label addLabel(String text, boolean allowHTML,
      GridLayoutData layoutData) {
    final Label label = new Label(text);
    final WidgetWrapper widgetWrapper = new WidgetWrapper(label, align, HasAlignment.ALIGN_MIDDLE);
    widgetWrapper.getWidget().getCellFormatter().setWordWrap(0, 0, false);
    if (debug) {
      DOM.setStyleAttribute(widgetWrapper.getElement(), "border", "1px solid #f00");
    }
    layoutPanel.add(widgetWrapper, layoutData);
    return label;
  }

  public Label addLabel(String text, int colspan) {
    return addLabel(text, false, new GridLayoutData(colspan, 1));
  }

  public Separator addSeparator(String text) {
    return addSeparator(text, false, new GridLayoutData());
  }

  protected Separator addSeparator(String text, boolean allowHTML,
      GridLayoutData layoutData) {
    final Separator separator = new Separator(text);
    if (debug) {
      DOM.setStyleAttribute(separator.getElement(), "border", "1px solid #f00");
    }
    layoutPanel.add(new WidgetWrapper(separator), layoutData);
    return separator;
  }

  public Separator addSeparator(String text, int colspan) {
    return addSeparator(text, false, new GridLayoutData(colspan, 0));
  }

  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return align;
  }

  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    this.align = align;
  }
}
