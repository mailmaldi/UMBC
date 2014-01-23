/**
 *
 */
package main.java.cz.cvut.fel.minimalSpanningTree.algorithm.implementation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.PointDT;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.TriangleDT;

import org.apache.log4j.Logger;

/**
 * Euclidean algorithm
 * (Delaunay's triangulation + Kruskal's MST algorithm)
 * @author Shaki
 * @since April 2009
 */
public class EuclideanMST {
    static Logger          logger;

    /**
     * Graph representation on which we are searching for MST
     */
    private DataStructures g;

    /**
     * Vector of vertices
     */
    private Vector<PointDT> vertices = new Vector<PointDT>();

    /**
     * Array of vertices for finding the top-left vertex in graph
     */
    private PointDT vertexArray[];

    /**
     * EuclideanMST (Delaunays triangulation + Kruskal's algorithm)
     * @param numVer number of vertices
     * @throws IOException
     * @throws NumberFormatException
     *
     */
    public EuclideanMST(DataStructures G) {
        this.g = G;
        this.vertices = G.getAllVertices();
    }

    /**
     * Delaunays triangulation
     *
     * Runs delaunay's triangulation on graph
     * @return edges after Delaunay's triangulation
     */
    public Vector<PointDT> computeDT() {
        logger = Logger.getLogger(this.getClass());

        /**
         * contains all triangles which exist during and after the triangulation
         */
        Vector<TriangleDT> triangles = new Vector<TriangleDT>();

        /**
         * array of points, two points form an edge
         * index i = starting point of edge
         * index i+1 = endpoint of edge
         */
        Vector<PointDT> edges = new Vector<PointDT>();

        /**
         * Triangulation starts with imaginative triangle of vertices p1,p2,p3
         * which coordinates are far enough from "normal" points to not alter
         * final result of triangulation.
         *
         */

        PointDT p1 = new PointDT(this.g.getNumVertices()+1,-1,-1000);
        PointDT p2 = new PointDT(this.g.getNumVertices()+2,-1,1000);
        PointDT p3 = new PointDT(this.g.getNumVertices()+3,1000,-1);

        /**
         * Find the top-left and bottom-right vertices in the graph
         */

        this.vertexArray = new PointDT[this.g.getNumVertices()];

        for (int i = 0; i < this.vertices.size(); i++)
            this.vertexArray[i] = this.vertices.elementAt(i);

        Arrays.sort(this.vertexArray);

        /**
         * Initialize T as the triangulation consisting
         * of the single triangle p1 p2 p3.
         */

        TriangleDT triangle0 = new TriangleDT(p1,p2,p3);
        triangles.add(triangle0);

        /** Pseudocode
         *  for r=1 to n
         *      do (Insert pr into T)
         *      Find a triangle pi pj pk in T containing pr.
         *      If pr lies in the interior of the triangle pi pj pk
         *          then Add edges from pr to the three vertices of pi pj pk,
         *          thereby splitting pi pj pk into three triangles.
         *          LEGALIZEEDGE(pr; pi pj;T)
         *          LEGALIZEEDGE(pr; pj pk;T)
         *          LEGALIZEEDGE(pr; pk pi;T)
         *      else (pr lies on an edge of pipjpk, say the edge pipj)
         *          Add edges from pr to pk and to the third vertex pl of the
         *          other triangle that is incident to pipj, thereby splitting the
         *          two triangles incident to pipj into four triangles.
         *          LEGALIZEEDGE(pr; pi pl ;T)
         *          LEGALIZEEDGE(pr; pl pj;T)
         *          LEGALIZEEDGE(pr; pj pk;T)
         *          LEGALIZEEDGE(pr; pk pi;T)
         *  Discard p-1 and p-2 with all their incident edges from T.
         *  return T
         *
         */

        /**
         * add all vertices...one at a time
         */
        for (int r=0; r<this.g.getNumVertices(); r++) {



            /**
             * point to be added
             */
            PointDT pToAdd = this.vertexArray[r];

            /**
             * find a triangle pi pj pk in T containing pr
             * =
             * go through all triangles, test if pToAdd lies on the side of the
             * triangle or inside
             *
             */

            for (int i=0; i<triangles.size();i++) {

                TriangleDT newTri;
                TriangleDT tri = triangles.elementAt(i);
                Vector<PointDT> linePoints = new Vector<PointDT>();

                /**
                 * check if point is on the line of triangle
                 */
                linePoints = isPointOnSideOfTriangle(pToAdd, tri);
                if ( linePoints != null) {
                    logger.debug("Point is on SIDE: ");

                    /**
                     * Add edges from pr to pk and to the third vertex pl
                     * of the other triangle that is incident to pipj,
                     * thereby splitting the two triangles incident to
                     * pipj into four triangles.
                     */

                    //2 points of the edge
                    PointDT A = linePoints.elementAt(0);
                    PointDT B = linePoints.elementAt(1);

                    //3 points of triangle
                    PointDT V1 = tri.getV1();
                    PointDT V2 = tri.getV2();
                    PointDT V3 = tri.getV3();

                    /**
                     * delete old triangle and create 2 new
                     */
                    triangles.remove(i);

                    /**
                     * VK is the third point of triangle, first two are set by
                     * the edge (points A and B)
                     */
                    PointDT VK = null;
                    if ( (V1 != A) && (V1 != B) )
                        VK = V1;
                    if ( (V2 != A) && (V2 != B) )
                        VK = V2;
                    if ( (V3 != A) && (V3 != B) )
                        VK = V3;
                    newTri = new TriangleDT(A,pToAdd,VK);
                    triangles.add(newTri);
                    newTri = new TriangleDT(pToAdd,B,VK);
                    triangles.add(newTri);

                    /**
                     * find the other triangle with same edge,
                     * if exists delete and create another 2 new triangles
                     */

                    for (int j=0; j<triangles.size();j++) {

                        if (isTriangleWithSameEdge(linePoints.elementAt(0),linePoints.elementAt(1),triangles.elementAt(j)) != null) {
                            //3 points of the other triangle
                            PointDT V1_2 = triangles.elementAt(j).getV1();
                            PointDT V2_2 = triangles.elementAt(j).getV2();
                            PointDT V3_2 = triangles.elementAt(j).getV3();

                            //delete the old triangle
                            triangles.remove(j);

                            /**
                             * VK_2 is the third point of triangle, first two
                             * are set by the edge (points A and B)
                             */
                            PointDT VK_2 = null;
                            if ( (V1_2 != A) && (V1_2 != B) )
                                VK_2 = V1_2;
                            if ( (V2_2 != A) && (V2_2 != B) )
                                VK_2 = V2_2;
                            if ( (V3_2 != A) && (V3_2 != B) )
                                VK_2 = V3_2;
                            newTri = new TriangleDT(B,pToAdd,VK_2);
                            triangles.add(newTri);
                            newTri = new TriangleDT(pToAdd,A,VK_2);
                            triangles.add(newTri);

                            /**
                             * legalize edges
                             */
                            legalizeEdge(pToAdd,triangles,A,VK_2);
                            legalizeEdge(pToAdd,triangles,VK_2,B);
                            legalizeEdge(pToAdd,triangles,B,VK);
                            legalizeEdge(pToAdd,triangles,VK,A);

                            break; // exit searching opposite side triangle
                        }
                    }
                    break; // point on the side added
                }

                /**
                 * check if point is in triangle
                 */
                if (isPointInTriangle(pToAdd, tri)) {
                    logger.debug("Point is inside");

                    /**
                     * Add edges from pr to the three vertices of pi pj pk
                     * thereby splitting pi pj pk into three triangles.
                     */
                    PointDT V1 = tri.getV1();
                    PointDT V2 = tri.getV2();
                    PointDT V3 = tri.getV3();

                    /**
                     * delete old triangle and create new triangles:
                     * V1, V2, Point
                     * V2, V3, Point
                     * V3, V1, Point
                     */
                    triangles.remove(i);

                    newTri = new TriangleDT(V1,V2,pToAdd);
                    triangles.add(newTri);

                    newTri = new TriangleDT(V2,V3,pToAdd);
                    triangles.add(newTri);

                    newTri = new TriangleDT(V3,V1,pToAdd);
                    triangles.add(newTri);

                    /**
                     * legalize edges
                     *
                     * do not legalize edges which contain unreal points p1,p2 or p3
                     */
                    logger.debug("p[" + pToAdd.getCoordX() + "," + pToAdd.getCoordY() +
                                 "] V1[" + V1.getCoordX() + "," + V1.getCoordY() +
                                 "] V2[" + V2.getCoordX() + "," + V2.getCoordY() + "]");
                    legalizeEdge(pToAdd,triangles,V1,V2);

                    logger.debug("p[" + pToAdd.getCoordX() + "," + pToAdd.getCoordY() +
                                 "] V2[" + V2.getCoordX() + "," + V2.getCoordY() +
                                 "] V3[" + V3.getCoordX() + "," + V3.getCoordY() + "]");
                    legalizeEdge(pToAdd,triangles,V2,V3);

                    logger.debug("p[" + pToAdd.getCoordX() + "," + pToAdd.getCoordY() +
                                 "] V3[" + V3.getCoordX() + "," + V3.getCoordY() +
                                 "] V1[" + V1.getCoordX() + "," + V1.getCoordY() + "]");
                    legalizeEdge(pToAdd,triangles,V3,V1);

                    break; // point in the triangle added
                }
                else
                    logger.debug("Point is outside");
            }

        }

        /**
         * Discard the initial edges with all their incident edges from T
         *
         * delete all edges containing points p1, p2, p3
         * return all other edges
         */

        // print all triangles after triangulation before deletion of points p1,p2,p3
        for (TriangleDT triangleDT : triangles) {
            logger.debug("A: [" + triangleDT.getV1().getCoordX() + "," + triangleDT.getV1().getCoordY() +
                         "] B: [" + triangleDT.getV2().getCoordX() + "," + triangleDT.getV2().getCoordY() +
                         "] C: [" + triangleDT.getV3().getCoordX() + "," + triangleDT.getV3().getCoordY() + "]");
        }

        /**
         * edges is a vector of points
         *
         * if edge contains two regular points (not p1, p2, p3), both points
         * are added to edges.
         */
        for (TriangleDT triangleDT : triangles) {
            PointDT ver1 = triangleDT.getV1();
            PointDT ver2 = triangleDT.getV2();
            PointDT ver3 = triangleDT.getV3();

            if ( (ver1.getIndex() <= this.g.getNumVertices()) &&
                 (ver2.getIndex() <= this.g.getNumVertices())
               ) {
                edges.add(ver1);
                edges.add(ver2);
            }

            if ( (ver2.getIndex() <= this.g.getNumVertices()) &&
                    (ver3.getIndex() <= this.g.getNumVertices())
                  ) {
                edges.add(ver2);
                edges.add(ver3);
               }

            if ( (ver3.getIndex() <= this.g.getNumVertices()) &&
                    (ver1.getIndex() <= this.g.getNumVertices())
                  ) {
                edges.add(ver3);
                edges.add(ver1);
               }
        }

        return edges;

    }

