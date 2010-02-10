/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos
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
/*
 * This is derived work from GWT Incubator project:
 * http://code.google.com/p/google-web-toolkit-incubator/
 * 
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
package org.gwt.mosaic.ui.client.table;

/**
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 *
 */
public interface RowValue {
  public enum DateColumnFormat {
    PATTERN, SHORT_DATE_FORMAT, MEDIUM_DATE_FORMAT, LONG_DATE_FORMAT, 
    SHORT_DATE_TIME_FORMAT, MEDIUM_DATE_TIME_FORMAT, LONG_DATE_TIME_FORMAT, 
    SHORT_TIME_FORMAT, MEDIUM_TIME_FORMAT, LONG_TIME_FORMAT;
  };
  
  public enum NumberColumnFormat {
    PATTERN, SCIENTIFIC_FORMAT, CURRENCY_FORMAT, PERCENT_FORMAT, DECIMAL_FORMAT; 
  };
  
  public @interface ColumnDefinition {
    int column();
    String header();
    boolean filterable() default true;
    boolean sortable() default true;
    boolean editable() default false;
    DateColumnFormat dateTimeFormat() default DateColumnFormat.SHORT_DATE_FORMAT; 
    String dateTimePattern() default "";
    NumberColumnFormat numberFormat() default NumberColumnFormat.DECIMAL_FORMAT; 
    String numberPattern() default "";
  }
}
