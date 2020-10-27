package edu.uw.bothell.css.dsl.MASS.PointLocation;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * edu.uw.bothell.css.dsl.MASS.PointLocation.ReadInFile.java
 * Project: edu.uw.bothell.css.dsl.MASS.PointLocation.PointLocation
 * University of Washington Bothell, Distributed Systems Laboratory
 * Autumn 2020
 * @author Satine Paronyan
 */
public class ReadInFile implements Serializable {
    public static List<Trapezoid> getTrapsList(String inFile)  {

        List<Trapezoid> res = new ArrayList<Trapezoid>();

        File in = new File(inFile);
        try {
            Scanner read = new Scanner(in);
            String line;

            while (read.hasNext()) {

                line = read.nextLine();
                String[] arr = line.split(";");

                int index;
                Point leftTop;
                Point leftBot;
                Point rightTop;
                Point rightBot;
                List<Integer> neighbors = new ArrayList<Integer>();
                List<Integer> segments = new ArrayList<Integer>();

                index = Integer.parseInt( arr[0].substring(1) );
                int idx = arr[1].indexOf(" ");
                double x = Integer.parseInt( arr[1].substring(0, idx) );
                double y = Integer.parseInt( arr[1].substring(idx+1) );
                leftTop = new Point(x, y);

                idx = arr[2].indexOf(" ");
                x = Integer.parseInt( arr[2].substring(0, idx) );
                y = Integer.parseInt( arr[2].substring(idx+1) );
                leftBot = new Point(x, y);

                idx = arr[3].indexOf(" ");
                x = Integer.parseInt( arr[3].substring(0, idx) );
                y = Integer.parseInt( arr[3].substring(idx+1) );
                rightTop = new Point(x, y);

                idx = arr[4].indexOf(" ");
                x = Integer.parseInt( arr[4].substring(0, idx) );
                y = Integer.parseInt( arr[4].substring(idx+1) );
                rightBot = new Point(x, y);

                String[] neighb = arr[5].split(" ");
                for (String el: neighb) {
                    neighbors.add(Integer.parseInt( el.substring(1) ));
                }

                String[] segm = arr[6].split(" ");
                for (String el : segm) {
                    if (el.length() != 0 && !el.equals("0")) {
                        segments.add(Integer.parseInt(el.substring(1)));
                    }
                }

                //  int index, Point leftTop, Point leftBot, Point rightTop, Point rightBot,
                //  List<Integer> neighbors, List<Integer> segments
                Trapezoid trap = new Trapezoid(index, leftTop, leftBot, rightTop, rightBot,
                                                neighbors, segments);
                res.add(trap);

            }

        } catch (IOException e) {
            System.out.println("Could not read file: " + inFile);
            e.printStackTrace();
        }
        return res;
    }
}
