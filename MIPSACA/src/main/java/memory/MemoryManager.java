package memory;

import java.util.Map;
import java.util.TreeMap;

public class MemoryManager
{
    public static final MemoryManager instace = new MemoryManager();

    private MemoryManager()
    {

    }

    // address to data
    // address starts at 0x100 or 256 decimal
    private Map<Integer, Integer> memoryDataMap = new TreeMap<Integer, Integer>();

    // Used only at the start by the parser
    public void setValueToAddress(int address, int data)
    {
        memoryDataMap.put(address, data);
    }

    public void dumpAllMemory()
    {
        String leftAlignFormat = "| %-5d | %-10d |%n";
        for (Integer key : memoryDataMap.keySet())
        {
            System.out.format(leftAlignFormat, key, memoryDataMap.get(key));
        }
    }
}
