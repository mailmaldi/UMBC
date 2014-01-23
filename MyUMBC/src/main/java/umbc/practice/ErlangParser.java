package umbc.practice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErlangParser
{

	private static final String FILEPATH = null;

	class NodeData
	{
		public int Fragment_Id = -1;

		// KCount, Data Values, min, max, average, median, neighboursList , sumseen, numbersseen, etc etc as keys
		public Map<String, String> initial_data = new HashMap<String, String>();

		public Map<String, String> metadata = new HashMap<String, String>();

		public Map<String, String> final_data = new HashMap<String, String>();

		public Map<Integer, Map<String, String>> data_step_i = null;

		public NodeData()
		{
		}

	}

	public static void main(String[] args) throws Exception
	{

		BufferedReader bReader = new BufferedReader(new FileReader(FILEPATH));
		
		String line = null;
		while( (line=bReader.readLine()) != null)
		{
			
		}

	}

}
