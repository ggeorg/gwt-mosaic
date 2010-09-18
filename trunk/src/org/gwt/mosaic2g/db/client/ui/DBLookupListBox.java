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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
/*
public class DBLookupListBox<T, L> extends AbstractViewer<T> {

	private final ListBox listBox = new ListBox();

	private DataSet<?> lookupDataSet;
	private Column<T> keyColumn;
	private Column<L> displayColumn;

	public DBLookupListBox() {
		initWidget(listBox);
		listBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int index = listBox.getSelectedIndex();
				if (index != -1) {
					getColumn().$(
							getColumn().parseString(listBox.getValue(index)));
				}
			}
		});
		getEnabled().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (lookupDataSet != null && keyColumn != null
						&& displayColumn != null) {
					if (event.getValue()) {
						lookupDataSet.open();
						lookupDataSet.getRow().addValueChangeHandler(
								new ValueChangeHandler<Integer>() {
									@Override
									public void onValueChange(
											ValueChangeEvent<Integer> event) {
										listBox.addItem(String
												.valueOf(keyColumn.$()), String
												.valueOf(displayColumn.$()));
									}
								});
						listBox.clear();
						lookupDataSet.first();
						while (!lookupDataSet.atLast()) {
							lookupDataSet.next();
						}
					} else {
						listBox.clear();
					}
				}
			}
		});
	}

	public DBLookupListBox(DataSet<?> dataSet, Column<T> column,
			DataSet<?> lookupDataSet, Column<T> keyColumn,
			Column<L> displayColumn) {
		this();
		setDataSet(dataSet);
		setColumn(column);
		setLookupDataSet(lookupDataSet);
		setKeyColumn(keyColumn);
		setDisplayColumn(displayColumn);
	}

	@Override
	protected void setValue(T value) {
		if (lookupDataSet == null) {
			return;
		}
		int index = lookupDataSet.getRow().$();
		if (index != -1) {
			listBox.setSelectedIndex(index);
		}
	}

	public DataSet<?> getLookupDataSet() {
		return lookupDataSet;
	}

	public void setLookupDataSet(DataSet<?> lookupDataSet) {
		this.lookupDataSet = lookupDataSet;
	}

	public Column<T> getKeyColumn() {
		return keyColumn;
	}

	public void setKeyColumn(Column<T> keyColumn) {
		this.keyColumn = keyColumn;
	}

	public Column<L> getDisplayColumn() {
		return displayColumn;
	}

	public void setDisplayColumn(Column<L> displayColumn) {
		this.displayColumn = displayColumn;
	}

}
*/