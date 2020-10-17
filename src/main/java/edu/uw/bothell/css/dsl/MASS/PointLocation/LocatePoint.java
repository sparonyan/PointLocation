package edu.uw.bothell.css.dsl.MASS.PointLocation;

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



    }
}
