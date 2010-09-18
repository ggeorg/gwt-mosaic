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

import org.gwt.mosaic2g.binding.client.AbstractBinder;
import org.gwt.mosaic2g.binding.client.Property;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * 
 * @param <T>
 * 
 * @author ggeorg
 */
public final class DataSource<T> {

	private DataSet<T> dataSet;

	private boolean open;
	private final Property<Boolean> openP = new Property<Boolean>(
			new AbstractBinder<Boolean>() {
				@Override
				public Boolean get() {
					return open;
				}
			});

	private int row;
	private final Property<Integer> rowP = new Property<Integer>(
			new AbstractBinder<Integer>() {
				@Override
				public Integer get() {
					return row;
				}
			});

	private boolean editing;
	private final Property<Boolean> editingP = new Property<Boolean>(
			new AbstractBinder<Boolean>() {
				@Override
				public Boolean get() {
					return editing;
				}
			});

	private boolean editingNewRow;
	private final Property<Boolean> editingNewRowP = new Property<Boolean>(
			new AbstractBinder<Boolean>() {
				@Override
				public Boolean get() {
					return editingNewRow;
				}
			});

	private HandlerRegistration openHR = null;
	private HandlerRegistration rowHR = null;
	private HandlerRegistration editingHR = null;
	private HandlerRegistration editingNewRowHR = null;

	public DataSource() {
		this(null);
	}

	public DataSource(DataSet<T> dataSet) {
		setDataSet(dataSet);
	}

	public DataSet<T> getDataSet() {
		return dataSet;
	}

	public void setDataSet(DataSet<T> dataSet) {
		if (this.dataSet == dataSet) {
			return;
		}

		if (openHR != null) {
			openHR.removeHandler();
			openHR = null;
		}

		if (rowHR != null) {
			rowHR.removeHandler();
			rowHR = null;
		}

		if (editingHR != null) {
			editingHR.removeHandler();
			editingHR = null;
		}

		if (editingNewRowHR != null) {
			editingNewRowHR.removeHandler();
			editingNewRowHR = null;
		}

		this.dataSet = dataSet;

		if (this.dataSet != null) {
			openHR = this.dataSet.getOpen().addValueChangeHandler(
					new ValueChangeHandler<Boolean>() {
						public void onValueChange(
								ValueChangeEvent<Boolean> event) {
							open = event.getValue();
							if (!DataSource.this.dataSet.isDisableControls()) {
								ValueChangeEvent.fire(openP, open);
							}
						}
					});
			rowHR = this.dataSet.getRow().addValueChangeHandler(
					new ValueChangeHandler<Integer>() {
						public void onValueChange(
								ValueChangeEvent<Integer> event) {
							row = event.getValue();
							if (!DataSource.this.dataSet.isDisableControls()) {
								ValueChangeEvent.fire(rowP, row);
							}
						}
					});
			editingHR = this.dataSet.getEditing().addValueChangeHandler(
					new ValueChangeHandler<Boolean>() {
						public void onValueChange(
								ValueChangeEvent<Boolean> event) {
							editing = event.getValue();
							if (!DataSource.this.dataSet.isDisableControls()) {
								ValueChangeEvent.fire(editingP, editing);
							}
						}
					});
			editingNewRowHR = this.dataSet.getEditingNewRow()
					.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
						public void onValueChange(
								ValueChangeEvent<Boolean> event) {
							editingNewRow = event.getValue();
							if (!DataSource.this.dataSet.isDisableControls()) {
								ValueChangeEvent.fire(editingNewRowP,
										editingNewRow);
							}
						}
					});
		}
	}

	public Property<Boolean> getOpen() {
		return openP;
	}

	public Property<Integer> getRow() {
		return rowP;
	}

	public Property<Boolean> getEditing() {
		return editingP;
	}

	public Property<Boolean> getEditingNewRow() {
		return editingNewRowP;
	}

}
