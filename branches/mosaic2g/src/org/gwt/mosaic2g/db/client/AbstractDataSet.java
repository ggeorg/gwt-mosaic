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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.gwt.mosaic2g.binding.client.AbstractBinder;
import org.gwt.mosaic2g.binding.client.Property;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @param <T>
 * 
 * @author ggeorg
 */
public abstract class AbstractDataSet<T> implements DataSet<T> {

	private final List<T> data;
	private final Set<T> insertedRows;
	private final Set<T> updatedRows;

	private final DataRowDescriptor<T> dataRow;
	private final Provider<T> provider;
	private final Resolver<T> resolver;

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

				@Override
				public void set(Integer value) {
					if(row == value) {
						return;
					}
					moveTo(value);
				}
			});

	private int rowCount;
	private int start;

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

	private boolean disableControls;

	// ---------------------------------------------------------------------
	public AbstractDataSet(Provider<T> provider, Resolver<T> resolver) {
		this.provider = provider;
		this.resolver = resolver;
		data = new ArrayList<T>();
		insertedRows = new HashSet<T>();
		updatedRows = new HashSet<T>();
		dataRow = new DataRowDescriptor<T>(this);
		start = 0;
	}

	public Provider<T> getProvider() {
		return provider;
	}

	public Resolver<T> getResolver() {
		return resolver;
	}

	public DataRowDescriptor<T> getDataRowDescriptor() {
		return dataRow;
	}

	public Property<Boolean> getOpen() {
		return openP;
	}

	public Property<Integer> getRow() {
		return rowP;
	}

	public T getRowData() {
		return data.get(row);
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getStart() {
		return start;
	}

	public int getVisibleRowCount() {
		return data.size();
	}

	public Property<Boolean> getEditing() {
		return editingP;
	}

	public Property<Boolean> getEditingNewRow() {
		return editingNewRowP;
	}

	public boolean isDisableControls() {
		return disableControls;
	}

	public void setDisableControls(boolean disableControls) {
		this.disableControls = disableControls;
	}

	// ---------------------------------------------------------------------
	public void open() {
		if (open) {
			return;
		}

		provider.provideData(this, data, new AsyncCallback<Range>() {

			public void onFailure(Throwable caught) {
				// TODO
			}

			public void onSuccess(Range result) {
				row = start = result.getStart();
				rowCount = result.getLength();
				ValueChangeEvent.fire(openP, open = true);
				first();
			}

		});
	}

	public void close() {
		if (!open) {
			return;
		}
		ValueChangeEvent.fire(openP, open = false);
	}

	// ---------------------------------------------------------------------
	public final void first() {
		if (!open || (rowCount == 0)) {
			return;
		}
		if (editing || editingNewRow) {
			try {
				post();
			} catch (DataSetException e) {
				Window.alert(e.getMessage());
				return;
			}
		}
		if (start == 0) {
			setRow(0);
		} else {
			provideMoreData(this, data, 0);
		}
	}

	public final void last() {
		if (!open || (rowCount == 0)) {
			return;
		}
		if (editing || editingNewRow) {
			try {
				post();
			} catch (DataSetException e) {
				Window.alert(e.getMessage());
				return;
			}
		}
		if (start <= (rowCount - 1) && (rowCount - 1) < (row + data.size())) {
			setRow(rowCount - 1);
		} else {
			provideMoreData(this, data, rowCount - 1);
		}
	}

	public void prior() {
		if (!open || (rowCount == 0)) {
			return;
		}
		if (editing || editingNewRow) {
			try {
				post();
			} catch (DataSetException e) {
				Window.alert(e.getMessage());
				return;
			}
		}
		if (start <= (row - 1)) {
			setRow(row - 1);
		} else {
			provideMoreData(this, data, row - 1);
		}
	}

	public void next() {
		if (!open || (rowCount == 0) || (row == rowCount - 1)) {
			return;
		}
		if (editing || editingNewRow) {
			try {
				post();
			} catch (DataSetException e) {
				Window.alert(e.getMessage());
				return;
			}
		}
		if (start <= (row + 1) && (row + 1) < (row + data.size())) {
			setRow(row + 1);
		} else {
			provideMoreData(this, data, row + 1);
		}
	}

	private void moveTo(int newRow) {
		if (!open || (rowCount <= 0) || (newRow >= rowCount)) {
			return;
		}
		if (editing || editingNewRow) {
			try {
				post();
			} catch (DataSetException e) {
				Window.alert(e.getMessage());
				return;
			}
		}
		if (start <= newRow && newRow < (row + data.size())) {
			setRow(newRow);
		} else {
			provideMoreData(this, data, newRow);
		}
	}

	public boolean atFirst() {
		return row == 0;
	}

	public boolean atLast() {
		return row == (rowCount - 1);
	}

	private void provideMoreData(DataSet<T> dataSet, List<T> data, final int row) {
		provider.provideMoreData(this, data, 0, new AsyncCallback<Range>() {
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			public void onSuccess(Range result) {
				start = result.getStart();
				rowCount = result.getLength();
				setRow(row);
			}
		});
	}

	private void setRow(int row) {
		this.row = row;
		ValueChangeEvent.fire(rowP, row);
	}

	// ---------------------------------------------------------------------
	public void editRow() {
		if (!open || (editing == true || editingNewRow == true)) {
			return;
		}
		setEditing(true);
	}

	public void insertRow(final boolean before) {
		if (!open || (editing == true || editingNewRow == true)) {
			return;
		}

		provider.createNew(this, new AsyncCallback<T>() {
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			public void onSuccess(T result) {
				data.add(before ? row : row + 1, result);
				insertedRows.add(result);
				++rowCount;
				setEditingNewRow(true);
			}
		});
	}

	public void post() throws DataSetException {
		if (!open || (editing == false || editingNewRow == false)) {
			return;
		}
		if (editing) {
			// check if we re-edit
			T rowData = getRowData();
			if (!insertedRows.contains(rowData)
					&& !updatedRows.contains(rowData)) {
				updatedRows.add(rowData);
			}
			onValidate();
			setEditing(false);
		} else if (editingNewRow) {
			onValidate();
			setEditingNewRow(false);
		}
	}

	public void cancel() throws DataSetException {
		if (!open || (editing == false || editingNewRow == false)) {
			return;
		}
		if (editing) {
			onCancel();
			setEditing(false);
		} else if (editingNewRow) {
			T rowData = getRowData();
			data.remove(rowData);
			insertedRows.remove(rowData);
			setEditingNewRow(false);
		}
	}

	protected void onValidate() throws DataSetException {
		// TODO
	}

	protected void onCancel() throws DataSetException {
		for (Iterator<Column<?>> iter = dataRow.iterator(); iter.hasNext();) {
			iter.next().restore();
		}
	}

	private void setEditingNewRow(boolean b) {
		editingNewRow = true;
		ValueChangeEvent.fire(editingNewRowP, editingNewRow);
	}

	private void setEditing(boolean b) {
		editing = true;
		ValueChangeEvent.fire(editingP, editing);
	}

	// ---------------------------------------------------------------------
	public void saveChanges() throws DataSetException {
		if (!open) {
			throw new DataSetException(
					"Can't perform this operation on a closed DataSet");
		}

		post();

		Set<T> rows = new HashSet<T>();
		rows.addAll(insertedRows);
		rows.addAll(updatedRows);
		resolver.resolveData(this, rows, new AsyncCallback<Set<T>>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			public void onSuccess(Set<T> result) {
				if (result != null) {
					for (T rowData : result) {
						if (insertedRows.contains(rowData)) {
							insertedRows.remove(rowData);
							// update data
						} else if (updatedRows.contains(rowData)) {
							updatedRows.remove(rowData);
							// update data
						} else {
							// TODO should not happen
						}
					}
				}
			}
		});
	}

}
