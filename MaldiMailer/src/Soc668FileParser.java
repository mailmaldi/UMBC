import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class Soc668FileParser
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try
		{
			reader = new BufferedReader(new FileReader("C:\\VirtualBox\\value_listing.txt"));
			writer = new BufferedWriter(new FileWriter("C:\\VirtualBox\\value_listing_out.txt"), 8 * 1024 * 1024);
			String line = null;
			int count = 0;
			while ((line = reader.readLine()) != null)
			{
				// count++;
				// if (count > 10000)
				// break;
				String[] splits = line.split(" ");
				if (splits.length == 10)
				{
					writer.write(printSplits(splits));
					writer.newLine();
					// System.out.println(printSplits(splits));

					// long a = Long.parseLong(splits[0]);
					// double b = Double.parseDouble(splits[1]);
					// double c = Double.parseDouble(splits[2]);
					// double d = Double.parseDouble(splits[3]);
					// double e = Double.parseDouble(splits[4]);
					// double f = Double.parseDouble(splits[5]);

				}

			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			reader.close();
			writer.flush();
			writer.close();
		}

	}

	public static String cleanString(String split)
	{
		return split.replaceAll("\\s", "");
	}

	public static String printSplits(String[] splits)
	{
		String returnStr = null;
		if (splits != null)
		{
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < splits.length; i++)
			{
				splits[i] = splits[i].replaceAll(";", "");
				splits[i] = splits[i].replaceAll("\0", "");
				splits[i] = splits[i].trim();
				if (i < 9)
					sb.append(splits[i]).append(",");
				else
					sb.append(splits[i].equalsIgnoreCase("t") ? "1" : "0");
			}
			returnStr = sb.toString();
		}
		return returnStr;
	}

	public static String replaceNonASCII(String msg)
	{
		if (!hashNonASCII(msg))
			return msg;
		return msg.replaceAll("[\u2010-\u2015]", "-").replaceAll("[\u2018\u2019\u201B]", "'").replaceAll("[\u201A]", ",").replaceAll("[\u201C-\u201F\u2036]", "\"")
				.replaceAll("[^\\p{ASCII}]", "");
	}

	/**
	 * Return true is the content has no ASCII
	 * 
	 * @param content
	 * @return
	 */
	public static boolean hashNonASCII(String content)
	{
		/**
		 * FIXME need to enable this code- AP if (!content.equals(removeControlChars(content))) return false;
		 */
		char[] data = content.toCharArray();
		for (int i = 0; i < data.length; i++)
		{
			int value = (int) data[i];
			if (value < 0 || value > 127)
				return true;
		}
		return false;
	}
}
