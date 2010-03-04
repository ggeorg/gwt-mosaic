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

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.uibinder.elementparsers.ElementParser;
import com.google.gwt.uibinder.rebind.UiBinderWriter;
import com.google.gwt.uibinder.rebind.XMLElement;

/**
 * Parses {@link org.gwt.mosaic.ui.client.PopupMenu} widgets.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class PopupMenuParser implements ElementParser {
  private static final String TAG_MENUITEM = "MenuItem";

  public void parse(XMLElement elem, String fieldName, JClassType type,
      UiBinderWriter writer) throws UnableToCompleteException {

    // Parse children.
    for (XMLElement child : elem.consumeChildElements()) {

      // PopupMenu can only contain MenuItem elements.
      {
        String tagName = child.getLocalName();

        if (!tagName.equals(TAG_MENUITEM)) {
          writer.die(
              "In %s, only com.google.gwt.user.client.ui.%s are valid children",
              elem, TAG_MENUITEM);
        }
      }

      String itemFieldName = writer.parseElementToField(child);

      writer.addStatement("%1$s.addItem(%2$s);", fieldName, itemFieldName);
    }
  }

}
