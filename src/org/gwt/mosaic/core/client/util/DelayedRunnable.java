/*
 * Copyright 2008 Google Inc.
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

import org.gwt.mosaic.core.client.CoreConstants;

import com.google.gwt.user.client.Timer;

/**
 * A helper class for delayed execution based on
 * {@code com.google.gwt.user.client.Timer}.
 * 
 * <p>
 * Example:
 * <pre>
 * new DelayedRunnable() {
 *   &#64;Override
 *   public void run() {
 *    maximize(WindowState.NORMAL);
 *   }
 * };
 * </pre>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public abstract class DelayedRunnable implements CoreConstants {

  private Timer timer = new Timer() {
    public void run() {
      DelayedRunnable.this.run();
    }
  };

  /**
   * Creates a {@code com.google.gwt.user.client.Timer} and calls
   * {@code com.google.gwt.user.client.Timer#schedule(int)} with the default
   * {@link CoreConstants#DEFAULT_DELAY_MILLIS} value.
   */
  public DelayedRunnable() {
    this(DEFAULT_DELAY_MILLIS);
  }

  /**
   * Creates a {@code com.google.gwt.user.client.Timer} and calls
   * {@code com.google.gwt.user.client.Timer#schedule(int)} with a given value.
   * 
   * @param delayMillis how long to wait before the timer elapses, in
   *          milliseconds
   */
  public DelayedRunnable(int delayMillis) {
    timer.schedule(delayMillis);
  }

  /**
   * This method will be called when the timer fires. Override it to implement
   * the timer's logic.
   */
  public abstract void run();

  /**
   * Cancels the timer.
   */
  public void cancel() {
    timer.cancel();
  }

}
