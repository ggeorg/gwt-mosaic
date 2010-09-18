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
 * A {@code DBTextBox} is a data-aware extension of {@link TextBox}. Although
 * data in a {@code DBTextBox} is always edited as a {@link String}, a
 * {@code DBTextBox} can be used to display and edit data from all data types.
 * <p>
 * When a {@code DBTextBox} is attached to a {@link DataSource}, the following
 * keystrokes perform special tasks when pressed:
 * <ol>
 * <li>ENTER - causes the data in the {@link TextBox} to be written to the
 * {@link DataSource} column.</li>
 * <li>ESC - makes the data in the {@link TextBox} change back to the value in
 * the {@link DataSource} column.</li>
 * </ol>
 * 
 * @param <T>
 *            the row type in {@link DataSource}
 * @param <D>
 *            the column type in {@link Column}
 * 
 * @author ggeorg
 */
public class DBTextBox<T, D> extends AbstractValueAware<T, D> {

	private final TextBox textBox = new TextBox();

	public DBTextBox() {
		this(null, null);
	}

	public DBTextBox(DataSource<T> dataSource, Column<D> column) {
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
		setDataSource(dataSource);
		setColumn(column);
	}

	@Override
	protected void setValue(Column<D> column) {
		textBox.setValue(column.getDisplayValue().$());
	}

	@Override
	protected void setEnabled(boolean enabled) {
		textBox.setEnabled(enabled);
	}

}
