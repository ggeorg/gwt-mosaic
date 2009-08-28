/*
 * Copyright (C) 2009 Georgios J. Georgopoulos, All rights reserved.
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
 * The application's {@code ActionManager} provides read-only cached access to
 * {@code ActionMaps} that contain one entry for each method marked with the
 * {@code @CmdAction} annotation in a class.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class ActionManager extends AbstractBean {
  private static final long serialVersionUID = -885791512987449028L;

  private final ApplicationContext context;

  private final ActionMap actionMap;

  protected ActionManager(ApplicationContext context) {
    assert (context != null);
    this.context = context;
    // create map
    actionMap = new ActionMap();
  }

  protected final ApplicationContext getContext() {
    return context;
  }

  public ActionMap getActionMap() {
    return actionMap;
  }

}
