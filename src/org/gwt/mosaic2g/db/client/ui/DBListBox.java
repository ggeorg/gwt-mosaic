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
 * A {@code DBListBox} is a data-aware extension of {@link ListBox}.
 * {@code DBListBox} displays a list of choices from which a user may choose a
 * single value.
 * 
 * @param <T>
 *            the row type in {@link DataSource}
 * 
 * @author ggeorg
 */
public class DBListBox<T> extends AbstractDataSourceAware<T> {

	private final ListBox listBox = new ListBox();

	private Column<?> valueColumn;
	private Column<?> itemTextColumn;

	private HandlerRegistration valueColumnHR = null;
	private HandlerRegistration itemTextColumnHR = null;

	private HandlerRegistration populateHR;

	public DBListBox() {
		this(null, null, null);
	}

	public DBListBox(DataSource<T> dataSource, Column<?> valueColumn,
			Column<?> itemTextColumn) {
		super();
		initWidget(listBox);
		listBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				int index = listBox.getSelectedIndex();
				DataSet<T> dataSet;
				if (index != -1 && getDataSource() != null
						&& (dataSet = getDataSource().getDataSet()) != null) {
					dataSet.getRow().$(index);
				}
			}
		});
		setDataSource(dataSource);
		setValueColumn(valueColumn);
		setItemTextColumn(itemTextColumn);
	}

	@Override
	public void setDataSource(final DataSource<T> dataSource) {
		super.setDataSource(dataSource);
	}

	public Column<?> getValueColumn() {
		return valueColumn;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setValueColumn(Column<?> valueColumn) {
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
							final DataSet<T> dataSet;
							if (index != -1
									&& getDataSource() != null
									&& (dataSet = getDataSource().getDataSet()) != null) {
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
							final DataSet<T> dataSet;
							if (index != -1
									&& getDataSource() != null
									&& (dataSet = getDataSource().getDataSet()) != null) {
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

	protected void populate() {
		final DataSet<T> dataSet;
		if (getDataSource() != null && getDataSource().getOpen().$()
				&& (dataSet = getDataSource().getDataSet()) != null) {
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

	@Override
	protected void setEnabled(boolean enabled) {
		listBox.setEnabled(enabled);
		if (enabled) {
			populate();
		} else {
			listBox.clear();
		}
	}

	@Override
	protected void setRow(int index) {
		listBox.setItemSelected(index, true);
	}

	public int getVisibleItemCount() {
		return listBox.getVisibleItemCount();
	}

	public void setVisibleItemCount(int visibleItems) {
		listBox.setVisibleItemCount(visibleItems);
	}

}
