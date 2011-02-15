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
package gwt.mosaic.client.util.concurrent;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

/**
 * Abstract base class for "tasks". A task is an asynchronous operation that may
 * optionally return a value.
 * 
 * @param <V>
 *            The type of the value returned by the operation. May be
 *            {@link Void} to indicate that the task does not return a value.
 */
public abstract class Task<V> {

	private class ExecuteCallback implements ScheduledCommand {
		@Override
		public void execute() {
			V result = null;
			Exception fault = null;

			try {
				result = Task.this.execute();
			} catch (Exception exception) {
				fault = exception;
			}

			TaskListener<V> taskListener;
			Task.this.result = result;
			Task.this.fault = fault;

			taskListener = Task.this.taskListener;
			Task.this.taskListener = null;

			if (fault == null) {
				taskListener.taskExecuted(Task.this);
			} else {
				taskListener.executeFailed(Task.this);
			}
		}
	}

	private V result = null;
	private Exception fault = null;
	private TaskListener<V> taskListener = null;

	/**
	 * Synchronously executes the task.
	 * 
	 * @return The result of the task's execution.
	 * 
	 * @throws TaskExcecutionException
	 *             If an error occurs while executing the task.
	 */
	public abstract V execute() throws TaskExecutionException;

	/**
	 * Asynchronously executes the task. The caller is notified of the task's
	 * completion via the listener argument.
	 * 
	 * @param taskListener
	 *            The listener to be notified when the task completes.
	 */
	public void execute(TaskListener<V> taskListener) {
		if (taskListener == null) {
			throw new IllegalArgumentException("taskListener is null.");
		}

		if (this.taskListener != null) {
			throw new IllegalStateException("Task is already pending.");
		}

		this.taskListener = taskListener;

		result = null;
		fault = null;

		ExecuteCallback executeCallback = new ExecuteCallback();
		Scheduler.get().scheduleDeferred(executeCallback);
	}

	/**
	 * Returns the result of executing the task.
	 * 
	 * @return The task result, or <tt>null</tt> if the task is still executing
	 *         or has failed. The result itself may also be <tt>null</tt>;
	 *         callers should call {@link #isPending()} and {@link #getFault()}
	 *         to distinguish between these cases.
	 */
	public V getResult() {
		return result;
	}

	/**
	 * Returns the fault that occurred while executing the task.
	 * 
	 * @return The task fault, or <tt>null</tt> if the task is still executing
	 *         or has succeeded. Callers should call {@link #isPending()} to
	 *         distinguish between these cases.
	 */
	public Exception getFault() {
		return fault;
	}

	/**
	 * Returns the pending state of the task.
	 * 
	 * @return <tt>true</tt> if the task is awaiting execution or currently
	 *         executing; <tt>false</tt>, otherwise.
	 */
	public synchronized boolean isPending() {
		// TODO test if this method has any value
		return (taskListener != null);
	}

}
