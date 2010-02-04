/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos
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
/*
 * This is derived work from GWT Incubator project:
 * http://code.google.com/p/google-web-toolkit-incubator/
 * 
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
package org.gwt.mosaic.ui.client.table;


/**
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * @deprecated use ScrollTable instead
 */
@Deprecated
public class ScrollTable2 extends ScrollTable {

  public static class DataGrid extends FixedWidthGrid {

    public DataGrid() {
      // Nothing to do here!
    }

  }

  /**
   * Construct a new {@link ScrollTable2}.
   * 
   * @param dataTable the data table
   * @param headerTable the header table
   */
  public ScrollTable2(FixedWidthGrid dataTable, FixedWidthFlexTable headerTable) {
    super(dataTable, headerTable);
  }

  /**
   * Construct a new {@link ScrollTable2}.
   * 
   * @param dataTable the data table
   * @param headerTable the header table
   * @param resources the images to use in the table
   */
  public ScrollTable2(FixedWidthGrid dataTable,
      FixedWidthFlexTable headerTable, ScrollTableResources resources) {
    super(dataTable, headerTable, resources);
  }

}
