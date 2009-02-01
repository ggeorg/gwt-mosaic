/*
 * Copyright (c) 2009 GWT Mosaic Georgopolos J. Georgios
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
package org.gwt.mosaic.core.client;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public interface Shape {
  public boolean contains(double x, double y);
  
  public boolean contains(double x, double y, double w, double h);
  
  public Rectangle getBounds();
  
  public boolean intersects(double x, double y, double w, double h);
}
