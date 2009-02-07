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
package org.gwt.mosaic.forms.client.factories;

import org.gwt.mosaic.forms.client.builder.ButtonBarBuilder;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Button;

/**
 * A factory class that consists only of static methods to build frequently used
 * button bars. Utilizes the {@link ButtonBarBuilder} that in turn uses the
 * {@link FormLayout} to lay out the bars.
 * <p>
 * The button bars returned by this builder comply with popular UI style guides.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see ButtonBarBuilder
 * @see LayoutStyle
 */
public final class ButtonBarFactory {

  private ButtonBarFactory() {
    // Suppresses default constructor, ensuring non-instantiability.
  }

  // General Purpose Factory Methods: Left Aligned ************************

  /**
   * Builds and returns a left aligned bar with one button.
   * 
   * @param button1 the first button to add
   * @return a button bar with the given button
   */
  public static LayoutPanel buildLeftAlignedBar(Button button1) {
    return buildLeftAlignedBar(new Button[] {button1});
  }

  /**
   * Builds and returns a left aligned bar with two buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildLeftAlignedBar(Button button1, Button button2) {
    return buildLeftAlignedBar(new Button[] {button1, button2}, true);
  }

  /**
   * Builds and returns a left aligned bar with three buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildLeftAlignedBar(Button button1, Button button2,
      Button button3) {
    return buildLeftAlignedBar(new Button[] {button1, button2, button3}, true);
  }

  /**
   * Builds and returns a left aligned bar with four buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @param button4 the fourth button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildLeftAlignedBar(Button button1, Button button2,
      Button button3, Button button4) {
    return buildLeftAlignedBar(new Button[] {
        button1, button2, button3, button4}, true);
  }

  /**
   * Builds and returns a left aligned bar with five buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @param button4 the fourth button to add
   * @param button5 the fifth button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildLeftAlignedBar(Button button1, Button button2,
      Button button3, Button button4, Button button5) {
    return buildLeftAlignedBar(new Button[] {
        button1, button2, button3, button4, button5}, true);
  }

  /**
   * Builds and returns a left aligned button bar with the given buttons.
   * 
   * @param buttons an array of buttons to add
   * @return a left aligned button bar with the given buttons
   */
  public static LayoutPanel buildLeftAlignedBar(Button[] buttons) {
    ButtonBarBuilder builder = new ButtonBarBuilder();
    builder.addGriddedButtons(buttons);
    builder.addGlue();
    return builder.getPanel();
  }

  /**
   * Builds and returns a left aligned button bar with the given buttons.
   * 
   * @param buttons an array of buttons to add
   * @param leftToRightButtonOrder the order in which the buttons to add
   * @return a left aligned button bar with the given buttons
   */
  public static LayoutPanel buildLeftAlignedBar(Button[] buttons,
      boolean leftToRightButtonOrder) {
    ButtonBarBuilder builder = new ButtonBarBuilder();
    builder.setLeftToRightButtonOrder(leftToRightButtonOrder);
    builder.addGriddedButtons(buttons);
    builder.addGlue();
    return builder.getPanel();
  }

  // General Purpose Factory Methods: Centered ****************************

  /**
   * Builds and returns a centered bar with one button.
   * 
   * @param button1 the first button to add
   * @return a button bar with the given button
   */
  public static LayoutPanel buildCenteredBar(Button button1) {
    return buildCenteredBar(new Button[] {button1});
  }

