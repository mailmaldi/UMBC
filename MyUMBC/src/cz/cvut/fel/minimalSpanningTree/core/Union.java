/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.core;

/**
 * Union interface
 *
 * @author Shaki
 * @since October 2008
 */
public interface Union {

    /**
     * finds the representative of the coherence component
     *
     * @param x coherence component
     * @return representative
     */
    public int find(int x);

    /**
     *
     *
     * @param p
     * @param q
     * @return
     */
    public boolean find(int p, int q);

    /**
     * unites two coherence components p and q
     * @param p first coherence component to be united
     * @param q second coherence component to be united
     */
    public void unite(int p, int q);
}
