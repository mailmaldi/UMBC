package umbc.practice.BinaryTree;

import java.util.Stack;

public class InOrder2Trees
{
    public static void inOrderInterleave(BinaryTree tree1, BinaryTree tree2)
    {
        Node root1 = tree1.root;
        Node root2 = tree2.root;
        Stack<Node> stack1 = new Stack<Node>(), stack2 = new Stack<Node>();

        while (root1 != null || !stack1.isEmpty() || root2 != null
                || !stack2.isEmpty())
        {
            if (root1 != null || root2 != null)
            {
                if (root1 != null)
                {
                    stack1.push(root1);
                    root1 = root1.left;
                }
                if (root2 != null)
                {
                    stack2.push(root2);
                    root2 = root2.left;
                }
            }
            else if (!stack1.isEmpty() || !stack2.isEmpty())
            {
                if (!stack1.isEmpty())
                {
                    root1 = stack1.pop();
                    System.out.print(root1.data + " ");
                    root1 = root1.right;
                }
                if (!stack2.isEmpty())
                {
                    root2 = stack2.pop();
                    System.out.print(root2.data + " ");
                    root2 = root2.right;
                }
            }
        }
        System.out.println();
        System.out.println(stack1.size());
        System.out.println(stack2.size());

    }

    public static void inOrderIncreasing(BinaryTree tree1, BinaryTree tree2)
    {
        Node root1 = tree1.root;
        Node root2 = tree2.root;
        Stack<Node> stack1 = new Stack<Node>(), stack2 = new Stack<Node>();

        while (root1 != null || !stack1.isEmpty() || root2 != null
                || !stack2.isEmpty())
        {
            if (root1 != null || root2 != null)
            {
                if (root1 != null)
                {
                    stack1.push(root1);
                    root1 = root1.left;
                }
                if (root2 != null)
                {
                    stack2.push(root2);
                    root2 = root2.left;
                }
            }
            else if (!stack1.isEmpty() && !stack2.isEmpty())
            {
                if (stack1.peek().data < stack2.peek().data)
                {
                    root1 = stack1.pop();
                    System.out.print(root1.data + " ");
                    root1 = root1.right;
                }
                else
                {
                    root2 = stack2.pop();
                    System.out.print(root2.data + " ");
                    root2 = root2.right;
                }
            }
        }
        System.out.println();
        System.out.println(stack1.size());
        System.out.println(stack2.size());

    }

    public static void main(String[] args)
    {
        BSTExtend tree1 = new BSTExtend();
        BSTExtend tree2 = new BSTExtend();

        tree1.insert(50);
        tree1.insert(25);
        tree1.insert(20);
        tree1.insert(30);
        tree1.insert(75);
        tree1.insert(18);
        tree1.insert(22);

        tree2.insert(100);
        tree2.insert(80);
        tree2.insert(150);
        tree2.insert(125);
        tree2.insert(175);
        tree2.insert(140);
        tree2.insert(160);

        tree1.preorderIterative();
        tree1.inorderIterative();
        tree2.preorderIterative();
        tree2.inorderIterative();

        inOrderInterleave(tree1, tree2);

        BSTExtend tree3 = new BSTExtend();
        BSTExtend tree4 = new BSTExtend();
        for (int i = 0; i < 10; i++)
            tree4.insert(i);
        for (int i = 20; i > 10; i--)
            tree3.insert(i);
        tree3.preorder();
        tree3.inorder();
        tree4.preorder();
        tree4.inorder();
        inOrderInterleave(tree3, tree4);

    }
}
