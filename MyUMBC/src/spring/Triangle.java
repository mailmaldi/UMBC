package spring;

public class Triangle
{
	private Point A;

	private Point B;

	private Point C;

	public Point getA()
	{
		return A;
	}

	public void setA(Point a)
	{
		A = a;
	}

	public Point getB()
	{
		return B;
	}

	public void setB(Point b)
	{
		B = b;
	}

	public Point getC()
	{
		return C;
	}

	public void setC(Point c)
	{
		C = c;
	}

	public void draw()
	{
		System.out.println("A = (" + getA().getX() + "," + getA().getY() + ")");
		System.out.println("B = (" + getB().getX() + "," + getB().getY() + ")");
		System.out.println("C = (" + getC().getX() + "," + getC().getY() + ")");
	}
}
