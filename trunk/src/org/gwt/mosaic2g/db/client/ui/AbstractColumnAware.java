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

import com.google.gwt.user.client.ui.Composite;

/**
 * Abstract implementation of {@link ColumnAware} as a {@link Composite}.
 * 
 * @param <T>
 *            the row type in {@link DataSource}
 * @param <D>
 *            the column type in {@link Column}
 * 
 * @author ggeorg
 */
public abstract class AbstractColumnAware<T, D> extends
		AbstractDataSourceAware<T> implements ColumnAware<T, D> {

	private Column<D> column;

	public Column<D> getColumn() {
		return column;
	}

	public void setColumn(Column<D> column) {
		if (this.column == column) {
			return;
		}
		this.column = column;
	}

}
