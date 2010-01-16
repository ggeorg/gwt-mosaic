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

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Convenience class to use user module and a default tear down code for widget
 * tests.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class WidgetTestBase extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "org.gwt.mosaic.Mosaic";
  }

  /**
   * A replacement for JUnit's {@link #tearDown()} method. This method runs once
   * per test method in your subclass, just after each test method runs and can
   * be used to perform clean up. Override this method instead of
   * {@link #tearDown()}.
   */
  @Override
  protected void gwtTearDown() throws Exception {
    RootPanel.get().clear();
  }
}
