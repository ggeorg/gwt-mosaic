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
 * Button group listener interface.
 */
public interface ButtonGroupListener {
    /**
     * Button group listener adapter.
     */
    public class Adapter implements ButtonGroupListener {
        @Override
        public void buttonAdded(ButtonGroup buttonGroup, Button button) {
        }

        @Override
        public void buttonRemoved(ButtonGroup buttonGroup, Button button) {
        }

        @Override
        public void selectionChanged(ButtonGroup buttonGroup, Button previousSelection) {
        }
    }

    /**
     * Called when a button has been added to a button group.
     *
     * @param buttonGroup
     * @param button
     */
    public void buttonAdded(ButtonGroup buttonGroup, Button button);

    /**
     * Called when a button has been removed from a button group.
     *
     * @param buttonGroup
     * @param button
     */
    public void buttonRemoved(ButtonGroup buttonGroup, Button button);

    /**
     * Called when a button group's selection has changed.
     *
     * @param buttonGroup
     * @param previousSelection
     */
    public void selectionChanged(ButtonGroup buttonGroup, Button previousSelection);
}
