package com.umbc.courses.aca.projects.mips.cache;

import com.umbc.courses.aca.projects.mips.main.CPU;

public class MemoryBusManager
{
    int                                  dCacheRequestClk;
    int                                  busRequestedBy;

    public static final MemoryBusManager instance = new MemoryBusManager();

    private MemoryBusManager()
    {
        resetValues();
    }

    public boolean canICacheAccessBus()
    {
        return (busRequestedBy != 1)
                || (busRequestedBy == 1 && dCacheRequestClk == CPU.CLOCK);
    }

    public boolean iCacheCanProceed()
    {
        boolean busAvailable = canICacheAccessBus();
        if (busAvailable)
        {
            setBusBusy(0);
        }
        return busAvailable;
    }

    public boolean canDCacheAccessBus()
    {
        return (busRequestedBy == -1);
    }

    public boolean dCacheCanProceed()
    {
        boolean busAvailable = canDCacheAccessBus();
        if (busAvailable)
        {
            dCacheRequestClk = CPU.CLOCK;
            setBusBusy(1);
        }
        return busAvailable;
    }

    public void setBusFree(int id)
    {
        if (id == busRequestedBy)
        {
            busRequestedBy = -1;
            dCacheRequestClk = -1;
        }
        else
        {

            // System.err.println(CPU.CLOCK + " "
            // + this.getClass().getSimpleName()
            // + " Bus set free by wrong CacheManager "
            // + ((id == 0) ? "ICacheManager" : "DCacheManager")
            // + " busRequestedBy: " + busRequestedBy);
        }

    }

    private void setBusBusy(int id)
    {
        busRequestedBy = id;
    }

    private void resetValues()
    {
        dCacheRequestClk = 0;
        busRequestedBy = -1; // -1 is free
        // 0 is icache
        // 1 is dcache

    }

}
