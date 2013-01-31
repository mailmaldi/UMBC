package com.umbc.practice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import javax.print.attribute.standard.Finishings;

public class BinaryTree
{

	public static void main(String[] args) throws Throwable
	{
		// TODO Auto-generated method stub
		/*
		 * 30 10 20 50 45 35
		 */
		BTreeNode root = new BTreeNode(null, null, 30);
		BTreeNode rootL = new BTreeNode(null, null, 10);
		BTreeNode rootR = new BTreeNode(null, null, 20);
		root.setLeft(rootL);
		root.setRight(rootR);
		BTreeNode rootL1 = new BTreeNode(null, null, 50);
		rootL.setLeft(rootL1);
		BTreeNode rootRL1 = new BTreeNode(null, null, 45);
		BTreeNode rootRR1 = new BTreeNode(null, null, 35);
		rootR.setLeft(rootRL1);
		rootR.setRight(rootRR1);

		BTreeNode.preorderTraversal(root);
		System.out.println();
		BTreeNode.preorderIterative(root);
		System.out.println("\npreorder end");
		BTreeNode.postorderTraversal(root);
		System.out.println();
		BTreeNode.postorderIterative(root);
		System.out.println("\npostorder end");
		BTreeNode.inorderTraversal(root);
		System.out.println();
		BTreeNode.inorderIterative(root);
		System.out.println("\ninorder end");
		String string = BTreeNode.serializeTree(root, "");
		System.out.println(string);
		BTreeNode newRoot = BTreeNode.deserializeTree(string);
		BTreeNode.preorderTraversal(newRoot);
		System.out.println("end");
		BTreeNode.postorderTraversal(newRoot);
		System.out.println("end");
		BTreeNode.inorderTraversal(newRoot);
		System.out.println("end");

		System.out.println("max depth:" + BTreeNode.maxDepth(root));
		System.out.println("min depth:" + BTreeNode.minDepth(root));

		int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		BTreeNode node = BTreeNode.createMinimalBST(array);
		BTreeNode.preorderTraversal(node);
		System.out.println("end");
		BTreeNode.postorderTraversal(node);
		System.out.println("end");
		BTreeNode.inorderTraversal(node);
		System.out.println("end");

		System.out.println(BTreeNode.getDiameter(root));
		System.out.println(BTreeNode.getDiameter(node));
		
		System.out.print("path: ");
		BTreeNode.printList(BTreeNode.findPath(node, 3));

		BTreeNode tester = BTreeNode.inOrderSuccessor(6, node);
		tester = BTreeNode.findNodeParent(node, tester);

		// BTreeNode delete = BTreeNode.remove(6, node);
		// System.out.print("deleted: ");
		// delete.printValue();
		// System.out.println();
		BTreeNode.deleteNode(node, BTreeNode.findNode(node, 5));

		BTreeNode.inorderTraversal(node);
		System.out.println("end");

	}
}

class BTreeNode
{
	private BTreeNode left;

	private BTreeNode right;

	private int value;

	public BTreeNode(BTreeNode left, BTreeNode right, int value)
	{
		this.left = left;
		this.right = right;
		this.value = value;
	}

	public BTreeNode getLeft()
	{
		return left;
	}

	public BTreeNode getRight()
	{
		return right;
	}

	public void setLeft(BTreeNode left)
	{
		this.left = left;
	}

