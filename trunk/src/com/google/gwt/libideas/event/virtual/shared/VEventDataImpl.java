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
package com.google.gwt.libideas.event.virtual.shared;

import com.google.gwt.dom.client.Element;

/**
 * Class used to store mock or synthetic information normally stored in a DOM
 * event object.
 */
public class VEventDataImpl
    implements HasKeyboardData, HasMouseData, IsSuppressable {

  private boolean altKey;

  private int button;

  private int clientX;

  private int keyCode;

  private int clientY;

  private boolean metaKey;

  private int mouseWheelVelocityY;

  private boolean repeat;

  private int screenX;

  private int screenY;

  private boolean shiftKey;

  private boolean suppressed = false;

  public boolean getAltKey() {
    return altKey;
  }

  public int getButton() {
    return button;
  }

  public int getClientX() {
    return clientX;
  }

  public int getClientY() {
    return clientY;
  }

  public boolean getCtrlKey() {
    return false;
  }

  public Element getCurrentTarget() {
    return null;
  }

  public Element getFromElement() {
    return null;
  }

  public int getKeyCode() {
    return keyCode;
  }

  public boolean getMetaKey() {
    return metaKey;
  }

  public int getMouseWheelVelocityY() {
    return mouseWheelVelocityY;
  }

  public boolean getRepeat() {
    return repeat;
  }

  public int getScreenX() {
    return screenX;
  }

  public int getScreenY() {
    return screenY;
  }

  public boolean getShiftKey() {
    return shiftKey;
  }

  public String getString() {
    return null;
  }

  public Element getTarget() {
    return null;
  }

  public Element getToElement() {
    return null;
  }

  public String getType() {
    return null;
  }

  public int getTypeInt() {
    return 0;
  }

  public boolean isSuppressed() {
    return suppressed;
  }

  public void preventDefault() {
  }

  public void setAltKey(boolean altKey) {
    this.altKey = altKey;
  }

  public void setButton(int button) {
    this.button = button;
  }

  public void setClientX(int clientX) {
    this.clientX = clientX;
  }

  public void setClientY(int clientY) {
    this.clientY = clientY;
  }

  public void setKeyCode(int keyCode) {
    this.keyCode = keyCode;
  }

  public void setMetaKey(boolean metaKey) {
    this.metaKey = metaKey;
  }

  public void setMouseWheelVelocityY(int mouseWheelVelocityY) {
    this.mouseWheelVelocityY = mouseWheelVelocityY;
  }

  public void setRepeat(boolean repeat) {
    this.repeat = repeat;
  }

  public void setScreenX(int screenX) {
    this.screenX = screenX;
  }

  public void setScreenY(int screenY) {
    this.screenY = screenY;
  }

  public void setShiftKey(boolean shiftKey) {
    this.shiftKey = shiftKey;
  }

  public void stopPropagation() {
    suppressed = true;
  }
}
