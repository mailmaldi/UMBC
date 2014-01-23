package umbc.practice.junit;

import java.util.Date;

/**
 * Created by IntelliJ IDEA. Time: 5:07:08 PM
 */

public class Person
{
	private Date birthday;

	private String name;

	public Person(String name, Date birthday)
	{
		this.name = name;
		this.birthday = birthday;
	}

	public Person(String name)
	{
		this.name = name;
	}

	public Date getBirthday()
	{
		return birthday;
	}

	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getAge()
	{
		if (this.birthday == null)
			return 0;
		Date now = new Date();
		return now.getYear() - this.birthday.getYear() + 1;
	}
}