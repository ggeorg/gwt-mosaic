/*
 * Copyright 2009 Georgopoulos J. Georgios
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
package org.gwt.mosaic.actions.client.db;

import org.gwt.mosaic.actions.client.CommandAction;

import com.google.gwt.user.client.Command;

/**
 * {@code FormAddAction} provides the action for the {@code FormAddCommand}.
 * <p>
 * The action has a default name and icon.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public final class FormAddAction extends CommandAction {

  /**
   * This type defines the form add command.
   */
  public interface FormAddCommand extends Command {
  }

  /**
   * @param command the form add command that the action should act upon.
   */
  public FormAddAction(FormAddCommand command) {
    super(ACTION_CONSTANTS.formAddName(), ACTION_IMAGES.form_add_action(),
        command);
  }

}
