package cz.cvut.fel.minimalSpanningTree.core;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import cz.cvut.fel.minimalSpanningTree.algorithm.implementation.BoruvkaMST;
import cz.cvut.fel.minimalSpanningTree.algorithm.implementation.EuclideanMST;
import cz.cvut.fel.minimalSpanningTree.algorithm.implementation.FredmanTarjanFibHeapMST;
import cz.cvut.fel.minimalSpanningTree.algorithm.implementation.KruskalMST;
import cz.cvut.fel.minimalSpanningTree.algorithm.implementation.PrimMST;
import cz.cvut.fel.minimalSpanningTree.algorithm.implementation.PrimPfsMST;
import cz.cvut.fel.minimalSpanningTree.algorithm.implementation.ReverseDeleteMST;
import cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.GraphAdjList;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.GraphAdjListAdv;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.GraphAdjMatrix;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.GraphAdjMatrixAdv;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.PointDT;

/**
 * SEM DOPLNIM ESTE ANGLICKY ABSTRACT
 *
 * @author Shaki
 * @since October 2008
 */
public class Application {
    /**
     * log4j variable for logging purposes
     */
    static Logger logger;

    /**
     * The starting point for the Minimal Spanning Trees applications
     *
     * @param args <algorithm> <representation> <filename>
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {

        // PropertyConfigurator.configure("log4j.properties");
        DOMConfigurator.configure("log4j.xml");
        logger = Logger.getLogger(Application.class.getName());
        logger.info("Application started.");

        // check for the correct number of input parameters
        if (args.length != 3) {
            logger.error("Incorrect number of input parameters!");
            logger.info("Usage: java MSTalgos <algorithm> <representation> <filename>");
            logger.info ("Alghorithms:");
            logger.info ("\t  p Prim's algorithm");
            logger.info ("\t  k Kruskal's algorithm");
            logger.info ("\t  r Reverse-delete algorithm");
            logger.info ("\t  b Boruvka's algorithm");
            logger.info ("\t  f Prim's priority queue algorithm");
            logger.info ("\t  e Euclidean MST algorithm");
            logger.info ("\t  t Tarjan/Fredman algorithm (Fibonacci heap)");
            logger.info ("Representations:");
            logger.info ("\t  m Adjacency matrix");
            logger.info ("\t  l Adjacency list");
            System.exit(0);
        }

        DataStructures G;

        /**
         * choose representation
         */
        String representation = args[1];
        if (representation.compareTo("m") == 0) { //adjacency matrix
            G = new GraphAdjMatrix();
        }
        else if (representation.compareTo("m2") == 0) { // quicker adjacency matrix
            G = new GraphAdjMatrixAdv();
        }
        else if (representation.compareTo("l") == 0) { // adjacency list
            G = new GraphAdjList();
        }
        else if (representation.compareTo("l2") == 0) {
            G = new GraphAdjListAdv();
        }
        else
            G = new GraphAdjListAdv();          // quicker adjacency list

        @SuppressWarnings("unused")
        InputData id = new InputData("input/" + args[2], G);

        OutputData od = new OutputData(representation, args[2]);
        Edge[] mst;

        // testovaci vypis grafu zo struktury do suboru checkGraph.txt
        // od.printStructToFile(G);

        long start;
        long stop;

        /**
         * choose algorithm
         */
        String algorithm = args[0];
        if (algorithm.compareTo("p") == 0) { // Prim's algorithm
            logger.info("PRIM's algorithm started");

            start = System.currentTimeMillis();
            PrimMST pMST = new PrimMST(G);
            mst = pMST.PrimMSTalg(representation);
            stop = System.currentTimeMillis();

            od.printToFile(mst, "PRIM's algorithm", stop-start);
            logger.info("PRIM's algorithm finished");
        }
        if (algorithm.compareTo("k") == 0) { // Kruskal's algorithm
            logger.info("KRUSKAL's algorithm started");

            start = System.currentTimeMillis();
            KruskalMST kMST = new KruskalMST(G);
            mst = kMST.KruskalMSTalg();
            stop = System.currentTimeMillis();

            od.printToFile(mst, "KRUSKAL's algorithm", stop-start);
            logger.info("KRUSKAL's algorithm finished");
        }

        if (algorithm.compareTo("r") == 0) { // Reverse-delete algorithm
            logger.info("Reverse-delete algorithm started");
            start = System.currentTimeMillis();
            ReverseDeleteMST rdMST = new ReverseDeleteMST(G);
            mst = rdMST.ReverseDeleteMSTalg();
            stop = System.currentTimeMillis();

            od.printToFile(mst, "Reverse-delete algorithm", stop-start);
            logger.info("Reverse-delete algorithm finished");
        }