  /**
   * Builds and returns a centered bar with two buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildCenteredBar(Button button1, Button button2) {
    return buildCenteredBar(new Button[] {button1, button2});
  }

  /**
   * Builds and returns a centered bar with three buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildCenteredBar(Button button1, Button button2,
      Button button3) {
    return buildCenteredBar(new Button[] {button1, button2, button3});
  }

  /**
   * Builds and returns a centered bar with four buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @param button4 the fourth button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildCenteredBar(Button button1, Button button2,
      Button button3, Button button4) {
    return buildCenteredBar(new Button[] {button1, button2, button3, button4});
  }

  /**
   * Builds and returns a centered bar with five buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @param button4 the fourth button to add
   * @param button5 the fifth button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildCenteredBar(Button button1, Button button2,
      Button button3, Button button4, Button button5) {
    return buildCenteredBar(new Button[] {
        button1, button2, button3, button4, button5});
  }

  /**
   * Builds and returns a centered button bar with the given buttons.
   * 
   * @param buttons an array of buttons to add
   * @return a centered button bar with the given buttons
   */
  public static LayoutPanel buildCenteredBar(Button[] buttons) {
    ButtonBarBuilder builder = new ButtonBarBuilder();
    builder.addGlue();
    builder.addGriddedButtons(buttons);
    builder.addGlue();
    return builder.getPanel();
  }

  /**
   * Builds and returns a filled bar with one button.
   * 
   * @param button1 the first button to add
   * @return a button bar with the given button
   */
  public static LayoutPanel buildGrowingBar(Button button1) {
    return buildGrowingBar(new Button[] {button1});
  }

  /**
   * Builds and returns a filled button bar with two buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildGrowingBar(Button button1, Button button2) {
    return buildGrowingBar(new Button[] {button1, button2});
  }

  /**
   * Builds and returns a filled bar with three buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildGrowingBar(Button button1, Button button2,
      Button button3) {
    return buildGrowingBar(new Button[] {button1, button2, button3});
  }

  /**
   * Builds and returns a filled bar with four buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @param button4 the fourth button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildGrowingBar(Button button1, Button button2,
      Button button3, Button button4) {
    return buildGrowingBar(new Button[] {button1, button2, button3, button4});
  }

  /**
   * Builds and returns a filled bar with five buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @param button4 the fourth button to add
   * @param button5 the fifth button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildGrowingBar(Button button1, Button button2,
      Button button3, Button button4, Button button5) {
    return buildGrowingBar(new Button[] {
        button1, button2, button3, button4, button5});
  }

  /**
   * Builds and returns a button bar with the given buttons. All button columns
   * will grow with the bar.
   * 
   * @param buttons an array of buttons to add
   * @return a filled button bar with the given buttons
   */
  public static LayoutPanel buildGrowingBar(Button[] buttons) {
    ButtonBarBuilder builder = new ButtonBarBuilder();
    builder.addGriddedGrowingButtons(buttons);
    return builder.getPanel();
  }

  // General Purpose Factory Methods: Right Aligned ***********************

  /**
   * Builds and returns a right aligned bar with one button.
   * 
   * @param button1 the first button to add
   * @return a button bar with the given button
   */
  public static LayoutPanel buildRightAlignedBar(Button button1) {
    return buildRightAlignedBar(new Button[] {button1});
  }

  /**
   * Builds and returns a right aligned bar with two buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildRightAlignedBar(Button button1, Button button2) {
    return buildRightAlignedBar(new Button[] {button1, button2}, true);
  }

  /**
   * Builds and returns a right aligned bar with three buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildRightAlignedBar(Button button1, Button button2,
      Button button3) {
    return buildRightAlignedBar(new Button[] {button1, button2, button3}, true);
  }

  /**
   * Builds and returns a right aligned bar with four buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @param button4 the fourth button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildRightAlignedBar(Button button1, Button button2,
      Button button3, Button button4) {
    return buildRightAlignedBar(new Button[] {
        button1, button2, button3, button4}, true);
  }

  /**
   * Builds and returns a right aligned bar with five buttons.
   * 
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @param button4 the fourth button to add
   * @param button5 the fifth button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildRightAlignedBar(Button button1, Button button2,
      Button button3, Button button4, Button button5) {
    return buildRightAlignedBar(new Button[] {
        button1, button2, button3, button4, button5}, true);
  }

  /**
   * Builds and returns a right aligned button bar with the given buttons.
   * 
   * @param buttons an array of buttons to add
   * @return a right aligned button bar with the given buttons
   */
  public static LayoutPanel buildRightAlignedBar(Button[] buttons) {
    ButtonBarBuilder builder = new ButtonBarBuilder();
    builder.addGlue();
    builder.addGriddedButtons(buttons);
    return builder.getPanel();
  }

