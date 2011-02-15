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
 * Class representing the system keyboard.
 */
public final class Keyboard {
    /**
     * Enumeration representing keyboard modifiers.
     */
    public enum Modifier {
        SHIFT,
        CTRL,
        ALT,
        META;

        public int getMask() {
            return 1 << ordinal();
        }
    }

    /**
     * Enumeration representing key locations.
     */
    public enum KeyLocation {
        STANDARD,
        LEFT,
        RIGHT,
        KEYPAD
    }

    /**
     * Represents a keystroke, a combination of a keycode and modifier flags.
     */
    public static final class KeyStroke {
        private int keyCode = 0;
        private int modifiers = 0x00;

        public static final String COMMAND_ABBREVIATION = "CMD";

        public KeyStroke(int keyCode, int modifiers) {
            this.keyCode = keyCode;
            this.modifiers = modifiers;
        }

        public int getKeyCode() {
            return keyCode;
        }

        public int getModifiers() {
            return modifiers;
        }

        @Override
        public boolean equals(Object object) {
            boolean equals = false;

            if (object instanceof KeyStroke) {
                KeyStroke keyStroke = (KeyStroke)object;
                equals = (this.keyCode == keyStroke.keyCode
                    && this.modifiers == keyStroke.modifiers);
            }

            return equals;
        }

        @Override
        public int hashCode() {
            // NOTE Key codes are currently defined as 16-bit values, so
            // shifting by 4 bits to append the modifiers should be OK.
            // However, if Sun changes the key code values in the future,
            // this may no longer be safe.
            int hashCode = keyCode << 4 | modifiers;
            return hashCode;
        }

        @Override
        public String toString() {
            throw new UnsupportedOperationException();
        }

        public static KeyStroke decode(String value) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Contains a set of key code constants that are common to all locales.
     */
    public static final class KeyCode {

    }

    private static int modifiers = 0;

    /**
     * Returns a bitfield representing the keyboard modifiers that are
     * currently pressed.
     */
    public static int getModifiers() {
        return modifiers;
    }

    protected static void setModifiers(int modifiers) {
        Keyboard.modifiers = modifiers;
    }

    /**
     * Tests the pressed state of a modifier.
     *
     * @param modifier
     *
     * @return
     * <tt>true</tt> if the modifier is pressed; <tt>false</tt>, otherwise.
     */
    public static boolean isPressed(Modifier modifier) {
        return (modifiers & modifier.getMask()) > 0;
    }

    /**
     * Returns the current drop action.
     *
     * @return
     * The drop action corresponding to the currently pressed modifier keys,
     * or <tt>null</tt> if no modifiers are pressed.
     */
    public static DropAction getDropAction() {
        // TODO Return an appropriate action for OS:
        // Windows: no modifier - move; control - copy; control-shift - link
        // Mac OS X: no modifier - move; option - copy; option-command - link

        DropAction dropAction = null;

        if (isPressed(Modifier.CTRL)
            && isPressed(Modifier.SHIFT)) {
            dropAction = DropAction.LINK;
        } else if (isPressed(Modifier.CTRL)) {
            dropAction = DropAction.COPY;
        } else {
            dropAction = DropAction.MOVE;
        }

        return dropAction;
    }
}

