/*
 * Copyright 2008 Google Inc.
 * 
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos
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

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * The images used throughout the Showcase.
 * 
 * @author ggeorgopoulos.georgios(at)gmail.com
 */
public interface ShowcaseImages extends ClientBundle {

  ImageResource calendar();

  ImageResource catForms();
  
  ImageResource catI18N();

  ImageResource catLists();

  ImageResource catOther();

  ImageResource catPanels();

  ImageResource catPopups();

  ImageResource catTables();

  ImageResource catTextInput();

  ImageResource catWidgets();
  
  ImageResource css();

  ImageResource cup();
  
  ImageResource gwtLogo();
  
  ImageResource gwtLogoThumb();
  
  /**
   * Indicates the locale selection box.
   */
  ImageResource locale();
  
  ImageResource mediaPlayGreen();
  
  ImageResource showcaseDemos();
  
  ImageResource person();
}
