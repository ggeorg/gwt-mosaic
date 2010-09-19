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
	private Column<D> valueColumn;
	private Column<?> itemTextColumn;

	private HandlerRegistration lookupDataSourceOpenHR;

	private HandlerRegistration valueColumnHR = null;
	private HandlerRegistration itemTextColumnHR = null;

	private HandlerRegistration populateHR;
	private HandlerRegistration setValueHR;

	public DBLookupListBox() {
		this(null, null, null, null, null);
	}

	public DBLookupListBox(DataSource<T> dataSource, Column<D> column,
			DataSource<?> lookupDataSource, Column<D> valueColumn,
			Column<?> itemTextColumn) {
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
		setValueColumn(valueColumn);
		setItemTextColumn(itemTextColumn);
	}

	public DataSource<?> getLookupDataSource() {
		return lookupDataSource;
	}

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

	public Column<D> getValueColumn() {
		return valueColumn;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setValueColumn(Column<D> valueColumn) {
		if (this.valueColumn == valueColumn) {
			return;
		}

		if (valueColumnHR != null) {
			valueColumnHR.removeHandler();
			valueColumnHR = null;
		}

		this.valueColumn = valueColumn;

		if (this.valueColumn != null) {
			populate();
			valueColumnHR = this.valueColumn.getValue().addValueChangeHandler(
					new ValueChangeHandler() {
						public void onValueChange(ValueChangeEvent event) {
							int index = listBox.getSelectedIndex();
							final DataSet<?> dataSet;
							if (index != -1
									&& getLookupDataSource() != null
									&& (dataSet = getLookupDataSource()
											.getDataSet()) != null) {
								final Column<?> valueColumn = getValueColumn();
								String value = (valueColumn == null) ? String
										.valueOf(dataSet.getRowData())
										: valueColumn.getDisplayValue().$();
								String itemText = (itemTextColumn == null ? value
										: itemTextColumn.getDisplayValue().$());
								listBox.setValue(index, value);
								listBox.setItemText(index, itemText);
							}
						}
					});
		}
	}

	public Column<?> getItemTextColumn() {
		return itemTextColumn;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setItemTextColumn(Column<?> itemTextColumn) {
		if (this.itemTextColumn == itemTextColumn) {
			return;
		}

		if (itemTextColumnHR != null) {
			itemTextColumnHR.removeHandler();
			itemTextColumnHR = null;
		}

		this.itemTextColumn = itemTextColumn;

		if (this.itemTextColumn != null) {
			populate();
			itemTextColumnHR = this.itemTextColumn.getValue()
					.addValueChangeHandler(new ValueChangeHandler() {
						public void onValueChange(ValueChangeEvent event) {
							int index = listBox.getSelectedIndex();
							final DataSet<?> dataSet;
							if (index != -1
									&& getLookupDataSource() != null
									&& (dataSet = getLookupDataSource()
											.getDataSet()) != null) {
								final Column<?> itemTextColumn = getItemTextColumn();
								String value = (valueColumn == null) ? String
										.valueOf(dataSet.getRowData())
										: valueColumn.getDisplayValue().$();
								String itemText = (itemTextColumn == null ? value
										: itemTextColumn.getDisplayValue().$());
								listBox.setItemText(index, itemText);
							}
						}
					});
		}
	}

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
								valueColumn.getValue());

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
				&& lookupDataSource.getDataSet() != null && valueColumn != null
				&& itemTextColumn != null) {
			lookupDataSource.getDataSet().open();
		}
	}

	protected void populate() {
		final DataSet<?> dataSet;
		if (lookupDataSource != null && lookupDataSource.getOpen().$()
				&& (dataSet = lookupDataSource.getDataSet()) != null) {
			dataSet.setDisableControls(true);
			populateHR = dataSet.getRow().addValueChangeHandler(
					new ValueChangeHandler<Integer>() {
						public void onValueChange(
								ValueChangeEvent<Integer> event) {
							String value = (valueColumn == null) ? String
									.valueOf(dataSet.getRowData())
									: valueColumn.getDisplayValue().$();
							String itemText = (itemTextColumn == null ? value
									: itemTextColumn.getDisplayValue().$());
							listBox.addItem(itemText, value);

							// check if we are done
							if (listBox.getItemCount() == dataSet.getRowCount()) {
								populateHR.removeHandler();
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
