package umbc.practice.zillow;

public class TrinaryTree
{
    TNode root;

    public TrinaryTree()
    {
        this(null);
    }

    public TrinaryTree(TNode root)
    {
        this.root = root;
    }

    public TrinaryTree(int data)
    {
        this(new TNode(data));
    }

    /**
     * 
     * @param data
     *            to be inserted
     */
    public void insert(int data)
    {
        TNode node = new TNode(data);
        if (root == null)
            root = node;
        else
            insert(root, node);
    }

    private void insert(TNode root, TNode node)
    {
        // trinary, so check for middle here
        if (root.data == node.data)
        {
            if (root.middle == null)
                root.middle = node;
            else
                insert(root.middle, node);
        }
        // insert in left subtree
        else if (root.data > node.data)
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

    public void insertIterative(int data)
    {
        TNode node = new TNode(data);
        if (root == null)
        {
            root = node;
            return;
        }
        TNode current = root;

        while (current != null)
        {
            if (data == current.data)
            {
                if (current.middle == null)
                {
                    current.middle = node;
                    return;
                }
                current = current.middle;
            }
            else if (data < current.data)
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
            System.out.print(current.data + " ");
            inOrderRecursive(current.right);
        }
    }

    /**
     * 
     */
    public static void main(String[] args)
    {
        TrinaryTree tree = new TrinaryTree();
        tree.insert(5);
        tree.insert(5);
        tree.insert(2);
        tree.insert(8);
        tree.insert(2);
        tree.insert(8);
        tree.insert(3);
        tree.insert(1);
        tree.insert(7);
        tree.insert(9);
        tree.inOrder();
    }
}

class TNode
{
    int   data;
    TNode left;
    TNode right;
    TNode middle;

    public TNode(int data)
    {
        this(data, null, null, null);
    }

    public TNode(int data, TNode left, TNode right, TNode middle)
    {
        this.data = data;
        this.left = left;
        this.right = right;
        this.middle = middle;
    }
}
