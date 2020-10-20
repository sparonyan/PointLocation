package edu.uw.bothell.css.dsl.MASS.PointLocation;

import edu.uw.bothell.css.dsl.MASS.Agent;
import edu.uw.bothell.css.dsl.MASS.MASS;
import edu.uw.bothell.css.dsl.MASS.Place;
import edu.uw.bothell.css.dsl.MASS.Places;
import edu.uw.bothell.css.dsl.MASS.annotations.OnArrival;
import edu.uw.bothell.css.dsl.MASS.annotations.OnCreation;
import edu.uw.bothell.css.dsl.MASS.annotations.OnMessage;
import edu.uw.bothell.css.dsl.MASS.messaging.MessageDestination;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

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
        MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] has arrived at [" + getPlace( ).getIndex( )[0] + "]" );
        if ( nextCell != cell.getIndex( )[0] ) { return; }//not arrived yet

        MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] has arrived at [" + getPlace( ).getIndex( )[0] + "] and has RESUlt " + (result != null) );

        if ( (Integer) cell.getIsVisited() == 1 ) {
            if (this.result != null && getPlace( ).getIndex( )[0] == originalPlace) {
                MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] wrote the result at cell ["
                        + getPlace( ).getIndex( )[0] + "]" );

                // arrived to write the result
                cell.setResult(this.result);
            }

            MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] KILLs itself at cell ["
                    + getPlace( ).getIndex( )[0] + "]" );
            kill();
            return;
        }

        cell.setIsVisited(1);

        Boolean isPoint = (Boolean)cell.locatePoint(query);

        if (isPoint) {
            // Send a message to ALL Agents, that this agent found the query point
            MASS.getMessagingProvider().sendAgentMessage( MessageDestination.ALL_AGENTS, "Found!" );

            // found trapezoid containing query point
            this.result = (Trapezoid)cell.getTrapezoid();

            MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] found RESULT at place ["
                    + getPlace( ).getIndex( )[0] + "]" );

            int placeIdx = getPlace( ).getIndex( )[0];

            /// TODO
            // no need to migrate, just write the result
            cell.setResult(this.result);

            MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] writes RESULT & KILLS itself at place ["
                    + placeIdx + "]" );
            kill();
            return;

            /*if (placeIdx == originalPlace) {
                // no need to migrate, just write the result
                cell.setResult(this.result);

                MASS.getLogger( ).debug( "####### Agent [" + getAgentId( ) + "] KILLS itself at place ["
                        + placeIdx + "]" );
                kill();
                return;
            }

            nextCell = originalPlace;
            migrate(nextCell); // migrate to cell #1 to write the result
             */
        }

        Trapezoid curTrap = (Trapezoid)cell.getTrapezoid();
        List<Integer> neigb = curTrap.getNeighbors();

        // If current trapezoid does not have neighbors try to go to the next cell.
        // This might be firs agent at the first cell
        if (neigb.size() == 0 && placesSize > 1) {
            nextCell++;
            migrate(nextCell);

        } else if (neigb.size() == 0) {
            kill();
            return;
        }else {
                nextCell = neigb.get(0) - 1;    // subtract 1 to match index in places array
                List<ArgsForAgent> args = new LinkedList<>();

                for (int i = 1; i < neigb.size(); i++) {
                    args.add(new ArgsForAgent(neigb.get(i), query, result, this.originalPlace, this.placesSize));
                }

                if (args.size() > 0) {
                    spawn(args.size(), args.toArray());
                }
                migrate( nextCell);
        }
    }



    @OnMessage
    public void receiveMessage( String msg ) {
        // its me who sent the message
        if (result != null) { return; }

        // other agents terminate
        if (msg.equals("Found!")) {
            kill(); // my mission end here
        }
    }

}
