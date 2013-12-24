import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;

public class DirectoryCreator
{
	public static void main(String[] args) throws IOException
	{
		String path = "C:\\Users\\Milind\\Downloads\\project2\\lawrence\\";
		CSVReader reader;
		try
		{
			reader = new CSVReader(new FileReader(path + "roster.csv"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}

		String[] nextLine;
		while ((nextLine = reader.readNext()) != null)
		{
			File file = new File(path + nextLine[0].trim());
			file.mkdir();
		}
	}
}
