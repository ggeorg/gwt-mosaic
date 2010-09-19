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
package org.gwt.mosaic2g.db.client;

import org.gwt.mosaic2g.binding.client.Binder;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * 
 * @param <T>
 * 
 * @author ggeorg
 */
public abstract class NumericColumn<T extends Number> extends Column<T> {

	private NumberFormat numberFormat = null;
	
	public NumericColumn(String name) {
		super(name);
	}

	public NumericColumn(String name, T value) {
		super(name, value);
	}

	public NumericColumn(String name, Binder<T> binder) {
		super(name, binder);
	}

	public NumberFormat getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}

}