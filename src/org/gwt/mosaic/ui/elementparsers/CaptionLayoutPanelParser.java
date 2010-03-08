package org.gwt.mosaic.ui.elementparsers;

import org.gwt.mosaic.ui.client.Caption.CaptionRegion;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.uibinder.elementparsers.ElementParser;
import com.google.gwt.uibinder.elementparsers.HtmlInterpreter;
import com.google.gwt.uibinder.rebind.UiBinderWriter;
import com.google.gwt.uibinder.rebind.XMLElement;

public class CaptionLayoutPanelParser implements ElementParser {

  private static class Children {
    XMLElement left;
    XMLElement right;
    XMLElement header;
  }

  private static final String LEFT = "left";
  private static final String RIGHT = "right";
  private static final String HEADER = "header";
  private static final String CAPTION = "caption";

  public void parse(XMLElement elem, String fieldName, JClassType type,
      final UiBinderWriter writer) throws UnableToCompleteException {

    // Parse children
    for (final XMLElement child : elem.consumeChildElements()) {

      // Get the header element.
      if (isElementType(elem, child, CAPTION)) {

        // Find all children of the <header>.
        Children children = findChildren(child, writer);

        if (children.header != null) {
          HtmlInterpreter htmlInt = HtmlInterpreter.newInterpreterForUiObject(
              writer, fieldName);
          String html = children.header.consumeInnerHtml(htmlInt);
          writer.addStatement("%s.getHeader().setHTML(\"%s\");", fieldName,
              html);
        }

        if (children.left != null) {
          for (XMLElement left : children.left.consumeChildElements()) {
            if (!writer.isWidgetElement(left)) {
              writer.die("%s can contain only widgets, but found %s", child,
                  left);
            }
            String childFieldName = writer.parseElementToField(left);
            writer.addStatement("%s.getHeader().add(%s, %s.LEFT);", fieldName,
                childFieldName, CaptionRegion.class.getCanonicalName());
          }
        }

        if (children.right != null) {
          for (XMLElement right : children.right.consumeChildElements()) {
            if (!writer.isWidgetElement(right)) {
              writer.die("%s can contain only widgets, but found %s", child,
                  right);
            }
            String childFieldName = writer.parseElementToField(right);
            writer.addStatement("%s.getHeader().add(%s, %s.RIGHT);", fieldName,
                childFieldName, CaptionRegion.class.getCanonicalName());
          }
        }

        continue;
      }

      // TODO (ggeorg) support for layout managers

      if (!writer.isWidgetElement(child)) {
        writer.die("%s can contain only widgets, but found %s", elem, child);
      }
      String childFieldName = writer.parseElementToField(child);
      writer.addStatement("%1$s.add(%2$s);", fieldName, childFieldName);
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

        if (hasTag(child, LEFT)) {
          assertFirstLeft();
          children.left = child;
          return true;
        }

        if (hasTag(child, RIGHT)) {
          assertFirstRight();
          children.right = child;
          return true;
        }

        // FIXME (ggeorg) ignore body?

        return true;
      }

      private void assertFirstHeader() throws UnableToCompleteException {
        if (null != children.header) {
          writer.die("In %s, may have only one %2$s:headers element", elem,
              elem.getPrefix());
        }
      }

      private void assertFirstLeft() throws UnableToCompleteException {
        if (null != children.left) {
          writer.die("In %s, may have only one %2$s:left element", elem,
              elem.getPrefix());
        }
      }

      private void assertFirstRight() throws UnableToCompleteException {
        if (null != children.right) {
          writer.die("In %s, may have only one %2$s:right element", elem,
              elem.getPrefix());
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
