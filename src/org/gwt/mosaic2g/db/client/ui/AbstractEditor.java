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
package org.gwt.mosaic2g.db.client.ui;

import org.gwt.mosaic2g.binding.client.AbstractBinder;
import org.gwt.mosaic2g.binding.client.Property;
import org.gwt.mosaic2g.db.client.Column;
import org.gwt.mosaic2g.db.client.DataSource;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * 
 * @param <T>
 * @param <D>
 * 
 * @author ggeorg
 */
public abstract class AbstractEditor<T, D> extends AbstractViewer<T> implements
		Editor<D> {

	private Column<D> column;
	private final Property<D> valueP = new Property<D>(new AbstractBinder<D>() {
		@Override
		public D get() {
			if (column != null) {
				return column.getValue().$();
			}
			return null;
		}
	});

	private HandlerRegistration columnHR = null;

	public Property<D> getValue() {
		return valueP;
	}

	@Override
	public void setDataSource(DataSource<T> dataSource) {
		super.setDataSource(dataSource);
		// setValueManually();
	}

	public Column<D> getColumn() {
		return column;
	}

	public void setColumn(Column<D> column) {
		if (this.column == column) {
			return;
		}
		if (columnHR != null) {
			columnHR.removeHandler();
			columnHR = null;
		}
		this.column = column;
		if (this.column != null) {
			this.columnHR = getDataSource().getRow().addValueChangeHandler(
					new ValueChangeHandler<Integer>() {
						public void onValueChange(
								ValueChangeEvent<Integer> event) {
							Column<D> column = AbstractEditor.this.column;
							setValue(column);
						}
					});
		}
	}

	protected abstract void setValue(Column<D> value);

	protected void edit() {
		if (getDataSource() != null && getDataSource().getOpen().$()) {
			getDataSource().getDataSet().editRow();
		}
	}

	protected void cancel() {
		if (getDataSource() != null && getDataSource().getOpen().$()
				&& column != null) {
			setValue(column);
		}
	}

}
