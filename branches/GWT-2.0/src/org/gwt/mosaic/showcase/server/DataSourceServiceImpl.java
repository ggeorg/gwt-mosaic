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
package org.gwt.mosaic.showcase.server;

import java.util.Random;

import org.gwt.mosaic.showcase.client.content.tables.DataSourceService;
import org.gwt.mosaic.showcase.client.content.tables.shared.Student;
import org.gwt.mosaic.showcase.client.content.tables.shared.StudentGenerator;
import org.gwt.mosaic.ui.client.table.SerializableResponse;
import org.gwt.mosaic.ui.client.table.TableModelHelper.ColumnSortList;
import org.gwt.mosaic.ui.client.table.TableModelHelper.Request;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementation of {@link DataSourceService}.
 */
public class DataSourceServiceImpl extends RemoteServiceServlet implements
    DataSourceService {
  private static final long serialVersionUID = 1L;

  /**
   * The source of the data.
   */
  private StudentGenerator data = new StudentGenerator() {
    @Override
    protected int getRandomInt(int max) {
      return random.nextInt(max);
    }
  };

  /**
   * A random number generator.
   */
  private Random random = new Random();

  public SerializableResponse<Student> requestRows(Request request) {
    // Get the sort info, even though we ignore it
    ColumnSortList sortList = request.getColumnSortList();
    sortList.getPrimaryColumn();
    sortList.isPrimaryAscending();

    // Return the data
    int numRows = request.getNumRows();
    return new SerializableResponse<Student>(data.generateStudents(numRows));
  }
}