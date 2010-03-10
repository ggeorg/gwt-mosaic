/*
 * Copyright (c) 2010 GWT Mosaic Georgios J. Georgopoulos.
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

import org.gwt.mosaic.ui.client.TabLayoutPanel;
import org.gwt.mosaic.ui.client.TabLayoutPanel.TabBarPosition;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JEnumType;
import com.google.gwt.uibinder.elementparsers.ElementParser;
import com.google.gwt.uibinder.elementparsers.HtmlInterpreter;
import com.google.gwt.uibinder.rebind.UiBinderWriter;
import com.google.gwt.uibinder.rebind.XMLElement;

/**
 * Parses {@link org.gwt.mosaic.ui.client.TabLayoutPanel} widgets.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class TabLayoutPanelParser implements ElementParser {

  private static class Children {
    XMLElement header;
    XMLElement body;
  }

  private static final String HEADER = "header";
  private static final String TAB = "tab";

  public void parse(XMLElement panelElem, String fieldName, JClassType type,
      UiBinderWriter writer) throws UnableToCompleteException {
    String decorate = panelElem.consumeBooleanAttribute("decorate", false);
    String decorateBody = panelElem.consumeBooleanAttribute("decorateBody", false);

    JEnumType regionEnumType = writer.getOracle().findType(
        TabBarPosition.class.getCanonicalName()).isEnum();
    String region = panelElem.consumeAttributeWithDefault("region",
        String.format("%s.%s", regionEnumType.getQualifiedSourceName(), "TOP"),
        regionEnumType);

    writer.setFieldInitializerAsConstructor(fieldName,
        writer.getOracle().findType(TabLayoutPanel.class.getName()), region,
        decorate, decorateBody);

    // Parse children.
    for (XMLElement tabElem : panelElem.consumeChildElements()) {
      // Get the tab element.
      if (!isElementType(panelElem, tabElem, TAB)) {
        writer.die("In %s, only <%s:%s> children are allowed.", panelElem,
            panelElem.getPrefix(), TAB);
      }

      // Find all the children of the <tab>.
      Children children = findChildren(tabElem, writer);

      // Parse the child widget.
      if (children.body == null) {
        writer.die("In %s, %s must have a child widget", panelElem, tabElem);
      }
      if (!writer.isWidgetElement(children.body)) {
        writer.die("In %s, %s must be a widget", tabElem, children.body);
      }
      String childFieldName = writer.parseElementToField(children.body);

      // Parse the header.
      if (children.header != null) {
        HtmlInterpreter htmlInt = HtmlInterpreter.newInterpreterForUiObject(
            writer, fieldName);
        String html = children.header.consumeInnerHtml(htmlInt);
        writer.addStatement("%s.add(%s, \"%s\", true);", fieldName,
            childFieldName, html);
      } else {
        writer.die(
            "In %1$s, %2$s requires a <%3$s:%4$s>",
            panelElem, tabElem, tabElem.getPrefix(), HEADER);
      }
    }
  }

  private Children findChildren(final XMLElement elem,
      final UiBinderWriter writer) throws UnableToCompleteException {
    final Children children = new Children();

    elem.consumeChildElements(new XMLElement.Interpreter<Boolean>() {
      public Boolean interpretElement(XMLElement child)
          throws UnableToCompleteException {

        if (hasTag(child, HEADER)) {
          assertFirstHeader();
          children.header = child;
          return true;
        }

        // Must be the body, then
        if (null != children.body) {
          writer.die("In %s, may have only one body element", elem);
        }

        children.body = child;
        return true;
      }

      void assertFirstHeader() throws UnableToCompleteException {
        if (null != children.header) {
          writer.die("In %1$s, may have only one %2$s:header ", elem, elem.getPrefix());
        }
      }

      private boolean hasTag(XMLElement child, final String attribute) {
        return rightNamespace(child) && child.getLocalName().equals(attribute);
      }

      private boolean rightNamespace(XMLElement child) {
        return child.getNamespaceUri().equals(elem.getNamespaceUri());
      }
    });

    return children;
  }

  private boolean isElementType(XMLElement parent, XMLElement child, String type) {
    return parent.getNamespaceUri().equals(child.getNamespaceUri())
        && type.equals(child.getLocalName());
  }

}
