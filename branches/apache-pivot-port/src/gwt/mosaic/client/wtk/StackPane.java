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

import gwt.mosaic.client.beans.BeanAdapter;
import gwt.mosaic.client.wtk.skin.StackPaneSkin;

import com.google.gwt.core.client.GWT;

/**
 * Container that behaves like a stack of transparencies, all of which are
 * visible at the same time.
 */
public class StackPane extends Container {
	interface SkinBeanAdapter extends BeanAdapter<StackPaneSkin> {
	}
	
	public StackPane() {
		SkinBeanAdapter adapter = GWT.create(SkinBeanAdapter.class);
		adapter.setBean(new StackPaneSkin());
		setSkin(adapter);
	}
}