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

import javax.swing.Action;

import org.gwt.mosaic.actions.client.CommandAction;

import com.google.gwt.user.client.Command;

/**
 * {@code PasteAction} provides the action for the {@code PasteCommand}.
 * <p>
 * The action has a default name, icon and short description.
 * 
 * @author Anthony Sintes ObjectWave Corporation
 * @author georgopoulos.georgios(at)gmail.com
 */
public class PasteAction extends CommandAction {
  
  /**
   * This type defines the paste command.
   */
  public interface PasteCommand extends Command {
  }

  /**
   * @param command the paste command that the action should act upon.
   */
  public PasteAction(PasteCommand command) {
    super(ACTION_CONSTANTS.pasteName(), ACTION_IMAGES.paste_action(), command);
    putValue(Action.SHORT_DESCRIPTION, ACTION_CONSTANTS.pasteShortDescription());
  }

}
