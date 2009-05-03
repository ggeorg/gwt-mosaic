/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopolos.
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
/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * o Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * o Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * o Neither the name of JGoodies Karsten Lentzsch nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.gwt.mosaic.forms.client.builder;

import org.gwt.mosaic.forms.client.factories.DefaultWidgetFactory;
import org.gwt.mosaic.forms.client.factories.WidgetFactory;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.Label;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.Separator;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

/**
 * An general purpose panel builder that uses the {@link FormLayout} to lay out
 * <code>JPanel</code>s. It provides convenience methods to set a default border
 * and to add labels, titles and titled separators.
 * <p>
 * 
 * The PanelBuilder is the working horse for layouts when more specialized
 * builders like the {@link ButtonBarBuilder} or {@link DefaultFormBuilder} are
 * inappropriate.
 * <p>
 * 
 * The Forms tutorial includes several examples that present and compare
 * different style to build with the PanelBuilder: static row numbers vs. row
 * variable, explicit CellConstraints vs. builder cursor, static rows vs.
 * dynamically added rows. Also, you may check out the Tips &amp; Tricks section
 * of the Forms HTML documentation.
 * <p>
 * 
 * The text arguments passed to the methods <code>#addLabel</code>,
 * <code>#addTitle</code>, and <code>#addSeparator</code> can contain an
 * optional mnemonic marker. The mnemonic and mnemonic index are indicated by a
 * single ampersand (<tt>&amp;</tt>). For example <tt>&quot;&amp;Save&quot</tt>,
 * or <tt>&quot;Save&nbsp;&amp;as&quot</tt>. To use the ampersand itself
 * duplicate it, for example <tt>&quot;Look&amp;&amp;Feel&quot</tt>.
 * <p>
 * 
 * <strong>Example:</strong><br>
 * This example creates a panel with 3 columns and 3 rows.
 * 
 * <pre>
 * FormLayout layout = new FormLayout(&quot;right:pref, 6dlu, 50dlu, 4dlu, default&quot;, // columns
 *     &quot;pref, 3dlu, pref, 3dlu, pref&quot;); // rows
 * PanelBuilder builder = new PanelBuilder(layout);
 * CellConstraints cc = new CellConstraints();
 * builder.addLabel(&quot;&amp;Title&quot;, cc.xy(1, 1));
 * builder.add(new JTextField(), cc.xywh(3, 1, 3, 1));
 * builder.addLabel(&quot;&amp;Price&quot;, cc.xy(1, 3));
 * builder.add(new JTextField(), cc.xy(3, 3));
 * builder.addLabel(&quot;&amp;Author&quot;, cc.xy(1, 5));
 * builder.add(new JTextField(), cc.xy(3, 5));
 * builder.add(new JButton(&quot;...&quot;), cc.xy(5, 5));
 * return builder.getPanel();
 * </pre>
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see com.WidgetFactory.forms.factories.ComponentFactory
 * @see I15dPanelBuilder
 * @see DefaultFormBuilder
 */
public class PanelBuilder extends AbstractFormBuilder {

  /**
   * Refers to a factory that is used to create labels, titles and paragraph
   * separators.
   */
  private WidgetFactory componentFactory;

  // Instance Creation ****************************************************

  /**
   * Constructs a <code>PanelBuilder</code> for the given layout. Uses an
   * instance of <code>JPanel</code> as layout container with the given layout
   * as layout manager.
   * 
   * @param layout the FormLayout to use
   */
  public PanelBuilder(FormLayout layout) {
    this(layout, new ScrollLayoutPanel());
  }

  /**
   * Constructs a <code>PanelBuilder</code> for the given FormLayout and layout
   * container.
   * 
   * @param layout the FormLayout to use
   * @param panel the layout container to build on
   */
  public PanelBuilder(FormLayout layout, LayoutPanel panel) {
    super(layout, panel);
  }

  // Accessors ************************************************************

  /**
   * Returns the panel used to build the form.
   * 
   * @return the panel used by this builder to build the form
   */
  public final LayoutPanel getPanel() {
    return getLayoutPanel();
  }

  // Frequently Used Panel Properties ***************************************

