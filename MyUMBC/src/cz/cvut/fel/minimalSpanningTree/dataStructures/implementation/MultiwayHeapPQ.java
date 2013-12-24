/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.dataStructures.implementation;


/**
 * Multiway heap priority queue representation of the graph
 * @extends GraphADT
 * @author Shaki
 * @since November 2008
 *
 */
public class MultiwayHeapPQ {

    // private representations

    private int N;
    /**
     * d-way heap
     */
    private int d = 3;

    /**
     * Array of lengths of the edges in the graph
     */
    private double[] weights;

    /**
     * Priority queue
     */
    private int[] pq;

    /**
     * Inverse array priority queue
     */
    private int[] qp;

    // constructors

    /**
     * @param maxN number of vertices of the graph
     * @param weights array of lengths of the edges
     */
    public MultiwayHeapPQ(int maxN, double[] weights) {
        this.setWeights(weights);
        this.N = 0;
        this.pq = new int[maxN+1];
        this.qp = new int[maxN+1];

        for (int i=0; i<= maxN; i++) {
            this.pq[i] = 0;
            this.qp[i] = 0;
        }
    }

    // methods - for altering data graph structure

    /**
     * Compares two items
     * @param i first item
     * @param j second item
     * @return true if i is lesser than j
     *
     */
    private boolean less(int i, int j) {
        return this.weights[this.pq[i]] < this.weights[this.pq[j]];
    }

    /**
     * Swap (change) two items
     * @param i first item to be changed
     * @param j second item to be changed
     */
    private void swap(int i, int j) {
        int hlp = this.pq[i];
        this.pq[i] = this.pq[j];
        this.pq[j] = hlp;

        this.qp[this.pq[i]] = i;
        this.qp[this.pq[j]] = j;
    }

    /**
     * Swim = bottom-up heapify:
     *
     * We got heap-ordered tree except for the node T on the bottom level. If we
     * exchange T with its parent, the tree is heap-ordered, except possibility
     * that T may be larger than its new parent. Continuing to exchange T with
     * its parent until we encountered the root or a node on the path from T to
     * the root that is larger than T, we can establish the heap condition for
     * the whole tree.
     *
     * @param k node which we compare with its parent and if it is bigger we
     * switch them
     */
    private void swim(int k) {
        while (k>1 && less(k, (k+this.d-2)/this.d)) {
            swap(k, (k+this.d-2)/this.d);
            k = (k+this.d-2)/this.d;
        }
    }

    /**
     * Sink = top-down heapify:
     *
     * We got heap-ordered tree except for the node T, which is a root of some
     * subtree. We exchange T with larger of its two children, the tree is
     * heap-ordered, except possibility that T may be smaller than one of its
     * new children. We continue to exchange T until its all OK.
     * @param k position of the node smaller than one of its two children
     * @param N number of vertices
     */
    private void sink(int k, int N) {
        int j;

        while ( (j=this.d*(k-1)+2) <= N ) {

            for (int i=j+1; i<j+this.d && i<=N; i++)
                if ( less(i,j))
                    j = i;
            if ( !(less(j,k)) )
                break;
            swap(k,j);
            k = j;
        }
    }

    /**
     * Checks if the queue is empty
     * @return true if queue is empty, false if queue is not empty
     */
    public boolean empty() {
        return this.N==0;
    }

    /**
     * @param v
     */
    public void insert(int v) {
        this.pq[++this.N] = v;
        this.qp[v] = this.N;
        swim(this.N);
    }

    /**
     * @return
     */
    public int getMin() {
        swap(1,this.N);
        sink(1,this.N-1);

        return this.pq[this.N--];
    }

    /**
     * @param k
     */
    public void lower(int k) {
        swim(this.qp[k]);
    }

    /**
     * @param weights the lengths of the edges to set
     */
    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    /**
     * @return the lengths of the edges
     */
    public double[] getWeights() {
        return this.weights;
    }
}   // end class MultiwayHeapPQ
