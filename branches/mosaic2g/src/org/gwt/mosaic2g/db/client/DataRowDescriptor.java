/*
 * Copyright 2010 ArkaSoft LLC.
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
package org.gwt.mosaic2g.db.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

/**
 * 
 * @param <T>
 * 
 * @author ggeorg
 */
public class DataRowDescriptor<T> implements ValueChangeHandler<Integer> {

	private final DataSet<T> dataSet;

	private final List<Column<?>> columns = new ArrayList<Column<?>>();

	public DataRowDescriptor(DataSet<T> dataSet) {
		this.dataSet = dataSet;
		this.dataSet.getRow().addValueChangeHandler(this);
	}

	public DataSet<T> getDataSet() {
		return dataSet;
	}

	public DataRowDescriptor<T> addColumn(Column<?> c) {
		assert !dataSet.getOpen().$();
		if (c != null) {
			columns.indexOf(c);
			columns.add(c);
		}
		return this;
	}

	public boolean removeColumn(Column<?> c) {
		assert !dataSet.getOpen().$();
		return columns.remove(c);
	}

	public void clear() {
		assert !dataSet.getOpen().$();
		columns.clear();
	}

	public boolean containsColumn(Column<?> c) {
		return columns.contains(c);
	}

	public int getColumnCount() {
		return columns.size();
	}

	public Iterator<Column<?>> iterator() {
		return new Iterator<Column<?>>() {
			final Iterator<Column<?>> iter = columns.iterator();

			public boolean hasNext() {
				return iter.hasNext();
			}

			public Column<?> next() {
				return iter.next();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public void onValueChange(ValueChangeEvent<Integer> event) {
		for (Iterator<Column<?>> iter = columns.iterator(); iter.hasNext();) {
			Column<?> c = iter.next();
			c.reset();
		}
	}
}
