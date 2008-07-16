package org.mosaic.core.client.util;

import com.google.gwt.user.client.Timer;

public abstract class DelayedRunnable {

  private Timer timer = new Timer() {
    public void run() {
      DelayedRunnable.this.run();
    }
  };

  public DelayedRunnable() {
    this(333);
  }

  public DelayedRunnable(int delay) {
    timer.schedule(delay);
  }

  public abstract void run();

  public void cancel() {
    timer.cancel();
  }

}
