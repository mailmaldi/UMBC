/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support;

/**
 * Triangle structure for Delaunays's triangulation
 *
 * @author Shaki
 * @since April 2009
 */
public class TriangleDT {

    /**
     * 1st-vertex
     */
    private PointDT v1;

    /**
     * 2nd-vertex
     */
    private PointDT v2;

    /**
     * 3rd-vertex
     */
    private PointDT v3;

    /**
     * Construct a new triangle with 3 vertices.
     *
     * @param v1 vertex 1
     * @param v2 vertex 2
     * @param v3 vertex 3
     */
    public TriangleDT(PointDT v1, PointDT v2, PointDT v3) {
        this.setV1(v1);
        this.setV2(v2);
        this.setV3(v3);
    }

    /**
     * Setter for vertex 1
     * @param v1 vertex 1
     */
    public void setV1(PointDT v1) {
        this.v1 = v1;
    }
    /**
     * Getter for vertex 1
     * @return v1
     */
    public PointDT getV1() {
        return this.v1;
    }

    /**
     * Setter for vertex 2
     * @param v2 vertex 2
     */
    public void setV2(PointDT v2) {
        this.v2 = v2;
    }
    /**
     * Getter for vertex 2
     * @return v2
     */
    public PointDT getV2() {
        return this.v2;
    }

    /**
     * Setter for vertex 3
     * @param v3 vertex 3
     */
    public void setV3(PointDT v3) {
        this.v3 = v3;
    }
    /**
     * Getter for vertex 3
     * @return v3
     */
    public PointDT getV3() {
        return this.v3;
    }

}
