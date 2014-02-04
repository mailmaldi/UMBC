package umbc.practice.BinaryTree;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import umbc.practice.MutableInt;

public class SerializeTree
{

    public static String serialize(BinaryTree tree)
    {
        if (tree == null)
            return null;
        StringBuilder sb = new StringBuilder();
        serialize(tree.root, sb);
        return sb.toString();
    }

    private static void serialize(Node root, StringBuilder sb)
    {
        if (root == null)
            sb.append('#').append(' ');
        else
        {
            sb.append(root.data).append(' ');
            serialize(root.left, sb);
            serialize(root.right, sb);
        }

    }

    public static String serializeIterative(BinaryTree tree)
    {
        if (tree == null)
            return null;
        StringBuilder sb = new StringBuilder();
        Node curr = tree.root;
        Stack<Node> stack = new Stack<Node>();
        stack.push(curr);

        while (!stack.isEmpty())
        {
            curr = stack.pop();
            if (curr == null)
            {
                sb.append("# ");
                continue;
            }
            sb.append(curr.data).append(' ');
            stack.push(curr.right);
            stack.push(curr.left);
        }

        return sb.toString();
    }

    public static BinaryTree deserializeTree(String string)
    {
        BinaryTree tree = new BinaryTree();

        Scanner scanner = new Scanner(string).useDelimiter("\\s+");
        if (!scanner.hasNext())
            return tree;

        tree.root = deserialize(scanner);

        return tree;
    }

    private static Node deserialize(Scanner scanner)
    {
        if (!scanner.hasNext())
        {
            // this shd not hit
            System.out.println("reached end of scanner");
            return null;
        }

        String token = scanner.next();

        if (token.equals("#"))
        {
            return null;
        }

        int data = Integer.parseInt(token);

        Node root = new Node(data);

        root.left = deserialize(scanner);
        root.right = deserialize(scanner);

        return root;

    }

    public static void main(String[] args)
    {
        Node root = new Node(30);
        Node rootL = new Node(10);
        Node rootR = new Node(20);
        root.left = (rootL);
        root.right = (rootR);
        Node rootL1 = new Node(50);
        rootL.left = (rootL1);
        Node rootRL1 = new Node(45);
        Node rootRR1 = new Node(35);
        rootR.left = (rootRL1);
        rootR.right = (rootRR1);

        BinaryTree tree = new BinaryTree();
        tree.root = root;

        tree.preorder();
        tree.postorder();
        tree.inorder();

        System.out.println(serialize(tree));
        System.out.println(serializeIterative(tree));

        BinaryTree tree2 = deserializeTree(serialize(tree));
        tree2.preorder();
        tree2.postorder();
        tree2.inorder();

        ArrayList<Node> inorder = new ArrayList<Node>();
        ArrayList<Node> preorder = new ArrayList<Node>();

        serialize(tree, preorder, inorder);
        System.out.println("new serialized");
        printArrayList(preorder);
        printArrayList(inorder);

        BinaryTree tree3 = deserializeTree(preorder, inorder);
        tree3.preorder();
        tree3.postorder();
        tree3.inorder();

    }

    public static void serialize(BinaryTree tree, ArrayList<Node> preorder,
            ArrayList<Node> inorder)
    {
        if (tree == null)
            return;

        serializePreorder(tree.root, preorder);
        serializeinorder(tree.root, inorder);

    }

    private static void serializeinorder(Node root, ArrayList<Node> inorder)
    {
        if (root == null)
            return;

        serializeinorder(root.left, inorder);
        inorder.add(root);
        serializeinorder(root.right, inorder);

    }

    private static void serializePreorder(Node root, ArrayList<Node> preorder)
    {
        if (root == null)
            return;

        preorder.add(root);
        serializePreorder(root.left, preorder);
        serializePreorder(root.right, preorder);

    }

    public static BinaryTree deserializeTree(ArrayList<Node> preorder,
            ArrayList<Node> inorder)
    {
        BinaryTree tree = new BinaryTree();

        if (preorder == null || preorder.size() == 0 || inorder == null
                || inorder.size() == 0 || preorder.size() != inorder.size())
            return tree;

        MutableInt preIndex = new MutableInt(0);

        tree.root = deserializeTree(preorder, inorder, preIndex, 0,
                inorder.size() - 1);

        return tree;

    }

    private static Node deserializeTree(ArrayList<Node> preorder,
            ArrayList<Node> inorder, MutableInt preIndex, int inIndexStart,
            int inIndexEnd)
    {
        if (inIndexStart > inIndexEnd)
        {
            // will hit this?
            System.out.println("inindexstart > inIndexEnd ");
            return null;
        }
        if (preIndex.value >= preorder.size())
        {
            // should never hit this
            System.out.println("PreIndex > preorder.size()");
            return null;
        }

        Node root = new Node(preorder.get(preIndex.value).data);
        preIndex.value++;

        // this condition makes no difference, only speeds up execution
        if (inIndexStart == inIndexEnd)
            return root;

        int inIndex = getInOrderIndex(root, inorder, inIndexStart, inIndexEnd);

        root.left = deserializeTree(preorder, inorder, preIndex, inIndexStart,
                inIndex - 1);

        root.right = deserializeTree(preorder, inorder, preIndex, inIndex + 1,
                inIndexEnd);

        return root;

    }

    private static int getInOrderIndex(Node node, ArrayList<Node> inorder,
            int start, int end)
    {
        for (int i = start; i <= end; i++)
        {

            if (inorder.get(i).data == node.data)
                return i;
        }
        return -1;
    }

    public static void printArrayList(ArrayList<Node> list)
    {
        for (Node node : list)
        {
            System.out.print(node.data + " ");
        }
        System.out.println();
    }
}
