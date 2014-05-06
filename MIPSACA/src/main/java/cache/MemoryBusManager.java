package cache;

import stages.CPU;

public class MemoryBusManager
{
    boolean                              iCacheRequested;
    int                                  iCacheRequestClk;
    boolean                              dCacheRequested;
    int                                  dCacheRequestClk;

    public static final MemoryBusManager instance = new MemoryBusManager();

    private MemoryBusManager()
    {
        resetValues();

    }

    public int getDelay()
    {
        return 0;
    }

    public int getDelayForDCache()
    {
        if (iCacheRequested == true)
        {
            return CPU.CLOCK - (iCacheRequestClk)
                    + ICacheManager.instance.get2TPlusKValue() - 1;
        }
        return 0;
    }

    public int getDelayForICache()
    {
        if (dCacheRequested == true
                && DCacheManager.instance.request.lastRequestInstructionEntryClock < CPU.CLOCK)
        {
            return CPU.CLOCK - (dCacheRequestClk)
                    + DCacheManager.instance.request.clockCyclesToBlock;
        }
        else
        {
            DCacheManager.instance.request.resetValues();
            return 0;
        }
    }

    private void resetValues()
    {
        iCacheRequested = false;
        iCacheRequestClk = 0;
        dCacheRequested = false;
        dCacheRequestClk = 0;

    }

}
