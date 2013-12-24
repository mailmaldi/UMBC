/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support;

/**
 *
 * @author Shaki
 * @since April 2009
 *
 */
public class PointDT implements Comparable<PointDT> {


    /**
     * label of vertex
     */
    private int index;

    /**
     * x-coordinate
     */
    private int coordX;

    /**
     * y-coordinate
     */
    private int coordY;

    /**
     * Construct a new point with index and coordinates.
     *
     * @param i index.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public PointDT(int i, int x, int y) {
        this.setIndex(i);
        this.setCoordX(x);
        this.setCoordY(y);
    }

    /**
     * @param i index
     */
    public void setIndex(int i) {
        this.index = i;
    }
    /**
     * @return index of the vertex.
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * @param x x-coordinate
     */
    public void setCoordX(int x) {
        this.coordX = x;
    }
    /**
     * @return x coordinate.
     */
    public int getCoordX() {
        return this.coordX;
    }

    /**
     * @param y y-coordinate
     */
    public void setCoordY(int y) {
        this.coordY = y;
    }
    /**
     * @return y coordinate
     */
    public int getCoordY() {
        return this.coordY;
    }

    @SuppressWarnings("boxing")
    @Override
    public int compareTo(PointDT arg0) {
        if ( this.coordX == arg0.coordX)
            return ((Integer)this.coordY).compareTo(arg0.coordY);
        return ((Integer)this.coordX).compareTo(arg0.coordX);
    }
}

