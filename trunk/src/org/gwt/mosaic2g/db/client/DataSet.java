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

import org.gwt.mosaic2g.binding.client.Property;

/**
 * 
 * @param <T>
 * 
 * @author ggeorg
 */
public interface DataSet<T> {

	/**
	 * TODO
	 * 
	 * @return
	 */
	Property<Boolean> getOpen();

	/**
	 * TODO
	 * 
	 * @param handler
	 */
	void open();

	/**
	 * TODO
	 * 
	 * @param handler
	 */
	void close();

	/**
	 * Returns current row position.
	 * 
	 * @return the current row position
	 */
	Property<Integer> getRow();

	/**
	 * Returns the item/data at the current row position.
	 * 
	 * @return the item (data) at the current row position
	 */
	T getRowData();

	/**
	 * Get the total count of all rows.
	 * 
	 * @return the total row count
	 */
	int getRowCount();

	/**
	 * Get the start index of the range.
	 * 
	 * @return the start index
	 */
	int getStart();

	/**
	 * Get the length of the range.
	 * 
	 * @return the length
	 */
	int getVisibleRowCount();

	/**
	 * Moves the row position to the first row.
	 */
	void first();

	/**
	 * Moves the current row position to the last row.
	 */
	void last();

	/**
	 * Moves the row position to the previous row.
	 */
	void prior();

	/**
	 * Moves the row position to the next row.
	 */
	void next();

	/**
	 * Returns {@code true} if the current position is the first row of
	 * {@code DataSet}.
	 * 
	 * @return {@code true} if the current position is the first row of
	 *         {@code DataSet}; {@code false} otherwise
	 */
	boolean atFirst();

	/**
	 * Returns {@code true} if the current position is the last row of
	 * {@code DataSet}.
	 * 
	 * @return {@code true} if the current position is the last row of
	 *         {@code DataSet}; {@code false} otherwise
	 */
	boolean atLast();

	/**
	 * Read-only property that returns whether the {@code DataSet} is being
	 * edited or not.
	 * 
	 * @return {@code true} if the current row is being edited; {@code false}
	 *         otherwise
	 */
	Property<Boolean> getEditing();

	/**
	 * Edits the existing row of the {@code DataSet}.
	 */
	void editRow();

	/**
	 * Read-only property that returns whether data is being added to a new row
	 * in the {@code DataSet} or not.
	 * 
	 * @return {@code true} if a new row is added to the {@code DataSet};
	 *         {@code false} otherwise
	 */
	Property<Boolean> getEditingNewRow();

	/**
	 * Start editing a new row.
	 * 
	 * @param before
	 *            indicates whether to insert the new row before or after the
	 *            current row
	 */
	void insertRow(boolean before);

	/**
	 * If a new or existing record is being edited, calling {@code #post()}
	 * force the record to be posted.
	 * 
	 * @throws DataSetException
	 */
	void post() throws DataSetException;

	/**
	 * Cancels the edits to a new or existing record.
	 * 
	 * @throws DataSetException
	 */
	void cancel() throws DataSetException;

	/**
	 * Save changes made to the data in the {@code DataSet} back to the data
	 * source using a {@link Resolver}.
	 */
	void saveChanges() throws DataSetException;

	// TODO cancel ???
	
	void setDisableControls(boolean disable);
	
	boolean isDisableControls();
}
