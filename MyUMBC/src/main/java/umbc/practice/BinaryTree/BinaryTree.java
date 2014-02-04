package umbc.practice.BinaryTree;

import java.util.Stack;

public class BinaryTree
{

    Node root;

    public BinaryTree()
    {
        root = null;
    }

    public boolean isEmpty()
    {
        return (root == null);
    }

    /*
     * Recursive Traversals
     */
    public void preorder()
    {
        preorder(root);
        System.out.println();
    }

    private void preorder(Node node)
    {
        if (node == null)
            return;
        System.out.print(node.data + " ");
        preorder(node.left);
        preorder(node.right);
    }

    public void inorder()
    {
        inorder(root);
        System.out.println();
    }

    private void inorder(Node node)
    {
        if (node == null)
            return;
        inorder(node.left);
        System.out.print(node.data + " ");
        inorder(node.right);
    }

    public void postorder()
    {
        postorder(root);
        System.out.println();
    }

    private void postorder(Node node)
    {
        if (node == null)
            return;
        postorder(node.left);
        postorder(node.right);
        System.out.print(node.data + " ");
    }

    /*
     * End of Recursive Traversals
     */

    /*
     * Iterative Traversals
     */

    public void preorderIterative()
    {
        if (root == null)
            return;
        Stack<Node> stack = new Stack<Node>();
        stack.push(root);
        while (!stack.isEmpty())
        {
            Node node = stack.pop();
            System.out.print(node.data + " ");
            if (node.right != null)
                stack.push(node.right);
            if (node.left != null)
                stack.push(node.left);
        }
        System.out.println();
    }

    public void inorderIterative()
    {
        if (root == null)
            return;

        Stack<Node> stack = new Stack<Node>();

        Node node = root;

        while (node != null || !stack.isEmpty())
        {
            if (node != null)
            {
                stack.push(node);
                node = node.left;
            }
            else
            {
                node = stack.pop();
                System.out.print(node.data + " ");
                node = node.right;
            }
        }
        System.out.println();
    }

    public void postorderIterative()
    {
        if (root == null)
            return;

        Stack<Node> stack = new Stack<Node>();

        Node node = root;

        while (node != null || !stack.isEmpty())
        {
            if (node != null)
            {
                if (node.right != null)
                    stack.push(node.right);
                stack.push(node);
                node = node.left;
            }
            else
            {
                node = stack.pop();
                if (node.right != null && !stack.isEmpty()
                        && node.right == stack.peek())
                {
                    stack.pop();
                    stack.push(node);
                    node = node.right;
                }
                else
                {
                    System.out.print(node.data + " ");
                    node = null;
                }
            }

        }
        System.out.println();
    }
    /*
     * End of Iterative Traversals
     */

    // Serialize , Deserialize binary tree
    // use just pre-order, pre+in , level based approach

    // implement compare two trees are same or not, to test
}
