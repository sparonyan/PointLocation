package edu.uw.bothell.css.dsl.MASS.PointLocation;

import edu.uw.bothell.css.dsl.MASS.MASS;
import edu.uw.bothell.css.dsl.MASS.Place;

public class Cell extends Place {

    public static final int INIT_CELL = 0;
    public static final int LOCATE_POINT = 1;

    public static final int SET_RESULT = 2;
    public static final int GET_RESULT = 3;

    public static final int SET_VISITED = 4;
    public static final int GET_VISITED = 5;

    public static final int GET_MYIDX = 6;
    public static final int GET_SIZE = 7;
    public static final int NEIGHBORS = 8;


    private Trapezoid trapezoid;
    private int isVisited; //1->true, 0->false
    private Trapezoid result;
    private int size; //size of places array
    private int myIdx;

    public Cell(Object trapezoid) {
        this.trapezoid = (Trapezoid)trapezoid;
        MASS.getLogger( ).debug("Place" + getIdx() + ": created --> " + this.getIndex( )[0]  );
    }


    public Object callMethod(int func, Object args) {
        switch (func) {
            case INIT_CELL: return init(args);
            case SET_VISITED: setVisited(args);
            case SET_RESULT: setResult(args);
            case LOCATE_POINT: return locatePoint(args);

            case GET_VISITED: return getIsVisited();
            case GET_RESULT: return getResult();
            case GET_MYIDX: return getIdx();
            case GET_SIZE: return getPlacesSize();
            case NEIGHBORS: return getNeighbors();

        }
        return null;
    }



    // args in the string form "size myIdx isVisited"
    public Object init(Object args) {
        String[] arr = ((String)args).split(" ");
        this.size = Integer.parseInt(arr[0]);
        this.myIdx = Integer.parseInt(arr[1]);
        this.isVisited = Integer.parseInt(arr[2]);
        return null;
    }

    public void setVisited(Object flag) {
        this.isVisited = (Integer) flag;
    }

    public Object getIsVisited() {
        return this.isVisited;
    }

    public void setResult(Object trapezoid) {
        this.result = (Trapezoid)trapezoid;
    }

    public Object getResult() {
        return this.result;
    }

    // return true if query point within this cell's trapezoid, false otherwise
    public Object locatePoint(Object queryP) {
        return trapezoid.insideTrapezoid((Point)queryP);
    }

    public Object getPlacesSize() {
        return this.size;
    }

    public Object getIdx() {
        return this.myIdx;
    }

    public Object getNeighbors() {
        return this.trapezoid.getNeighbors();
    }


}
