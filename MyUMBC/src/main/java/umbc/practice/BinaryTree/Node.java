package umbc.practice.BinaryTree;

public class Node
{

    int  data;

    Node left;

    Node right;

    public Node(int data)
    {
        this.data = data;
        left = right = null;
    }

    public Node(int data, Node left, Node right)
    {
        this.data = data;
        this.left = left;
        this.right = right;
    }

}
