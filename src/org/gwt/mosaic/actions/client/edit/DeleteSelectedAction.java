/*
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

import org.gwt.mosaic.actions.client.Action;
import org.gwt.mosaic.actions.client.CommandAction;

import com.google.gwt.user.client.Command;

/**
 * {@code DeleteSelectedAction} provides the action for the {@code
 * DeleteSelectedCommand}.
 * <p>
 * The action has a default name, icon and short description.
 * 
 * @author Anthony Sintes ObjectWave Corporation
 * @author georgopoulos.georgios(at)gmail.com
 */
public final class DeleteSelectedAction extends CommandAction {

  /**
   * This type defines the delete selected command.
   */
  public interface DeleteSelectedCommand extends Command {
  }

  /**
   * @param command the delete selected command that the should act upon.
   */
  public DeleteSelectedAction(DeleteSelectedCommand command) {
    super(ACTION_CONSTANTS.deleteSelectedName(), ACTION_IMAGES.delete_action(),
        command);
    putValue(Action.SHORT_DESCRIPTION,
        ACTION_CONSTANTS.deleteSelectedDescription());
  }

}
