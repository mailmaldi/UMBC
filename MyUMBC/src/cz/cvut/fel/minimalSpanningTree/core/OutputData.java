/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.core;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;

import cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.GraphAdjList;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.GraphAdjListAdv;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.GraphAdjMatrix;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.GraphAdjMatrixAdv;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;

/**
 * Writes data (results of MST algorithms) to the output file - name of the
 * output file is same as the name of the input file, with .
 *
 * @author Shaki
 * @since December 2008
 */
public class OutputData {
    /**
     * log4j variable for logging
     */
    static Logger          logger;

    /**
     * output file name
     */
    private String outFile;

    /**
     * graph representation, matrix or list
     */
    private String representation;

    /**
     * Construct a new input data structure.
     *
     * @param representation matrix or list
     * @param fileName name of the output file.
     */
    public OutputData(String representation, String fileName) {
        logger = Logger.getLogger(this.getClass());

        // find the separator between filename and extension
        int sep = fileName.lastIndexOf(".") + 1;
        // get the filename part
        this.outFile = fileName.substring(0,sep);
        // add .out extension
        this.outFile = this.outFile + "out";

        this.representation = representation;
    }

    /**
     * prints results of the MST algorithm to output file
     *
     * @param mst array of minimal spanning tree edges
     * @param algo algorithm that we used to get MST
     * @param time time of computation
     */
    public void printToFile(Edge[] mst, String algo, long time) {

        /**
         * path to the output file
         */
        String outPath ="";

        try {
            if (this.representation.compareTo("m") == 0) { //adjacency matrix
                outPath = "output/matrix/";
            }
            else if (this.representation.compareTo("m2") == 0) { // quicker adjacency matrix
                outPath = "output/matrix2/";
            }
            else if (this.representation.compareTo("l") == 0) { // adjacency list
                outPath = "output/list/";
            }
            else
                outPath = "output/list2/";          // quicker adjacency list


            FileWriter fw = new FileWriter(outPath + this.outFile, true);
            BufferedWriter bw = new BufferedWriter(fw);

            /**
             * length of the minimal spanning tree
             */
            double lengthMST = 0;

            /**
             * Write output to file + compute MST length
             */
            for (int i=0; i<mst.length; i++)
                if (mst[i] != null) {

                    //compute
                    lengthMST = lengthMST + mst[i].getLength();

                    //write
                    /*
                    bw.write("Source: " + mst[i].getSource() +
                          " Destination: " + mst[i].getDestination() +
                          " Length: " + mst[i].getLength()
                    );

                    bw.newLine();
                    */

                    logger.debug("Source: " + mst[i].getSource() +
                             " Destination: " + mst[i].getDestination() +
                             " Length: " + mst[i].getLength());

                }

            bw.write(time + "\t[ms]\t\t" + algo + "\t\t(" + lengthMST + ")" );

            bw.newLine();

            //Close the output stream
            bw.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found - wrong file name?");
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (Exception e) { //Catch exception if any
            System.err.println("Error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
    *
    * @param G graph structure
    */
   public void printStructToFile(DataStructures G) {

       try {
           FileWriter fw = new FileWriter("graphCheck.txt", true);
           BufferedWriter bw = new BufferedWriter(fw);

           bw.write(" Num vertices: " + G.getNumVertices());
           bw.newLine();
           bw.write(" Num edges: " + G.getNumEdges());
           bw.newLine();
           Vector<Edge> edges = G.getAllEdges();

           for (int i=0; i<edges.size(); i++) {
               bw.write(" Source: " + edges.elementAt(i).getSource() +
                        " Destination: " + edges.elementAt(i).getDestination() +
                        " Length: " + edges.elementAt(i).getLength());
               bw.newLine();
           }


           bw.newLine();
           bw.newLine();

           //Close the output stream
           bw.close();
       } catch (FileNotFoundException e) {
           System.err.println("File not found - wrong file name?");
           e.printStackTrace();
           System.exit(-1);
       } catch (IOException e) {
           e.printStackTrace();
           System.exit(-1);
       } catch (Exception e) { //Catch exception if any
           System.err.println("Error : " + e.getMessage());
           e.printStackTrace();
       }
   }
}