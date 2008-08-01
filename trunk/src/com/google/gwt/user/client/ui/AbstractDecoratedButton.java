package com.google.gwt.user.client.ui;

import com.google.gwt.user.client.Element;

public abstract class AbstractDecoratedButton extends FocusWidget {
  
  protected static class ButtonPanel extends DecoratorPanel {
    
    public ButtonPanel(String[] rowStyles) {
      super(rowStyles, 1);
    }

    @Override
    public Element getContainerElement() {
      return super.getContainerElement();
    }
  }
  
  protected AbstractDecoratedButton(Element elem) {
    super(elem);
  }
}
