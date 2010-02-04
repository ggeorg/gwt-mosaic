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

import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.gen2.commonevent.shared.HasBeforeCloseHandlers;
import com.google.gwt.gen2.commonevent.shared.HasBeforeOpenHandlers;

/**
 * Represents a convenience interface for all FastTreeItem events.
 * 
 * @deprecated as of feb 10th, this interface is probably going to be removed in
 *             the near future.
 */
@Deprecated
public interface HasFastTreeItemHandlers extends HasOpenHandlers<FastTreeItem>,
    HasBeforeOpenHandlers<FastTreeItem>, HasCloseHandlers<FastTreeItem>,
    HasBeforeCloseHandlers<FastTreeItem>, HasSelectionHandlers<FastTreeItem>,
    HasBeforeSelectionHandlers<FastTreeItem> {

}