        if (algorithm.compareTo("b") == 0) { // Boruvka algorithm
            logger.info("BORUVKA's algorithm started");
            start = System.currentTimeMillis();
            BoruvkaMST bMST = new BoruvkaMST(G);
            mst = bMST.BoruvkaMSTalg();
            stop = System.currentTimeMillis();

            od.printToFile(mst, "BORUVKA's algorithm", stop-start);
            logger.info("BORUVKA's algorithm finished");
        }

        if (algorithm.compareTo("f") == 0) { // Prim's priority queue algorithm
            logger.info("PRIM's priority queue algorithm started");
            start = System.currentTimeMillis();
            PrimPfsMST pMSTpf = new PrimPfsMST(G);
            mst = pMSTpf.PrimPfsalg();
            stop = System.currentTimeMillis();

            od.printToFile(mst, "PRIM's PQ algorithm", stop-start);
            logger.info("PRIM's priority queue algorithm finished");
        }

        if (algorithm.compareTo("t") == 0) { // Fredman/Tarjan Fibonacci heaps algorithm

            logger.info("Fredman & Tarjan's Fibonacci heap algorithm started");
            start = System.currentTimeMillis();
            FredmanTarjanFibHeapMST ftMST = new FredmanTarjanFibHeapMST(G);
            mst = ftMST.FredmanTarjanFibHeapMSTalg();
            stop = System.currentTimeMillis();

            od.printToFile(mst, "Fredman Tarjan Fibonacci heap", stop-start);
            logger.info("Fredman & Tarjan's Fibonacci heap algorithm finished");
        }

        if (algorithm.compareTo("e") == 0) { // Euclidean MST algorithm
            logger.info("Euclidean MST with KRUSKAL started");

            start = System.currentTimeMillis();
            EuclideanMST eMST = new EuclideanMST(G);
            Vector<PointDT> DT = eMST.computeDT();
            stop = System.currentTimeMillis();
            @SuppressWarnings("unused")
            long part1 = stop - start;

            for (int i = 0; i < DT.size(); i++) {
                logger.debug(DT.elementAt(i).getIndex() + "[" +
                            DT.elementAt(i).getCoordX() + "," +
                            DT.elementAt(i).getCoordY() + "]" +
                            " - " +
                            DT.elementAt(i+1).getIndex() + "[" +
                            DT.elementAt(i+1).getCoordX() + "," +
                            DT.elementAt(i + 1).getCoordY() + "]");
                i++;
            }

            /**
             * vytvorim si graph EG
             * obsahuje uz len hrany po delaunayovej triangulacii
             */
            GraphAdjList EG = new GraphAdjList();
            EG.setNumVertices(G.getNumVertices());


            Vector<PointDT> ver = new Vector<PointDT>();
            ver = G.getAllVertices();

            for (int i = 0; i < ver.size(); i++) {
                EG.addVertex(ver.elementAt(i).getIndex(),
                             ver.elementAt(i).getCoordX(),
                             ver.elementAt(i).getCoordY());
            }

            for (int i = 0; i < DT.size(); i++) {
                int sv = DT.elementAt(i).getIndex();
                int dv = DT.elementAt(i + 1).getIndex();

                /**
                 * The distance between two points <Ax,Ay> and <Bx,By > can be found
                 * using the pythagorus theorem: dx = Ax-Bx dy = Ay-By distance =
                 * sqrt(dx*dx + dy*dy)
                 */
                int dx = DT.elementAt(i).getCoordX() - DT.elementAt(i + 1)
                        .getCoordX();
                int dy = DT.elementAt(i).getCoordY() - DT.elementAt(i + 1)
                        .getCoordY();
                double length = Math.sqrt(dx * dx + dy * dy);

                EG.addEdge(sv, dv, length);
                i++;
            }
            EG.setNumEdges(EG.countNumEdges());

            start = System.currentTimeMillis();
            KruskalMST kMST = new KruskalMST(EG);
            mst = kMST.KruskalMSTalg();
            stop = System.currentTimeMillis();

            //od.printToFile(mst, "Euclidean MST with KRUSKAL", part1 + (stop - start));
            od.printToFile(mst, "Euclidean MST with KRUSKAL", stop - start);
            logger.info("Euclidean MST with KRUSKAL finished");
        }

    }
}
