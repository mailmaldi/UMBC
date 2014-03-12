package umbc.practice.zillow;


class TNode
{
    int   key;
    TNode left;
    TNode right;
    TNode middle;

    public TNode(int key)
    {
        this(key, null, null, null);
    }

    public TNode(int key, TNode left, TNode right, TNode middle)
    {
        this.key = key;
        this.left = left;
        this.right = right;
        this.middle = middle;
    }
}

public class TrinaryTree
{
    TNode root;

    /**
     * Constructors, make empty, initialize with some key
     */
    public TrinaryTree()
    {
        this(null);
    }

    private TrinaryTree(TNode root)
    {
        this.root = root;
    }

    public TrinaryTree(int key)
    {
        this(new TNode(key));
    }

    /**
     * Recursive version of insert
     * 
     * @param key
     *            to be inserted
     */
    public void insert(int key)
    {
        TNode node = new TNode(key);
        if (root == null)
            root = node;
        else
            insert(root, node);
    }

    private void insert(TNode root, TNode node)
    {
        // trinary, so check for middle here
        if (root.key == node.key)
        {
            if (root.middle == null)
                root.middle = node;
            else
                insert(root.middle, node);
        }
        // insert in left subtree
        else if (root.key > node.key)
        {
            if (root.left == null)
                root.left = node;
            else
                insert(root.left, node);
        }
        // insert in right sub tree
        else
        {
            if (root.right == null)
                root.right = node;
            else
                insert(root.right, node);
        }
    }

    /**
     * another recursive implementation
     * 
     * @param key
     */
    public void insertRecursive(int key)
    {
        this.root = insertRecursive(key, root);
    }

    private TNode insertRecursive(int key, TNode current)
    {
        if (current == null)
            current = new TNode(key);
        else if (key == current.key)
            current.middle = insertRecursive(key, current.middle);
        else if (key < current.key)
            current.left = insertRecursive(key, current.left);
        else
            current.right = insertRecursive(key, current.right);

        return current;
    }

    /**
     * iterative version of insert
     * 
     * @param key
     */
    public void insertIterative(int key)
    {
        TNode node = new TNode(key);
        if (root == null)
        {
            root = node;
            return;
        }
        TNode current = root;

        while (current != null)
        {
            if (key == current.key)
            {
                if (current.middle == null)
                {
                    current.middle = node;
                    return;
                }
                current = current.middle;
            }
            else if (key < current.key)
            {
                if (current.left == null)
                {
                    current.left = node;
                    return;
                }
                current = current.left;
            }
            else
            {
                if (current.right == null)
                {
                    current.right = node;
                    return;
                }
                current = current.right;
            }
        }
    }

    /**
     * Deleting is a complicated affair. This is assuming deleting a key only
     * removes 1 instance from tree, and not all its middle instances. The main
     * logic of deleting in a tree remains same as in a regular BST. If key to
     * be deleted has non null middle, then simply replace itself by one of the
     * middle ones. For leaf, just remove, For 1 child, replace by child, for 2
     * child replace by largest in left subtree or smallest in right subtree,
     * i.e. in order predecessor or successor
     */
    public void delete(int key)
    {
        this.root = delete(key, this.root);
    }

    public TNode delete(int key, TNode current)
    {
        if (current == null)
        {
            // couldnt find key in the tree,
            // either raise exception or do nothing, i do nothing
            return current;
        }
        // delete in my left subtree
        else if (key < current.key)
            current.left = delete(key, current.left);
        // delete in my right subtree
        else if (key > current.key)
            current.right = delete(key, current.right);

        // now we've found what we were looking for case key == current.key

        // if the deleting key has middle, then simply remove one of them
        // if we had a logic based on deleting one of values,this would be
        // different
        else if (current.middle != null)
        {
            current.middle = delete(key, current.middle);
        }

        // now we have to delete an item that has both left & right children
        else if (current.left != null && current.right != null)
        {
            // find the minimum node in the right subtree (or max from left)
            TNode temp = getMiniMumFromNode(current.right);
            // update my current node's key from the temp
            current.key = temp.key;
            // now play with the middle pointers
            // current already has NO middle
            current.middle = temp.middle;
            temp.middle = null;
            // now delete the min node's key from right subtree
            // we already know that the min node now has no middle.
            current.right = delete(current.key, current.right);
        }
        // if one of or both child are null, then replace itself with that child
        else
        {
            // this also takes care of the case when one middle is to be deleted
            current = (current.left != null) ? current.left : current.right;
        }
        return current;
    }

    public TNode getMiniMumFromNode(TNode current)
    {
        if (current == null)
            return null;
        while (current.left != null)
            current = current.left;
        return current;
    }

    /**
     * printing tree in order, because middle cant have left & right, this is
     * uncomplicated
     */
    public void inOrder()
    {
        if (root == null)
            return;
        inOrderRecursive(root);
        System.out.println();
    }

    private void inOrderRecursive(TNode current)
    {
        if (current == null)
            return;
        else
        {
            inOrderRecursive(current.left);
            inOrderRecursive(current.middle);
            System.out.print(current.key + " ");
            inOrderRecursive(current.right);
        }
    }

    /**
     * main
     */
    public static void main(String[] args)
    {
        TrinaryTree tree = new TrinaryTree();
        int[] data = new int[]
        { 5, 5, 2, 8, 2, 8, 3, 1, 7, 9 };
        for (int i : data)
        {
            tree.insert(i);
        }
        tree.inOrder();

        TrinaryTree tree2 = new TrinaryTree();
        int[] data2 = new int[]
        { 5, 2, 1, 3, 8, 7, 9, 5, 5, 2, 2, 8, 3, 4, 4, 7 };
        for (int i : data2)
        {
            tree2.insert(i);
        }
        tree2.inOrder();
        tree2.delete(2);
        tree2.delete(2);
        tree2.delete(2);
        tree2.inOrder();
        tree2.delete(4);
        tree2.inOrder();
        tree2.delete(5);
        tree2.delete(5);
        tree2.delete(5);
        tree2.inOrder();

    }

}
