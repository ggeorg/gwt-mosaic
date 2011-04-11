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
public class FloatColumn extends NumericColumn<Float> {

	private final Property<String> displayValue = getValue().createBinding(
			new Converter<Float, String>() {
				@Override
				public String convertForward(Float value) {
					if (getNumberFormat() == null || value == null) {
						return value != null ? String.valueOf(value) : "";
					} else {
						return getNumberFormat().format(value);
					}
				}

				@Override
				public Float convertReverse(String value) {
					if (getNumberFormat() == null) {
						return Float.parseFloat(value);
					} else {
						return (float) getNumberFormat().parse(value);
					}
				}
			}, new Validator<Float>() {
				@Override
				public Result validate(Float value) {
					// TODO Auto-generated method stub
					return null;
				}
			});
	
	public FloatColumn(String name) {
		super(name);
	}

	public FloatColumn(String name, Float value) {
		super(name, value);
	}

	public FloatColumn(String name, Binder<Float> binder) {
		super(name, binder);
	}

	@Override
	public Property<String> getDisplayValue() {
		return displayValue;
	}

}
