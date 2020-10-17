package edu.uw.bothell.css.dsl.MASS.PointLocation;

import java.io.Serializable;

/**
 * edu.uw.bothell.css.dsl.MASS.PointLocation.Point.java
 * Project: edu.uw.bothell.css.dsl.MASS.PointLocation.Point Location
 * University of Washington Bothell, Distributed Systems Laboratory
 * Autumn 2020
 * @author Satine Paronyan
 */

public class Point implements Serializable {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    // Overriding equals() to compare two Point2D objects
    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        // Check if o is an instance of edu.uw.bothell.css.dsl.MASS.PointLocation.Point or not
        // "null instanceof [type]" also returns false
        if (!(o instanceof Point)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Point c = (Point) o;

        // Compare the data members and return accordingly
        //return Integer.compare(x, c.x) == 0 && Integer.compare(y, c.y) == 0;
        return (x == ((Point) o).x && y == ((Point) o).y);
    }


    @Override
    public int hashCode() {
        int result = 373;
        result = 37 * result + (int)x;
        result = 37 * result + (int)y;
        return result;
    }

    @Override
    public String toString(){
        return "[" + x + "," + y + "]";
    }
}