  /**
   * Sets the panel's background color.
   * 
   * @param background the color to set as new background
   * 
   * @see JComponent#setBackground(Color)
   * 
   * @since 1.1
   */
  // public final void setBackground(Color background) {
  // getPanel().setBackground(background);
  // }
  /**
   * Sets the panel's border.
   * 
   * @param border the border to set
   * 
   * @see JComponent#setBorder(Border)
   */
  // public final void setBorder(Border border) {
  // getPanel().setBorder(border);
  // }
  /**
   * Sets the default dialog border.
   * 
   * @see Borders
   */
  // public final void setDefaultDialogBorder() {
  // setBorder(Borders.DIALOG_BORDER);
  // }
  /**
   * Sets the panel's opaque state.
   * 
   * @param b true for opaque, false for non-opaque
   * 
   * @see JComponent#setOpaque(boolean)
   * 
   * @since 1.1
   */
  // public final void setOpaque(boolean b) {
  // getPanel().setOpaque(b);
  // }
  // Adding Labels **********************************************************
  /**
   * Adds a textual label to the form using the default constraints.
   * <p>
   * 
   * <pre>
   * addLabel(&quot;Name:&quot;); // No Mnemonic
   * addLabel(&quot;N&amp;ame:&quot;); // Mnemonic is 'a'
   * addLabel(&quot;Save &amp;as:&quot;); // Mnemonic is the second 'a'
   * addLabel(&quot;Look&amp;&amp;Feel:&quot;); // No mnemonic, text is &quot;look&amp;feel&quot;
   * </pre>
   * 
   * @param textWithMnemonic the label's text - may contain an ampersand (
   *          <tt>&amp;</tt>) to mark a mnemonic
   * @return the new label
   * 
   * @see WidgetFactory
   */
  public final Widget addLabel(String textWithMnemonic) {
    return addLabel(textWithMnemonic,
        (CellConstraints) cellConstraints().clone());
  }

  /**
   * Adds a textual label to the form using the specified constraints.
   * <p>
   * 
   * <pre>
   * addLabel(&quot;Name:&quot;, cc.xy(1, 1)); // No Mnemonic
   * addLabel(&quot;N&amp;ame:&quot;, cc.xy(1, 1)); // Mnemonic is 'a'
   * addLabel(&quot;Save &amp;as:&quot;, cc.xy(1, 1)); // Mnemonic is the second 'a'
   * addLabel(&quot;Look&amp;&amp;Feel:&quot;, cc.xy(1, 1)); // No mnemonic, text is &quot;look&amp;feel&quot;
   * </pre>
   * 
   * @param textWithMnemonic the label's text - may contain an ampersand (
   *          <tt>&amp;</tt>) to mark a mnemonic
   * @param constraints the label's cell constraints
   * @return the new label
   * 
   * @see WidgetFactory
   */
  public final Label addLabel(String textWithMnemonic,
      CellConstraints constraints) {
    Label label = getComponentFactory().createLabel(textWithMnemonic);
    // XXX DOM.setStyleAttribute(label.getElement(), "overflow", "hidden");
    add(label, constraints);
    return label;
  }

  /**
   * Adds a textual label to the form using the specified constraints.
   * <p>
   * 
   * <pre>
   * addLabel(&quot;Name:&quot;, &quot;1, 1&quot;); // No Mnemonic
   * addLabel(&quot;N&amp;ame:&quot;, &quot;1, 1&quot;); // Mnemonic is 'a'
   * addLabel(&quot;Save &amp;as:&quot;, &quot;1, 1&quot;); // Mnemonic is the second 'a'
   * addLabel(&quot;Look&amp;&amp;Feel:&quot;, &quot;1, 1&quot;); // No mnemonic, text is &quot;look&amp;feel&quot;
   * </pre>
   * 
   * @param textWithMnemonic the label's text - may contain an ampersand (
   *          <tt>&amp;</tt>) to mark a mnemonic
   * @param encodedConstraints a string representation for the constraints
   * @return the new label
   * 
   * @see WidgetFactory
   */
  public final Widget addLabel(String textWithMnemonic,
      String encodedConstraints) {
    return addLabel(textWithMnemonic, new CellConstraints(encodedConstraints));
  }

  // Adding Label with related Component ************************************

