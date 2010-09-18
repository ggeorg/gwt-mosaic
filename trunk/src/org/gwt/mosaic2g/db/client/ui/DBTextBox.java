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
import org.gwt.mosaic2g.db.client.DataSource;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @param <T>
 * @param <D>
 * 
 * @author ggeorg
 */
public class DBTextBox<T, D> extends AbstractEditor<T, D> {

	private final TextBox textBox = new TextBox();

	public DBTextBox() {
		super();
		initWidget(textBox);
		textBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				edit();
			}
		});
		textBox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				int keyCode = event.getNativeKeyCode();
				if (keyCode == KeyCodes.KEY_ESCAPE) {
					cancel();
				}
			}
		});
		textBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				getColumn().getDisplayValue().$(event.getValue());
			}
		});
	}

	@Override
	public void setDataSource(DataSource<T> dataSource) {
		super.setDataSource(dataSource);
		if (dataSource == null) {
			return;
		}
		dataSource.getOpen().addValueChangeHandler(
				new ValueChangeHandler<Boolean>() {
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						textBox.setEnabled(event.getValue());
					}
				});
	}

	public DBTextBox(DataSource<T> dataSource, Column<D> column) {
		this();
		setDataSource(dataSource);
		setColumn(column);
	}

	@Override
	protected void setValue(Column<D> column) {
		textBox.setValue(column.getDisplayValue().$());
	}

	@Override
	protected void setEnabled(Boolean value) {
		textBox.setEnabled(value);
	}

}
