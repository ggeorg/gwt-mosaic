/*
 * Copyright 2008 Google Inc.
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
package com.google.gwt.libideas.resources.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.libideas.resources.rebind.ResourceGeneratorType;
import com.google.gwt.libideas.resources.rg.CssResourceGenerator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Aggregates and minifies CSS stylesheets. A CssResource represents a regular
 * CSS file with GWT-specific at-rules.
 * <p>
 * Currently-supported accessor functions:
 * 
 * <ul>
 * <li>{@code String someClassName();} will allow the css class
 * <code>.someClassName</code> to be obfuscated at runtime. The function will
 * return the obfuscated class name.</li>
 * <li>{@code Sprite someSpriteName();} allows bundled images to be used as CSS
 * background images. Use {@link Sprite#apply(Element)} to display the sprite in
 * the given Element.</li>
 * </ul>
 * 
 * <p>
 * Currently-supported rules:
 * 
 * <ul>
 * <li>{@code @def NAME literal-value; .myClass {background: NAME;}} Define a
 * static constant.</li>
 * <li>{@code @eval NAME Java-expression; .myClass {background: NAME;}} Define
 * a constant based on a Java expression.</li>
 * <li>{@code @if [!]property (list of values) {ruleBlock}} Include or exclude
 * CSS rules based on the value of a deferred-binding property.</li>
 * <li>{@code @if Java-expression {ruleBlock}} Include or exclude CSS rules
 * based on a boolean Java expression.</li>
 * <li>{@code @sprite className siblingImageResource;} Return a {@link Sprite}
 * to access the style.</li>
 * <li>{@code @url NAME siblingDataResource; .myClass {background: NAME repeat-x;}}
 * Use a DataResource to generate a <code>url('...'}</code> value.</li>
 * </ul>
 * 
 * @see <a
 *      href="http://code.google.com/p/google-web-toolkit-incubator/wiki/CssResource">
 *      CssResource design doc</a>
 */
@ResourceGeneratorType(CssResourceGenerator.class)
public interface CssResource extends ResourcePrototype {
  /**
   * The original CSS class name specified in the resource. This allows CSS
   * classes that do not correspond to Java identifiers to be mapped onto
   * obfuscated class accessors.
   * 
   * <pre>
   * .some-non-java-ident { background: blue; }
   * 
   * interface MyCssResource extends CssResource {
   *   {@literal @}ClassName("some-non-java-ident")
   *   String classAccessor();
   * }
   * </pre>
   */
  @Documented
  @Target(ElementType.METHOD)
  @interface ClassName {
    String value();
  }

  /**
   * Overrides the prefix used for obfuscated CSS class names within a bundle
   * type. This annotation must be applied to the enclosing
   * ImmutableResourceBundle because the bundle itself defines the scope in
   * which the obfuscation of CSS class identifiers is applied.
   * <p>
   * The default algorithm is designed to be safe, but will not produce the
   * shortest possible CSS class identifiers. The developer should choose a
   * prefix that is known to not conflict with external CSS class names. An
   * application written by FooBar Inc. might choose to use a prefix
   * <code>FB</code>.
   */
  @Target(ElementType.TYPE)
  @interface ClassPrefix {
    String value();
  }

  /**
   * Represents a {@literal @sprite} meta-class in the stylesheet.
   */
  interface Sprite {
    /**
     * Alters an Element to show the sprite. The structure of the Element should
     * be considered opaque after applying the Sprite function.
     */
    void apply(Element e);
  }

  /**
   * Provides the contents of the CssResource.
   */
  String getText();
}