  /**
   * Builds and returns a right aligned button bar with the given buttons.
   * 
   * @param buttons an array of buttons to add
   * @param leftToRightButtonOrder the order in which the buttons to add
   * @return a right aligned button bar with the given buttons
   */
  public static LayoutPanel buildRightAlignedBar(Button[] buttons,
      boolean leftToRightButtonOrder) {
    ButtonBarBuilder builder = new ButtonBarBuilder();
    builder.setLeftToRightButtonOrder(leftToRightButtonOrder);
    builder.addGlue();
    builder.addGriddedButtons(buttons);
    return builder.getPanel();
  }

  // Right Aligned Button Bars with Help in the Left **********************

  /**
   * Builds and returns a right aligned bar with help and one button.
   * 
   * @param help the help button to add on the left side
   * @param button1 the first button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildHelpBar(Button help, Button button1) {
    return buildHelpBar(help, new Button[] {button1});
  }

  /**
   * Builds and returns a right aligned bar with help and two buttons.
   * 
   * @param help the help button to add on the left side
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildHelpBar(Button help, Button button1,
      Button button2) {
    return buildHelpBar(help, new Button[] {button1, button2});
  }

  /**
   * Builds and returns a right aligned bar with help and three buttons.
   * 
   * @param help the help button to add on the left side
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildHelpBar(Button help, Button button1,
      Button button2, Button button3) {
    return buildHelpBar(help, new Button[] {button1, button2, button3});
  }

  /**
   * Builds and returns a right aligned bar with help and four buttons.
   * 
   * @param help the help button to add on the left side
   * @param button1 the first button to add
   * @param button2 the second button to add
   * @param button3 the third button to add
   * @param button4 the fourth button to add
   * @return a button bar with the given buttons
   */
  public static LayoutPanel buildHelpBar(Button help, Button button1,
      Button button2, Button button3, Button button4) {
    return buildHelpBar(help,
        new Button[] {button1, button2, button3, button4});
  }

  /**
   * Builds and returns a right aligned bar with help and other buttons.
   * 
   * @param help the help button to add on the left side
   * @param buttons an array of buttons to add
   * @return a right aligned button bar with the given buttons
   */
  public static LayoutPanel buildHelpBar(Button help, Button[] buttons) {
    ButtonBarBuilder builder = new ButtonBarBuilder();
    builder.addGridded(help);
    builder.addRelatedGap();
    builder.addGlue();
    builder.addGriddedButtons(buttons);
    return builder.getPanel();
  }

  // Popular Dialog Button Bars: No Help **********************************

