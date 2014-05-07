package com.umbc.courses.aca.projects.mips.cache;

import com.umbc.courses.aca.projects.mips.config.ConfigManager;
import com.umbc.courses.aca.projects.mips.main.CPU;

public class ICacheManager
{

    private ICache                    cache;
    private ICacheRequestData         request;

    private int                       iCacheAccessRequests;
    private int                       iCacheAccessHits;

    public static final ICacheManager instance = new ICacheManager();

    private ICacheManager()
    {
        request = new ICacheRequestData();
        cache = new ICache();
        iCacheAccessRequests = 0;
        iCacheAccessHits = 0;
    }

    public void setRequest(int address) throws Exception
    {
        // check if request.address is not same here?
        if (request.lastRequestInstruction == address)
            System.err.println(CPU.CLOCK + this.getClass().getSimpleName()
                    + " duplicate request address=" + address + " request="
                    + request.toDebugString());

        System.out.println(CPU.CLOCK + " " + request.toDebugString());

        request = new ICacheRequestData();
        request.lastRequestInstruction = address;
        request.lastRequestInstructionEntryClock = CPU.CLOCK;

        // check hit
        iCacheAccessRequests++;
        if (cache.checkInCache(address))
        {
            iCacheAccessHits++;
            request.cacheHit = true;
            request.clockCyclesToBlock = ConfigManager.instance.ICacheLatency;
        }
        else
        {
            if (MemoryBusManager.instance.iCacheCanProceed())
            {
                request.hasBusAccess = true;
            }

            request.clockCyclesToBlock = get2TPlusKValue();
            System.out.println(CPU.CLOCK + " ICacheM "
                    + request.toDebugString() + " 2T+K " + get2TPlusKValue());

        }
    }

    // will be called by isReadytoSend
    public boolean canProceed() throws Exception
    {
        if (!request.hasBusAccess && !request.cacheHit)
        {
            if (MemoryBusManager.instance.iCacheCanProceed())
            {
                request.lastRequestInstructionEntryClock = CPU.CLOCK;
            }
            return false;
        }
        if ((request.hasBusAccess || request.cacheHit)
                && (request.lastRequestInstruction >= 0)
                && (CPU.CLOCK - request.lastRequestInstructionEntryClock >= request.clockCyclesToBlock))
        {
            if (request.hasBusAccess)
                MemoryBusManager.instance.setBusFree();
            cache.setInCache(request.lastRequestInstruction); // hack
            // request.resetValues();
            return true;
        }
        return false;

    }

    public int getICacheAccessRequests()
    {
        return iCacheAccessRequests;
    }

    public int getICacheAccessHits()
    {
        return iCacheAccessHits;
    }

    public int get2TPlusKValue()
    {
        return 2 * (ConfigManager.instance.ICacheLatency + ConfigManager.instance.MemoryLatency);
    }

    public String getICacheStatistics()
    {
        String format = "%-60s %4s";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(format,
                "Total number of access requests for instruction cache:",
                getICacheAccessRequests()));
        sb.append('\n');
        sb.append(String.format(format, "Number of instruction cache hits:",
                getICacheAccessHits()));
        return sb.toString();
    }
}

class ICacheRequestData
{
    int     lastRequestInstruction;
    int     lastRequestInstructionEntryClock;
    int     clockCyclesToBlock;
    boolean cacheHit;
    boolean hasBusAccess;

    public ICacheRequestData()
    {
        resetValues();
    }

    public void resetValues()
    {
        lastRequestInstruction = -1;
        lastRequestInstructionEntryClock = -1;
        clockCyclesToBlock = -1;
        hasBusAccess = false;
        cacheHit = false;
    }

    public String toDebugString()
    {
        return String
                .format("iCacheRequest [address: %s , entry: %s , block: %s , hit: %s , bus: %s]",
                        lastRequestInstruction,
                        lastRequestInstructionEntryClock, clockCyclesToBlock,
                        cacheHit, clockCyclesToBlock);
    }

}
