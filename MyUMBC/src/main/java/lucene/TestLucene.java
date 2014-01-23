package main.java.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.FSDirectory;

public class TestLucene
{

	public static void main(String[] args) throws Exception
	{
		String path = "C:\\VirtualBox\\Wikitology\\wikitology\\";
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(path)));
		// System.out.println(reader.maxDoc());
		// System.out.println(reader.numDocs());

		for (int i = 0; i < 1; i++)
		{

			Document doc = reader.document(i);
			for (IndexableField indexableField : doc)
			{
				System.out.println(indexableField.stringValue());
				
			}

			// System.out.println(doc.toString());

			// do something with docId here...
		}
	}

	public static String arrayToString(String[] values)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (String string : values)
		{
			sb.append(string).append(",");
		}
		sb.append("]");
		return sb.toString();
	}

	public static Properties propertyReader()
	{
		Properties prop = new Properties();

		try
		{
			// load a properties file
			prop.load(new FileInputStream("config.properties"));

		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

		return prop;
	}
}
