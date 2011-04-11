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
import org.gwt.mosaic2g.binding.client.Converter;
import org.gwt.mosaic2g.binding.client.Property;
import org.gwt.mosaic2g.binding.client.Validator;

/**
 * 
 * @author ggeorg
 * 
 */
public class DoubleColumn extends NumericColumn<Double> {

	private final Property<String> displayValue = getValue().createBinding(
			new Converter<Double, String>() {
				@Override
				public String convertForward(Double value) {
					if (getNumberFormat() == null || value == null) {
						return value != null ? String.valueOf(value) : "";
					} else {
						return getNumberFormat().format(value);
					}
				}

				@Override
				public Double convertReverse(String value) {
					if (getNumberFormat() == null) {
						return Double.parseDouble(value);
					} else {
						return getNumberFormat().parse(value);
					}
				}
			}, new Validator<Double>() {
				@Override
				public Result validate(Double value) {
					// TODO Auto-generated method stub
					return null;
				}
			});
	
	public DoubleColumn(String name) {
		super(name);
	}

	public DoubleColumn(String name, Double value) {
		super(name, value);
	}

	public DoubleColumn(String name, Binder<Double> binder) {
		super(name, binder);
	}

	@Override
	public Property<String> getDisplayValue() {
		return displayValue;
	}

}
