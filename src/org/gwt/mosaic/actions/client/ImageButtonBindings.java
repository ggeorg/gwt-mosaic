package org.gwt.mosaic.actions.client;

import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.mosaic.actions.client.ButtonBindings.ButtonBean;
import org.gwt.mosaic.ui.client.ImageButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class ImageButtonBindings extends ActionBindings<ImageButton> implements
    ClickHandler {

  public final class ImageButtonBean extends TargetBean {
    public ImageButtonBean(ImageButton target) {
      super(target);
    }
    
    @Override
    public void setEnabled(Boolean enabled) {
      // Do nothing!
    }
    
    @Override
    public Boolean getEnabled() {
      return Boolean.TRUE;
    }
    
    @Override
    public void setImage(AbstractImagePrototype image) {
      super.setImage(image);
      image.applyTo(getTarget().getImage());
    }
  }

  private ImageButtonBean targetBean;

  public ImageButtonBindings(Action source) {
    this(source, new ImageButton());
  }

  public ImageButtonBindings(Action source, ImageButton target) {
    super(source, target);

    // Action.SHORT_DESCRIPTION
    addBinding(Action.SHORT_DESCRIPTION,
        BeanProperty.<Action, String> create(Action.SHORT_DESCRIPTION),
        BeanProperty.<TargetBean, String> create("title"));

    // Action.SMALL_ICON
    addBinding(Action.SMALL_ICON,
        BeanProperty.<Action, String> create(Action.SMALL_ICON),
        BeanProperty.<ButtonBean, String> create("image"));

    // Action.ACTION_COMMAND_KEY

    // "enabled"
    addBinding("enabled", BeanProperty.<Action, String> create("enabled"),
        BeanProperty.<TargetBean, String> create("enabled"));

    // "visible"
    addBinding("visible", BeanProperty.<Action, String> create("visible"),
        BeanProperty.<TargetBean, String> create("visible"));

  }

  @Override
  protected ImageButtonBean getTargetBean() {
    if (targetBean == null) {
      targetBean = new ImageButtonBean(getTarget());
    }
    return targetBean;
  }

  private HandlerRegistration handlerReg = null;

  @Override
  protected void onBind() {
    handlerReg = getTarget().addClickHandler(this);
  }

  @Override
  protected void onUnBind() {
    if (handlerReg != null) {
      handlerReg.removeHandler();
    }
  }

  public void onClick(ClickEvent event) {
    getSource().actionPerformed(new ActionEvent(getSource(), event.getSource()));
  }

}
