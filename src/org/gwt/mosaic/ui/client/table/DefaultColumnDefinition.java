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

import com.google.gwt.user.client.ui.HasHorizontalAlignment;

/**
 * A definition of a column in a table.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 *
 * @param <RowType> the type of the row value
 * @param <ColType> the data type of the column
 */
public class DefaultColumnDefinition<RowType, ColType> extends
		AbstractColumnDefinition<RowType, ColType> implements HasHorizontalAlignment {
	
	/**
	 * Construct a new {@link DefaultColumnDefinition}.
	 * 
	 * @param header the name of the column.
	 */
	public DefaultColumnDefinition(String header) {
		setHeader(0, header);
	}

	@Override
	public void setCellValue(RowType rowValue, ColType cellValue) {
		// Ignore
	}

	@Override
	public ColType getCellValue(RowType rowValue) {
		// TODO Auto-generated method stub
		return null;
	}

	public HorizontalAlignmentConstant getHorizontalAlignment() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
		// TODO Auto-generated method stub
		
	}

}
