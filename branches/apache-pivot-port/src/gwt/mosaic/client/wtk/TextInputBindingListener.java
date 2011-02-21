/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gwt.mosaic.client.wtk;

/**
 * Text input binding listener interface.
 */
public interface TextInputBindingListener {
	/**
	 * Text input binding listener adapter.
	 */
	public static class Adapter implements TextInputBindingListener {
		@Override
		public void textKeyChanged(TextInput textInput, String previousTextKey) {
		}

		@Override
		public void textBindTypeChanged(TextInput textInput,
				BindType previousTextBindType) {
		}

		@Override
		public void textBindMappingChanged(TextInput textInput,
				TextInput.TextBindMapping previousTextBindMapping) {
		}
	}

	/**
	 * Called when a text input's text key has changed.
	 * 
	 * @param textInput
	 * @param previousTextKey
	 */
	public void textKeyChanged(TextInput textInput, String previousTextKey);

	/**
	 * Called when a text input's text bind type has changed.
	 * 
	 * @param textInput
	 * @param previousTextBindType
	 */
	public void textBindTypeChanged(TextInput textInput,
			BindType previousTextBindType);

	/**
	 * Called when a text input's text bind mapping has changed.
	 * 
	 * @param textInput
	 * @param previousTextBindMapping
	 */
	public void textBindMappingChanged(TextInput textInput,
			TextInput.TextBindMapping previousTextBindMapping);
}
