package edu.uw.bothell.css.dsl.MASS.PointLocation;

import java.io.Serializable;

/**
 * edu.uw.bothell.css.dsl.MASS.PointLocation.ArgsForAgent.java
 * Project: edu.uw.bothell.css.dsl.MASS.PointLocation.PointLocation
 * University of Washington Bothell, Distributed Systems Laboratory
 * Autumn 2020
 * @author Satine Paronyan
 */
public class ArgsForAgent implements Serializable {
    int nextCell = -1;
    Point query = null;
    Trapezoid result = null;
    int original = -1;
    int placesSize = -1;

    public ArgsForAgent(int cell, Point point, Trapezoid result, int original, int placesSize) {
        this.nextCell = cell;
        this.query = point;
        this.result = result;
        this.original = original;
        this.placesSize = placesSize;
    }

    public int getNextCell() {
        return this.nextCell;
    }

    public Point getQuery() {
        return this.query;
    }

    public Trapezoid getResult() {
        return this.result;
    }

    public void setResult(Trapezoid trapezoid) {
        this.result = trapezoid;
    }
}
