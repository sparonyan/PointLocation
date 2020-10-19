package edu.uw.bothell.css.dsl.MASS.PointLocation;

import java.io.Serializable;

public class ArgsForAgent implements Serializable {
    int nextCell = -1;
    Point query = null;
    Trapezoid result = null;

    public ArgsForAgent(int cell, Point point, Trapezoid result) {
        this.nextCell = cell;
        this.query = point;
        this.result = result;
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