  /**
   * Adds a label and component to the panel using the given cell constraints.
   * Sets the given label as <i>the</i> component label using
   * {@link JLabel#setLabelFor(java.awt.Component)}.
   * <p>
   * 
   * <strong>Note:</strong> The {@link CellConstraints} objects for the label
   * and the component must be different. Cell constraints are implicitly cloned
   * by the <code>FormLayout</code> when added to the container. However, in
   * this case you may be tempted to reuse a <code>CellConstraints</code> object
   * in the same way as with many other builder methods that require a single
   * <code>CellConstraints</code> parameter. The pitfall is that the methods
   * <code>CellConstraints.xy*(...)</code> just set the coordinates but do
   * <em>not</em> create a new instance. And so the second invocation of
   * <code>xy*(...)</code> overrides the settings performed in the first
   * invocation before the object is cloned by the <code>FormLayout</code>.
   * <p>
   * 
   * <strong>Wrong:</strong>
   * 
   * <pre>
   * CellConstraints cc = new CellConstraints();
   * builder.add(nameLabel, cc.xy(1, 7), // will be modified by the code below
   *     nameField, cc.xy(3, 7) // sets the single instance to (3, 7)
   * );
   * </pre>
   * 
   * <strong>Correct:</strong>
   * 
   * <pre>
   * // Using a single CellConstraints instance and cloning
   * CellConstraints cc = new CellConstraints();
   * builder.add(nameLabel, (CellConstraints) cc.xy(1, 7).clone(), // cloned before the next modification
   *     nameField, cc.xy(3, 7) // sets this instance to (3, 7)
   * );
   * // Using two CellConstraints instances
   * CellConstraints cc1 = new CellConstraints();
   * CellConstraints cc2 = new CellConstraints();
   * builder.add(nameLabel, cc1.xy(1, 7), // sets instance 1 to (1, 7)
   *     nameField, cc2.xy(3, 7) // sets instance 2 to (3, 7)
   * );
   * </pre>
   * 
   * @param label the label to add
   * @param labelConstraints the label's cell constraints
   * @param component the component to add
   * @param componentConstraints the component's cell constraints
   * @return the added label
   * @throws IllegalArgumentException if the same cell constraints instance is
   *           used for the label and the component
   * 
   * @see JLabel#setLabelFor(java.awt.Component)
   * @see DefaultFormBuilder
   */
  public final Label add(Label label, CellConstraints labelConstraints,
      Widget component, CellConstraints componentConstraints) {
    if (labelConstraints == componentConstraints)
      throw new IllegalArgumentException(
          "You must provide two CellConstraints instances, "
              + "one for the label and one for the component.\n"
              + "Consider using #clone(). See the JavaDocs for details.");

    add(label, labelConstraints);
    add(component, componentConstraints);
    // XXX label.setLabelFor(component);
    return label;
  }

  /**
   * Adds a label and component to the panel using the given cell constraints.
   * Sets the given label as <i>the</i> component label using
   * {@link JLabel#setLabelFor(java.awt.Component)}.
   * <p>
   * 
   * <strong>Note:</strong> The {@link CellConstraints} objects for the label
   * and the component must be different. Cell constraints are implicitly cloned
   * by the <code>FormLayout</code> when added to the container. However, in
   * this case you may be tempted to reuse a <code>CellConstraints</code> object
   * in the same way as with many other builder methods that require a single
   * <code>CellConstraints</code> parameter. The pitfall is that the methods
   * <code>CellConstraints.xy*(...)</code> just set the coordinates but do
   * <em>not</em> create a new instance. And so the second invocation of
   * <code>xy*(...)</code> overrides the settings performed in the first
   * invocation before the object is cloned by the <code>FormLayout</code>.
   * <p>
   * 
   * <strong>Wrong:</strong>
   * 
   * <pre>
   * builder.addLabel(&quot;&amp;Name:&quot;, // Mnemonic is 'N'
   *     cc.xy(1, 7), // will be modified by the code below
   *     nameField, cc.xy(3, 7) // sets the single instance to (3, 7)
   * );
   * </pre>
   * 
   * <strong>Correct:</strong>
   * 
   * <pre>
   * // Using a single CellConstraints instance and cloning
   * CellConstraints cc = new CellConstraints();
   * builder.addLabel(&quot;&amp;Name:&quot;, (CellConstraints) cc.xy(1, 7).clone(), // cloned before the next modification
   *     nameField, cc.xy(3, 7) // sets this instance to (3, 7)
   * );
   * // Using two CellConstraints instances
   * CellConstraints cc1 = new CellConstraints();
   * CellConstraints cc2 = new CellConstraints();
   * builder.addLabel(&quot;&amp;Name:&quot;, // Mnemonic is 'N'
   *     cc1.xy(1, 7), // sets instance 1 to (1, 7)
   *     nameField, cc2.xy(3, 7) // sets instance 2 to (3, 7)
   * );
   * </pre>
   * 
   * @param textWithMnemonic the label's text - may contain an ampersand (
   *          <tt>&amp;</tt>) to mark a mnemonic
   * @param labelConstraints the label's cell constraints
   * @param component the component to add
   * @param componentConstraints the component's cell constraints
   * @return the added label
   * @throws IllegalArgumentException if the same cell constraints instance is
   *           used for the label and the component
   * 
   * @see JLabel#setLabelFor(java.awt.Component)
   * @see WidgetFactory
   * @see DefaultFormBuilder
   */
  public final Label addLabel(String textWithMnemonic,
      CellConstraints labelConstraints, Widget component,
      CellConstraints componentConstraints) {

    if (labelConstraints == componentConstraints)
      throw new IllegalArgumentException(
          "You must provide two CellConstraints instances, "
              + "one for the label and one for the component.\n"
              + "Consider using #clone(). See the JavaDocs for details.");

    Label label = addLabel(textWithMnemonic, labelConstraints);
    add(component, componentConstraints);
    // XXX label.setLabelFor(component);
    return label;
  }

