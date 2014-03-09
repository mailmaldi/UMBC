package umbc.practice.BinaryTree;

import java.util.Stack;

import umbc.practice.MutableInt;

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

    /*
     * Check if the tree is a BST
     */

    public boolean isBSTIterative()
    {
        Node node = this.root;
        Stack<Node> stack = new Stack<Node>();

        int prev = Integer.MIN_VALUE;

        while (!stack.isEmpty() || node != null)
        {
            if (node != null)
            {
                stack.push(node);
                node = node.left;
            }
            else
            {
                node = stack.pop();
                // inorder
                if (node.data < prev)
                    return false;
                else
                    prev = node.data;
                node = node.right;
            }
        }
        return true;
    }

    /*
     * this is probably not correct
     */
    public boolean isBSTrecursive()
    {
        MutableInt prev = new MutableInt(Integer.MIN_VALUE);

        return isBSTRecursive(this.root, prev);
    }

    private boolean isBSTRecursive(Node node, MutableInt prev)
    {
        if (node == null)
            return true;

        boolean left = isBSTRecursive(node.left, prev);
        boolean check;
        if (node.data < prev.value)
        {
            check = false;
        }
        else
        {
            prev.value = node.data;
            check = true;
        }
        // This implementation is probably not very correct

        boolean right = check ? isBSTRecursive(node.right, prev) : false;

        return left && check && right;
    }

    public boolean isBSTrecursive2()
    {
        MutableInt prev = new MutableInt(Integer.MIN_VALUE);

        return isBSTRecursive2(this.root, prev);
    }

    private boolean isBSTRecursive2(Node node, MutableInt prev)
    {
        if (node == null)
            return true;

        if (isBSTRecursive(node.left, prev))
        {
            if (node.data < prev.value)
            {
                return false;
            }
            else
            {
                prev.value = node.data;
                return isBSTRecursive2(node.right, prev);
            }
        }
        else
        {
            return false;
        }
    }

    public boolean isBSTRecursive3()
    {
        return isBSTRecursive3(this.root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean isBSTRecursive3(Node root, int minValue, int maxValue)
    {
        if (root == null)
            return true;

        if (root.data > minValue && root.data < maxValue)
        {
            return isBSTRecursive3(root.left, minValue, root.data)
                    && isBSTRecursive3(root, root.data, maxValue);
        }
        else
            return false;
    }

    /*
     * End of BST
     */
}
