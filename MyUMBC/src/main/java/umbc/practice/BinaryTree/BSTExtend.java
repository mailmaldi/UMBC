package umbc.practice.BinaryTree;

public class BSTExtend extends BinaryTree
{

    public BSTExtend()
    {
        super();
    }

    public void insert(int data)
    {
        insert(new Node(data));
    }

    public void insert(Node node)
    {
        if (node == null)
            return;
        if (this.root == null)
        {
            this.root = node;
            return;
        }

        Node current = this.root;

        while (current != null)
        {
            if (node.data == current.data)
                break; // already exists ,no duplicates
            if (node.data < current.data)
            {
                if (current.left == null)
                {
                    current.left = node;
                    break;
                }
                current = current.left;
            }
            else
            {
                if (current.right == null)
                {
                    current.right = node;
                    break;
                }
                current = current.right;
            }
        }
    }
}