  /**
   * Builds and returns a button bar with Close.
   * 
   * @param close the Close button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildCloseBar(Button close) {
    return buildRightAlignedBar(close);
  }

  /**
   * Builds and returns a button bar with OK.
   * 
   * @param ok the OK button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildOKBar(Button ok) {
    return buildRightAlignedBar(ok);
  }

  /**
   * Builds and returns a button bar with OK and Cancel.
   * 
   * @param ok the OK button
   * @param cancel the Cancel button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildOKCancelBar(Button ok, Button cancel) {
    return buildRightAlignedBar(new Button[] {ok, cancel});
  }

  /**
   * Builds and returns a button bar with OK, Cancel and Apply.
   * 
   * @param ok the OK button
   * @param cancel the Cancel button
   * @param apply the Apply button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildOKCancelApplyBar(Button ok, Button cancel,
      Button apply) {
    return buildRightAlignedBar(new Button[] {ok, cancel, apply});
  }

  // Popular Dialog Button Bars: Help in the Left *************************

  /**
   * Builds and returns a button bar with Help and Close.
   * 
   * @param help the Help button
   * @param close the Close button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildHelpCloseBar(Button help, Button close) {
    return buildHelpBar(help, close);
  }

  /**
   * Builds and returns a button bar with Help and OK.
   * 
   * @param help the Help button
   * @param ok the OK button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildHelpOKBar(Button help, Button ok) {
    return buildHelpBar(help, ok);
  }

  /**
   * Builds and returns a button bar with Help, OK and Cancel.
   * 
   * @param help the Help button
   * @param ok the OK button
   * @param cancel the Cancel button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildHelpOKCancelBar(Button help, Button ok,
      Button cancel) {
    return buildHelpBar(help, ok, cancel);
  }

  /**
   * Builds and returns a button bar with Help, OK, Cancel and Apply.
   * 
   * @param help the Help button
   * @param ok the OK button
   * @param cancel the Cancel button
   * @param apply the Apply button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildHelpOKCancelApplyBar(Button help, Button ok,
      Button cancel, Button apply) {
    return buildHelpBar(help, ok, cancel, apply);
  }

  // Popular Dialog Button Bars: Help in the Right Hand Side **************

  /**
   * Builds and returns a button bar with Close and Help.
   * 
   * @param close the Close button
   * @param help the Help button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildCloseHelpBar(Button close, Button help) {
    return buildRightAlignedBar(new Button[] {close, help});
  }

  /**
   * Builds and returns a button bar with OK and Help.
   * 
   * @param ok the OK button
   * @param help the Help button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildOKHelpBar(Button ok, Button help) {
    return buildRightAlignedBar(new Button[] {ok, help});
  }

  /**
   * Builds and returns a button bar with OK, Cancel, and Help.
   * 
   * @param ok the OK button
   * @param cancel the Cancel button
   * @param help the Help button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildOKCancelHelpBar(Button ok, Button cancel,
      Button help) {
    return buildRightAlignedBar(new Button[] {ok, cancel, help});
  }

  /**
   * Builds and returns a button bar with OK, Cancel, Apply and Help.
   * 
   * @param ok the OK button
   * @param cancel the Cancel button
   * @param apply the Apply button
   * @param help the Help button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildOKCancelApplyHelpBar(Button ok, Button cancel,
      Button apply, Button help) {
    return buildRightAlignedBar(new Button[] {ok, cancel, apply, help});
  }

  // Add..., Remove *******************************************************

  /**
   * Builds and returns a left aligned button bar with Add and Remove.
   * 
   * @param add the Add button
   * @param remove the Remove button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildAddRemoveLeftBar(Button add, Button remove) {
    return buildLeftAlignedBar(add, remove);
  }

  /**
   * Builds and returns a filled button bar with Add and Remove.
   * 
   * @param add the Add button
   * @param remove the Remove button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildAddRemoveBar(Button add, Button remove) {
    return buildGrowingBar(add, remove);
  }

  /**
   * Builds and returns a right aligned button bar with Add and Remove.
   * 
   * @param add the Add button
   * @param remove the Remove button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildAddRemoveRightBar(Button add, Button remove) {
    return buildRightAlignedBar(add, remove);
  }

  // Add..., Remove, Properties... ****************************************

  /**
   * Builds and returns a left aligned button bar with Add, Remove, and
   * Properties.
   * 
   * @param add the Add button
   * @param remove the Remove button
   * @param properties the Properties button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildAddRemovePropertiesLeftBar(Button add,
      Button remove, Button properties) {
    return buildLeftAlignedBar(add, remove, properties);
  }

  /**
   * Builds and returns a filled button bar with Add, Remove, and Properties.
   * 
   * @param add the Add button
   * @param remove the Remove button
   * @param properties the Properties button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildAddRemovePropertiesBar(Button add, Button remove,
      Button properties) {
    ButtonBarBuilder builder = new ButtonBarBuilder();
    builder.addGriddedGrowing(add);
    builder.addRelatedGap();
    builder.addGriddedGrowing(remove);
    builder.addRelatedGap();
    builder.addGriddedGrowing(properties);
    return builder.getPanel();
  }

  /**
   * Builds and returns a right aligned button bar with Add, Remove, and
   * Properties.
   * 
   * @param add the Add button
   * @param remove the Remove button
   * @param properties the Properties button
   * @return a panel that contains the button(s)
   */
  public static LayoutPanel buildAddRemovePropertiesRightBar(Button add,
      Button remove, Button properties) {
    return buildRightAlignedBar(add, remove, properties);
  }

