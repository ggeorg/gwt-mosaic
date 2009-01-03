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
package org.gwt.mosaic.actions.client.file;

import org.gwt.mosaic.actions.client.CommandAction;

import com.google.gwt.user.client.Command;

/**
 * {@code CloseAllAction} provides the action for the {@code CloseAllCommand} command.
 * <p>
 * The action has a default name and icon.
 * 
 * @author Anthony Sintes ObjectWave Corporation
 * @author georgopoulos.georgios(at)gmail.com
 */
public final class CloseAllAction extends CommandAction {
  
  /**
   * This type defines the close all command.
   */
  public interface CloseAllCommand extends Command {
  }

  /**
   * @param command the close all command that the action should act upon.
   */
  public CloseAllAction(CloseAllCommand command) {
    super(ACTION_CONSTANTS.closeAllName(), ACTION_IMAGES.close_all_action(), command);
  }

}
