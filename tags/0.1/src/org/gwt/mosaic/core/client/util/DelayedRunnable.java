/*
 * Copyright 2008 Georgios J. Georgopoulos.
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
package org.gwt.mosaic.core.client.util;

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