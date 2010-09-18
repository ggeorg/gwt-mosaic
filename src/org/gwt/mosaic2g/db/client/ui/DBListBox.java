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
 * 
 * @author ggeorg
 */
public class DBListBox<T> extends AbstractViewer<T> {

	private final ListBox listBox = new ListBox();

	private Column<?> valueColumn;
	private Column<?> textColumn;

	private HandlerRegistration valueColumnHR = null;
	private HandlerRegistration textColumnHR = null;

	private HandlerRegistration fillHR;

	public DBListBox() {
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
	}

	public DBListBox(DataSource<T> dataSource, Column<?> valueColumn,
			Column<?> textColumn) {
		this();
		setDataSource(dataSource);
		setValueColumn(valueColumn);
		setTextColumn(textColumn);
	}

	@Override
	public void setDataSource(final DataSource<T> dataSource) {
		super.setDataSource(dataSource);

		if (dataSource == null) {
			return;
		}

		dataSource.getOpen().addValueChangeHandler(
				new ValueChangeHandler<Boolean>() {
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						final DataSet<T> dataSet = dataSource.getDataSet();
						if (event.getValue() && dataSet != null) {
							populate();
						} else {
							listBox.clear();
						}
					}
				});
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
							rowDataChanged();
						}
					});
		}
	}

	public Column<?> getTextColumn() {
		return textColumn;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setTextColumn(Column<?> textColumn) {
		if (this.textColumn == textColumn) {
			return;
		}

		if (textColumnHR != null) {
			textColumnHR.removeHandler();
			textColumnHR = null;
		}

		this.textColumn = textColumn;

		if (this.textColumn != null) {
			populate();
			textColumnHR = this.textColumn.getValue().addValueChangeHandler(
					new ValueChangeHandler() {
						public void onValueChange(ValueChangeEvent event) {
							rowDataChanged();
						}
					});
		}
	}

	protected void populate() {
		final DataSet<T> dataSet;
		if (getDataSource() != null && getDataSource().getOpen().$()
				&& (dataSet = getDataSource().getDataSet()) != null) {
			dataSet.setDisableControls(true);
			fillHR = dataSet.getRow().addValueChangeHandler(
					new ValueChangeHandler<Integer>() {
						public void onValueChange(
								ValueChangeEvent<Integer> event) {
							String value = (valueColumn == null) ? String
									.valueOf(dataSet.getRowData())
									: valueColumn.getDisplayValue().$();
							String text = (textColumn == null ? value
									: textColumn.getDisplayValue().$());
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

	protected void rowDataChanged() {
		int index = listBox.getSelectedIndex();
		DataSet<T> dataSet;
		if (index != -1 && getDataSource() != null
				&& (dataSet = getDataSource().getDataSet()) != null) {
			String value = (valueColumn == null) ? String.valueOf(dataSet
					.getRowData()) : valueColumn.getDisplayValue().$();
			String text = (textColumn == null ? value : textColumn
					.getDisplayValue().$());
			listBox.setValue(index, value);
			listBox.setItemText(index, text);
		}
	}

	@Override
	protected void setEnabled(Boolean value) {
		listBox.setEnabled(value);
	}

}