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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A representation of a college student.
 */
public class Student implements IsSerializable {
  /**
   * The number of fields in this class.
   */
  public static final int NUM_FIELDS = 12;

  private String firstName = null;
  private String lastName = null;
  private int age = 0;
  private boolean isMale = true;
  private String race = null;
  private String favoriteColor = null;
  private String favoriteSport = null;
  private String college = null;
  private int graduationYear = 0;
  private double gpa = 0.0;
  private int id = 0;
  private int pin = 0;

  public int getAge() {
    return age;
  }

  public String getCollege() {
    return college;
  }

  public String getFavoriteColor() {
    return favoriteColor;
  }

  public String getFavoriteSport() {
    return favoriteSport;
  }

  public String getFirstName() {
    return firstName;
  }

  public double getGpa() {
    return gpa;
  }

  public int getGraduationYear() {
    return graduationYear;
  }

  public int getId() {
    return id;
  }

  public String getLastName() {
    return lastName;
  }

  public int getPin() {
    return pin;
  }

  public String getRace() {
    return race;
  }

  public boolean isMale() {
    return isMale;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public void setCollege(String college) {
    this.college = college;
  }

  public void setFavoriteColor(String favoriteColor) {
    this.favoriteColor = favoriteColor;
  }

  public void setFavoriteSport(String favoriteSport) {
    this.favoriteSport = favoriteSport;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setGpa(double gpa) {
    this.gpa = gpa;
  }

  public void setGraduationYear(int graduationYear) {
    this.graduationYear = graduationYear;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setMale(boolean isMale) {
    this.isMale = isMale;
  }

  public void setPin(int pin) {
    this.pin = pin;
  }

  public void setRace(String race) {
    this.race = race;
  }
}