    /**
     * Checks, if point is inside triangle or not
     *
     * @param p point
     * @param t triangle
     * @return false point is outside the triangle, true if is inside
     */
    public Vector<PointDT> isPointOnSideOfTriangle(PointDT p, TriangleDT t) {

        double left = 0.0;
        double right = 0.0;

        /**
         * 2 points, specifying the line on which is the point
         */
        Vector<PointDT> res = new Vector<PointDT>();

        /**
         * V1[x1,y1] V2[x2,y2] P[p1,p2]
         *
         * (p2-y1)/(p1-x1) = (y2-y1)/(x2-x1)
         *
         * To avoid the division by zero you have to prove if
         * i) x1 = x2 = p1 => 3 points are lying on a line _|_ to the x-axis
         * ii) y1 = y2 = p2 => 3 points are lying on a line _|_ to the y-axis
         *
         */

        /**
         * Check edge V1-V2
         */

        // v1.x = v2.x = p.x and p.y is between v1.y and v2.y
        if (t.getV1().getCoordX() == t.getV2().getCoordX()) {
            if (t.getV1().getCoordX() == p.getCoordX()) {
                if ( !( (p.getCoordY() < t.getV1().getCoordY()) && (p.getCoordY() < t.getV2().getCoordY())
                        ||
                        (p.getCoordY() > t.getV1().getCoordY()) && (p.getCoordY() > t.getV2().getCoordY())
                      )
                   ) {
                    res.add(t.getV1());
                    res.add(t.getV2());
                    return res;
                }
            }
            return null;
        }

        // v1.y = v2.y = p.y and p.x is between v1.x and v2.x
        if (t.getV1().getCoordY() == t.getV2().getCoordY()) {
            if (t.getV1().getCoordY() == p.getCoordY()) {
                if ( !( (p.getCoordX() < t.getV1().getCoordX()) && (p.getCoordX() < t.getV2().getCoordX())
                        ||
                        (p.getCoordX() > t.getV1().getCoordX()) && (p.getCoordX() > t.getV2().getCoordX())
                      )
                   ) {
                    res.add(t.getV1());
                    res.add(t.getV2());
                    return res;
                }
            }
            return null;
        }

        left = (double)(p.getCoordY()-t.getV1().getCoordY()) / (double)(p.getCoordX()-t.getV1().getCoordX());
        right = (double)(t.getV2().getCoordY()-t.getV1().getCoordY()) / (double)(t.getV2().getCoordX()-t.getV1().getCoordX());
        if (left == right) {
            // check if p.x is between V1.x and V2.x
            if ( !( (p.getCoordX() < t.getV1().getCoordX()) && (p.getCoordX() < t.getV2().getCoordX())
                    ||
                    (p.getCoordX() > t.getV1().getCoordX()) && (p.getCoordX() > t.getV2().getCoordX())
                 )
               ) {
                // check if p.y is between V1.y and V2.y
                if ( !( (p.getCoordY() < t.getV1().getCoordY()) && (p.getCoordY() < t.getV2().getCoordY())
                        ||
                        (p.getCoordY() > t.getV1().getCoordY()) && (p.getCoordY() > t.getV2().getCoordY())
                     )
                   ) {
                    // OK, point is on the line
                    res.add(t.getV1());
                    res.add(t.getV2());
                    return res;
                }
            }
        }

        /**
         * Check edge V2-V3
         */

        // v2.x = v3.x = p.x and p.y is between v2.y and v3.y
        if (t.getV2().getCoordX() == t.getV3().getCoordX()) {
            if (t.getV2().getCoordX() == p.getCoordX()) {
                if ( !( (p.getCoordY() < t.getV2().getCoordY()) && (p.getCoordY() < t.getV3().getCoordY())
                        ||
                        (p.getCoordY() > t.getV2().getCoordY()) && (p.getCoordY() > t.getV3().getCoordY())
                      )
                   ) {
                    res.add(t.getV2());
                    res.add(t.getV3());
                    return res;
                }
            }
            return null;
        }

        // v2.y = v3.y = p.y and p.x is between v2.x and v3.x
        if (t.getV2().getCoordY() == t.getV3().getCoordY()) {
            if (t.getV2().getCoordY() == p.getCoordY()) {
                if ( !( (p.getCoordX() < t.getV2().getCoordX()) && (p.getCoordX() < t.getV3().getCoordX())
                        ||
                        (p.getCoordX() > t.getV2().getCoordX()) && (p.getCoordX() > t.getV3().getCoordX())
                      )
                   ) {
                    res.add(t.getV2());
                    res.add(t.getV3());
                    return res;
                }
            }
            return null;
        }

        left = (double)(p.getCoordY()-t.getV2().getCoordY())/(double)(p.getCoordX()-t.getV2().getCoordX());
        right = (double)(t.getV3().getCoordY()-t.getV2().getCoordY())/(double)(t.getV3().getCoordX()-t.getV2().getCoordX());
        if (left == right) {

            // check if p.x is between V2.x and V3.x
            if ( !( (p.getCoordX() < t.getV2().getCoordX()) && (p.getCoordX() < t.getV3().getCoordX())
                    ||
                    (p.getCoordX() > t.getV2().getCoordX()) && (p.getCoordX() > t.getV3().getCoordX())
                 )
               ) {
                // check if p.y is between V2.y and V3.y
                if ( !( (p.getCoordY() < t.getV2().getCoordY()) && (p.getCoordY() < t.getV3().getCoordY())
                        ||
                        (p.getCoordY() > t.getV2().getCoordY()) && (p.getCoordY() > t.getV3().getCoordY())
                     )
                   ) {
                    // OK, point is on the line
                    res.add(t.getV2());
                    res.add(t.getV3());
                    return res;
                }
            }
        }

        /**
         * Check edge V3-V1
         */

        // v3.x = v1.x = p.x and p.y is between v3.y and v1.y
        if (t.getV3().getCoordX() == t.getV1().getCoordX()) {
            if (t.getV3().getCoordX() == p.getCoordX()) {
                if ( !( (p.getCoordY() < t.getV3().getCoordY()) && (p.getCoordY() < t.getV1().getCoordY())
                        ||
                        (p.getCoordY() > t.getV3().getCoordY()) && (p.getCoordY() > t.getV1().getCoordY())
                      )
                   ) {
                    res.add(t.getV3());
                    res.add(t.getV1());
                    return res;
                }
            }
            return null;
        }

        // v3.y = v1.y = p.y and p.x is between v3.x and v1.x
        if (t.getV3().getCoordY() == t.getV1().getCoordY()) {
            if (t.getV3().getCoordY() == p.getCoordY()) {
                if ( !( (p.getCoordX() < t.getV3().getCoordX()) && (p.getCoordX() < t.getV1().getCoordX())
                        ||
                        (p.getCoordX() > t.getV3().getCoordX()) && (p.getCoordX() > t.getV1().getCoordX())
                      )
                   ) {
                    res.add(t.getV3());
                    res.add(t.getV1());
                    return res;
                }
            }
            return null;
        }

        left = (double)(p.getCoordY()-t.getV3().getCoordY())/(double)(p.getCoordX()-t.getV3().getCoordX());
        right = (double)(t.getV1().getCoordY()-t.getV3().getCoordY())/(double)(t.getV1().getCoordX()-t.getV3().getCoordX());
        if (left == right) {
            // check if p.x is between V3.x and V1.x
            if ( !( (p.getCoordX() < t.getV3().getCoordX()) && (p.getCoordX() < t.getV1().getCoordX())
                    ||
                    (p.getCoordX() > t.getV3().getCoordX()) && (p.getCoordX() > t.getV1().getCoordX())
                 )
               ) {
                // check if p.y is between V3.y and V1.y
                if ( !( (p.getCoordY() < t.getV3().getCoordY()) && (p.getCoordY() < t.getV1().getCoordY())
                        ||
                        (p.getCoordY() > t.getV3().getCoordY()) && (p.getCoordY() > t.getV1().getCoordY())
                     )
                   ) {
                    // OK, point is on the line
                    res.add(t.getV3());
                    res.add(t.getV1());
                    return res;
                }
            }
        }

        return null;
    }

