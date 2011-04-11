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
public interface Provider<T> {

	void close(DataSet<T> dataSet);

	boolean hasMoreData(DataSet<T> dataSet);

	void provideData(DataSet<T> dataSet, List<T> data,
			AsyncCallback<Range> asyncCallback);

	void provideMoreData(DataSet<T> dataSet, List<T> data, int row,
			AsyncCallback<Range> asyncCallback);

	void createNew(DataSet<T> dataSet, AsyncCallback<T> asyncCallback);
}
