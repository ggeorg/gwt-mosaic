/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos.
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

import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.uibinder.elementparsers.ElementParser;
import com.google.gwt.uibinder.rebind.UiBinderWriter;
import com.google.gwt.uibinder.rebind.XMLAttribute;
import com.google.gwt.uibinder.rebind.XMLElement;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class LayoutPanelParser implements ElementParser {

  public void parse(XMLElement elem, String fieldName, JClassType type,
      UiBinderWriter writer) throws UnableToCompleteException {

    // Consume the single layout element.
    XMLElement layout = elem.consumeSingleChildElement();
    if (!isLayoutManagerElement(layout)) {
      writer.die(
          "In %1$s, child must be an instance of LayoutManager but found %2$s",
          elem, layout);
    }

    Map<String, String> layoutAttrs = new HashMap<String, String>();

    while (layout.getAttributeCount() > 0) {
      XMLAttribute attr = layout.getAttribute(0);
      layoutAttrs.put(attr.getLocalName(), attr.consumeStringValue());
    }

    // Parse children.
    for (XMLElement child : layout.consumeChildElements()) {
      if (!writer.isWidgetElement(child)) {
        writer.die("%s can contain only widgets, but found %s", elem, child);
      }
      if (child.getLocalName().endsWith("LayoutData")) {
        Map<String, String> layoutDataAttrs = new HashMap<String, String>();
        while (child.getAttributeCount() > 0) {
          XMLAttribute attr = child.getAttribute(0);
          layoutDataAttrs.put(attr.getLocalName(), attr.consumeStringValue());
        }
        XMLElement widget = child.consumeSingleChildElement();

        String childFieldName = writer.parseElementToField(child);
        String widgetFieldName = writer.parseElementToField(widget);

        boolean decorated = false;

        for (Map.Entry<String, String> entry : layoutDataAttrs.entrySet()) {
          if (entry.getKey().equals("decorated".intern())) {
            String value = entry.getValue().trim().toLowerCase();
            decorated = value.equals("\"true\"".intern());
            continue;
          }
          writer.genPropertySet(childFieldName, entry.getKey(),
              entry.getValue());
        }

        writer.addStatement("%1$s.add(%2$s, %3$s);", fieldName,
            widgetFieldName, childFieldName);

        if (decorated) {
          // Get the class associated with this child.
          JClassType childType = writer.findFieldType(child);

          writer.setFieldInitializerAsConstructor(childFieldName,
              writer.getOracle().findType(
                  childType.getParameterizedQualifiedSourceName()),
              "true".intern());
        }

      } else {
        String childFieldName = writer.parseElementToField(child);
        writer.addStatement("%1$s.add(%2$s);", fieldName, childFieldName);
      }
    }

    String layoutFieldName = writer.parseElementToField(layout);

    for (Map.Entry<String, String> entry : layoutAttrs.entrySet()) {
      writer.genPropertySet(layoutFieldName, entry.getKey(), entry.getValue());
    }

    writer.setFieldInitializerAsConstructor(fieldName,
        writer.getOracle().findType(LayoutPanel.class.getName()),
        layoutFieldName);
  }

  private boolean isLayoutManagerElement(XMLElement layout) {
    String uri = layout.getNamespaceUri();
    return uri != null && uri.startsWith("urn:import:");
  }

}
