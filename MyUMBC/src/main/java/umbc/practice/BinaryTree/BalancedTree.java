package umbc.practice.BinaryTree;

public class BalancedTree
{

    public static boolean isBalanced(BinaryTree tree)
    {
        if (tree == null)
            return false;

        return isBalanced(tree.root);
    }

    private static boolean isBalanced(Node root)
    {
        if (maxDepth(root) - minDepth(root) <= 1)
            return true;
        return false;
    }

    private static int maxDepth(Node root)
    {
        if (root == null)
            return 0;
        int ldepth = maxDepth(root.left);
        int rdepth = maxDepth(root.right);

        return 1 + Math.max(ldepth, rdepth);

    }

    private static int minDepth(Node root)
    {
        if (root == null)
            return 0;
        int ldepth = minDepth(root.left);
        int rdepth = minDepth(root.right);

        return 1 + Math.min(ldepth, rdepth);

    }

}
