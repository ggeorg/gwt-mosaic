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
package org.gwt.mosaic.actions.client.file;

import org.gwt.mosaic.actions.client.Action;
import org.gwt.mosaic.actions.client.CommandAction;

import com.google.gwt.user.client.Command;

/**
 * {@code AddFolderAction} provides the action for the {@code AddFolderCommand}.
 * <p>
 * The action has a default name, icon and short description.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public final class AddFolderAction extends CommandAction {

  /**
   * This type defines the add folder command.
   */
  public interface AddFolderCommand extends Command {
  }

  /**
   * @param command the add folder command that the action should act upon.
   */
  public AddFolderAction(AddFolderCommand command) {
    super(ACTION_CONSTANTS.addFolderName(), ACTION_IMAGES.add_folder_action(),
        command);
    putValue(Action.SHORT_DESCRIPTION, ACTION_CONSTANTS.addFolderShortDescription());
  }

}
