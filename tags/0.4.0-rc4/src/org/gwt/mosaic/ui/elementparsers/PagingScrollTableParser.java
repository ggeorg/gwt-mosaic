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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.uibinder.elementparsers.ElementParser;
import com.google.gwt.uibinder.elementparsers.TextInterpreter;
import com.google.gwt.uibinder.rebind.UiBinderWriter;
import com.google.gwt.uibinder.rebind.XMLAttribute;
import com.google.gwt.uibinder.rebind.XMLElement;
import com.google.gwt.uibinder.rebind.messages.AttributeMessage;
import com.google.gwt.uibinder.rebind.model.OwnerFieldClass;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class PagingScrollTableParser implements ElementParser {

  public void parse(XMLElement elem, String fieldName, JClassType type,
      UiBinderWriter writer) throws UnableToCompleteException {

    // Consume the single tableDefinition element.
    final XMLElement tableDefinition = elem.consumeSingleChildElement();

    if (!isTableDefinitionElement(tableDefinition)) {
      writer.die(
          "In %1$s, child must be an instance of TableDefinition but found %2$s",
          elem, tableDefinition);
    }

    final List<String> columnDefFieldNames = new ArrayList<String>();

    // Parse children.
    for (XMLElement columnDefinition : tableDefinition.consumeChildElements()) {
      if (!isColumnDefinitionElement(columnDefinition)) {
        writer.die("%s can contain only widgets, but found %s", elem,
            columnDefinition);
      }

      final Map<String, String> setterValues = new HashMap<String, String>();
      final Map<String, String> localizedValues = fetchLocalizedAttributeValues(
          columnDefinition, writer);

      final JClassType ownerFieldClass = writer.findFieldType(columnDefinition);

      // Work through the localized attribute values and assign them to
      // appropriate setters (which had better be ready to accept strings)
      for (Entry<String, String> property : localizedValues.entrySet()) {
        String key = property.getKey();
        String value = property.getValue();

        JMethod setter = OwnerFieldClass.getFieldClass(ownerFieldClass,
            writer.getLogger()).getSetter("key");
        JParameter[] params = setter == null ? null : setter.getParameters();

        if (setter == null || !(params.length == 1)
            || !isString(writer, params[0].getType())) {
          writer.die("In %s, no method found to apply message attribute %s",
              elem, key);
        } else {
          setterValues.put(key, value);
        }
      }

      // Now go through the element and dispatch its attributes
      while (columnDefinition.getAttributeCount() > 0) {
        XMLAttribute attribute = columnDefinition.getAttribute(0);

        // Ignore xmlns attributes
        if (attribute.getName().startsWith("xmlns:")) {
          continue;
        }

        String propertyName = attribute.getLocalName();
        if (setterValues.keySet().contains(propertyName)) {
          writer.die("In %s, duplicate attribute name: %s", columnDefinition,
              propertyName);
        }

        JMethod setter = OwnerFieldClass.getFieldClass(ownerFieldClass,
            writer.getLogger()).getSetter(propertyName);
        if (setter == null) {
          writer.die("In %s, class %s has no appropriate set%s() method",
              columnDefinition, columnDefinition.getLocalName(),
              initialCap(propertyName));
        } else {
          String n = attribute.getName();
          String value = columnDefinition.consumeAttributeWithDefault(n, null,
              getParamTypes(setter));

          if (value == null) {
            writer.die("In %s, unable to parse %s.", elem, attribute);
          }
          setterValues.put(propertyName, value);
        }
      }

      final String columnHeader = columnDefinition.consumeInnerTextEscapedAsHtmlStringLiteral(
          new TextInterpreter(writer)).trim();

      final String columnDefFieldName = writer.parseElementToField(columnDefinition);

      for (Map.Entry<String, String> entry : setterValues.entrySet()) {
        writer.genPropertySet(columnDefFieldName, entry.getKey(),
            entry.getValue());
      }

      writer.addStatement("%1$s.setHeader(0, \"%2$s\");", columnDefFieldName,
          columnHeader);

      columnDefFieldNames.add(columnDefFieldName);
    }

    final String tableDefFieldName = writer.parseElementToField(tableDefinition);

    for (String columnDefFieldName : columnDefFieldNames) {
      writer.addStatement("%1$s.addColumnDefinition(%2$s);", tableDefFieldName,
          columnDefFieldName);
    }

    writer.addStatement("%1$s.setTableDefinition(%2$s);", fieldName,
        tableDefFieldName);
  }

  private JType[] getParamTypes(JMethod setter) {
    JParameter[] params = setter.getParameters();
    JType[] types = new JType[params.length];
    for (int i = 0; i < params.length; i++) {
      types[i] = params[i].getType();
    }
    return types;
  }

  private String initialCap(String propertyName) {
    return propertyName.substring(0, 1).toUpperCase()
        + propertyName.substring(1);
  }

  private boolean isString(UiBinderWriter writer, JType paramType) {
    JType stringType = writer.getOracle().findType(String.class.getName());
    return stringType.equals(paramType);
  }

  /**
   * Fetch the localized attributes that were stored by the
   * AttributeMessageParser.
   */
  private Map<String, String> fetchLocalizedAttributeValues(XMLElement elem,
      UiBinderWriter writer) {
    final Map<String, String> localizedValues = new HashMap<String, String>();

    Collection<AttributeMessage> attributeMessages = writer.getMessages().retrieveMessageAttributesFor(
        elem);

    if (attributeMessages != null) {
      for (AttributeMessage att : attributeMessages) {
        String propertyName = att.getAttribute();
        localizedValues.put(propertyName, att.getMessageUnescaped());
      }
    }
    return localizedValues;
  }

  private boolean isColumnDefinitionElement(XMLElement columnDefinition) {
    String uri = columnDefinition.getNamespaceUri();
    return uri != null && uri.startsWith("urn:import:")
        && columnDefinition.getLocalName().endsWith("ColumnDefinition");
  }

  private boolean isTableDefinitionElement(XMLElement tableDefinition) {
    String uri = tableDefinition.getNamespaceUri();
    return uri != null && uri.startsWith("urn:import:")
        && tableDefinition.getLocalName().endsWith("TableDefinition");
  }

}
