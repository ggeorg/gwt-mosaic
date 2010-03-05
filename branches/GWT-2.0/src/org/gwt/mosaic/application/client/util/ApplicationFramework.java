package org.gwt.mosaic.application.client.util;

import org.gwt.beansbinding.core.client.util.GWTBeansBinding;
import org.gwt.mosaic.actions.client.ActionMap;
import org.gwt.mosaic.actions.client.ButtonBindings;
import org.gwt.mosaic.actions.client.CheckBoxMenuItemBindings;
import org.gwt.mosaic.actions.client.ImageButtonBindings;
import org.gwt.mosaic.actions.client.MenuItemBindings;
import org.gwt.mosaic.actions.client.RadioButtonMenuItemBindings;
import org.gwt.mosaic.actions.client.ToolButtonBindings;
import org.gwt.mosaic.application.client.Application;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.MenuItem;

public class ApplicationFramework {

  static {
    GWTBeansBinding.init();
  }

  public void setupActions() {
    // Nothing to do here!
  };

  protected ApplicationFramework() {
    // Nothing to do here!
  }

  private static ActionMap actionMap;

  protected static ActionMap getActionMap() {
    if (actionMap == null) {
      actionMap = Application.getInstance().getContext().getActionMap();
    }
    return actionMap;
  }

  // ---

  public static ImageButton newImageButton(final String action) {
    final ImageButtonBindings bindings = new ImageButtonBindings(
        getActionMap().get(action));
    final ImageButton imageButton = (ImageButton) bindings.getTarget();
    bindings.bind();
    return imageButton;
  }

  // ---

  public static Button newButton(final String action) {
    return newButton(action, ButtonLabelType.TEXT_ON_RIGHT);
  }

  public static Button newButton(final String action,
      final ButtonLabelType labelType) {
    final ButtonBindings bindings = new ButtonBindings(getActionMap().get(
        action));
    final Button button = (Button) bindings.getTarget(); // FIXME
    bindings.setLabelType(labelType);
    bindings.bind();
    return button;
  }

  public static void bind(final String action, final Button button) {
    bind(action, button, ButtonLabelType.TEXT_ON_RIGHT);
  }

  public static void bind(final String action, final Button button,
      final ButtonLabelType labelType) {
    final ButtonBindings bindings = new ButtonBindings(getActionMap().get(
        action), button);
    bindings.setLabelType(labelType);
    bindings.bind();
  }

  // ---

  public static ToolButton newToolButton(final String action) {
    return newToolButton(action, ToolButtonStyle.PUSH,
        ButtonLabelType.TEXT_ON_RIGHT);
  }

  public static ToolButton newToolButton(final String action,
      final ToolButtonStyle style) {
    return newToolButton(action, style, ButtonLabelType.TEXT_ON_RIGHT);
  }

  public static ToolButton newToolButton(final String action,
      final ButtonLabelType labelType) {
    return newToolButton(action, ToolButtonStyle.PUSH, labelType);
  }

  public static ToolButton newToolButton(final String action,
      final ToolButtonStyle style, final ButtonLabelType labelType) {
    final ToolButtonBindings bindings = new ToolButtonBindings(
        getActionMap().get(action));
    final ToolButton toolButton = bindings.getTarget();
    toolButton.setStyle(style);
    bindings.setLabelType(labelType);
    bindings.bind();
    return toolButton;
  }

  public static void bind(final String action, final ToolButton toolButton) {
    bind(action, toolButton, ButtonLabelType.TEXT_ON_RIGHT);
  }

  public static void bind(final String action, final ToolButton toolButton,
      final ButtonLabelType labelType) {
    final ToolButtonBindings bindings = new ToolButtonBindings(
        getActionMap().get(action), toolButton);
    bindings.setLabelType(labelType);
    bindings.bind();
  }

  // ---

  public static MenuItem newMenuItem(final String action) {
    final MenuItemBindings bindings = new MenuItemBindings(getActionMap().get(
        action));
    final MenuItem menuItem = bindings.getTarget();
    bindings.bind();
    return menuItem;
  }

  public static void bind(final String action, final MenuItem menuItem) {
    final MenuItemBindings bindings = new MenuItemBindings(getActionMap().get(
        action), menuItem);
    bindings.bind();
  }

  public static MenuItem newCheckBoxMenuItem(final String action) {
    final CheckBoxMenuItemBindings bindings = new CheckBoxMenuItemBindings(
        getActionMap().get(action));
    final MenuItem menuItem = bindings.getTarget();
    bindings.bind();
    return menuItem;
  }

  public static void bindAsCheckBox(final String action, final MenuItem menuItem) {
    final CheckBoxMenuItemBindings bindings = new CheckBoxMenuItemBindings(
        getActionMap().get(action), menuItem);
    bindings.bind();
  }

  public static MenuItem newRadioButtonMenuItem(final String name,
      final String action) {
    final RadioButtonMenuItemBindings bindings = new RadioButtonMenuItemBindings(
        name, getActionMap().get(action));
    final MenuItem menuItem = bindings.getTarget();
    bindings.bind();
    return menuItem;
  }

  public static void bindAsRadioButton(final String name, final String action,
      final MenuItem menuItem) {
    final RadioButtonMenuItemBindings bindings = new RadioButtonMenuItemBindings(
        name, getActionMap().get(action), menuItem);
    bindings.bind();
  }
}
