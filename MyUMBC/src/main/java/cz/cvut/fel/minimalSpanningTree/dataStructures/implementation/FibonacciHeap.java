/**
 *
 */
package main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;

import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.EdgeComparator;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.FibHeapNode;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.FibHeapNodeComparator;

import org.apache.log4j.Logger;

/**
 * Data structure for Fibonacci heap.
 *
 * Implements methods:
 * <p>isEmpty         with Running time: O(1) actual</p>
 * <p>merge           with Running time: O(1) actual</p>
 * <p>insert          with Running time: O(1) actual</p>
 * <p>extractMinimum  with Running time: O(log n) amortized</p>
 * <p>decreaseKey     with Running time: O(1) amortized</p>
 * <p>deleteNode      with Running time: O(log n) amortized</p>
 * <p>findMinimum     with Running time: O(1) actual</p>
 * <p>size            with Running time: O(1) actual</p>
 *
 * @author Shaki
 * @since May 2009
 *
 */
public class FibonacciHeap<T> {
    static Logger          logger;

    private static final double oneOverLogPhi = 1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);

    /**
     * Number of heap nodes.
     */
    private int numHeapNodes;


    /**
     * Pointer to the node with minimum key in the heap (minimum node).
     */
    private FibHeapNode minimumNode;

    private boolean testDvojitehoMinima = false;
    private FibHeapNode testMinima = null;

    /**
     * Default constructor for Fibonacci heap.
     */
    public FibonacciHeap() {
        logger = Logger.getLogger(this.getClass());
        // no elements - empty heap
    }

    /**
     * Check, if Fibonacci heap is empty
     *
     * <p>Running time: O(1)</p>
     *
     * @return true if the heap is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.minimumNode == null;
    }

    /**
     * Delete all elements from this heap => empty Fibonacci heap
     */
    public void reset() {
        this.minimumNode = null;
        this.numHeapNodes = 0;
    }

    /**
     * Inserts a new data element into the heap.
     *
     * <p>Running time: O(1) </p>
     *
     * @param node node to be inserted to the heap
     */
    public void insert(FibHeapNode node) {

        //logger.debug("START: insert " + node.getKey());
        //printNeighbours(node);

        // concatenate node into min list
        if (this.minimumNode != null) {
            node.setLeft(this.minimumNode);
            node.setRight(this.minimumNode.getRight());
            this.minimumNode.setRight(node);
            node.getRight().setLeft(node);

            if (node.getKey() < this.minimumNode.getKey()) {
                this.minimumNode = node;
            }
        } else {
            this.minimumNode = node;
        }

        this.numHeapNodes++;

        //logger.debug("END: insert " + node.getKey());
        //printNeighbours(this.minimumNode);
    }

    /**
     * Extract minimum (delete) the smallest element from the heap.
     * This will cause the trees in the heap to be consolidated, if necessary.
     *
     * <p>Running time: O(log n) amortized</p>
     *
     * @return node with the smallest key
     */
    public FibHeapNode extractMin() {
        FibHeapNode z = this.minimumNode;

        //logger.debug("START minimum na zaciatku extractMin");
        //printNeighbours(z);

        //FibHeapNode zc = z.getChild();
        //logger.debug("START deti minima na zaciatku extractMin");
        //printNeighbours(zc);

        if (z != null) {
            int numChildren = z.getDegree();
            FibHeapNode x = z.getChild();
            FibHeapNode tempRight;

            // for each child of z do
            while (numChildren > 0) {
                tempRight = x.getRight();

                // remove x from child list
                x.getLeft().setRight(x.getRight());
                x.getRight().setLeft(x.getLeft());

                // add x to root list of heap
                x.setLeft(this.minimumNode);
                x.setRight(this.minimumNode.getRight());
                this.minimumNode.setRight(x);
                x.getRight().setLeft(x);

                // set parent[x] to null
                x.setParent(null);
                x = tempRight;
                numChildren--;
            }

            //logger.debug("Minimum po vybrati deti z child listu a vlozeni do rootu: extractMin");
            //printNeighbours(z);

            //logger.debug("Deti minima po ich odobrati a vlozeni do rootu: extractMin");
            //printNeighbours(zc);

            //FibHeapNode xx = z.getLeft();
            //logger.debug("Chystam sa odobrat minimum zo zoznamu korenov, vypisujem si jeho laveho suseda");
            //printNeighbours(xx);

            // remove z from root list of heap
            z.getLeft().setRight(z.getRight());
            z.getRight().setLeft(z.getLeft());

            //logger.debug("Vypisujem si laveho suseda uz odobrateho minima");
            //printNeighbours(xx);

            if (z == z.getRight()) {
                this.minimumNode = null;
            } else {
                this.minimumNode = z.getRight();

                consolidate();

            }

            // node delete, decrease number of nodes
            this.numHeapNodes--;
        }

        return z;
    }

    /**
     * Decreases the key value for a heap node, given the new value to take on.
     * Place new edge with lower key in the place of the old edge (bigger
     * key value)
     *
     * <p>Running time: O(1) amortized</p>
     *
     * @param oE old edge node to decrease the key of
     * @param nE new edge
     *
     */
    public void decreaseKey(FibHeapNode oE, FibHeapNode nE) {
        if (oE == null)
            logger.debug("no element specified for operation decrease-key");

        if (nE.getKey() > oE.getKey())
            logger.debug("decreaseKey() got larger key value");

        //logger.debug("DECREASE-KEY: kontrola susedov oE");
        //printNeighbours(oE);

        // upravim kluc, zdroj, ciel a dlzku hrany oE podla novej nE
        oE.setKey(nE.getKey());
        ((Edge)oE).setDestination(((Edge)nE).getDestination());
        ((Edge)oE).setSource(((Edge)nE).getSource());
        ((Edge)oE).setLength(((Edge)nE).getLength());

        FibHeapNode son = oE;
        FibHeapNode father = oE.getParent();

        if ((father != null) && (son.getKey() < father.getKey())) {
            // kluc syna je mensi ako kluc otca => odrez syna
            cut(son, father);

            // kontrola a pripadne odrezanie aj otca
            cascadingCut(father);
        }

        // skontrolujem, ci nemam nahodou nove minimum
        if (oE.getKey() < this.minimumNode.getKey())
                this.minimumNode = oE;
    }

    /**
     * Find smallest element in the heap.
     * The one with the minimum key value (edge length).
     *
     * <p>Running time: O(1) actual</p>
     *
     * @return heap node with the smallest key
     */
    public FibHeapNode findMin() {
        return this.minimumNode;
    }

    /**
     * Size of the heap = number of elements contained in the heap.
     *
     * <p>Running time: O(1) actual</p>
     *
     * @return number of elements in the heap
     */
    public int size() {
        return this.numHeapNodes;
    }

    /**
     * Creates a String representation of this Fibonacci heap.
     *
     * @return String of this.
     */
    @Override
    public String toString() {

        // Fibonacci heap is empty
        if (this.minimumNode == null)
            return "FibonacciHeap=[]";

        // create a new stack and put root on it
        Stack<FibHeapNode> stack = new Stack<FibHeapNode>();
        stack.push(this.minimumNode);

        StringBuffer buf = new StringBuffer(512);
        buf.append("FibonacciHeap=[");

        // do a simple breadth-first traversal on the tree
        while (!stack.empty()) {
            FibHeapNode curr = stack.pop();
            buf.append(curr);
            buf.append(", ");

            if (curr.getChild() != null)
                stack.push(curr.getChild());

            FibHeapNode start = curr;
            curr = curr.getRight();

            while (curr != start) {
                buf.append(curr);
                buf.append(", ");

                if (curr.getChild() != null)
                    stack.push(curr.getChild());

                curr = curr.getRight();
            }
        }

        buf.append(']');

        return buf.toString();
    }

    /**
     * Cascading cut.
     *
     * This cuts y from its parent and then does the same for its parent,
     * and so on up the tree.
     *
     * <p>Running time: O(log n); O(1) excluding the recursion</p>
     *
     * @param y node to perform cascading cut on
     */
    protected void cascadingCut(FibHeapNode y)
    {
        FibHeapNode z = y.getParent();

        // if there's a parent...
        if (z != null) {

            // if y is unmarked, set it marked
            if (!y.isMark())
                y.setMark(true);
            // it's marked, cut it from parent
            else {

                //logger.debug("START: y : cascadeCut(y)");
                //printNeighbours(y);

                //logger.debug("START: z (rodic y) : cascadeCut(y) ");
                //printNeighbours(z);

                //FibHeapNode zc = z.getChild();
                //logger.debug("START: z-child (vlastne y) : cascadeCut(y) ");
                //printNeighbours(zc);

                cut(y, z);

                //logger.debug("END y po cut(y,z) : cascadeCut(y) ");
                //printNeighbours(y);

                //logger.debug("END z po cut(y,z) : cascadeCut(y)");
                //printNeighbours(z);

                //logger.debug("END z-child po cut(y,z) : cascadeCut(y)");
                //printNeighbours(zc);

                // cut its parent as well
                cascadingCut(z);
            }
        }
    }

    /**
     * Consolidate heap
     */
    protected void consolidate() {

        Vector<FibHeapNode> helper = new Vector<FibHeapNode>();

        int arraySize = ((int) Math.floor(Math.log(this.numHeapNodes) * oneOverLogPhi)) + 1;
        List<FibHeapNode> array = new ArrayList<FibHeapNode>(arraySize);

        // Initialize degree array
        for (int i = 0; i < arraySize; i++)
            array.add(null);

        // Find the number of root nodes.
        int numRoots = 0;
        FibHeapNode x = this.minimumNode;

        if (x != null) {
            //logger.debug("START: susedia minima OK?, predtym nez budem pocitat korene: consolidate ");
            //printNeighbours(x);
            numRoots++;
            x = x.getRight();

            while (x != this.minimumNode) {
                numRoots++;
                x = x.getRight();
            }
        }

        // For each node in root list do...
        while (numRoots > 0) {
            // Access this node's degree..
            int d = x.getDegree();
            FibHeapNode next = x.getRight();

            //logger.debug("element z root zoznamu, je ok? : consolidate (numRoots: " + numRoots);
            //printNeighbours(next);

            // ..and see if there's another of the same degree.
            for (;;) {
                FibHeapNode y = array.get(d);
                // Nope.
                if (y == null)
                    break;

                // There is, make one of the nodes a child of the other.
                // Do this based on the key value.
                if (x.getKey() > y.getKey()) {
                    FibHeapNode temp = y;
                    y = x;
                    x = temp;
                }

                //logger.debug("x pred upravou LINK(y,x) : consolidate ");
                //printNeighbours(x);

                //logger.debug("y pred upravou LINK(y,x): consolidate");
                //printNeighbours(y);

                //FibHeapNode xc = x.getChild();
                //logger.debug("x.child pred upravou LINK(y,x): consolidate ");
                //printNeighbours(xc);

                // FibonacciHeapNode<T> y disappears from root list.
                link(y, x);

                //logger.debug("x po uprave: LINK(y,x) : consolidate");
                //printNeighbours(x);

                //logger.debug("y po uprave: LINK(y,x) : consolidate");
                //printNeighbours(y);

                //logger.debug("x.child po uprave: LINK(y,x) : consolidate");
                //printNeighbours(xc);

                // We've handled this degree, go to next one.
                array.set(d, null);
                d++;
            }

            // Save this node for later when we might encounter another
            // of the same degree.
            array.set(d, x);

            // Move forward through list.
            x = next;
            numRoots--;
        }

        FibHeapNode z = this.minimumNode;
        //logger.debug("min pred vynulovanim : consolidate MIN = " + z.getKey());
        //printNeighbours(z);

        // Set min to null (effectively losing the root list) and
        // reconstruct the root list from the array entries in array[].
        this.minimumNode = null;

        for (int i = 0; i < arraySize; i++) {
            FibHeapNode y = array.get(i);
            if (y == null) {
                continue;
            }

            // We've got a live one, add it to root list.
            if (this.minimumNode != null) {
                // First remove node from root list.
                y.getLeft().setRight(y.getRight());
                y.getRight().setLeft(y.getLeft());

                // Now add to root list, again.
                y.setLeft(this.minimumNode);
                y.setRight(this.minimumNode.getRight());
                this.minimumNode.setRight(y);
                y.getRight().setLeft(y);

                // Check if this is a new minimum
                if (y.getKey() < this.minimumNode.getKey()) {
                    this.minimumNode = y;
                }
            } else {
                this.minimumNode = y;
            }
        }

        z = this.minimumNode;
        //logger.debug("nove minimum : consolidate MIN = " + z.getKey());
        //printNeighbours(z);
    }

    /**
     * Cut (reverse of the link)
     * Removes son from the child list of father.
     * This method assumes that min is non-null.
     *
     * <p>Running time: O(1)</p>
     *
     * @param son child of father to be removed from father's child list
     * @param father parent of son about to lose a child
     */
     protected void cut(FibHeapNode son, FibHeapNode father) {

         //logger.debug("START son : CUT(son, father) ");
         //printNeighbours(son);

         //logger.debug("START father : CUT(son, father)");
         //printNeighbours(father);

         //FibHeapNode fc = father.getChild();
         //logger.debug("START father.child : CUT(son, father)");
         //printNeighbours(fc);

        // remove son from childlist of father and decrement degree[father]
        son.getLeft().setRight(son.getRight());
        son.getRight().setLeft(son.getLeft());
        father.setDegree(father.getDegree() - 1);

        // reset father.child if necessary
        if (father.getChild() == son)
            father.setChild(son.getRight());

        if (father.getDegree() == 0)
            father.setChild(null);

        // add son to root list of heap
        son.setLeft(this.minimumNode);
        son.setRight(this.minimumNode.getRight());
        this.minimumNode.setRight(son);
        son.getRight().setLeft(son);

        // set parent[son] to nil
        son.setParent(null);

        // set mark[son] to false
        son.setMark(false);

        //logger.debug("END son : CUT(son, father)");
        //printNeighbours(son);

        //logger.debug("END minimum : CUT(son, father)");
        //printNeighbours(this.minimumNode);

        //logger.debug("END father : CUT(son, father)");
        //printNeighbours(father);

        //logger.debug("END father.child : CUT(son, father)");
        //printNeighbours(fc);
    }

    /**
     * Make node y a child of node x.
     *
     * <p>Running time: O(1) actual</p>
     *
     * @param y node to become child
     * @param x node to become parent
     */
    protected void link(FibHeapNode y, FibHeapNode x) {


        //logger.debug("START y : link(y,x)");
        //printNeighbours(y);

        //logger.debug("START x : link(y,x)");
        //printNeighbours(x);

        //FibHeapNode yl = y.getLeft();
        //logger.debug("START yl : link(y,x)");
        //printNeighbours(yl);

        //FibHeapNode xc = x.getChild();
        //logger.debug("START x.child : link (y,x) ");
        //printNeighbours(xc);

        // premenna len kvoli testovaciemu vypisu, potom zmaznut
        FibHeapNode xy = y.getLeft();

        // remove y from root list of heap
        y.getLeft().setRight(y.getRight());
        y.getRight().setLeft(y.getLeft());

        //logger.debug("Y.left Po odstraneni y zo zoznamu korenov: link(y,x) ");
        //printNeighbours(xy);

        //logger.debug("X Po odstraneni y zo zoznamu korenov : link(y,x) ");
        //printNeighbours(x);

        // make y a child of x
        y.setParent(x);

        if (x.getChild() == null) {
            x.setChild(y);
            y.setRight(y);
            y.setLeft(y);
        } else {
            y.setLeft(x.getChild());
            y.setRight(x.getChild().getRight());
            x.getChild().setRight(y);
            y.getRight().setLeft(y);
        }

        // increase degree[x]
        x.setDegree(x.getDegree() + 1);

        // set mark[y] false
        y.setMark(false);

        //logger.debug("END y : link(y,x) ");
        //printNeighbours(y);

        //logger.debug("END x : link(y,x) ");
        //printNeighbours(x);

        //logger.debug("END yl : link(y,x) ");
        //printNeighbours(yl);

        //logger.debug("END x.child : link(y,x) ");
        //printNeighbours(xc);

    }

    /**
     * @return the numHeapNodes
     */
    public int getNumHeapNodes() {
        return this.numHeapNodes;
    }

    /**
     * @param numHeapNodes the numHeapNodes to set
     */
    public void setNumHeapNodes(int numHeapNodes) {
        this.numHeapNodes = numHeapNodes;
    }

    // na ucel debugu
    public void printNeighbours(FibHeapNode node) {

        if (node == null) {
            logger.debug("Vypisovat susedov NULL nemozem");
        }
        else {
            FibHeapNode x = node;
            int cislo = 0;

            logger.debug("uzol: " + x.getKey() + " left: " + x.getLeft().getKey() + " right: " + x.getRight().getKey() + "\n");

            TreeMap<FibHeapNode,Integer> korene;
            korene = new TreeMap<FibHeapNode,Integer>(new FibHeapNodeComparator());
            x = x.getRight();

            Integer ii = new Integer(1);
            korene.put(node,ii);
            while (x != node) {
                if (korene.put(x, ii) != null) {
                    logger.debug("Uz tam nieco je: " + x.getKey());
                    logger.debug(node.getKey());
                    break;
                }
                if (node.getLeft() != (node.getRight()))
                    if ((x.getRight() == node) && (x != node.getLeft())) {
                        logger.debug("Lavy node nerovna sa lavemu co sa vracia!!!");
                }
                x = x.getRight();
                cislo++;
            }

            if (x == node)
                logger.debug("OK");

            x = node;

            for (int i=0; i<1000; i++) {
                logger.debug(x.getKey() + "->");
                x = x.getRight();
                if (x == node)
                    break;
            }
        }
    }
}