    /**
     * Checks, if point is inside triangle or not
     *
     * @param p point
     * @param t triangle
     * @return false point is outside the triangle, true if is inside
     */
    public boolean isPointInTriangle(PointDT p, TriangleDT t) {

        double ax, ay;
        double bx, by;
        double z1, z2, z3;

        /**
         * first normal vector
         */
        ax = t.getV2().getCoordX() - t.getV1().getCoordX();
        ay = t.getV2().getCoordY() - t.getV1().getCoordY();
        bx = p.getCoordX() - t.getV1().getCoordX();
        by = p.getCoordY() - t.getV1().getCoordY();
        z1 = ax*by - ay*bx;

        /**
         * second normal vector
         */
        ax = t.getV1().getCoordX() - t.getV3().getCoordX();
        ay = t.getV1().getCoordY() - t.getV3().getCoordY();
        bx = p.getCoordX() - t.getV3().getCoordX();
        by = p.getCoordY() - t.getV3().getCoordY();
        z2 = ax*by - ay*bx;

        /**
         * third normal vector
         */
        ax = t.getV3().getCoordX() - t.getV2().getCoordX();
        ay = t.getV3().getCoordY() - t.getV2().getCoordY();
        bx = p.getCoordX() - t.getV2().getCoordX();
        by = p.getCoordY() - t.getV2().getCoordY();
        z3 = ax*by - ay*bx;

        if ( (z1 >= 0) && (z2 >= 0) && (z3 >= 0) )
            return true;
        if ( (z1 <= 0) && (z2 <= 0) && (z3 <= 0) )
            return true;

        return false;
    }

