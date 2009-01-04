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
package org.gwt.mosaic.actions.client.old;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * @deprecated
 */
public class ActionRegistry {

  private static final Map<String, Action> actionMap = new HashMap<String, Action>();

  public static Action get(String id) {
    return actionMap.get(id);
  }

  public static void register(Action action) {
    actionMap.put(action.getId(), action);
  }

  public static void unregister(Action action) {
    actionMap.remove(action.getId());
  }
}
