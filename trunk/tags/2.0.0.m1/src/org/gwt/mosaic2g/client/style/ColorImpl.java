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
package org.gwt.mosaic2g.client.style;

public class ColorImpl {

	public native Color parse(String color)
	/*-{
		var pad = function(num, totalChars) {
		  var pad = '0';
		  num = num + '';
		  while (num.length < totalChars) {
		      num = pad + num;
		  }
		  return num;
		};
		
		// Trim trailing/leading whitespace
		color = color.replace(/^\s*|\s*$/, '');
		
		// Expand three-digit hex
		color = color.replace(/^#?([a-f0-9])([a-f0-9])([a-f0-9])$/i, '#$1$1$2$2$3$3');
		
		// Determine if input is RGB(A)
		var rgb = color.match(new RegExp('^rgba?\\(\\s*' +
		      '(\\d|[1-9]\\d|1\\d{2}|2[0-4][0-9]|25[0-5])' +
		      '\\s*,\\s*' +
		      '(\\d|[1-9]\\d|1\\d{2}|2[0-4][0-9]|25[0-5])' +
		      '\\s*,\\s*' +
		      '(\\d|[1-9]\\d|1\\d{2}|2[0-4][0-9]|25[0-5])' +
		      '(?:\\s*,\\s*' +
		      '(0|1|0?\\.\\d+))?' +
		      '\\s*\\)$'
		  , 'i')),
		  alpha = !!rgb && rgb[4] != null ? rgb[4] : null,
		
		  // Convert hex to decimal
		  decimal = !!rgb? [rgb[1], rgb[2], rgb[3]] : color.replace(
		      /^#?([a-f0-9][a-f0-9])([a-f0-9][a-f0-9])([a-f0-9][a-f0-9])/i,
		      function() {
		          return parseInt(arguments[1], 16) + ',' +
		              parseInt(arguments[2], 16) + ',' +
		              parseInt(arguments[3], 16);
		      }
		  ).split(/,/),
		  returnValue;

		var result = @org.gwt.mosaic2.client.style.Color::new(IIID)(
			parseInt(decimal[0], 10),
			parseInt(decimal[1], 10),
			parseInt(decimal[2], 10),
			(alpha != null ? parseFloat(alpha) : 1.0));
			
		return result;
	}-*/;

	public String toString(int r, int g, int b, double alpha) {
		StringBuilder sb = new StringBuilder();
		if (alpha == 1.0) {
			sb.append("rgb(").append(r).append(", ").append(g).append(", ")
					.append(b).append(')');
		} else {
			sb.append("rgba(").append(r).append(", ").append(g).append(", ")
					.append(b).append(", ").append(alpha).append(')');
		}
		return sb.toString();
	}
}
