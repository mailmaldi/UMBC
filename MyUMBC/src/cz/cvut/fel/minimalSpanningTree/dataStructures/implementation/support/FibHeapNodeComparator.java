/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support;

import java.util.Comparator;

/**
 * @author Shaki
 *
 */

public class FibHeapNodeComparator implements Comparator<FibHeapNode> {

   /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
   @Override
   public int compare(FibHeapNode arg0, FibHeapNode arg1) {
       // TODO Auto-generated method stub
       int source1;
       int dest1;
       int source2;
       int dest2;

       if ( arg0.getKey() < arg1.getKey())
               return -1;
       else if ( arg0.getKey() > arg1.getKey())
           return 1;

       if (((Edge)arg0).getSource() < ((Edge)arg0).getDestination()) {
           source1 = ((Edge)arg0).getSource();
           dest1 = ((Edge)arg0).getDestination();
       }
       else {
           source1 = ((Edge)arg0).getDestination();
           dest1 = ((Edge)arg0).getSource();
       }

       if (((Edge)arg1).getSource() < ((Edge)arg1).getDestination()) {
           source2 = ((Edge)arg1).getSource();
           dest2 = ((Edge)arg1).getDestination();
       }
       else {
           source2 = ((Edge)arg1).getDestination();
           dest2 = ((Edge)arg1).getSource();
       }

       if ( (arg0.getKey() == arg1.getKey()) &&
            (((Edge)arg0).getSource() == ((Edge)arg1).getSource()) &&
            (((Edge)arg0).getDestination() == ((Edge)arg1).getDestination())
          ) // su rovnake ak je rovnaky zdroj ciel aj dlzka
           return 0;

       if (source1 != source2)
           return source1-source2;

       return dest1-dest2;

   }
}

