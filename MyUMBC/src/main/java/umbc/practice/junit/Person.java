package umbc.practice.junit;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA. Time: 5:07:08 PM
 */

public class Person {
	private Date birthday;

	private String name;

	public Person(String name, Date birthday) {
		this.name = name;
		this.birthday = birthday;
	}

	public Person(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		if (this.birthday == null)
			return 0;
		Date now = new Date();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(now);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(this.birthday);
		return cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR) + 1;
	}
}