package org.mosaic.ui.client;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface MessageBoxImages extends ImageBundle {

  @Resource("dialog-error.png")
  AbstractImagePrototype dialogError();
  
  @Resource("dialog-information.png")
  AbstractImagePrototype dialogInformation();
  
  @Resource("dialog-password.png")
  AbstractImagePrototype dialogPassword();
  
  @Resource("dialog-question.png")
  AbstractImagePrototype dialogQuestion();
  
  @Resource("dialog-warning.png")
  AbstractImagePrototype dialogWarning();

}
