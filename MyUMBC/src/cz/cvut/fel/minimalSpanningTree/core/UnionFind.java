/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.core;

/**
 * @author Shaki
 *
 */
public class UnionFind implements Union{

    private int[] id, sz;

    /* (non-Javadoc)
     * @see cz.cvut.fel.minimalSpanningTree.core.Union#find(int)
     */
    @Override
    public int find(int x) {
        while (x != this.id[x])
            x = this.id[x];

        return x;
    }

    public UnionFind(int N) {
        id = new int[N];
        sz = new int[N];

        for (int i=0; i<N; i++) {
            id[i] = i;
            sz[i] = 1;
        }
    }

    /* (non-Javadoc)
     * @see cz.cvut.fel.minimalSpanningTree.core.Union#find(int, int)
     */
    @Override
    public boolean find(int p, int q) {
        return ( find(p) == find(q) );
    }

    /* (non-Javadoc)
     * @see cz.cvut.fel.minimalSpanningTree.core.Union#unite(int, int)
     */
    @Override
    public void unite(int p, int q) {
        int i = find(p);
        int j = find(q);
        if (i==j)
            return;
        if (sz[i] < sz[j]) {
            id[i] = j;
            sz[j] += sz[i];
        }
        else {
            id[j] = i;
            sz[i] += sz[j];
        }
    }
}
