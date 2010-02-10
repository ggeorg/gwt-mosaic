/*
 * Copyright 2009 Google Inc.
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
package org.gwt.mosaic.ui.client.tree;

/**
 * 
 * A widget that implements this interface contains {@link FastTreeItem}
 * children and can add and remove them.
 * 
 * Note: This interface is NOT intended to be implemented by any classes other
 * then FastTree and FastTreeItem, even for testing, as methods will be added to
 * it over time.
 */
interface HasFastTreeItems extends HasFastTreeItemsImpl {

}