/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support;

/**
 * @author Shaki
 * @since October 2008
 */
public class Edge extends FibHeapNode implements Comparable<Edge>
{
	/**
	 * source vertex of the edge
	 */
	private int source;

	/**
	 * destination vertex of the edge
	 */
	private int destination;

	/**
	 * length of the edge
	 */
	private double length;

	/**
     *
     */
	private boolean used;

	/**
	 * Construct a new edge with length parameter set.
	 * 
	 * @param s
	 *            source vertex.
	 * @param d
	 *            destination vertex
	 * @param l
	 *            length of the edge
	 */
	public Edge(int s, int d, double l)
	{
		super(l);

		this.setSource(s);
		this.setDestination(d);
		this.setLength(l);
		this.setUsed(false);
	}

	/**
	 * copy constructor
	 * 
	 * @param e
	 *            Edge to be copied
	 */
	public Edge(Edge e)
	{
		super(e.getLength());

		this.setSource(e.getSource());
		this.setDestination(e.getDestination());
		this.setLength(e.getLength());
	}

	/**
	 * @param source
	 *            the source vertex to set
	 */
	public void setSource(int source)
	{
		this.source = source;
	}

	/**
	 * @return name of the source vertex.
	 */
	public int getSource()
	{
		return this.source;
	}

	/**
	 * @param destination
	 *            the destination vertex to set
	 */
	public void setDestination(int destination)
	{
		this.destination = destination;
	}

	/**
	 * @return name of the destination vertex.
	 */
	public int getDestination()
	{
		return this.destination;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(double length)
	{
		this.length = length;
	}

	/**
	 * @return the length of the edge
	 */
	public double getLength()
	{
		return this.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@SuppressWarnings("boxing")
	@Override
	public int compareTo(Edge arg0)
	{
		return ((Double) this.length).compareTo(arg0.length);
	}

	/**
	 * @param arg0
	 *            Edge to compare
	 * @return true if source and destination of edges are the same
	 */
	@SuppressWarnings("boxing")
	@Override
	public boolean equals(Object o)
	{
		if ((this.getSource() == ((Edge) o).getSource()) && (this.getDestination() == ((Edge) o).getDestination())
		// && ( this.getLength() == ((Edge)o).getLength() )
		)
			return true;

		return false;

	}

	/**
	 * @return the used
	 */
	public boolean isUsed()
	{
		return this.used;
	}

	/**
	 * @param used
	 *            the used to set
	 */
	public void setUsed(boolean used)
	{
		this.used = used;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(this.source).append("-").append(this.destination);
		return sb.toString();
	}
}
