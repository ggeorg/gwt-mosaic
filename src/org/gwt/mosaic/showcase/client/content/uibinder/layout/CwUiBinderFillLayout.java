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
package org.gwt.mosaic.showcase.client.content.uibinder.layout;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle({".mosaic-LayoutPanel"})
public class CwUiBinderFillLayout extends ContentWidget {
  interface Binder extends UiBinder<Widget, CwUiBinderFillLayout>{}
  private static final Binder binder = GWT.create(Binder.class);

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwUiBinderFillLayout(CwConstants constants) {
    super(constants);
  }
  
  @Override
  public String getDescription() {
    return "UiBinder FillLayout description";
  }

  @Override
  public String getName() {
    return "FillLayout";
  }
  
  @Override
  public boolean hasUiBinderSource() {
    return true;
  }
  
  /**
   * Initialized by UiBinder.
   */
  @ShowcaseData
  @UiField
  LayoutPanel layoutPanel;

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    binder.createAndBindUi(this);
    layoutPanel.setPadding(0);
    return layoutPanel;
  }
  
}
