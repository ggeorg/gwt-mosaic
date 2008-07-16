package org.mosaic.ui.client;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface CaptionImages extends ImageBundle {

  @Resource("window-button-close.png")
  AbstractImagePrototype windowClose();

  @Resource("window-button-maximize.png")
  AbstractImagePrototype windowMaximize();

  @Resource("window-button-maximized.png")
  AbstractImagePrototype windowMaximized();

  @Resource("window-button-minimize.png")
  AbstractImagePrototype windowMinimize();

}
