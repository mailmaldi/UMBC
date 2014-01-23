/**
 *
 */
package main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support;

import java.util.Comparator;

/**
 * @author Shaki
 *
 */
public class EdgeComparator implements Comparator<Edge> {

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Edge arg0, Edge arg1) {
        // TODO Auto-generated method stub
        int source1;
        int dest1;
        int source2;
        int dest2;

        if ( arg0.getLength() < arg1.getLength())
                return -1;
        else if ( arg0.getLength() > arg1.getLength())
            return 1;

        if (arg0.getSource() < arg0.getDestination()) {
            source1 = arg0.getSource();
            dest1 = arg0.getDestination();
        }
        else {
            source1 = arg0.getDestination();
            dest1 = arg0.getSource();
        }

        if (arg1.getSource() < arg1.getDestination()) {
            source2 = arg1.getSource();
            dest2 = arg1.getDestination();
        }
        else {
            source2 = arg1.getDestination();
            dest2 = arg1.getSource();
        }

        if ( (arg0.getLength() == arg1.getLength()) &&
             (arg0.getSource() == arg1.getSource()) &&
             (arg0.getDestination() == arg1.getDestination())
           ) // su rovnake ak je rovnaky zdroj ciel aj dlzka
            return 0;

        if (source1 != source2)
            return source1-source2;

        return dest1-dest2;

    }
}
