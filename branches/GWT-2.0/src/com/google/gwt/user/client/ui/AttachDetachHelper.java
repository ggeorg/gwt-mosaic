package com.google.gwt.user.client.ui;

/**
 * Helper class, used by {@link LayoutPanel} to call attach/detach DecoratorPanel.
 * 
 * @author ggeorg
 *
 */
public class AttachDetachHelper {
  public static void onAttach(Widget w) {
    w.onAttach();
  }

  public static void onDetach(Widget w) {
    w.onDetach();
  }
}
