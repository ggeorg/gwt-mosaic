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
package org.gwt.mosaic.actions.client;

/**
 * An {@code Action} represents the non-UI side of a command which can be
 * triggered by the end user. {@code Action}s are typically associated with
 * buttons, menu items, and items in tool bars. When the end user triggers the
 * command via its control, the action's {@code #actionPerformed(ActionEvent)}
 * method is invoked to do the real work.
 * <p>
 * This interface exists only to define the API for {@code Action}s. It is not
 * intended to be implemented by clients.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * @see CommandAction
 */
public interface Action extends ActionListener {

  /** A key to access the default property for the action (this is not used). */
  String DEFAULT = "Default";

  /** A key to access the name for the action. */
  String NAME = "Name";

  /**
   * A key to access the short description for the action (the short description
   * is typically used as the tool tip text).
   */
  String SHORT_DESCRIPTION = "ShortDescription";

  /**
   * A key to access the long description for the action.
   */
  String LONG_DESCRIPTION = "LongDescription";

  /**
   * A key to access the icon for the action.
   */
  String SMALL_ICON = "SmallIcon";

  /**
   * A key to access the action command string for the action (this is not used
   * in GWT Mosaic).
   */
  String ACTION_COMMAND_KEY = "ActionCommandKey";

  /**
   * A key to access the key stroke used as the accelerator for the action (this
   * is not used in GWT Mosaic).
   */
  String ACCELERATOR_KEY = "AcceleratorKey";

  /**
   * A key to access the mnemonic for the action.
   */
  String MNEMONIC_KEY = "MnemonicKey";

  /**
   * Returns the value associated with the specified key.
   * 
   * @param key the key (not {@code null}).
   * @return the value associated with the specified key, or {@code null} if the
   *         key is not found.
   */
  Object getValue(String key);

  /**
   * Returns the flag that indicates whether or not this action is enabled.
   * 
   * @return the flag
   */
  boolean isEnabled();

  /**
   * Sets the value associated with the specified key and sends a {@code
   * PropertyChangeEvent} to all registered listeners. Any existing value
   * associated with the key will be overwritten.
   * 
   * @param key the key (not {@code null}).
   * @param value the value ({@code null} permitted).
   */
  void putValue(String key, Object value);

  /**
   * Sets the flag that indicates whether or not this action is enabled. If the
   * value changes, a {@code java.beans.PropertyChangeEvent} is sent to all
   * registered listeners.
   * 
   * @param enabled the new value of the flag.
   */
  void setEnabled(boolean enabled);
}
