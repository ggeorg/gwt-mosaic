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
package org.mosaic.ui.client.tree;

import com.google.gwt.user.client.ui.Widget;

/**
 * Convenience class used to allow IDE users to quickly override the event
 * methods on {@link FastTreeItem}.
 */
public abstract class ListeningFastTreeItem extends FastTreeItem {

  /**
   * Creates an empty tree item.
   */
  public ListeningFastTreeItem() {
    super();
  }

  /**
   * Constructs a tree item with the given HTML.
   * 
   * @param html the item's HTML
   */
  public ListeningFastTreeItem(String html) {
    super(html);
  }

  /**
   * Constructs a tree item with the given <code>Widget</code>.
   * 
   * @param widget the item's widget
   */
  public ListeningFastTreeItem(Widget widget) {
    super(widget);
  }

  public abstract void beforeClose();

  public abstract void beforeOpen();
  
  protected abstract boolean beforeSelectionLost();

  protected abstract void ensureChildren();

  protected abstract void onSelected();
}
