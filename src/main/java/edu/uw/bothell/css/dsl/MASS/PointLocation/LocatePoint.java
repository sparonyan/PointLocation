package edu.uw.bothell.css.dsl.MASS.PointLocation;

import edu.uw.bothell.css.dsl.MASS.Agents;
import edu.uw.bothell.css.dsl.MASS.MASS;
import edu.uw.bothell.css.dsl.MASS.Place;
import edu.uw.bothell.css.dsl.MASS.Places;
import edu.uw.bothell.css.dsl.MASS.logging.LogLevel;

import java.util.ArrayList;
import java.util.List;

public class LocatePoint {
    private static final String NODE_FILE = "nodes.xml";


    public static void main(String[] args) {
        // Read and validate input parameters
        if ( args.length != 1 ) {
            System.err.println( "args = " + args.length + " should be 1: infile.txt" );
            System.exit( -1 );
        }

        String inFile = args[0];
        List<Trapezoid> traps = new ArrayList<Trapezoid>();
        traps = ReadInFile.getTrapsList(inFile);

        // init MASS library
        MASS.setNodeFilePath( NODE_FILE );
        MASS.setLoggingLevel( LogLevel.DEBUG );
        //MASS.setLoggingLevel( LogLevel.OFF );

        // start MASS
        MASS.init();

        Places places = new Places(1, Cell.class.getName(), null, 1, traps.size());
        MASS.getLogger( ).debug("==== Places size is " + places.getPlacesSize() );
        System.out.println("==== Point Location: Places of dimension: "+ 0 +
                            " x " +traps.size() +" created!");

        places.callAll(Cell.INIT_CELL, traps);


        ArgsForAgent arguments = new ArgsForAgent(0, new Point(140, 120), null);
        Agents agents = new Agents(2, Crawler.class.getName(), arguments, places, 1);

        agents.doWhile( ()->agents.hasAgents() );

        Place[] placesList = places.getPlaces();
       // Cell first = (Cell)placesList[0];
       // Trapezoid result = (Trapezoid)first.getResult();
       /* if (result != null) {
            MASS.getLogger( ).debug("====== RESULT IS trapezoid [" + result.getIndex() + "]");
        }
        */
        MASS.finish( );
    }
}
