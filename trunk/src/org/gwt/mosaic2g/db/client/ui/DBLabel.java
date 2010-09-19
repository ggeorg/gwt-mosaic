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

import com.google.gwt.user.client.ui.HTML;

/**
 * A {@code DBLabel} is a data-aware extension of {@link HTML}.
 * 
 * @param <T> the row type in {@link DataSource}
 * @param <D> the column type in {@link Column}
 * 
 * @author ggeorg
 */
public class DBLabel<T, D> extends AbstractColumnAware<T, D> {

	private final HTML html = new HTML();

	private boolean asHTML = false;
	private boolean enabled = false;;

	public DBLabel() {
		this(null, null);
	}

	public DBLabel(DataSource<T> dataSource, Column<D> column) {
		super();
		initWidget(html);
		setDataSource(dataSource);
		setColumn(column);
	}

	public boolean isAsHTML() {
		return asHTML;
	}

	public void setAsHTML(boolean asHTML) {
		this.asHTML = asHTML;
		if (enabled) {
			setRow(getDataSource().getRow().$());
		}
	}

	@Override
	protected void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	protected void setValue(Column<D> value) {
		if (asHTML) {
			html.setHTML(getColumn().getDisplayValue().$());
		} else {
			html.setText(getColumn().getDisplayValue().$());
		}
	}

}
