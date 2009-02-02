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
package org.gwt.mosaic.forms.client.builder.old;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class Separator extends Composite {
  
  private static final String DEFAULT_STYLENAME = "mosaic-FormSeparator";

  public Separator(String text) {
    final HorizontalPanel panel = new HorizontalPanel();
    panel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
    //panel.setWidth("100%");
    final Label l = new Label(text);
    l.setWordWrap(false);
    panel.add(l);
    panel.add(new HTML("&nbsp;"));
    final HTML hr = new HTML("<hr></hr>");
    panel.add(hr);
    panel.setCellWidth(hr, "99%");
    initWidget(panel);
    setStyleName(DEFAULT_STYLENAME);
  }
}
