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

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @param <T>
 * 
 * @author ggeorg
 */
public class BeanStoreProvider<T> implements Provider<T> {

	private final T store;

	public BeanStoreProvider(T store) {
		this.store = store;
	}

	public void close(DataSet<T> dataSet) {
		// do nothing
	}

	public boolean hasMoreData(DataSet<T> dataSet) {
		return false;
	}

	public void provideData(DataSet<T> dataSet, List<T> data,
			AsyncCallback<Range> callback) {
		data.clear();
		data.add(store);
		callback.onSuccess(new Range(0, 1));
	}

	public void provideMoreData(DataSet<T> dataSet, List<T> data, int row,
			AsyncCallback<Range> asyncCallback) {
		// do nothing
	}

	public void createNew(DataSet<T> dataSet, AsyncCallback<T> asyncCallback) {
		throw new UnsupportedOperationException();
	}

}
