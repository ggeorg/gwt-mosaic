/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos
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
package org.gwt.mosaic.core.client;

import org.gwt.mosaic.core.client.impl.UserAgentImpl;

import com.google.gwt.core.client.GWT;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public final class UserAgent {

	private static final UserAgentImpl impl = GWT.create(UserAgentImpl.class);

	public static final boolean isGecko() {
		return impl.isGecko();
	}

	public static final boolean isGecko18() {
		return impl.isGecko18();
	}

	public static final boolean isIE8() {
		return impl.isIE8();
	}

	public static final boolean xisIE6() {
		return impl.isIE6();
	}
	
	public static final boolean isIE() {
		return impl.isIE6() || impl.isIE8();
	}

	public static final boolean isOpera() {
		return impl.isOpera();
	}

	public static final boolean isSafari() {
		return impl.isSafari();
	}

}
