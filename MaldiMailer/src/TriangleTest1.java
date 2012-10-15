
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class TriangleTest1 {

	public Triangle t;
	public TriangleTest1() {
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	public void testTriangle() {
	}
	
	@Test 
	public void testSetSideLengths() {
		Triangle t1 = new Triangle(1,1,1);
		Triangle t2 = new Triangle(2,2,2);
		t2=t2.setSideLengths(1,1,1);
		assertEquals(true,t2.getSideLengths().equals(t1.getSideLengths()));
	}
	
	
	@Test
	public void testGetSideLengths() {
		Triangle t = new Triangle(5,4,5);
		assertEquals("5,4,5",t.getSideLengths());
	}

	@Test
	public void testGetPerimeter() {
		Triangle t = new Triangle(-5,4,-5);
		assertEquals(-1,t.getPerimeter());	
		Triangle t1= new Triangle(2,3,4);
		assertEquals(9,t1.getPerimeter());
	}

	@Test
	public void testGetArea() {
		Triangle t = new Triangle(3,4,5);
		assertEquals(6,t.getArea(),1);	
	}

	@Test
	public void testIsIsosceles() {
		Triangle t = new Triangle(5,4,5);
		assertTrue(t.isIsosceles());
		Triangle t1 = new Triangle(5,4,3);
		assertFalse(t1.isIsosceles());
	}

	@Test
	public void testIsEquilateral() {
		Triangle t1 = new Triangle(5,5,5);
		assertTrue(t1.isEquilateral());
		Triangle t = new Triangle(0,4,-5);
		assertFalse(t.isEquilateral());
	}
	
	@Test
	public void testIsEquilateral1() {
		Triangle t = new Triangle(5,4,5);
		assertNotNull(t);
		assertFalse(t.isImpossible());
		assertTrue(t.isEquilateral());
	}
	
	@Ignore
	public void testIsnotEquilateral() {
		Triangle t = new Triangle(5,4,4);
		assertNotNull(t);	
		assertFalse(t.isImpossible());
		assertTrue("triangle is possible",t.isEquilateral());
	}

	@Test
	public void testIsRightAngled() {
		Triangle t = new Triangle(3,4,5);
		assertTrue(t.isRightAngled());
		Triangle t1 = new Triangle(3,3,3);
		assertFalse(t1.isRightAngled());
	}

	@Test
	public void testIsScalene() {
		Triangle t = new Triangle(3,4,5);
		assertTrue(t.isScalene());
		Triangle t1 = new Triangle(3,3,3);
		assertFalse(t1.isScalene());
	}

	@Test
	public void testIsImpossible() {
		Triangle t = new Triangle(0,0,0);
		assertNotNull(t);
		assertTrue(t.isImpossible());
	}

}