  // Adding Titles ----------------------------------------------------------

  /**
   * Adds a title label to the form using the default constraints.
   * <p>
   * 
   * <pre>
   * addTitle(&quot;Name&quot;); // No mnemonic
   * addTitle(&quot;N&amp;ame&quot;); // Mnemonic is 'a'
   * addTitle(&quot;Save &amp;as&quot;); // Mnemonic is the second 'a'
   * addTitle(&quot;Look&amp;&amp;Feel&quot;); // No mnemonic, text is Look&amp;Feel
   * </pre>
   * 
   * @param textWithMnemonic the title label's text - may contain an ampersand (
   *          <tt>&amp;</tt>) to mark a mnemonic
   * @return the added title label
   * 
   * @see WidgetFactory
   */
  public final Label addTitle(String textWithMnemonic) {
    return addTitle(textWithMnemonic, cellConstraints());
  }

  /**
   * Adds a title label to the form using the specified constraints.
   * <p>
   * 
   * <pre>
   * addTitle(&quot;Name&quot;, cc.xy(1, 1)); // No mnemonic
   * addTitle(&quot;N&amp;ame&quot;, cc.xy(1, 1)); // Mnemonic is 'a'
   * addTitle(&quot;Save &amp;as&quot;, cc.xy(1, 1)); // Mnemonic is the second 'a'
   * addTitle(&quot;Look&amp;&amp;Feel&quot;, cc.xy(1, 1)); // No mnemonic, text is Look&amp;Feel
   * </pre>
   * 
   * @param textWithMnemonic the title label's text - may contain an ampersand (
   *          <tt>&amp;</tt>) to mark a mnemonic
   * @param constraints the separator's cell constraints
   * @return the added title label
   * 
   * @see WidgetFactory
   */
  public final Label addTitle(String textWithMnemonic,
      CellConstraints constraints) {
    Label titleLabel = null;// XXX
    // getComponentFactory().createTitle(textWithMnemonic);
    add(titleLabel, constraints);
    return titleLabel;
  }

  /**
   * Adds a title label to the form using the specified constraints.
   * <p>
   * 
   * <pre>
   * addTitle(&quot;Name&quot;, &quot;1, 1&quot;); // No mnemonic
   * addTitle(&quot;N&amp;ame&quot;, &quot;1, 1&quot;); // Mnemonic is 'a'
   * addTitle(&quot;Save &amp;as&quot;, &quot;1, 1&quot;); // Mnemonic is the second 'a'
   * addTitle(&quot;Look&amp;&amp;Feel&quot;, &quot;1, 1&quot;); // No mnemonic, text is Look&amp;Feel
   * </pre>
   * 
   * @param textWithMnemonic the title label's text - may contain an ampersand (
   *          <tt>&amp;</tt>) to mark a mnemonic
   * @param encodedConstraints a string representation for the constraints
   * @return the added title label
   * 
   * @see WidgetFactory
   */
  public final Label addTitle(String textWithMnemonic, String encodedConstraints) {
    return addTitle(textWithMnemonic, new CellConstraints(encodedConstraints));
  }

  // Adding Separators ------------------------------------------------------

  /**
   * Adds a titled separator to the form that spans all columns.
   * <p>
   * 
   * <pre>
   * addSeparator(&quot;Name&quot;); // No Mnemonic
   * addSeparator(&quot;N&amp;ame&quot;); // Mnemonic is 'a'
   * addSeparator(&quot;Save &amp;as&quot;); // Mnemonic is the second 'a'
   * addSeparator(&quot;Look&amp;&amp;Feel&quot;); // No mnemonic, text is &quot;look&amp;feel&quot;
   * </pre>
   * 
   * @param textWithMnemonic the separator label's text - may contain an
   *          ampersand (<tt>&amp;</tt>) to mark a mnemonic
   * @return the added separator
   */
  public final Widget addSeparator(String textWithMnemonic) {
    return addSeparator(textWithMnemonic, getLayout().getColumnCount());
  }

