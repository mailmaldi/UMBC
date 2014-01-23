package umbc.spring;

public class Triangle_1
{
	private String type;

	private int height;

	public Triangle_1()
	{

	}

	public Triangle_1(String type)
	{
		this.type = type;
	}

	public Triangle_1(int height)
	{
		this.height = height;
	}

	public Triangle_1(String type, int height)
	{
		this.type = type;
		this.height = height;
	}

	public int getHeight()
	{
		return height;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void draw()
	{
		System.out.println(getType() + " Triangle draw , height " + getHeight());
	}
}
