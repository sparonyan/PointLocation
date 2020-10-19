package edu.uw.bothell.css.dsl.MASS.PointLocation;

import edu.uw.bothell.css.dsl.MASS.Agent;
import edu.uw.bothell.css.dsl.MASS.MASS;
import edu.uw.bothell.css.dsl.MASS.annotations.OnArrival;
import edu.uw.bothell.css.dsl.MASS.annotations.OnCreation;
import edu.uw.bothell.css.dsl.MASS.annotations.OnMessage;
import edu.uw.bothell.css.dsl.MASS.messaging.MessageDestination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Crawler extends Agent implements Serializable {
    //List<Integer> visitedCells = new ArrayList<>();
    //List<Integer> myChildren = new ArrayList<>();
    int nextCell;
    Point query;
    Trapezoid result;

    public Crawler( Object arg) {
        ArgsForAgent arguments = (ArgsForAgent)arg;
        nextCell = arguments.getNextCell();
        query = arguments.getQuery();
        result = arguments.getResult();

        MASS.getLogger( ).debug( "==== Agent [" + getAgentId( ) + "] created and instructed to go to ["
                                + nextCell + "] with query point [" + query.getX() + "," + query.getY() + "]" );
    }


    public static int map(int maxAgents, int[] size, int[] coordinates) {
        
    }

    @OnCreation
    public void initCrawler() {
        MASS.getLogger( ).debug( "==== Initializing agent [" + getAgentId( ) + "] and migrating to [" + nextCell + "]" );
        // migrate to the cell specified during construction
        int[] next = {0, nextCell};
        migrate(next);
        //migrate(nextCell);

    }


    @OnArrival
    public void onArrival() {
        Cell cell = (Cell) getPlace();
        MASS.getLogger( ).debug( "==== Agent [" + getAgentId( ) + "] has arrived at [" + getPlace( ).getIndex( )[1] + "]" );
        if ( nextCell != cell.getIndex( )[1] ) { return; }//not arrived yet

        if (cell.getVisited()) {
            MASS.getLogger( ).debug( "==== Agent [" + getAgentId( ) + "] terminated onArrival at visited cell ["
                                    + getPlace( ).getIndex( )[1] + "]" );

            if (this.result != null && getPlace( ).getIndex( )[1] == 0) {
                // arrived to write the result
                cell.setResult(this.result);
            }
            kill();
            return;
        }

        cell.setVisited(1);

        //visitedCells.add(cell.getIndex()[1]);
        //nextCell = -1; //reset

        Boolean isPoint = (Boolean)cell.locatePoint(query);

        if (isPoint) {
            // found trapezoid containing query point
            this.result = (Trapezoid)cell.getTrapezoid();

            // Send a message to ALL Agents that this agent found the query point
            MASS.getMessagingProvider().sendAgentMessage( MessageDestination.ALL_AGENTS, "Found!" );
            int[] next = {0, 0};
            migrate(next); // migrate to cell #1 to write the result
        }

        Trapezoid curTrap = (Trapezoid)cell.getTrapezoid();
        List<Integer> neigb = curTrap.getNeighbors();

        // if current trapezoid does not have neighbors try to go to the next cell.
        // This might be firs agent at the first cell
        if (neigb.size() == 0) {
            nextCell++;
            migrate(0, nextCell);

        } else {
            nextCell = neigb.get(0);
            List<ArgsForAgent> args = new LinkedList<>();
            for (int i = 1; i < neigb.size(); i++) {
                //Integer[] args = {neigb.get(i), (int)query.getX(), (int)query.getY()};
                args.add(new ArgsForAgent(neigb.get(i), query, result));
            }
            if (args.size() > 0) {
                spawn(args.size(), args.toArray());
            }
            migrate(0, nextCell);
        }


    }


    @OnMessage
    public void receiveMessage( String msg ) {
        // other agent found the query point
        if (msg.equals("Found!")) {
            kill(); // my mission end here
        }
    }



}
