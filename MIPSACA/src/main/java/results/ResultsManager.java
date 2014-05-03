package results;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

public class ResultsManager
{

    public static final ResultsManager     instance       = new ResultsManager();

    private final TreeMap<Integer, Object> instructionMap = new TreeMap<Integer, Object>();

    private BufferedWriter                 resultsWriter  = null;

    private ResultsManager()
    {
    }

    // call this only once in ur program
    public void setResultsPath(String path) throws IOException
    {
        if (resultsWriter != null)
            return;
        resultsWriter = new BufferedWriter(new FileWriter(new File(path)));
    }

    public void printResults()
    {
        // http://stackoverflow.com/questions/15215326/how-can-i-create-ascii-table-in-java-in-a-console
        // use String.format() and then print at the end
        // ordered by key which is clock entry of entry into IF
        //
        // for(int key : instructionMap.keySet()
        // {
        // Instruction inst = instructionMap.get(key);
        // inst.toString() , inst.exitCLKCycle[0]
        // inst.exitCLKCycle[1] , inst.exitCLKCycle[2] ,
        // inst.exitCLKCycle[3]
        // inst.hasRAW , inst.hasWAR , inst.hasWAW , inst.hasStruct
        //
        // }
    }

    public void writeResults()
    {
        // use the printResults String, write it to writer and then close

        try
        {
            resultsWriter.flush();
            resultsWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addInstruction(Object instruction)
    {
        // int key = instruction.getEntryCycle[0] , IF cycles entry clock
        // instructionMap.put(key, instruction)
    }

}
