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

import com.google.gwt.core.client.GWT;
import com.google.gwt.gen2.table.client.MutableTableModel;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.List;

import org.gwt.mosaic.showcase.client.content.tables.shared.Student;
import org.gwt.mosaic.showcase.client.content.tables.shared.StudentGenerator;

/**
 * An iterator that serves as the data source for TableOracle requests.
 */
public class DataSourceTableModel extends MutableTableModel<Student> {
  /**
   * The source of the data.
   */
  private StudentGenerator data = new StudentGenerator() {
    @Override
    public int getRandomInt(int max) {
      return Random.nextInt(max);
    }
  };

  /**
   * The RPC service used to generate data, if RPC mode is enabled.
   */
  private DataSourceServiceAsync dataService = null;

  /**
   * A boolean indicating that we should throw an error.
   */
  private boolean errorMode = false;

  /**
   * A boolean indicating that we should use an rpc request instead of
   * generating data locally.
   */
  private boolean rpcMode = false;

  /**
   * A boolean indicating that we should return 0 rows in the response.
   */
  private boolean zeroMode = false;

  /**
   * Check if error mode is enabled.
   * 
   * @return true if enabled
   */
  public boolean isErrorModeEnabled() {
    return errorMode;
  }

  /**
   * Check if RPC mode is enabled.
   * 
   * @return true if enabled
   */
  public boolean isRPCModeEnabled() {
    return rpcMode;
  }

  /**
   * Check if zero mode is enabled.
   * 
   * @return true if enabled
   */
  public boolean isZeroModeEnabled() {
    return zeroMode;
  }

  /**
   * Override that can optionally throw an error.
   */
  @Override
  public void requestRows(final Request request,
      final Callback<Student> callback) {
    if (errorMode) {
      // Return an error
      callback.onFailure(new Exception("An error has occured."));
    } else if (zeroMode) {
      // Return an empty result
      List<Student> students = data.generateStudents(0);
      callback.onRowsReady(request, new SerializableResponse<Student>(students));
    } else if (rpcMode) {
      // Create the service if needed
      if (dataService == null) {
        dataService = GWT.create(DataSourceService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) dataService;
        String moduleRelativeURL = GWT.getModuleBaseURL() + "datasource";
        endpoint.setServiceEntryPoint(moduleRelativeURL);
      }

      // Send RPC request for data
      dataService.requestRows(request,
          new AsyncCallback<SerializableResponse<Student>>() {
            public void onFailure(Throwable caught) {
              callback.onFailure(new Exception("RPC Failure"));
            }

            public void onSuccess(SerializableResponse<Student> result) {
              callback.onRowsReady(request, result);
            }
          });
    } else {
      // Generate data locally
      int numRows = request.getNumRows();
      List<Student> students = data.generateStudents(numRows);
      SerializableResponse<Student> response = new SerializableResponse<Student>(
          students);
      callback.onRowsReady(request, response);
    }
  }

  /**
   * Enable or disable error mode.
   * 
   * @param enabled true to enable
   */
  public void setErrorModeEnabled(boolean enabled) {
    this.errorMode = enabled;
  }

  /**
   * Enable or disable rpc mode.
   * 
   * @param enabled true to enable
   */
  public void setRPCModeEnabled(boolean enabled) {
    this.rpcMode = enabled;
  }

  /**
   * Enable or disable zero mode.
   * 
   * @param enabled true to enable
   */
  public void setZeroModeEnabled(boolean enabled) {
    this.zeroMode = enabled;
  }

  @Override
  protected boolean onRowInserted(int beforeRow) {
    return true;
  }

  @Override
  protected boolean onRowRemoved(int row) {
    return true;
  }

  @Override
  protected boolean onSetRowValue(int row, Student rowValue) {
    return true;
  }
}