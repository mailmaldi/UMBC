package umbc.practice.junit;

import org.junit.*;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA. Date: Aug 23, 2010 Time: 5:13:25 PM
 */
public class PersonTest extends Assert
{
	public static SimpleDateFormat df;

	private Person person;

	@BeforeClass
	public static void BeforeClass()
	{
		df = new SimpleDateFormat("yyyy-mm-dd");
		System.out.println("BeforeClass");
	}

	@Before
	public void setUp() throws Exception
	{
		Date date = df.parse("2005-10-14");
		person = new Person("Jammy", date);
		System.out.println("setUp");
	}

	@Test
	public void getName()
	{
		assertEquals(person.getName(), "Jammy");
		System.out.println("Test getName");
	}

	@Test
	public void getAge()
	{
		assertEquals(person.getAge(), 6);
		System.out.println("Test getAge");
	}

	@After
	public void tearDown() throws Exception
	{
		person = null;
		System.out.println("tearDown");
	}

	@AfterClass
	public static void AfterClass()
	{
		df = null;
		System.out.println("AfterClass");
	}
}