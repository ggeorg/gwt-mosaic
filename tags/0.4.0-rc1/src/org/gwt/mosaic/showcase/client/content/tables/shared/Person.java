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
package org.gwt.mosaic.showcase.client.content.tables.shared;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class Person {
  private String name;
  private String gender;
  private Boolean isMarried = false;

  public Person(String name, String gender, boolean isMarried) {
    this.name = name;
    this.gender = gender;
    this.isMarried = isMarried;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public boolean isMarried() {
    return isMarried;
  }

  public void setIsMarried(boolean isMarried) {
    this.isMarried = isMarried;
  }

  @Override
  public String toString() {
    return getName() + " " + getGender() + " " + String.valueOf(isMarried());
  }
}
