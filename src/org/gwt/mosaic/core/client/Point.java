/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgopoulos J. Georgios.
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

/**
 * A point represents a location in {@code (x,y)} coordinate space, specified in
 * integer precision.
 * 
 * @author ggeorg
 * 
 */
public class Point implements java.io.Serializable {
  private static final long serialVersionUID = 2172972691509840096L;

  /**
   * Returns the distance between two points.
   * 
   * @param x1 the X coordinate of the first specified point
   * @param y1 the Y coordinate of the first specified point
   * @param x2 the X coordinate of the second specified point
   * @param y2 the Y coordinate of the second specified point
   * @return the distance between the two sets of specified coordinates.
   */
  public static double distance(double x1, double y1, double x2, double y2) {
    x1 -= x2;
    y1 -= y2;
    return Math.sqrt(x1 * x1 + y1 * y1);
  }

  /**
   * Returns the square of the distance between two points.
   * 
   * @param x1 the X coordinate of the first specified point
   * @param y1 the Y coordinate of the first specified point
   * @param x2 the X coordinate of the second specified point
   * @param y2 the Y coordinate of the second specified point
   * @return the square of the distance between the two sets of specified
   *         coordinates.
   */
  public static double distanceSq(double x1, double y1, double x2, double y2) {
    x1 -= x2;
    y1 -= y2;
    return (x1 * x1 + y1 * y1);
  }

  /**
   * The X coordinate of this <code>Point</code>. If no X coordinate is set
   * it will default to 0.
   */
  public int x;

  /**
   * The Y coordinate of this <code>Point</code>. If no Y coordinate is set
   * it will default to 0.
   */
  public int y;

  /**
   * Constructs and initializes a point at the origin (0,&nbsp;0) of the
   * coordinate space.
   */
  public Point() {
    this(0, 0);
  }

  /**
   * Constructs and initializes a point at the specified {@code (x,y)} location
   * in the coordinate space.
   * 
   * @param x the X coordinate of the newly constructed <code>Point</code>
   * @param y the Y coordinate of the newly constructed <code>Point</code>
   */
  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Constructs and initializes a point with the same location as the specified
   * <code>Point</code> object.
   * 
   * @param p a point
   */
  public Point(Point p) {
    this(p.x, p.y);
  }

  /**
   * Returns the distance from this <code>Point</code> to a specified point.
   * 
   * @param px the X coordinate of the specified point to be measured against
   *            this <code>Point</code>
   * @param py the Y coordinate of the specified point to be measured against
   *            this <code>Point</code>
   * @return the distance between this <code>Point</code> and a specified
   *         point.
   */
  public double distance(double px, double py) {
    px -= getX();
    py -= getY();
    return Math.sqrt(px * px + py * py);
  }

  /**
   * Returns the distance from this <code>Point</code> to a specified
   * <code>Point</code>.
   * 
   * @param pt the specified point to be measured against this
   *            <code>Point</code>
   * @return the distance between this <code>Point</code> and the specified
   *         <code>Point</code>.
   */
  public double distance(Point pt) {
    double px = pt.getX() - this.getX();
    double py = pt.getY() - this.getY();
    return Math.sqrt(px * px + py * py);
  }

  /**
   * Returns the square of the distance from this <code>Point2D</code> to a
   * specified point.
   * 
   * @param px the X coordinate of the specified point to be measured against
   *            this <code>Point2D</code>
   * @param py the Y coordinate of the specified point to be measured against
   *            this <code>Point2D</code>
   * @return the square of the distance between this <code>Point2D</code> and
   *         the specified point.
   * @since 1.2
   */
  public double distanceSq(double px, double py) {
    px -= getX();
    py -= getY();
    return (px * px + py * py);
  }

  /**
   * Returns the square of the distance from this <code>Point2D</code> to a
   * specified <code>Point2D</code>.
   * 
   * @param pt the specified point to be measured against this
   *            <code>Point2D</code>
   * @return the square of the distance between this <code>Point2D</code> to a
   *         specified <code>Point2D</code>.
   * @since 1.2
   */
  public double distanceSq(Point pt) {
    double px = pt.getX() - this.getX();
    double py = pt.getY() - this.getY();
    return (px * px + py * py);
  }

  /**
   * Determines whether or not two points are equal. Two instances of
   * <code>Point</code> are equal if the values of their <code>x</code> and
   * <code>y</code> member fields, representing their position in the
   * coordinate space, are the same.
   * 
   * @param obj an object to be compared with this <code>Point</code>
   * @return <code>true</code> if the object to be compared is an instance of
   *         <code>Point</code> and has the same values; <code>false</code>
   *         otherwise.
   */
  public boolean equals(Object obj) {
    if (obj instanceof Point) {
      Point pt = (Point) obj;
      return (x == pt.x) && (y == pt.y);
    }
    return false;
  }

  /**
   * Returns the X coordinate of this <code>Point</code>.
   * 
   * @return the X coordinate of this <code>Point</code>.
   */
  public int getX() {
    return x;
  }

  /**
   * Returns the location of this point.
   * 
   * @return a copy of this point, at the same location
   */
  public Point getXY() {
    return new Point(x, y);
  }

  /**
   * Returns the Y coordinate of this <code>Point</code>.
   * 
   * @return the Y coordinate of this <code>Point</code>.
   */
  public int getY() {
    return y;
  }

  /**
   * Moves this point to the specified location in the {@code (x,y)} coordinate
   * plane. This method is identical with <code>setXY(int,&nbsp;int)</code>.
   * 
   * @param x the X coordinate of the new location
   * @param y the Y coordinate of the new location
   */
  public void move(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Set the X coordinate of this <code>Point</code>.
   * 
   * @param x the X coordinate of this <code>Point</code>.
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Sets the location of the point to the specified location.
   * 
   * @param x the X coordinate of the new location
   * @param y the Y coordinate of the new location
   */
  public void setXY(int x, int y) {
    move(x, y);
  }

  /**
   * Sets the location of the point to the specified location.
   * 
   * @param p a point, the new location for this point
   */
  public void setXY(Point p) {
    setXY(p.x, p.y);
  }

  /**
   * Set the Y coordinate of this <code>Point</code>.
   * 
   * @param y the Y coordinate of this <code>Point</code>.
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * Returns a string representation of this point and its location in the
   * {@code (x,y)} coordinate space. This method is intended to be used only for
   * debugging purposes, and the content and format of the returned string may
   * vary between implementations. The returned string may be empty but may not
   * be <code>null</code>.
   * 
   * @return a string representation of this point
   */
  public String toString() {
    return getClass().getName() + "[x=" + x + ",y=" + y + "]";
  }

  /**
   * Translates this point, at location {@code (x,y)}, by {@code dx} along the
   * {@code x} axis and {@code dy} along the {@code y} axis so that it now
   * represents the point {@code (x+dx,y+dy)}.
   * 
   * @param dx the distance to move this point along the X axis
   * @param dy the distance to move this point along the Y axis
   */
  public void translate(int dx, int dy) {
    this.x += dx;
    this.y += dy;
  }
}
