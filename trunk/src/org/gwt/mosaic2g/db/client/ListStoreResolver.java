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
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @param <T>
 * 
 * @author ggeorg
 */
public class ListStoreResolver<T> implements Resolver<T> {

	private final List<T> store;

	public ListStoreResolver(List<T> store) {
		this.store = store;
	}

	public void close(DataSet<T> dataSet) {
		// do nothing
	}

	public void resolveData(DataSet<T> dataSet, Set<T> rows,
			AsyncCallback<Set<T>> callaback) {
		if (rows != null) {
			for (T rowData : rows) {
				if (!store.contains(rowData)) {
					store.add(rowData);
				}
			}
		}
	}

}
