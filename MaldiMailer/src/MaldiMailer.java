import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVReader;

public class MaldiMailer
{

	static final String[] CONTENT_HEADERS = { "Discussion1", "Discussion2", "Discussion3" }; // Discussion, Homework , project header, etc

	static final String DISCUSSION_HEADER = "Discussion";

	static final String HOMEWORK_HEADER = "Homework";

	static final String PROJECT_HEADER = "Project";

	static final String EMAIL_TO_HEADER = "Email Address";

	static final String NAME_HEADER = "Name";

	static final String GRADE_FILE_PATH = "C:\\Users\\Milind\\Dropbox\\UMBC\\421\\test.csv";// 421-discussion-grades.csv";

	static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	static Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	static final String EMAILID = "EMAILID";

	static final String NAME = "NAME";

	static final String SUBJECT = "CMSC 421 - Section 1 - Grades For Classroom Discussions";

	static final String BODY = "The following are your Grades in Discussions, etc till date:\n";

	static final String POST_SCRIPT = "\n\nPS: Clarification folks, Discussion 6 was the Producer-Consumer Problem (although printed Discussion 7 on the sheets), and Discussion 7 was finding the absolute value of an integer without using branching.\n";

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		/**
		 ** 1. open csv file 2. read the headers and store in an array 3. read student data line, and use the Name , Email address and the Discussion* fields from the string[] , 4.
		 * form Email object replacing placeholders using the above data 5. Call emailsender to send a email 6. loop 7. catch exception for every line & email and create a output
		 * showing which students didnt get
		 */

		HashMap<String, Integer> headerMap = new HashMap<String, Integer>();

		CSVReader reader;
		try
		{
			reader = new CSVReader(new FileReader(GRADE_FILE_PATH));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
		String[] nextLine;
		String[] headerLine;

		try
		{
			headerLine = reader.readNext();
			if (!isEmpty(headerLine))
			{
				System.out.println("Number of Headers = " + headerLine.length);
				for (int i = 0; i < headerLine.length; i++)
				{
					if (headerLine[i].equalsIgnoreCase(NAME_HEADER))
						headerMap.put(NAME, i);// name header index is i
					if (headerLine[i].equalsIgnoreCase(EMAIL_TO_HEADER))
						headerMap.put(EMAILID, i);// name header index is i
					if (headerLine[i].matches(DISCUSSION_HEADER + "[0-9]+"))
						headerMap.put(headerLine[i], i);// name header index is i
				}
			}
			else
			{
				System.out.println("Header line not found");
				return;
			}
		}
		catch (Exception e)
		{

			e.printStackTrace();
			return;
		}

		try
		{
			while ((nextLine = reader.readNext()) != null)
			{
				try
				{
					StringBuffer messageBody = new StringBuffer();
					messageBody.append(nextLine[headerMap.get(NAME)]).append("\n\n").append(BODY).append("\n");
					for (String string : headerMap.keySet())
					{
						if (string.matches(DISCUSSION_HEADER + "[0-9]+"))
							messageBody.append(string + " : " + nextLine[headerMap.get(string)]).append("\n");
					}
					messageBody.append(POST_SCRIPT);
					String emailTo = nextLine[headerMap.get(EMAILID)];

					if (!isEmpty(emailTo))
					{
						// TODO send Email
						// System.out.println(">>>>>>>>>>>>\n" + "BODY=" + messageBody + "\n" + "EMAIL=" + emailTo);

						EMail email = new EMail("milindp1@umbc.edu", SUBJECT, messageBody.toString());
						email.setTo(emailTo);
						EmailSender.send(email);

						System.out.println(">>>>>>>>>>>>\n" + "sent email to: " + "\n" + "EMAIL=" + emailTo);

					}
					else
					{
						// TODO print in output file
						System.out.println(">>>>>>> Not sent for :" + nextLine[headerMap.get(NAME)]);

					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}

	}

	public static boolean isEmpty(String value)
	{
		if (isEmpty((Object) value))
			return true;
		if (value.trim().length() == 0)
		{
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Object value)
	{
		if (value == null)
			return true;
		return false;
	}

	public static boolean isEmpty(Object[] values)
	{
		if (values == null)
			return true;
		for (Object value : values)
		{
			if (!isEmpty(value))
				return false;
		}
		return true;
	}

	public static <T> boolean isOneOf(T item, T... items)
	{
		if (items != null && item != null)
		{
			for (T itemToCheck : items)
			{
				if (item.equals(itemToCheck))
					return true;
			}
		}
		return false;
	}

}
