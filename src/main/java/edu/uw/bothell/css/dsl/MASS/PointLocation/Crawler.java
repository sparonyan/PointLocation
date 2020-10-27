package edu.uw.bothell.css.dsl.MASS.PointLocation;

import edu.uw.bothell.css.dsl.MASS.Agent;
import edu.uw.bothell.css.dsl.MASS.MASS;
import edu.uw.bothell.css.dsl.MASS.annotations.OnArrival;
import edu.uw.bothell.css.dsl.MASS.annotations.OnCreation;
import edu.uw.bothell.css.dsl.MASS.annotations.OnMessage;
import edu.uw.bothell.css.dsl.MASS.messaging.MessageDestination;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * edu.uw.bothell.css.dsl.MASS.PointLocation.Crawler.java
 * Project: edu.uw.bothell.css.dsl.MASS.PointLocation.PointLocation
 * University of Washington Bothell, Distributed Systems Laboratory
 * Autumn 2020
 * @author Satine Paronyan
 */
public class Crawler extends Agent implements Serializable {

    int nextCell;
    Point query;
    Trapezoid result;
    int originalPlace;
    int placesSize;

    public Crawler( Object arg) {
        ArgsForAgent arguments = (ArgsForAgent)arg;
        nextCell = arguments.getNextCell();
        query = arguments.getQuery();
        result = arguments.getResult();
        this.originalPlace = arguments.original;
        this.placesSize = arguments.placesSize;

        MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] created and instructed to go to ["
                                + nextCell + "] with query point [" + query.getX() + "," + query.getY() + "]" );
    }


    @OnCreation
    public void initCrawler() {
        MASS.getLogger( ).debug( "####### Initializing agent [" + getAgentId( ) + "] and migrating to [" + nextCell + "]" );

        // migrate to the cell specified during construction
        migrate(nextCell);

    }


    @OnArrival
    public void onArrival() {
        Cell cell = (Cell) getPlace();
        int placeIdx = getPlace( ).getIndex( )[0];
        if ( nextCell != placeIdx ) { return; }//not arrived yet

        MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] has arrived at [" + placeIdx + "]" );


        if ( (Integer) cell.getIsVisited() == 1 ) {

            MASS.getLogger( ).debug( "####### Place is Visited. Agent [" + getAgentId( ) + "] KILLs itself at cell ["
                    + placeIdx + "]" );
            kill();
            return;
        }

        cell.setIsVisited(1);
        nextCell = -1;

        Boolean isPoint = (Boolean)cell.locatePoint(query);

        if (isPoint) {

            MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] found RESULT at place ["
                    + placeIdx + "]" );


            // found trapezoid containing query point
            this.result = (Trapezoid)cell.getTrapezoid();

            // Send a message to ALL Agents, that this agent found the query point
            MASS.getMessagingProvider().sendAgentMessage( MessageDestination.ALL_AGENTS, "Found!" );

            MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] sent MSG from place ["
                    + placeIdx + "]" );

            /// TODO potentially the result should be written always at place with index 0
            // but for now, no need to migrate, just write the result where it is found
            cell.setResult(this.result);

            MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] writes RESULT & KILLS itself at place ["
                    + placeIdx + "]" );
            kill();
            return;
        }

        Trapezoid curTrap = (Trapezoid)cell.getTrapezoid();
        List<Integer> neigb = curTrap.getNeighbors();

        // If current trapezoid does not have neighbors try to go to the next cell.
        // This might be firs agent at the first cell
        if (neigb.size() == 0 && placesSize > 1) {
            nextCell++;
            migrate(nextCell);

        } else if (neigb.size() > 0) {
            nextCell = neigb.get(0) - 1;    // subtract 1 to match index in places array
            List<ArgsForAgent> args = new LinkedList<>();

            for (int i = 1; i < neigb.size(); i++) {
                args.add(new ArgsForAgent(neigb.get(i)-1, query, null, this.originalPlace, this.placesSize));
            }

            int len = args.size();
            if ( len > 0 && this.result==null) {
                MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] spawns " + len + " children at place ["
                        + placeIdx + "]" );
                spawn(len, args.toArray());
            }
            migrate( nextCell);
        }

        if (nextCell == -1) {
            kill();
            return;
        }
    }


    @OnMessage
    public void receiveMessage( String msg ) {
        // its me who sent the message
        if (result != null) { return; }

        // other agents terminate
        if (msg.equals("Found!")) {

            MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] received MSG and KILLS itself" );
            kill(); // my mission ends here
        }
    }

}