  // Wizard Bars **********************************************************

  /**
   * Builds and returns a wizard button bar with: Back, Next, Finish, Cancel.
   * 
   * @param back the Back button
   * @param next the Next button
   * @param finish the Finish button
   * @param cancel the Cancel button
   * @return a wizard button bar for back, next, finish, cancel
   */
  public static LayoutPanel buildWizardBar(Button back, Button next,
      Button finish, Button cancel) {
    return buildWizardBar(back, next, new Button[] {finish, cancel});
  }

  /**
   * Builds and returns a wizard button bar with: Help and Back, Next, Finish,
   * Cancel.
   * 
   * @param help the Help button
   * @param back the Back button
   * @param next the Next button
   * @param finish the Finish button
   * @param cancel the Cancel button
   * @return a wizard button bar for help, back, next, finish, cancel
   */
  public static LayoutPanel buildWizardBar(Button help, Button back, Button next,
      Button finish, Button cancel) {
    return buildWizardBar(new Button[] {help}, back, next, new Button[] {
        finish, cancel});
  }

  /**
   * Builds and returns a wizard button bar that consists of the back and next
   * buttons, and some right aligned buttons.
   * 
   * @param back the mandatory back button
   * @param next the mandatory next button
   * @param rightAlignedButtons an optional array of buttons that will be
   *          located in the bar's right hand side
   * @return a wizard button bar with back, next and a bunch of buttons
   */
  public static LayoutPanel buildWizardBar(Button back, Button next,
      Button[] rightAlignedButtons) {
    return buildWizardBar(null, back, next, rightAlignedButtons);
  }

  /**
   * Builds and returns a wizard button bar. It consists of some left aligned
   * buttons, the back and next buttons, and some right aligned buttons.
   * 
   * @param leftAlignedButtons an optional array of buttons that will be
   *          positioned in the bar's left hand side
   * @param back the mandatory back button
   * @param next the mandatory next button
   * @param rightAlignedButtons an optional array of buttons that will be
   *          located in the bar's right hand side
   * @return a wizard button bar with back, next and a bunch of buttons
   */
  public static LayoutPanel buildWizardBar(Button[] leftAlignedButtons,
      Button back, Button next, Button[] rightAlignedButtons) {
    return buildWizardBar(leftAlignedButtons, back, next, null,
        rightAlignedButtons);
  }

  /**
   * Builds and returns a wizard button bar. It consists of some left aligned
   * buttons, the back, next group, and some right aligned buttons. To allow the
   * finish button to overlay the next button, you can optionally provide the
   * <code>overlayedFinish</code> parameter.
   * 
   * @param leftAlignedButtons an optional array of buttons that will be
   *          positioned in the bar's left hand side
   * @param back the mandatory back button
   * @param next the mandatory next button
   * @param overlaidFinish the optional overlaid finish button
   * @param rightAlignedButtons an optional array of buttons that will be
   *          located in the bar's right hand side
   * @return a wizard button bar with back, next and a bunch of buttons
   */
  public static LayoutPanel buildWizardBar(Button[] leftAlignedButtons,
      Button back, Button next, Button overlaidFinish,
      Button[] rightAlignedButtons) {

    ButtonBarBuilder builder = new ButtonBarBuilder();
    if (leftAlignedButtons != null) {
      builder.addGriddedButtons(leftAlignedButtons);
      builder.addRelatedGap();
    }
    builder.addGlue();
    builder.addGridded(back);
    builder.addGridded(next);

    // Optionally overlay the finish and next button.
    if (overlaidFinish != null) {
      builder.nextColumn(-1);
      builder.add(overlaidFinish);
      builder.nextColumn();
    }

    if (rightAlignedButtons != null) {
      builder.addRelatedGap();
      builder.addGriddedButtons(rightAlignedButtons);
    }
    return builder.getPanel();
  }

}
