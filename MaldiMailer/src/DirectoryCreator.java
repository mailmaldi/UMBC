import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;

public class DirectoryCreator
{
	public static void main(String[] args) throws IOException
	{
		CSVReader reader;
		try
		{
			reader = new CSVReader(new FileReader("C:\\Users\\Milind\\Downloads\\421roster.csv"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}

		String[] nextLine;
		while ((nextLine = reader.readNext()) != null)
		{
			File file = new File("C:\\Users\\Milind\\Downloads\\" + nextLine[0]);
			file.mkdir();
		}
	}
}
