/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopolos.
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
package org.gwt.mosaic.actions.client.edit;

import org.gwt.mosaic.actions.client.CommandAction;

import com.google.gwt.user.client.Command;

/**
 * {@code SelectAllAction} provides the action for the {@code SelectAllCommand}.
 * <p>
 * The action has a default name.
 * 
 * @author Anthony Sintes ObjectWave Corporation
 * @author georgopoulos.georgios(at)gmail.com
 */
public final class SelectAllAction extends CommandAction {

  /**
   * This type defines the select all command.
   */
  public interface SelectAllCommand extends Command {
  }

  /**
   * @param command the select all command that the action should act upon.
   */
  public SelectAllAction(SelectAllCommand command) {
    super(ACTION_CONSTANTS.selectAllName(), command);
  }

}