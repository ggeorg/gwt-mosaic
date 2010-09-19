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

import org.gwt.mosaic2g.db.client.Column;
import org.gwt.mosaic2g.db.client.DataSet;
import org.gwt.mosaic2g.db.client.DataSource;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ListBox;

/**
 * 
 * @param <T>
 * @param <D>
 * 
 * @author ggeorg
 */
public class DBLookupListBox<T, D> extends AbstractValueAware<T, D> {

	private final ListBox listBox = new ListBox();

	private DataSource<?> lookupDataSource;
	private Column<D> keyColumn;
	private Column<?> displayColumn;

	private HandlerRegistration fillHR;

	public DBLookupListBox() {
		this(null, null, null, null, null);
	}

	public DBLookupListBox(DataSource<T> dataSource, Column<D> column,
			DataSource<?> lookupDataSource, Column<D> keyColumn,
			Column<?> displayColumn) {
		initWidget(listBox);
		listBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				int index = listBox.getSelectedIndex();
				if (index != -1) {
					getColumn().getDisplayValue().$(listBox.getValue(index));
				}
			}
		});
		setDataSource(dataSource);
		setColumn(column);
		setLookupDataSource(lookupDataSource);
		setKeyColumn(keyColumn);
		setDisplayColumn(displayColumn);
	}

	public DataSource<?> getLookupDataSource() {
		return lookupDataSource;
	}

	private HandlerRegistration lookupDataSourceOpenHR;

	public void setLookupDataSource(DataSource<?> lookupDataSource) {
		if (this.lookupDataSource == lookupDataSource) {
			return;
		}
		if (lookupDataSourceOpenHR != null) {
			lookupDataSourceOpenHR.removeHandler();
			lookupDataSourceOpenHR = null;
		}
		this.lookupDataSource = lookupDataSource;
		if (this.lookupDataSource != null) {
			this.lookupDataSource.getOpen().addValueChangeHandler(
					new ValueChangeHandler<Boolean>() {
						public void onValueChange(
								ValueChangeEvent<Boolean> event) {
							if (event.getValue()) {
								populate();
							} else {
								listBox.clear();
							}
						}
					});
		}
	}

	public Column<D> getKeyColumn() {
		return keyColumn;
	}

	public void setKeyColumn(Column<D> keyColumn) {
		this.keyColumn = keyColumn;
	}

	public Column<?> getDisplayColumn() {
		return displayColumn;
	}

	public void setDisplayColumn(Column<?> displayColumn) {
		this.displayColumn = displayColumn;
	}

	private HandlerRegistration setValueHR;

	@Override
	protected void setValue(final Column<D> value) {
		final DataSet<?> dataSet;
		if (lookupDataSource == null
				|| (dataSet = lookupDataSource.getDataSet()) == null) {
			return;
		}
		dataSet.setDisableControls(true);
		setValueHR = dataSet.getRow().addValueChangeHandler(
				new ValueChangeHandler<Integer>() {
					public void onValueChange(ValueChangeEvent<Integer> event) {
						boolean found = value.getValue().equals(
								keyColumn.getValue());

						// check if we are done
						if (found
								|| (event.getValue() == dataSet.getRowCount() - 1)) {
							setValueHR.removeHandler();
							dataSet.setDisableControls(false);
							if (found) {
								listBox.setSelectedIndex(event.getValue());
							}
						}
					}
				});
		dataSet.first();
		while (!dataSet.atLast()) {
			dataSet.next();
		}
	}

	@Override
	protected void setEnabled(boolean enabled) {
		listBox.setEnabled(enabled);
		if (enabled && lookupDataSource != null
				&& lookupDataSource.getDataSet() != null && keyColumn != null
				&& displayColumn != null) {
			lookupDataSource.getDataSet().open();
		}
	}

	protected void populate() {
		final DataSet<?> dataSet;
		if (lookupDataSource != null && lookupDataSource.getOpen().$()
				&& (dataSet = lookupDataSource.getDataSet()) != null) {
			dataSet.setDisableControls(true);
			fillHR = dataSet.getRow().addValueChangeHandler(
					new ValueChangeHandler<Integer>() {
						public void onValueChange(
								ValueChangeEvent<Integer> event) {
							String value = (keyColumn == null) ? String
									.valueOf(dataSet.getRowData()) : keyColumn
									.getDisplayValue().$();
							String text = (displayColumn == null ? value
									: displayColumn.getDisplayValue().$());
							listBox.addItem(text, value);

							// check if we are done
							if (listBox.getItemCount() == dataSet.getRowCount()) {
								fillHR.removeHandler();
								dataSet.setDisableControls(false);
							}
						}
					});
			listBox.clear();
			dataSet.first();
			while (!dataSet.atLast()) {
				dataSet.next();
			}
		}
	}

}
