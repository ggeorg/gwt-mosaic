/*
 * Copyright (C) 2009-2010 Georgios J. Georgopoulos, All rights reserved.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.gwt.mosaic.application.client;

import org.gwt.mosaic.actions.client.ActionMap;

/**
 * A singleton that manages shared objects, like actions, resources, and tasks,
 * for {@code Applications}.
 * <p>
 * {@link Application Applications} use the {@code ApplicationContext} singleton
 * to find global values and services. The majority of the GWT Mosaic
 * Application Framework API can be accessed through the {@code
 * ApplicationContext}. The static {@code #getInstance()} method returns the
 * singleton Typically it's only called after the application has been
 * {@link Application#launch(Application) launched}, however it is always safe
 * to call {@code getInstance()}.
 * 
 * @see Application
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class ApplicationContext {

  private Application application = null;

  private ActionManager actionManager;
  
  private ApplicationConstants constants;
  
  private ApplicationClientBundle clientBundle;

  /**
   * Return the {@code Application} singleton, or {@code null} if {@code launch}
   * hasn't been called yet.
   * 
   * @return the launched {@code Application} singleton.
   * @seet {@link Application#launch(Application)}
   */
  public final Application getApplication() {
    return application;
  }

  /**
   * Called by {@link Application#launch(Application)}.
   * 
   * @param application the {@code Application} singleton.
   */
  void setApplication(Application application) {
    if (this.application != null) {
      throw new IllegalStateException("application has already been launched");
    }
    this.application = application;
  }

  /**
   * Returns the shared {@code ActionMap} chain for the entire {@code
   * Application}.
   * 
   * @return the {@code ActionMap} chain for the entire {@code Application}.
   * @see ActionManager#getActionMap()
   */
  public final ActionMap getActionMap() {
    if (actionManager == null) {
      actionManager = new ActionManager(this);
    }
    return actionManager.getActionMap();
  }
  
  void setConstants(ApplicationConstants constants) {
    this.constants = constants;
  }
  
  public final ApplicationConstants getConstants() {
    return constants;
  }
  
  void setClientBundle(ApplicationClientBundle clientBundle) {
    this.clientBundle = clientBundle;
  }
  
  public final ApplicationClientBundle getClientBundle() {
    return clientBundle;
  }

}
