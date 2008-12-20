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

import java.io.Serializable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.widgetideas.table.client.ClientTableModel;

/**
 * An iterator that serves as the data source for TableOracle requests.
 */
public class DataSourceTableModel extends ClientTableModel<Serializable> {

  /**
   * The column count.
   */
  public static final int COLUMN_COUNT = 12;

  /**
   * The source of the data.
   */
//  private StudentGenerator data = new StudentGenerator() {
//    @Override
//    public int getRandomInt(int max) {
//      return Random.nextInt(max);
//    }
//  };
  private DataSourceData data = new DataSourceData() {
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
   * Check if zero mode is enabled.
   * 
   * @return true if enabled
   */
  public boolean isZeroModeEnabled() {
    return zeroMode;
  }

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
   * Override that can optionally throw an error.
   */
  @Override
  public void requestRows(final Request request,
      final Callback<Serializable> callback) {
    if (errorMode) {
      // Return an error
      callback.onFailure(new Exception("An error has occured."));
    } else if (zeroMode) {
      // Return an empty result
      //List<Serializable> students = data.generateStudents(0);
      //callback.onRowsReady(request, new SerializableResponse<Serializable>(students));
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
          new AsyncCallback<SerializableResponse<Serializable>>() {
            public void onFailure(Throwable caught) {
              callback.onFailure(new Exception("RPC Failure"));
            }

            public void onSuccess(SerializableResponse<Serializable> result) {
              callback.onRowsReady(request, result);
            }
          });
    } else {
   // Generate data locally
//      int numRows = request.getNumRows();
//      List<Serializable> students = data.generateStudents(numRows);
//      SerializableResponse<Serializable> response = new SerializableResponse<Serializable>(
//          students);
//      callback.onRowsReady(request, response);
   // Request rows from the local client
      super.requestRows(request, callback);
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
  
  //-------------------------

  @Override
  public Object getCell(int rowNum, int colNum) {
    System.out.println(rowNum+"x"+colNum+"?????"+COLUMN_COUNT);
    return data.getCell(rowNum, colNum);
  }

  @Override
  protected boolean onSetData(int row, int cell, Object data) {
    return true;
  }
}