  /**
   * Adds a titled separator to the form using the specified constraints.
   * <p>
   * 
   * <pre>
   * addSeparator(&quot;Name&quot;, cc.xy(1, 1)); // No Mnemonic
   * addSeparator(&quot;N&amp;ame&quot;, cc.xy(1, 1)); // Mnemonic is 'a'
   * addSeparator(&quot;Save &amp;as&quot;, cc.xy(1, 1)); // Mnemonic is the second 'a'
   * addSeparator(&quot;Look&amp;&amp;Feel&quot;, cc.xy(1, 1)); // No mnemonic, text is &quot;look&amp;feel&quot;
   * </pre>
   * 
   * @param textWithMnemonic the separator label's text - may contain an
   *          ampersand (<tt>&amp;</tt>) to mark a mnemonic
   * @param constraints the separator's cell constraints
   * @return the added separator
   */
  public final Widget addSeparator(String textWithMnemonic,
      CellConstraints constraints) {
    HorizontalAlignmentConstant titleAlignment = isLeftToRight()
        ? Separator.ALIGN_LEFT : Separator.ALIGN_RIGHT;
    Widget titledSeparator = getComponentFactory().createSeparator(
        textWithMnemonic, titleAlignment);
    add(titledSeparator, constraints);
    return titledSeparator;
  }

  /**
   * Adds a titled separator to the form using the specified constraints.
   * <p>
   * 
   * <pre>
   * addSeparator(&quot;Name&quot;, &quot;1, 1&quot;); // No Mnemonic
   * addSeparator(&quot;N&amp;ame&quot;, &quot;1, 1&quot;); // Mnemonic is 'a'
   * addSeparator(&quot;Save &amp;as&quot;, &quot;1, 1&quot;); // Mnemonic is the second 'a'
   * addSeparator(&quot;Look&amp;&amp;Feel&quot;, &quot;1, 1&quot;); // No mnemonic, text is &quot;look&amp;feel&quot;
   * </pre>
   * 
   * @param textWithMnemonic the separator label's text - may contain an
   *          ampersand (<tt>&amp;</tt>) to mark a mnemonic
   * @param encodedConstraints a string representation for the constraints
   * @return the added separator
   */
  public final Widget addSeparator(String textWithMnemonic,
      String encodedConstraints) {
    return addSeparator(textWithMnemonic, new CellConstraints(
        encodedConstraints));
  }

  /**
   * Adds a titled separator to the form that spans the specified columns.
   * <p>
   * 
   * <pre>
   * addSeparator(&quot;Name&quot;, 3); // No Mnemonic
   * addSeparator(&quot;N&amp;ame&quot;, 3); // Mnemonic is 'a'
   * addSeparator(&quot;Save &amp;as&quot;, 3); // Mnemonic is the second 'a'
   * addSeparator(&quot;Look&amp;&amp;Feel&quot;, 3); // No mnemonic, text is &quot;look&amp;feel&quot;
   * </pre>
   * 
   * @param textWithMnemonic the separator label's text - may contain an
   *          ampersand (<tt>&amp;</tt>) to mark a mnemonic
   * @param columnSpan the number of columns the separator spans
   * @return the added separator
   */
  public final Widget addSeparator(String textWithMnemonic, int columnSpan) {
    return addSeparator(textWithMnemonic,
        createLeftAdjustedConstraints(columnSpan));
  }

  // Accessing the ComponentFactory *****************************************

  /**
   * Returns the builder's component factory. If no factory has been set before,
   * it is lazily initialized using with an instance of
   * {@link com.DefaultWidgetFactory.forms.factories.DefaultComponentFactory}.
   * 
   * @return the component factory
   * 
   * @see #setComponentFactory(WidgetFactory)
   */
  public final WidgetFactory getComponentFactory() {
    if (componentFactory == null) {
      componentFactory = DefaultWidgetFactory.getInstance();
    }
    return componentFactory;
  }

  /**
   * Sets a new component factory.
   * 
   * @param newFactory the component factory to be set
   * 
   * @see #getComponentFactory()
   */
  public final void setComponentFactory(WidgetFactory newFactory) {
    componentFactory = newFactory;
  }

}
