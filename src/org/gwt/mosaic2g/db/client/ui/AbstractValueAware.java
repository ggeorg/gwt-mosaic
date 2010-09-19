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

import org.gwt.mosaic2g.binding.client.AbstractBinder;
import org.gwt.mosaic2g.binding.client.Property;
import org.gwt.mosaic2g.db.client.Column;

import com.google.gwt.user.client.ui.Composite;

/**
 * Abstract implementation of {@link ValueAware} as a {@link Composite}.
 * 
 * @param <T>
 *            the row type in {@link DataSource}
 * @param <D>
 *            the column type in {@link Column}
 * 
 * @author ggeorg
 */
public abstract class AbstractValueAware<T, D> extends
		AbstractColumnAware<T, D> implements ValueAware<T, D> {

	private final Property<D> valueP = new Property<D>(new AbstractBinder<D>() {
		@Override
		public D get() {
			if (getColumn() != null) {
				return getColumn().getValue().$();
			}
			return null;
		}
	});

	public Property<D> getValue() {
		return valueP;
	}

	protected void edit() {
		if (getDataSource() != null && getDataSource().getOpen().$()) {
			getDataSource().getDataSet().editRow();
		}
	}

	protected void cancel() {
		if (getDataSource() != null && getDataSource().getOpen().$()
				&& getColumn() != null) {
			setValue(getColumn());
		}
	}

}
