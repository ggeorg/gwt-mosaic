/*
 * Copyright 2006-2008 Google Inc.
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
package org.gwt.mosaic.ui.client.table;

/**
 * A cell editor that lists its options as radio buttons.
 * 
 * @param <R> the type of the row value associated with the editor
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class RadioCellEditor<R> extends
    com.google.gwt.widgetideas.table.client.RadioCellEditor<R> implements
    TableCellEditor<R> {

  public RadioCellEditor() {
    super();
  }
  
  public RadioCellEditor(InlineCellEditorImages images) {
    super(images);
  }
  
}
