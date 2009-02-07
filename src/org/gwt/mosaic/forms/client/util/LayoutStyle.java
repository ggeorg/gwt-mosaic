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
package org.gwt.mosaic.forms.client.util;

import org.gwt.mosaic.forms.client.layout.ConstantSize;
import org.gwt.mosaic.forms.client.layout.Size;

/**
 * An abstract class that describes a layout and design style guide. It provides
 * constants used to lay out panels consistently.
 * <p>
 * This class is work in progress and the API may change without notice.
 * Therefore it is recommended to not write custom subclasses for production
 * code. A future version of this class will likely collaborate with a class
 * <code>LogicalSize</code> or <code>StyledSize</code>.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see com.jgoodies.forms.util.MacLayoutStyle
 * @see com.GWTMosaicLayoutStyle.forms.util.WindowsLayoutStyle
 * @see com.jgoodies.forms.factories.FormFactory
 * @see com.jgoodies.forms.factories.Borders
 */
public abstract class LayoutStyle {

  /**
   * Holds the current layout style.
   */
  private static LayoutStyle current = initialLayoutStyle();
  
  private static LayoutStyle initialLayoutStyle() {
    return GWTMosaicLayoutStyle.INSTANCE;
}


  // Accessing the current style ******************************************

  /**
   * Returns the current <code>LayoutStyle</code>.
   * 
   * @return the current <code>LayoutStyle</code>
   */
  public static LayoutStyle getCurrent() {
    return current;
  }

  // Layout Sizes *********************************************************

  /**
   * Returns this style's default button width.
   * 
   * @return the default button width
   * 
   * @see #getDefaultButtonHeight()
   */
  public abstract Size getDefaultButtonWidth();

  /**
   * Returns this style's default button height.
   * 
   * @return the default button height
   * 
   * @see #getDefaultButtonWidth()
   */
  public abstract Size getDefaultButtonHeight();

  /**
   * Returns this style's horizontal margin for general dialogs.
   * 
   * @return the horizontal margin for general dialogs
   * 
   * @see #getDialogMarginY()
   * @see #getTabbedDialogMarginX()
   */
  public abstract ConstantSize getDialogMarginX();

  /**
   * Returns this style's vertical margin for general dialogs.
   * 
   * @return the vertical margin for general dialogs
   * 
   * @see #getDialogMarginX()
   * @see #getTabbedDialogMarginY()
   */
  public abstract ConstantSize getDialogMarginY();

  /**
   * Returns this style's horizontal margin for dialogs that consist of a tabbed
   * pane.
   * 
   * @return the horizontal margin for dialogs that consist of a tabbed pane
   * 
   * @see #getTabbedDialogMarginY()
   * @see #getDialogMarginX()
   */
  public abstract ConstantSize getTabbedDialogMarginX();

  /**
   * Returns this style's vertical margin for dialogs that consist of a tabbed
   * pane.
   * 
   * @return the vertical margin for dialogs that consist of a tabbed pane
   * 
   * @see #getTabbedDialogMarginX()
   * @see #getDialogMarginY()
   */
  public abstract ConstantSize getTabbedDialogMarginY();

  /**
   * Returns a gap used to separate a label and associated control.
   * 
   * @return a gap between label and associated control
   * 
   * @see #getRelatedComponentsPadX()
   * @see #getUnrelatedComponentsPadX()
   */
  public abstract ConstantSize getLabelComponentPadX();

  /**
   * Returns a horizontal gap used to separate related controls.
   * 
   * @return a horizontal gap between related controls
   * 
   * @see #getLabelComponentPadX()
   * @see #getRelatedComponentsPadY()
   * @see #getUnrelatedComponentsPadX()
   */
  public abstract ConstantSize getRelatedComponentsPadX();

  /**
   * Returns a vertical gap used to separate related controls.
   * 
   * @return a vertical gap between related controls
   * 
   * @see #getRelatedComponentsPadX()
   * @see #getUnrelatedComponentsPadY()
   */
  public abstract ConstantSize getRelatedComponentsPadY();

  /**
   * Returns a horizontal gap used to separate unrelated controls.
   * 
   * @return a horizontal gap between unrelated controls
   * 
   * @see #getLabelComponentPadX()
   * @see #getUnrelatedComponentsPadY()
   * @see #getRelatedComponentsPadX()
   */
  public abstract ConstantSize getUnrelatedComponentsPadX();

  /**
   * Returns a vertical gap used to separate unrelated controls.
   * 
   * @return a vertical gap between unrelated controls
   * 
   * @see #getUnrelatedComponentsPadX()
   * @see #getRelatedComponentsPadY()
   */
  public abstract ConstantSize getUnrelatedComponentsPadY();

  /**
   * Returns a narrow vertical pad used to separate lines.
   * 
   * @return a narrow vertical pad used to separate lines
   * 
   * @see #getLinePad()
   * @see #getParagraphPad()
   */
  public abstract ConstantSize getNarrowLinePad();

  /**
   * Returns a narrow vertical pad used to separate lines.
   * 
   * @return a vertical pad used to separate lines
   * 
   * @see #getNarrowLinePad()
   * @see #getParagraphPad()
   */
  public abstract ConstantSize getLinePad();

  /**
   * Returns a pad used to separate paragraphs.
   * 
   * @return a vertical pad used to separate paragraphs
   * 
   * @see #getNarrowLinePad()
   * @see #getLinePad()
   */
  public abstract ConstantSize getParagraphPad();

  /**
   * Returns a pad used to separate a button bar from a component.
   * 
   * @return a vertical pad used to separate paragraphs
   * 
   * @see #getRelatedComponentsPadY()
   * @see #getUnrelatedComponentsPadY()
   */
  public abstract ConstantSize getButtonBarPad();

  /**
   * Checks and answers whether buttons are typically ordered from left to right
   * or from right to left. Useful for building button bars that shall comply
   * with the platform's layout style guide.
   * <p>
   * 
   * For example the Windows style guide recommends to layout out
   * <em>OK, Cancel, Apply</em> from left to right, where the Mac Aqua style
   * guide recommends to layout out these buttons from right to left.
   * <p>
   * 
   * Although most button sequences shall honor this order some buttons require
   * a left to right order. For example <em>Back, Next</em> or
   * <em>Move Left, Move Right</em>.
   * <p>
   * 
   * @return true if buttons are typically ordered from left to right
   * 
   * @see com.jgoodies.forms.builder.ButtonBarBuilder
   * @see com.jgoodies.forms.factories.ButtonBarFactory
   */
  public abstract boolean isLeftToRightButtonOrder();

}
