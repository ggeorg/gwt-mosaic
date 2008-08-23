/*
 * Copyright 2008 Google Inc.
 * Copyright 2008 Georgios J. Georgopoulos
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
package org.mosaic.showcase.client.pages;

/**
 * The static set of data used to populate the grid.
 */
public abstract class DataSourceData {
  public static final String[] colleges = {
      "Georgia", "Connecticut", "Arizona", "Florida", "Wisconsin", "Maryland",
      "Tennessee", "Arkansas", "Virginia", "Maine", "Texes", "South Carolina"};

  public static final String[] colors = {
      "red", "blue", "green", "orange", "purple"};

  public static final String[] firstNames = {
      "Arnold", "Bob", "Cathy", "Dennis", "Earl", "Frank", "George", "Hillary",
      "Irma", "John", "Kelly", "Michelle", "Natalie", "Oscar", "Paul", "Susan",
      "Todd"};

  public static final String[] genders = {"male", "female"};

  public static final String[] lastNames = {
      "Anderson", "Billings", "Corper", "Donavon", "Elkins", "Fitzgerald",
      "Gaskins", "Haskins", "Iterby", "Johnson", "Kellickson", "Lee",
      "Peterson", "Richardson", "Sauxby", "Wilkinson", "York"};

  public static final String[] races = {
      "African American", "Caucasian", "Hispanic", "Green Alien", "Asian",
      "Other"};

  public static final String[] sports = {
      "baseball", "soccor", "football", "basketball", "hockey", "softball",
      "rugby", "water polo", "golf", "tennis"};

  /**
   * Return the contents of a cell.
   */
  public String getCell(int rowNum, int colNum) {
    switch (colNum) {
      case 0:
        // First name
        return nextValue(firstNames);
      case 1:
        // Last name
        return nextValue(lastNames);
      case 2:
        // Age
        return getRandomInt(100) + "";
      case 3:
        // Gender
        return nextValue(genders);
      case 4:
        // Race
        return nextValue(races);
      case 5:
        // Favorite color
        return nextValue(colors);
      case 6:
        // Preferred Sport
        return nextValue(sports);
      case 7:
        // College
        return "University of " + nextValue(colleges);
      case 8:
        // Graduation year
        return (1990 + getRandomInt(20)) + "";
      case 9:
        // GPA
        String gpa = (1 + getRandomInt(300) / 100.0) + "";
        if (gpa.length() > 4) {
          return gpa.substring(0, 4);
        }
        return gpa;
      case 10:
        // Student ID
        return (1000000 + getRandomInt(8999999)) + "";
      case 11:
        // Pin number
        return (1000 + getRandomInt(8999)) + "";
      default:
        return null;
    }
  }

  /**
   * Generate a random number. Override this with different server and client
   * side implementations.
   * 
   * @param max the max number to return, exclusive
   * @return a random int between 0 (inclusive) and max (exclusive)
   */
  public abstract int getRandomInt(int max);

  /**
   * The a random value from the specified array.
   * 
   * @param array the array to get the value from
   * @return a random value from the array
   */
  private String nextValue(String[] array) {
    return array[getRandomInt(array.length)];
  }

}
