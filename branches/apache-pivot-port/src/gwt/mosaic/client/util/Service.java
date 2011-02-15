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
package gwt.mosaic.client.util;

/**
 * Utility class for locating and instantiating service providers.
 */
public class Service {
	/**
	 * Attempts to load a service provider.
	 * 
	 * @param providerName
	 *            The name of the provider to load. The method first looks for a
	 *            system property with this name. The value of the property is
	 *            expected to be the name of a class that implements the
	 *            expected provider interface.
	 *            <p>
	 *            If the system property does not exist, the method then
	 *            attempts to load a resource with this name from the
	 *            META-INF/services directory. The resource is expected to be a
	 *            text file containing a single line that is the name of the
	 *            provider class.
	 */
	public static Object getProvider(String providerName) {
		throw new UnsupportedOperationException();
	}
}
