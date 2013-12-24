/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support;

/**
 * 
 * Support class for the Fibonnaci heap. Implements a node of the Fibonacci heap.
 * 
 * @author Shaki
 * @since May 2009
 * 
 */

public class FibHeapNode
{

	/**
	 * first child node
	 */
	private FibHeapNode child;

	/**
	 * parent node
	 */
	private FibHeapNode parent;

	/**
	 * left sibling node
	 */
	private FibHeapNode left;

	/**
	 * right sibling node
	 */
	private FibHeapNode right;

	/**
	 * 
	 * true or false
	 * 
	 * it is set to true if this node has had a child removed since this node was added to its parent.
	 */
	private boolean mark;

	/**
	 * key value for this node (in my case edge length)
	 */
	private double key;

	/**
	 * number of children of this node (does not count grandchildren)
	 */
	private int degree;

	/**
	 * Default constructor.
	 * 
	 * In Fibonacci heap, the roots must be stored in circle double-linked list. Initializes the right and left pointers.
	 * 
	 * @param key
	 *            initial key for node
	 */
	public FibHeapNode(double key)
	{
		setRight(this);
		setLeft(this);
		setKey(key);
	}

	/**
	 * Get the key of this node (edge length).
	 * 
	 * @return the key
	 */
	public final double getKey()
	{
		return this.key;
	}

	/**
	 * Return the string representation of this object.
	 * 
	 * @return string representing this object
	 */
	@Override
	public String toString()
	{

		if (true)
		{
			return Double.toString(getKey());
		}

		StringBuffer sb = new StringBuffer();
		sb.append("Node=[parent = ");

		if (this.parent != null)
		{
			sb.append(Double.toString(getParent().getKey()));
		}
		else
		{
			sb.append("---");
		}

		sb.append(", key = ");
		sb.append(Double.toString(getKey()));
		sb.append(", degree = ");
		sb.append(Integer.toString(getDegree()));
		sb.append(", right = ");

		if (this.right != null)
		{
			sb.append(Double.toString(getRight().getKey()));
		}
		else
		{
			sb.append("---");
		}

		sb.append(", left = ");

		if (this.left != null)
		{
			sb.append(Double.toString(getLeft().getKey()));
		}
		else
		{
			sb.append("---");
		}

		sb.append(", child = ");

		if (this.child != null)
		{
			sb.append(Double.toString(getChild().getKey()));
		}
		else
		{
			sb.append("---");
		}

		sb.append(']');

		return sb.toString();
	}

	/**
	 * @return the child
	 */
	public FibHeapNode getChild()
	{
		return this.child;
	}

	/**
	 * @param child
	 *            the child to set
	 */
	public void setChild(FibHeapNode child)
	{
		this.child = child;
	}

	/**
	 * @return the parent
	 */
	public FibHeapNode getParent()
	{
		return this.parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(FibHeapNode parent)
	{
		this.parent = parent;
	}

	/**
	 * @return the left
	 */
	public FibHeapNode getLeft()
	{
		return this.left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(FibHeapNode left)
	{
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public FibHeapNode getRight()
	{
		return this.right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(FibHeapNode right)
	{
		this.right = right;
	}

	/**
	 * @return the mark
	 */
	public boolean isMark()
	{
		return this.mark;
	}

	/**
	 * @param mark
	 *            the mark to set
	 */
	public void setMark(boolean mark)
	{
		this.mark = mark;
	}

	/**
	 * @return the degree
	 */
	public int getDegree()
	{
		return this.degree;
	}

	/**
	 * @param degree
	 *            the degree to set
	 */
	public void setDegree(int degree)
	{
		this.degree = degree;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(double key)
	{
		this.key = key;
	}
}
