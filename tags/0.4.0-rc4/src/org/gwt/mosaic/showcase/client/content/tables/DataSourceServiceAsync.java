/*
 * Copyright 2008 Google Inc.
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
package org.gwt.mosaic.showcase.client.content.tables;

import org.gwt.mosaic.showcase.client.content.tables.shared.Student;
import org.gwt.mosaic.ui.client.table.SerializableResponse;
import org.gwt.mosaic.ui.client.table.TableModelHelper.Request;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous version of {@link DataSourceService}.
 */
public interface DataSourceServiceAsync {
  void requestRows(Request request,
      AsyncCallback<SerializableResponse<Student>> callback);
}
