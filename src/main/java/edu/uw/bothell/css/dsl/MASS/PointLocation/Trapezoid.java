package edu.uw.bothell.css.dsl.MASS.PointLocation;

import edu.uw.bothell.css.dsl.MASS.PointLocation.Point;

import java.io.Serializable;
import java.util.List;

/**
 * edu.uw.bothell.css.dsl.MASS.PointLocation.Trapezoid.java
 * Project: edu.uw.bothell.css.dsl.MASS.PointLocation.Point Location
 * University of Washington Bothell, Distributed Systems Laboratory
 * Autumn 2020
 * @author Satine Paronyan
 */

public class Trapezoid implements Serializable {
    private int index;
    private Point leftTop;
    private Point leftBot;
    private Point rightTop;
    private Point rightBot;
    private List<Integer> neighbors;
    private List<Integer> segments;

    public Trapezoid() {
        this.index = 0;
        this.leftTop = null;
        this.leftBot = null;
        this.rightTop = null;
        this.rightBot = null;
        this.neighbors = null;
        this.segments = null;
    }

    public Trapezoid(int index, Point leftTop, Point leftBot, Point rightTop, Point rightBot,
                     List<Integer> neighbors, List<Integer> segments) {
        this.index = index;
        this.leftTop = leftTop;
        this.leftBot = leftBot;
        this.rightTop = rightTop;
        this.rightBot = rightBot;
        this.neighbors = neighbors;
        this.segments = segments;
    }

    public int getIndex() { return this.index; }

    public Point getLeftTop() {
        return this.leftTop;
    }

    public Point getLeftBot() {
        return this.leftBot;
    }

    public Point getRightTop() {
        return this.rightTop;
    }

    public Point getRightBot() {
        return this.rightBot;
    }

    public List<Integer> getNeighbors() {
        return this.neighbors;
    }

    public List<Integer> getSegments() {
        return this.segments;
    }

    /**
     * Given query point determines whether the query point lies within the this trapezoid.
     * @param query - query point.
     * @return true is the query point lies within this trapezoid, false otherwise.
     */
    public boolean insideTrapezoid(Point query) {
        return insideTriangle(leftTop, rightBot, leftBot, query) ||
                insideTriangle(leftTop, rightTop, rightBot, query);
    }

    private boolean insideTriangle(Point a, Point b, Point c, Point query) {
        double A = area(a, b , c);
        double A1 = area(query, b, c);
        double A2 = area(a, query, c);
        double A3 = area(a, b, query);

        return A == A1 + A2 + A3;
    }

    /*
     * Returns double value representing the area of a triangle.
     */
    private double area(Point a, Point b, Point c) {
        return Math.abs(
                           ( a.getX() * (b.getY() - c.getY()) +
                             b.getX() * (c.getY() - a.getY()) +
                             c.getX() * (a.getY() - b.getY())
                           ) / 2.0 );
    }
}