    /**
     * Legalize edge of triangle
     *
     * @param p point added to triangulation
     * @param t triangles of triangulation
     * @param a starting point of the edge we are going to legalize
     * @param b ending point of the edge we are going to legalize
     * @param c point of the deleted triangle abc
     */
    public void legalizeEdge(PointDT p, Vector<TriangleDT> t, PointDT a, PointDT b) {

        // skontrolujem, ci nemam 2 body nahodou rovnake alebo nie su kolinearne
        if ((p == a) || (p == b))
            return;

        if ((p.getCoordX() == a.getCoordX()) || (p.getCoordX() == b.getCoordX()))
            return;

        if ((p.getCoordY() == a.getCoordY()) || (p.getCoordY() == b.getCoordY()))
            return;

        //hladam trojuholnik, co ma spolocnu hranu s mojou hranou co testujem
        //ak ho najdem testujem ilegalnost, ak nie nemoze byt ilegalna
        TriangleDT tr = new TriangleDT(a,b,p);
        TriangleDT tr2 = null;

        int index=0;

        for (int i=0; i<t.size();i++) {
            tr2 = isTriangleWithSameEdge(a,b,t.elementAt(i));
            if (tr2 != null) {
                //je to naozaj druhy trojuholnik a nie ten isty co mam?
                if ( (tr2.getV1().equals(a) || tr2.getV1().equals(b) || tr2.getV1().equals(p)) &&
                     (tr2.getV2().equals(a) || tr2.getV2().equals(b) || tr2.getV2().equals(p)) &&
                     (tr2.getV3().equals(a) || tr2.getV3().equals(b) || tr2.getV3().equals(p))
                   )
                    logger.info("ROVNAKY TRIANGL PRI POKUSE O LEGALIZACIU");
                else {
                    index=i;
                    break;
                }
            }
        }

        // nasiel som druhy trojuholnik, moze byt nelegalna
        if (tr2 != null) {

            // moze byt nelegalna, nasiel som druhy trojuholnik
            // nelegalnost urcim pomocou kruznice opisanej prvemu trojuholniku,
            // ak je vrchol druheho trojuholnika v kruznici, tak je nelegalna
            PointDT V1 = tr2.getV1();
            PointDT V2 = tr2.getV2();
            PointDT V3 = tr2.getV3();
            PointDT VK = null;

            if ( !(V1.equals(a)) && !(V1.equals(b)) )
                VK = V1;

            if ( !(V2.equals(a)) && !(V2.equals(b)) )
                VK = V2;

            if ( !(V3.equals(a)) && !(V3.equals(b)) )
                VK = V3;

                /**
                 * Calculate radius and middle point of the circle through 3pts
                 */
                double mx, my;
                double radius = 0;

                /**
                 * Calculate x-coordinate of centre
                 */
               double div = 2.0 * ( (a.getCoordX() - b.getCoordX()) *
                                     (b.getCoordY() - p.getCoordY()) -
                                     (b.getCoordX() - p.getCoordX()) *
                                     (a.getCoordY() - b.getCoordY())
                                   );

               // nemalo by nastat pokial nemam kolinearne body
               // v takom pripade ale nemam trojuholnik
                if(div == 0.0) {
                    logger.error("Chyba...div = 0, budem delit nulou");
                    logger.error("A[" + a.getCoordX() + "," + a.getCoordY() + "]");
                    logger.error("B[" + b.getCoordX() + "," + b.getCoordY() + "]");
                    logger.error("P[" + p.getCoordX() + "," + p.getCoordY() + "]");
                }

                mx = (( b.getCoordY() - p.getCoordY()) *
                       (a.getCoordX() * a.getCoordX() -
                        b.getCoordX() * b.getCoordX() +
                        a.getCoordY() * a.getCoordY() -
                        b.getCoordY() * b.getCoordY()) -
                       (a.getCoordY() - b.getCoordY()) *
                       (b.getCoordX() * b.getCoordX() -
                        p.getCoordX() * p.getCoordX() +
                        b.getCoordY() * b.getCoordY() -
                        p.getCoordY() * p.getCoordY())) / div;

                /**
                 *  Calculate y-coordinate of centre
                 */

                if (a.getCoordY() != b.getCoordY())
                    my = ((2.0 * mx * (b.getCoordX()-a.getCoordX())) +
                                      (a.getCoordX()*a.getCoordX() -
                                       b.getCoordX()*b.getCoordX() +
                                       a.getCoordY()*a.getCoordY() -
                                       b.getCoordY()*b.getCoordY())) /
                                      (2.0 * (a.getCoordY()-b.getCoordY()));

                else
                    my = ((2.0 * mx * (p.getCoordX()-a.getCoordX())) +
                                      (a.getCoordX()*a.getCoordX() -
                                       p.getCoordX()*p.getCoordX() +
                                       a.getCoordY()*a.getCoordY() -
                                       p.getCoordY()*p.getCoordY())) /
                                       (2.0 * (a.getCoordY()-p.getCoordY()));

                /**
                 *  Derive radius
                 */

                radius = Math.sqrt((mx - a.getCoordX()) * (mx - a.getCoordX()) +
                              (my - a.getCoordY()) * (my - a.getCoordY()));


                // mam polomer a suradnice kruhu, ktory opisem cez 3 body trojuholnika
                // ak bod K lezi v kruznici => hrana je ilegalna, inac vpoho
                // rovnica kruznice: (x-sx)^2 + (y-sy)^2 <= r^2
                // <= lezi bud vnutri alebo na hrane kruznice

                double left = (VK.getCoordX()-mx)*(VK.getCoordX()-mx) +
                              (VK.getCoordY()-my)*(VK.getCoordY()-my);
                double right = radius*radius;

                /**
                 * pouzijem fintu so zaokruhlenim, ale len ak to nie su
                 * koncove body
                 * pri porovnani kruznic s koncovymi bodmi som legalizoval aj
                 * to co nebolo treba, lebo 2.49999E15 bolo vacsie ako
                 * 2.4985E15 a tym patom sa mi to legalizaciou rozbilo
                 *
                 *
                 */
/*
                boolean zlyBod = false;
                if ( (a.getIndex()>this.g.getNumVertices()) ||
                     (b.getIndex()>this.g.getNumVertices()) ||
                     (p.getIndex()>this.g.getNumVertices())
                   )
                    zlyBod = true;

                if (zlyBod) {
                    double test = left/right;
                    if ( !(Math.abs(test-1)<0.1))
                        left=right+1;
                }
                */

                if ( left <= right ) {
                    //NELEGALNA - musim zlegalizovat
                    //spravim flip...nahradim pipj za pkpr
                    //zmaznem v triangles tr2, body mam stale ulozene v tr2
                    t.remove(index);
                    //najdem a zmaznem aj povodny triangle abp, body ulozene v tr
                    for (int j=0;j<t.size();j++) {
                        TriangleDT tri = t.elementAt(j);
                        if ( (tri.getV1()==tr.getV1() || tri.getV1()==tr.getV2() || tri.getV1()==tr.getV3()) &&
                             (tri.getV2()==tr.getV1() || tri.getV2()==tr.getV2() || tri.getV2()==tr.getV3()) &&
                             (tri.getV3()==tr.getV1() || tri.getV3()==tr.getV2() || tri.getV3()==tr.getV3()) ) {
                            t.remove(j);
                            break;
                        }
                    }
                    //flip: nove trojuholniky budu akp a kbp
                    PointDT k = tr2.getV1();
                    if ( k.equals(a) || k.equals(b) ) {
                        k = tr2.getV2();
                        if ( k.equals(a) || k.equals(b) )
                            k = tr2.getV3();
                    }

                    if (k.equals(p) || k.equals(a)) {
                        logger.debug("DVA BODY ROVNAKE");
                        logger.debug("p[" + p.getCoordX() + "," + p.getCoordY() +
                                     "] a[" + a.getCoordX() + "," + a.getCoordY() +
                                     "] k[" + k.getCoordX() + "," + k.getCoordY() + "]");
                    }
                    TriangleDT newTri = new TriangleDT(a,k,p);
                    t.add(newTri);
                    TriangleDT newTri2 = new TriangleDT(k,b,p);
                    t.add(newTri2);

                    logger.debug("LEGALIZUJEM HRANU: [" + a.getCoordX() + "," + a.getCoordY() +
                                "]-[" + k.getCoordX() + "," + k.getCoordY() + "]");
                    legalizeEdge(p,t,a,k);

                    logger.debug("LEGALIZUJEM HRANU: [" + k.getCoordX() + "," + k.getCoordY() +
                                 "]-[" + b.getCoordX() + "," + b.getCoordY() + "]");
                    legalizeEdge(p,t,k,b);
                }
        } // end if nasiel som druhy trojuholnik
    }

    /**
     * Test if triangle has same edge as my triangle
     *
     * @param a starting point of the edge
     * @param b ending point of the edge
     * @param t tested triangle
     * @return tr triangle with edge ab
     */
    public TriangleDT isTriangleWithSameEdge(PointDT a, PointDT b, TriangleDT t) {

        // triangle with same edge
        TriangleDT tr = null;

        //3 points of triangle
        PointDT V1 = t.getV1();
        PointDT V2 = t.getV2();
        PointDT V3 = t.getV3();

        if ( (V1 == b) && (V2 == a) )
            tr = new TriangleDT(V1,V2,V3);

        if ( (V2 == b) && (V3 == a) )
            tr = new TriangleDT(V1,V2,V3);

        if ( (V3 == b) && (V1 == a) )
            tr = new TriangleDT(V1,V2,V3);

        return tr;
    }

}
