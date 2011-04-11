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
package org.gwt.mosaic2g.binding.client;

/**
 * {@code Validator} is responsible for validating the value from the target of
 * a binding.
 * 
 * @param <T>
 *            the type of value that this validator can validate
 * 
 * @author ggeorg
 */
public abstract class Validator<T> {

	/**
	 * An instance of {@code Result} is returned from a {@code Validator}'s
	 * {@code validate} method to indicate an invalid value.
	 * 
	 * @author ggeorg
	 */
	public class Result {
		private final Object errorCode;
		private final String description;

		/**
		 * Creates a {@code Result} with the given error code and description.
		 * 
		 * @param errorCode
		 *            an error code for this {@code Result}, may be {@code null}
		 * @param description
		 *            description a textual description of the {@code Result},
		 *            may be {@code null}
		 */
		public Result(Object errorCode, String description) {
			this.errorCode = errorCode;
			this.description = description;
		}

		/**
		 * Returns a description of the validation result, which may be
		 * {@code null}.
		 * 
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * Returns the error code for the result, which may be {@code null}.
		 * 
		 * @return the error code
		 */
		public Object getErrorCode() {
			return errorCode;
		}

		/**
		 * Returns a string representation of the {@code Result}. This method is
		 * intended to be used for debugging purposes only, and the content and
		 * format of the returned string may vary between implementations. The
		 * returned string may be empty but may not be {@code null}.
		 * 
		 * @return a string representation of this {@code Result}
		 */
		public String toString() {
			return getClass().getName() + " [" + "errorCode=" + errorCode
					+ ", description=" + description + "]";
		}
	}

	/**
	 * Validates a value; returns {@code null} for a valid value, and a
	 * {@code Result} object describing the problem for an invalid value.
	 * 
	 * @param value
	 *            the value to validate, may be {@code null}
	 * @return {@code null} for a valid value or a {@code Result} describing the
	 *         problem for an invalid value
	 */
	public abstract Result validate(T value);
}
