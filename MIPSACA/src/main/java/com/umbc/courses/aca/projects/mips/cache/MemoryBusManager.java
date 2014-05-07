package com.umbc.courses.aca.projects.mips.cache;

import com.umbc.courses.aca.projects.mips.stages.CPU;

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
            System.out.println(CPU.CLOCK + " DUMMY " + dCacheRequestClk + " "
                    + DCacheManager.instance.request.clockCyclesToBlock);
            return (dCacheRequestClk + DCacheManager.instance.request.clockCyclesToBlock)
                    - CPU.CLOCK;
        }
        else
        {
            DCacheManager.instance.request.resetValues();
            return 0;
        }
    }

    public void setICacheBusy()
    {
        iCacheRequested = true;
        iCacheRequestClk = CPU.CLOCK;
    }

    public void setICacheFree()
    {
        iCacheRequestClk = -1;
        iCacheRequested = false;
    }

    public void setDCacheBusy()
    {
        dCacheRequested = true;
        dCacheRequestClk = CPU.CLOCK;
    }

    public void setDCacheFree()
    {
        dCacheRequestClk = -1;
        dCacheRequested = false;
    }

    private void resetValues()
    {
        iCacheRequested = false;
        iCacheRequestClk = 0;
        dCacheRequested = false;
        dCacheRequestClk = 0;

    }

}
