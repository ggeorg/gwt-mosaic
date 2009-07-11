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

import java.util.ArrayList;
import java.util.List;

/**
 * The static set of data used to populate the grid.
 */
public abstract class StudentGenerator {
  public static final String[] colleges = {
      "Georgia", "Connecticut", "Arizona", "Florida", "Wisconsin", "Maryland",
      "Tennessee", "Arkansas", "Virginia", "Maine", "Texas", "South Carolina"};

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
   * Generate a random {@link Student}.
   * 
   * @return a randomly generated student
   */
  public Student generateStudent() {
    Student student = new Student();
    student.setFirstName(nextValue(firstNames));
    student.setLastName(nextValue(lastNames));
    student.setAge(getRandomInt(100));
    student.setMale(getRandomInt(2) == 1);
    student.setRace(nextValue(races));
    student.setFavoriteColor(nextValue(colors));
    student.setFavoriteSport(nextValue(sports));
    student.setCollege("University of " + nextValue(colleges));
    student.setGraduationYear((1990 + getRandomInt(20)));
    student.setId(1000000 + getRandomInt(8999999));
    student.setPin(1000 + getRandomInt(8999));
    student.setGpa(1 + getRandomInt(300) / 100.0);
    return student;
  }

  /**
   * Generate a {@link List} of random {@link Student Students}.
   * 
   * @param count the number of students to generate
   * @return a list of randomly generated students
   */
  public List<Student> generateStudents(int count) {
    List<Student> students = new ArrayList<Student>();
    for (int i = 0; i < count; i++) {
      students.add(generateStudent());
    }
    return students;
  }

  /**
   * Generate a random number. Override this with different server and client
   * side implementations.
   * 
   * @param max the max number to return, exclusive
   * @return a random int between 0 (inclusive) and max (exclusive)
   */
  protected abstract int getRandomInt(int max);

  /**
   * Get a random value from the specified array.
   * 
   * @param array the array to get the value from
   * @return a random value from the array
   */
  private String nextValue(String[] array) {
    return array[getRandomInt(array.length)];
  }

}