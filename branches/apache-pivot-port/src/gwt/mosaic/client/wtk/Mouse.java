/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gwt.mosaic.client.wtk;

/**
 * Class representing the system mouse.
 */
public final class Mouse {
    /**
     * Enumeration representing mouse buttons.
     */
    public enum Button {
        LEFT,
        RIGHT,
        MIDDLE;

        public int getMask() {
            return 1 << ordinal();
        }
    }

    /**
     * Enumeration defining supported scroll types.
     */
    public enum ScrollType {
        UNIT,
        BLOCK
    }

    private static int buttons = 0;
    private static Component capturer = null;

    /**
     * Returns a bitfield representing the mouse buttons that are currently
     * pressed.
     */
    public static int getButtons() {
        return buttons;
    }

    protected static void setButtons(int buttons) {
        Mouse.buttons = buttons;
    }

    /**
     * Tests the pressed state of a button.
     *
     * @param button
     *
     * @return
     * <tt>true</tt> if the button is pressed; <tt>false</tt>, otherwise.
     */
    public static boolean isPressed(Button button) {
        return (buttons & button.getMask()) > 0;
    }

    /**
     * Returns the number of mouse buttons.
     */
    public static int getButtonCount() {
        return 0;// MouseInfo.getNumberOfButtons();
    }

    /**
     * "Captures" the mouse, causing all mouse input to be delegated to the
     * given component rather than propagating down the component hierarchy.
     *
     * @param capturer
     * The component that wants to capture the mouse. The mouse pointer must
     * currently be over the component.
     */
    public static void capture(Component capturer) {
        if (capturer == null) {
            throw new IllegalArgumentException("capturer is null.");
        }

        if (!capturer.isMouseOver()) {
            throw new IllegalArgumentException("Mouse pointer is not currently over capturer.");
        }

        if (Mouse.capturer != null) {
            throw new IllegalStateException("Mouse is already captured.");
        }

        Mouse.capturer = capturer;
    }

    /**
     * Releases mouse capture, causing mouse input to resume propagation down
     * the component hierarchy.
     */
    public static void release() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the mouse capturer.
     *
     * @return
     * The component that has captured the mouse, or <tt>null</tt> if the mouse
     * is not currently captured.
     */
    public static Component getCapturer() {
        return capturer;
    }

    /**
     * Returns the current cursor.
     *
     * @throws IllegalStateException
     * If the mouse is not currently captured.
     */
    public static Cursor getCursor() {
    	throw new UnsupportedOperationException();
    }

    /**
     * Sets the cursor to an explicit value.
     *
     * @param cursor
     *
     * @throws IllegalStateException
     * If the mouse is not currently captured.
     */
    public static void setCursor(Cursor cursor) {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets the cursor based on a given component.
     *
     * @param component
     */
    public static void setCursor(Component component) {
        throw new UnsupportedOperationException();
    }
}
