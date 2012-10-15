/**
 * Triangle. The main function takes 3 positive whole-number lengths
 * to be typed in as command line arguments. The program responds
 * with a description of the triangle, as follows:
 *
 * <ul>
 *  <li><b>equilateral</b> - if all three sides have equal length
 *  <li><b>isosceles</b> - if two sides have equal length
 *  <li><b>right-angled</b> - if one angle is a right angle
 *  <li><b>scalene</b> - all sides different lengths, no right angles
 *  <li><b>impossible</b> - if the given side lengths do not form a triangle
 * </ul>
 * Area and perimeter of the triangle are calculated, too.
 *
 * @author Sreedevi Sampath
 */
public class Triangle
{
	private int side1, side2, side3;
	private String P_EQUILATERAL = "equilateral";
	private String P_ISOSCELES   = "isossceles";
	private String P_RIGHTANGLED = "right-angled";
	private String P_SCALENE     = "scalene";
	private String P_IMPOSSIBLE  = "impossible";

	/**
	 * Constuctor (without error checking)
	 * @param s1 length of the side1 as an integer.
	 * @param s2 length of the side2 as an integer.
	 * @param s3 length of the side3 as an integer.
	 */
	public Triangle(int s1, int s2, int s3)
	{
		side1 = s1;
		side2 = s2;
		side3 = s3;
	}

	/**
	 * Sets the lenghts of the sides of this triangle.
	 * @param s1 length of the side1
	 * @param s2 length of the side2
	 * @param s3 length of the side3
	 * @return a reference to this triangle.
	 */
	public Triangle setSideLengths(int s1, int s2, int s3)
	{
		side1 = s1;
		side2 = s2;
		side3 = s3;
		return this;
	}

	/**
	 * Gets the side lengths.
	 * @return a comma separated list of side lengths
	 */
	public String getSideLengths()
	{
		return side1 + "," + side2 + "," + side3;
	}

	/**
	 * Gets the perimeter of the triangle.
	 * @return -1 if input values are invalid, otherwise the perimeter.
	 */
	public int getPerimeter()
	{  
		return side1 + side2 + side3;
	}

	/**
	 * Gets the area of the triangle. The Heron's formula is used to calcuate the area of the triangle
	 * The Heron's formula states that: If a,b,c are the lengths of the sides of a triangle. 
	 * The area is given by: squareroot (p(p-a)(p-b)(p-c)), where p is half the perimeter
	 * @return area of the triangle, -1.0 if triangle is impossible.
	 */
	public double getArea()
	{
		if (!isImpossible())
		{
			return Math.sqrt(getPerimeter()
			/ 2
			* (getPerimeter() / 2 - side1)
			* (getPerimeter() / 2 - side2)
			* (getPerimeter() / 2 - side3));
		}
		return -1;
	}


	/**
	 * Checks if the triangle is isosceles. Note: isosceles triangle may also
	 * be equilateral and right-angled.
	 * @return true if two sides have equal length
	 */
	public boolean isIsosceles()
	{
		if (side1 == side2 || side2 == side3 || side1 == side3)
		{
			return true;
		}
		return false;
	}

	/**
	 * Checks if the triangle is equilateral.
	 * @return true if all three sides have equal length.
	 */
	public boolean isEquilateral()
	{
		if (side1 == side3) 
		{
			return true;

		}
		return false;
	}

	/**
	 * Checks if the triangle is right-angled. Note: right-angled triangle may
	 * also be isosceles.
	 * @return true if one angle is a right angle, otherwise false.
	 */
	public boolean isRightAngled()
	{
		int[] sides = new int[] { side1, side2, side3 };
		int check = (sides[0]*sides[0])+ (sides[1]*sides[1]);
			
		if(check == sides[2] * sides[2])
		{
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Checks if the triangle is scalene.
	 * @return true if all sides different lengths, no right angles.
	 */
	public boolean isScalene()
	{
		if (side1 != side2 && side1 != side3 && side2 != side3)
		{
			return true;
		}

		return false;
	}

	/**
	 * Checks whether the given side lengths form a
	 * valid triangle.
	 * @return true if the given side lenghts do not form a triangle.
	 */
	public boolean isImpossible()
	{
		if (side1 <= 0 || side2 <= 0 || side3 <= 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * Usage: java Triangle &lt;side1:int&gt; &lt;side2:int&gt; &lt;side3:int&gt;
	 * <p>Main method is only used for testing purposes, no unit tests need to
	 * be written for this method.</p>
	 */
	public static void main(String[] args)
	{
		Triangle triangle = null;
		try
		{
			triangle = new Triangle(
					Integer.parseInt(args[0]),
					Integer.parseInt(args[1]),
					Integer.parseInt(args[2]));
		}
		catch (Exception e)
		{
			System.out.println(
				"Usage: java Quadrangle <side1:int> <side2:int> <side3:int>");
			return;
		}
		System.out.println("Type: " + triangle.isEquilateral());
		System.out.println("Triangle sides: " + triangle.getSideLengths());
		System.out.println("Area: " + triangle.getArea());
		System.out.println("Perimeter: " + triangle.getPerimeter());
	}

} // End of class.
