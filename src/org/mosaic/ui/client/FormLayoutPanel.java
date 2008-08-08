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
package org.mosaic.ui.client;

import org.mosaic.core.client.DOM;
import org.mosaic.ui.client.layout.BaseLayout;
import org.mosaic.ui.client.layout.HasLayout;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;

public class FormLayoutPanel extends FormPanel implements HasLayout {
  
  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-FormLayoutPanel";

  public FormLayoutPanel() {
    super();
    setStyleName(DEFAULT_STYLENAME);
  }

  public int[] getPreferredSize() {
    int[] result = {0, 0};
    try {
      final Widget child = getWidget();
      if (!DOM.isVisible(child.getElement())) {
        return result;
      }
      result[0] = BaseLayout.getFlowWidth(child);
      result[1] = BaseLayout.getFlowHeight(child);
    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }
    return result;
  }

  public void layout() {
    final Widget child = getWidget();

    try {
      final int[] box = DOM.getClientSize(getElement());
      final int[] paddings = DOM.getPaddingSizes(getElement());

      final int left = paddings[3];
      final int top = paddings[0];
      int width = box[0] - (paddings[1] + paddings[3]);
      int height = box[1] - (paddings[0] + paddings[2]);

      if (!DOM.isVisible(child.getElement())) {
        return;
      }

      setBounds(child, left, top, width, height);

    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }

    if (child instanceof HasLayout && DOM.isVisible(child.getElement())) {
      ((HasLayout) child).layout();
    }
  }

  protected void setBounds(final Widget widget, final int x, final int y, int width,
      int height) {
    int[] margins = DOM.getMarginSizes(widget.getElement());
    if (width != -1) {
      width -= (margins[1] + margins[3]);
    }
    if (height != -1) {
      height -= (margins[0] + margins[2]);
    }
    // setXY(layoutPanel, widget, x, y);
    BaseLayout.setSize(widget, width, height);
  }

}
