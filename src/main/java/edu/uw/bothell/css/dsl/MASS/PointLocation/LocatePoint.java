package edu.uw.bothell.css.dsl.MASS.PointLocation;

import edu.uw.bothell.css.dsl.MASS.Agents;
import edu.uw.bothell.css.dsl.MASS.MASS;
import edu.uw.bothell.css.dsl.MASS.Places;
import edu.uw.bothell.css.dsl.MASS.logging.LogLevel;

import java.util.List;

/**
 * edu.uw.bothell.css.dsl.MASS.PointLocation.LocatePoint.java
 * Project: edu.uw.bothell.css.dsl.MASS.PointLocation.Point Location
 * University of Washington Bothell, Distributed Systems Laboratory
 * Autumn 2020
 * @author Satine Paronyan
 */
public class LocatePoint {
    private static final String NODE_FILE = "nodes.xml";


    public static void main(String[] args) {
        // Read and validate input parameters
        if ( args.length != 1 ) {
            System.err.println( "args = " + args.length + " should be 1: infile.txt" );
            System.exit( -1 );
        }

        String inFile = args[0];  // input file name
        List<Trapezoid> traps = ReadInFile.getTrapsList(inFile);

        // init MASS library
        MASS.setNodeFilePath( NODE_FILE );
        MASS.setLoggingLevel( LogLevel.DEBUG );
        //MASS.setLoggingLevel( LogLevel.OFF );

        // start MASS
        MASS.init();

        long startTime = System.currentTimeMillis( );

        Places places = new Places(1, Cell.class.getName(), null, traps.size());
        MASS.getLogger( ).debug("####### Places size is " + places.getPlacesSize() );
        System.out.println("####### Point Location: Places of dimension: " + traps.size() +" created!");
        places.callAll(Cell.INIT_CELL, traps);

        long placesInitTime = System.currentTimeMillis( );

        // point is in trapezoid 1
        //ArgsForAgent arguments = new ArgsForAgent(0, new Point(20, 200), null);

        // point is in trapezoid 2
        //ArgsForAgent arguments = new ArgsForAgent(0, new Point(55, 300), null, 0, traps.size());

        // point is in trapezoid 3
        //ArgsForAgent arguments = new ArgsForAgent(0, new Point(80, 120), null, 0, traps.size());

        // point is in trapezoid 9
        ArgsForAgent arguments = new ArgsForAgent(0, new Point(270, 400), null, 0, traps.size());

        // Start by creating only one agent at place with index 0
        Agents agents = new Agents(2, Crawler.class.getName(), arguments, places, 1);

        // Once all agents terminated with kill() the while loop exits
        agents.doWhile(agents::hasAgents);

        System.out.println("####### Exited doWhile !");

        // Time measurement ends
        long graphCreationTime = (placesInitTime - startTime);
        long totalElapsedTime = (System.currentTimeMillis( ) - startTime);
        System.out.println( "####### Graph Generation Time: " + graphCreationTime + " milliseconds" );
        System.err.println( "####### Elapsed Time: " + totalElapsedTime + " milliseconds" );

        // Check if point has been found in any of the trapezoids.
        Object[] placesResults = places.callAll( 3 , new Object[traps.size()] );
        boolean result = false;
        Trapezoid resTrap = null;

        for ( Object obj : placesResults ) {
            if ( (Trapezoid) obj != null ) {
                result = true;
                resTrap = (Trapezoid)obj;
                break;
            }

        }

        if ( result ) {
            System.err.println( "####### RESULT found in trapezoid [" + resTrap.getIndex() + "]" );
        } else {
            System.err.println("####### RESULT Not Found");

        }


        MASS.finish( );
        System.out.println("####### Stopped Mass !");
    }
}
