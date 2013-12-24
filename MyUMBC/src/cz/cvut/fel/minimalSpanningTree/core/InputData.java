/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;

/**
 * Reads data from the input file and stores them in temporary data structure.
 *
 * @author Shaki
 * @since October 2008
 */
public class InputData {
    /**
     * log4j variable for logging
     */
    static Logger          logger;

    /**
     * Construct a new input data structure.
     *
     * @param fileName name of the input file.
     * @param G graph representation, can be adjacency matrix or adjacency list
     */
    public InputData(String fileName, DataStructures G) {
        logger = Logger.getLogger(this.getClass());

        try {
            // Open the file that is the first
            FileInputStream fstream = new FileInputStream(fileName);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;

            /**
             * Read File Line By Line
             */
            while ((strLine = br.readLine()) != null) {

                switch (strLine.charAt(0)) {
                    case 'c':  /* it's a comment line, do nothin' */ break;
                    case 'p':
                        StringTokenizer sp = new StringTokenizer(strLine);
                        while (sp.hasMoreTokens()) {
                            sp.nextToken();
                            sp.nextToken();
                            G.setNumVertices(Integer.parseInt(sp.nextToken()));
                            logger.info("Number of vertices: " + G.getNumVertices());
                            G.setNumEdges(Integer.parseInt(sp.nextToken()));
                            logger.info("Number of edges: " + G.getNumEdges());
                            if (sp.hasMoreTokens())
                                logger.info("Density: " + sp.nextToken());
                        }
                        break;
                    case 'v':
                        StringTokenizer st = new StringTokenizer(strLine);
                        while (st.hasMoreTokens()) {
                            st.nextToken();     // just to read first token
                            // index-label of vertex
                            int index = Integer.parseInt(st.nextToken());
                            // x-coordinate
                            int x = Integer.parseInt(st.nextToken());
                            // y-coordinate
                            int y = Integer.parseInt(st.nextToken());
                            G.addVertex(index, x, y);
                            logger.debug("Added vertex: " + index + "[" + x + "," + y + "]");
                        }
                        break;
                    case 'e':
                        StringTokenizer se = new StringTokenizer(strLine);
                        while (se.hasMoreTokens()) {
                            se.nextToken();     // just to read first token
                            int sv = Integer.parseInt(se.nextToken());
                            int dv = Integer.parseInt(se.nextToken());
                            double le = Double.parseDouble(se.nextToken());
                            G.addEdge(sv, dv, le);
                            logger.debug("Added edge: " + sv + "-" + dv + "(" + le + ")");
                        }
                        break;

                    default: throw new NoSuchElementException();
                }
            }

            //Close the input stream
            in.close();
        } catch (FileNotFoundException e) {
            logger.error("File not found - wrong file name?");
            e.printStackTrace();
            System.exit(-1);
        } catch (NoSuchElementException e) {
            logger.error("Wrong format of input file");
            e.printStackTrace();
            System.exit(-1);
        } catch (Exception e) { //Catch exception if any
            logger.error("Error : " + e.getMessage());
            e.printStackTrace();
        }
    }
}