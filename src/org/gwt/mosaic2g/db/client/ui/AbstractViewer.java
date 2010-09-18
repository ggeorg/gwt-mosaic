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

import org.gwt.mosaic2g.db.client.DataSource;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;

/**
 * 
 *  @param <T>
 *  
 *  @author ggeorg
 */
public abstract class AbstractViewer<T> extends Composite implements Viewer<T> {

	private DataSource<T> dataSource;
	private HandlerRegistration dataSourceHR = null;

	public DataSource<T> getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource<T> dataSource) {
		if (this.dataSource == dataSource) {
			return;
		}
		if (dataSourceHR != null) {
			dataSourceHR.removeHandler();
			dataSourceHR = null;
		}
		this.dataSource = dataSource;
		if (this.dataSource != null) {
			dataSourceHR = this.dataSource.getOpen().addValueChangeHandler(
					new ValueChangeHandler<Boolean>() {
						public void onValueChange(
								ValueChangeEvent<Boolean> event) {
							setEnabled(event.getValue());
						}
					});
		}
	}

	protected abstract void setEnabled(Boolean value);

}
