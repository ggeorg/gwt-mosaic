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
 * {@code FormDeleteAction} provides the action for the {@code FormDeleteCommand}.
 * <p>
 * The action has a default name and icon.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public final class FormDeleteAction extends CommandAction {

  /**
   * This type defines the form delete command.
   */
  public interface FormDeleteCommand extends Command {
  }

  /**
   * @param command the form delete command that the action should act upon.
   */
  public FormDeleteAction(FormDeleteCommand command) {
    super(ACTION_CONSTANTS.formDeleteName(), ACTION_IMAGES.form_delete_action(),
        command);
  }

}