	public void setRight(BTreeNode right)
	{
		this.right = right;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public void printValue()
	{
		System.out.print(value + " ");
	}

	public static List<BTreeNode> findPath(BTreeNode root, int value)
	{
		List<BTreeNode> path = new ArrayList<BTreeNode>();
		while (root != null)
		{
			path.add(root);
			int currval = root.getValue();
			if (currval == value)
			{
				break;
			}
			if (currval < value)
			{
				root = root.getRight();
			}
			else
			{ // currval > value
				root = root.getLeft();
			}
		}
		return path;
	}

	public static BTreeNode findNode(BTreeNode root, int value)
	{
		while (root != null)
		{
			int currval = root.getValue();
			if (currval == value)
				break;
			if (currval < value)
			{
				root = root.getRight();
			}
			else
			{ // currval > value
				root = root.getLeft();
			}
		}
		return root;
	}

	public static BTreeNode findNodeParent(BTreeNode root, BTreeNode node)
	{
		BTreeNode parent = null;
		while (root != null)
		{
			int currval = root.getValue();
			if (currval == node.getValue())
				break;
			if (currval < node.getValue())
			{
				parent = root;
				root = root.getRight();
			}
			else
			{ // currval > value
				parent = root;
				root = root.getLeft();
			}
		}
		return parent;
	}

	public static BTreeNode findNodeRecursive(BTreeNode root, int value)
	{
		if (root == null)
			return null;
		int currval = root.getValue();
		if (currval == value)
			return root;
		if (currval < value)
		{
			return findNode(root.getRight(), value);
		}
		else
		{ // currval > value
			return findNode(root.getLeft(), value);
		}
	}

	public static void insertIterative(BTreeNode m_root, int data)
	{
		if (m_root == null)
		{
			m_root = new BTreeNode(null, null, data);
			return;
		}
		BTreeNode root = m_root;
		while (root != null)
		{
			/*
			 * // Choose not add 'data' if already present (an implementation decision) if (data == root.getValue()) { return; } else
			 */
			if (data < root.getValue())
			{
				// insert left
				if (root.getLeft() == null)
				{
					root.setLeft(new BTreeNode(null, null, data));
					return;
				}
				else
				{
					root = root.getLeft();
				}
			}
			else
			{
				// insert right
				if (root.getRight() == null)
				{
					root.setRight(new BTreeNode(null, null, data));
					return;
				}
				else
				{
					root = root.getRight();
				}
			}
		}
	}

	public static void insertRecusrive(BTreeNode m_root, int data)
	{
		if (m_root == null)
		{
			m_root = new BTreeNode(null, null, data);
		}
		else
		{
			internalInsert(m_root, data);
		}
	}

	private static void internalInsert(BTreeNode node, int data)
	{
		// Choose not add 'data' if already present (an implementation decision)
		/*
		 * if (data == node.getValue()) { return; } else
		 */
		if (data < node.getValue())
		{
			if (node.getLeft() == null)
			{
				node.setLeft(new BTreeNode(null, null, data));
			}
			else
			{
				internalInsert(node.getLeft(), data);
			}
		}
		else
		{
			if (node.getRight() == null)
			{
				node.setRight(new BTreeNode(null, null, data));
			}
			else
			{
				internalInsert(node.getRight(), data);
			}
		}
	}

	// http://en.wikipedia.org/wiki/Binary_search_tree#Deletion
	// 1. Deleting a leaf (node with no children): Deleting a leaf is easy, as we can simply remove it from the tree.
	// 2. Deleting a node with one child: Remove the node and replace it with its child.
	// 3. Deleting a node with two children: Call the node to be deleted N. Do not delete N. Instead, choose either its in-order successor node or its in-order predecessor node, R.
	// Replace the value of N with the value of R, then delete R.
	public static void deleteNode(BTreeNode root, BTreeNode node)
	{
		if (root == null)
			return;
		if (node.getLeft() == null && node.getRight() == null)
		{
			// find parent and set this particular child null & return
			BTreeNode temp = findNodeParent(root, node);
			if (temp.getLeft() == node)
				temp.setLeft(null);
			else if (temp.getRight() == node)
				temp.setRight(null);
		}
		else if (node.getLeft() != null && node.getRight() == null)
		{
			// copy value of left child into this & set its left & right as left's left & left's right
			BTreeNode temp = node.getLeft();
			node.setValue(temp.getValue());
			node.setLeft(temp.getLeft());
			node.setRight(temp.getRight());
		}
		else if (node.getRight() != null && node.getLeft() == null)
		{
			// similar as above
			BTreeNode temp = node.getRight();
			node.setValue(temp.getValue());
			node.setLeft(temp.getLeft());
			node.setRight(temp.getRight());
		}
		else
		{
			// has both children
			// find its in-order predecessor or successor R, replace value of N with R , delete R (recursive?)
			// As with all binary trees,
			// a node's in-order successor is the left-most child of its right subtree,
			// a node's in-order predecessor is the right-most child of its left subtree.
			// In either case, this node will have zero or one children. Delete it according to one of the two simpler cases above.
			BTreeNode temp = inOrderSuccessor(node, root);
			deleteNode(root, temp);
			node.setValue(temp.getValue());
		}

	}

	public static BTreeNode remove(int x, BTreeNode t)
	{
		if (t == null)
			return t; // Item not found; do nothing
		if (x < t.getValue())
			t.setLeft(remove(x, t.getLeft()));
		else if (x > t.getValue())
			t.setRight(remove(x, t.getRight()));
		else if (t.getLeft() != null && t.getRight() != null) // Two children
		{
			t.setValue(getMinimumFromNode(t.getRight()).getValue());
			t.setRight(remove(t.getValue(), t.getRight()));
		}
		else
			t = (t.getLeft() != null) ? t.getLeft() : t.getRight();
		return t;
	}

	// in order successor, Trees.pdf page 23
	// The successor of a node xcan be found out using the following rules:
	// a) If the right sub-tree of x is non-empty, the successor is simply the left-mostnode in the right sub-tree.
	// b) Otherwise, the successor is the lowest ancestor of xwhose left childis also an ancestor of x.
	// let initially current_node be root, succ_node = null;

	// case1: If the search element is less than current_node, then the current element is a potential successor - place succ_node at the current_node and move the current_node to
	// its left node(because the search element is in the left subtree)

	// case2: If the search element is greater then current_node, its not a potential successor (How can a lesser element be the successor?). So no need to place the succ_node
	// here, but move the current_node to right.

	// keep repeating the process until you reach null or the element itself and return the succ_node.
	// http://stackoverflow.com/questions/3796003/find-the-successor-in-a-bst-without-using-parent-pointer
	// perhaps ,http://codereview.stackexchange.com/questions/13255/deleting-a-node-from-binary-search-tree
	public static BTreeNode inOrderSuccessor(BTreeNode node, BTreeNode root)
	{
		if (node.getRight() != null)
		{
			return getMinimumFromNode(node.getRight());
		}
		return inOrderSuccesorHelper(node, root);

	}

	public static BTreeNode inOrderSuccessor(int value, BTreeNode root)
	{
		BTreeNode node = findNode(root, value);
		return inOrderSuccessor(node, root);
	}

	private static BTreeNode inOrderSuccesorHelper(BTreeNode node, BTreeNode root)
	{
		BTreeNode curr_node = root;
		BTreeNode succ_node = null;

		while (curr_node != null && curr_node != node)
		{
			if (node.getValue() < curr_node.getValue())
			{
				succ_node = curr_node;
				curr_node = curr_node.getLeft();
			}
			else
			// node's value is greater than root
			{
				curr_node = curr_node.getRight();
			}
		}

		return succ_node;
	}

	public static BTreeNode getMinimumFromNode(BTreeNode root)
	{
		BTreeNode node = root;
		if (node == null)
			return null;
		while (node.getLeft() != null)
			node = node.getLeft();
		return node;
	}

	public static BTreeNode getMaximumFromNode(BTreeNode root)
	{
		BTreeNode node = root;
		if (node == null)
			return null;
		while (node.getRight() != null)
			node = node.getRight();
		return node;
	}

	public static void preorderTraversal(BTreeNode root)
	{
		if (root == null)
			return;
		root.printValue();
		preorderTraversal(root.getLeft());
		preorderTraversal(root.getRight());
	}

	public static void inorderTraversal(BTreeNode root)
	{
		if (root == null)
			return;
		inorderTraversal(root.getLeft());
		root.printValue();
		inorderTraversal(root.getRight());
	}

	public static void postorderTraversal(BTreeNode root)
	{
		if (root == null)
			return;
		postorderTraversal(root.getLeft());
		postorderTraversal(root.getRight());
		root.printValue();
	}

	public static void preorderIterative(BTreeNode root)
	{
		Stack<BTreeNode> stack = new Stack<BTreeNode>();
		stack.push(root);
		while (!stack.isEmpty())
		{
			BTreeNode curr = stack.pop();

			curr.printValue();
			BTreeNode n = curr.getRight();
			if (n != null)
				stack.push(n);
			n = curr.getLeft();
			if (n != null)
				stack.push(n);
		}
	}

	public static void inorderIterative(BTreeNode root)
	{
		Stack<BTreeNode> stack = new Stack<BTreeNode>();
		BTreeNode node = root;

		while (!stack.isEmpty() || node != null)
		{
			if (node != null)
			{
				stack.push(node);
				node = node.getLeft();
			}
			else
			{
				node = stack.pop();
				node.printValue();
				node = node.getRight();
			}
		}
	}

	/** Iteratively traverses the binary tree in post-order */
	public static void postorderIterative(BTreeNode root)
	{
		if (root == null)
			return;

		Stack<BTreeNode> stack = new Stack<BTreeNode>();
		BTreeNode current = root;

		while (true)
		{

			if (current != null)
			{
				if (current.getRight() != null)
					stack.push(current.getRight());
				stack.push(current);
				current = current.getLeft();
				continue;
			}

			if (stack.isEmpty())
				return;
			current = stack.pop();

			if (current.getRight() != null && !stack.isEmpty() && current.getRight() == stack.peek())
			{
				stack.pop();
				stack.push(current);
				current = current.getRight();
			}
			else
			{
				current.printValue();
				current = null;
			}
		}
	}

	public static BTreeNode findLowestCommonAncestor(BTreeNode root, int value1, int value2)
	{
		while (root != null)
		{
			int value = root.getValue();
			if (value > value1 && value > value2)
			{
				root = root.getLeft();
			}
			else if (value < value1 && value < value2)
			{
				root = root.getRight();
			}
			else
			{
				return root;
			}
		}
		return null; // only if empty tree
	}

	// Overload it to handle nodes as well
	public static BTreeNode findLowestCommonAncestor(BTreeNode root, BTreeNode child1, BTreeNode child2)
	{
		if (root == null || child1 == null || child2 == null)
		{
			return null;
		}
		return findLowestCommonAncestor(root, child1.getValue(), child2.getValue());
	}

	public static String serializeTree(BTreeNode root, String string)
	{
		StringBuffer sb = new StringBuffer("");
		if (root == null)
		{
			sb.append(" # ");
		}
		else
		{
			sb.append(root.getValue()).append(" ");
			sb.append(serializeTree(root.getLeft(), sb.toString()));
			sb.append(serializeTree(root.getRight(), sb.toString()));
		}
		return sb.toString();
	}

	public static BTreeNode deserializeTree(String serialTree) throws Throwable
	{
		Scanner s = new Scanner(serialTree).useDelimiter("\\s+");
		if (!s.hasNext())
			return null;
		BTreeNode root = new BTreeNode(null, null, -1);
		deserialize(root, root, s);
		return root;
	}

	private static boolean deserialize(BTreeNode node, BTreeNode child, Scanner s) throws Throwable
	{
		if (!s.hasNext())
			return true;
		String temp = s.next();
		int token = 0;
		boolean isNumber = true;
		if (temp.equals("#"))
		{
			isNumber = false;
			return false;
		}
		else
		{
			token = Integer.parseInt(temp);
		}
		if (isNumber)
		{
			BTreeNode nodeL = new BTreeNode(null, null, -1);
			BTreeNode nodeR = new BTreeNode(null, null, -1);
			child.setLeft(nodeL);
			child.setRight(nodeR);
			child.setValue(token);
			if (!deserialize(child, child.getLeft(), s))
				child.setLeft(null);
			if (!deserialize(child, child.getRight(), s))
				child.setRight(null);
		}
		return true;
	}

	private static void deserialize(BTreeNode node, Scanner s) throws Throwable
	{
		if (!s.hasNext())
			return;
		String temp = s.next();
		int token = 0;
		boolean isNumber = true;
		if (temp.equals("#"))
		{
			isNumber = false;
		}
		else
		{
			token = Integer.parseInt(temp);
		}
		if (isNumber)
		{
			BTreeNode nodeL = new BTreeNode(null, null, -1);
			BTreeNode nodeR = new BTreeNode(null, null, -1);
			node.setLeft(nodeL);
			node.setRight(nodeR);
			node.setValue(token);
			deserialize(node.getLeft(), s);
			deserialize(node.getRight(), s);
		}
	}

	/**
	 * depth of tree
	 */

	public static int maxDepth(BTreeNode root)
	{
		if (root == null)
		{
			return 0;
		}
		return 1 + Math.max(maxDepth(root.getLeft()), maxDepth(root.getRight()));
	}

	public static int minDepth(BTreeNode root)
	{
		if (root == null)
		{
			return 0;
		}
		return 1 + Math.min(minDepth(root.getLeft()), minDepth(root.getRight()));
	}

	public static boolean isBalanced(BTreeNode root)
	{
		return (maxDepth(root) - minDepth(root) <= 1);
	}

	/**
	 * create BST from sorted array of minimal height
	 */
	public static BTreeNode addToTree(int arr[], int start, int end)
	{
		if (end < start)
		{
			return null;
		}
		int mid = (start + end) / 2;
		BTreeNode n = new BTreeNode(null, null, arr[mid]);
		n.setLeft(addToTree(arr, start, mid - 1));
		n.setRight(addToTree(arr, mid + 1, end));
		return n;
	}

	public static BTreeNode createMinimalBST(int array[])
	{
		return addToTree(array, 0, array.length - 1);
	}

	/**
	 * Given a binary search tree, design an algorithm which creates a linked list of all the nodes at each depth (eg, if you have a tree with depth D, you’ll have D linked lists)
	 * 
	 * Hint: use dummy node in queue to delineate
	 */
	public static ArrayList<LinkedList<BTreeNode>> findLevelLinkList(BTreeNode root)
	{
		int level = 0;
		ArrayList<LinkedList<BTreeNode>> result = new ArrayList<LinkedList<BTreeNode>>();
		LinkedList<BTreeNode> list = new LinkedList<BTreeNode>();
		list.add(root);
		result.add(level, list);
		while (true)
		{
			list = new LinkedList<BTreeNode>();
			for (int i = 0; i < result.get(level).size(); i++)
			{
				BTreeNode n = (BTreeNode) result.get(level).get(i);
				if (n != null)
				{
					if (n.getLeft() != null)
						list.add(n.getLeft());
					if (n.getRight() != null)
						list.add(n.getRight());
				}
			}
			if (list.size() > 0)
			{
				result.add(level + 1, list);
			}
			else
			{
				break; // I dont think need this, level+1 with an empty list makes it easy to iterate
			}
			level++;
		}
		return result;
	}

	// stackoverflow.com/questions/3124566/binary-tree-longest-path-between-2-nodes
	// http://www.careercup.com/question?id=15261804
	// Find the longest tree walk in a tree. The question is to print the longest path in a tree. the path need not got through the root. The path should be between two leaves.

	private static int max = 0;

	private static Stack<BTreeNode> path;

	public static Stack<BTreeNode> longestPath(BTreeNode root)
	{
		if (root == null)
		{
			Stack<BTreeNode> s = new Stack<BTreeNode>();
			return s;
		}
		Stack<BTreeNode> l = longestPath(root.getLeft());
		Stack<BTreeNode> r = longestPath(root.getRight());
		if (l.size() + r.size() + 1 > max)
		{
			max = l.size() + r.size() + 1;
			Stack<BTreeNode> tmp = new Stack<BTreeNode>();
			tmp.addAll(l);
			tmp.push(root);
			tmp.addAll(r);
			path = tmp;
		}
		l.push(root);
		r.push(root);
		return l.size() > r.size() ? l : r;
	}

	// Same as above, http://stackoverflow.com/questions/11897088/diameter-of-binary-tree-better-design
	// There are three cases to consider when trying to find the longest path between two nodes in a binary tree (diameter):
	// 1. The longest path passes through the root,
	// 2. The longest path is entirely contained in the left sub-tree,
	// 3. The longest path is entirely contained in the right sub-tree.
	// The longest path through the root is simply the sum of the heights of the left and right sub-trees + 1 (for the root node),
	// and the other two can be found recursively:
	public static long getDiameter(BTreeNode root)
	{
		if (root != null)
		{
			long leftDiameter = getDiameter(root.getLeft());
			long rightDiameter = getDiameter(root.getRight());
			long leftHeight = getHeight(root.getLeft());
			long rightHeight = getHeight(root.getRight());
			return Math.max(leftHeight + rightHeight + 1, Math.max(leftDiameter, rightDiameter));
		}
		return 0;
	}

	public static long getHeight(BTreeNode root)
	{
		if (root != null)
		{
			long leftHeight = getHeight(root.getLeft());
			long rightHeight = getHeight(root.getRight());
			return 1 + Math.max(leftHeight, rightHeight);
		}
		return 0;
	}

	public static void printList(List<BTreeNode> list)
	{
		for (BTreeNode bTreeNode : list)
		{
			System.out.print(bTreeNode.getValue() + " ");
		}
		System.out.println();
	}
}
