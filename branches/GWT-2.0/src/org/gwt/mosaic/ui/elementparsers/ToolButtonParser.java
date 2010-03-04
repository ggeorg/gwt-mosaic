/*
 * Copyright (c) 2010 GWT Mosaic, Georgios J. Georgopoulos.
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
package org.gwt.mosaic.ui.elementparsers;

import org.gwt.mosaic.ui.client.PopupMenu;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.uibinder.elementparsers.ElementParser;
import com.google.gwt.uibinder.rebind.UiBinderWriter;
import com.google.gwt.uibinder.rebind.XMLElement;
import com.google.gwt.uibinder.rebind.XMLElement.Interpreter;

/**
 * Parses {@link org.gwt.mosaic.ui.client.ToolButton} widgets.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class ToolButtonParser implements ElementParser {

  public void parse(XMLElement elem, String fieldName, JClassType type,
      final UiBinderWriter writer) throws UnableToCompleteException {
    final JClassType popupMenuType = writer.getOracle().findType(
        PopupMenu.class.getCanonicalName());

    class PopupMenuInterpreter implements Interpreter<Boolean> {
      String popupMenuField = null;

      public Boolean interpretElement(XMLElement child)
          throws UnableToCompleteException {

        if (isPopupMenu(child)) {
          if (popupMenuField != null) {
            writer.die(
                "In %s, only one PopupMenu may be contained in a ToolButton",
                child);
          }
          popupMenuField = writer.parseElementToField(child);
          return true;
        }

        return false;
      }

      boolean isPopupMenu(XMLElement child) throws UnableToCompleteException {
        return popupMenuType.equals(writer.findFieldType(child));
      }
    }

    String menu = null;

    for (XMLElement child : elem.consumeChildElements()) {
      if ("menu".equals(child.getLocalName())) {
        if (menu != null) {
          writer.die("In %s, may have only one <%s:menu>", elem,
              elem.getPrefix());
        }

        PopupMenuInterpreter interpreter = new PopupMenuInterpreter();
        child.consumeChildElements(interpreter);

        if (interpreter.popupMenuField != null) {
          writer.genPropertySet(fieldName, "menu", interpreter.popupMenuField);
        }
      }
    }

  }

}
