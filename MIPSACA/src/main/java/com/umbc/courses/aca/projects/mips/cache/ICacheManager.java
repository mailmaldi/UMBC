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
        // check if request.address is not same here? Do I terminate here?
        if (request.lastRequestInstruction == address)
            System.err.println(CPU.CLOCK + this.getClass().getSimpleName()
                    + " duplicate request address=" + address + " request="
                    + request.toDebugString());

        request = new ICacheRequestData();
        request.lastRequestInstruction = address;
        request.lastRequestInstructionEntryClock = CPU.CLOCK + 1;

        // check hit
        iCacheAccessRequests++;
        if (cache.checkInCache(address))
        {
            iCacheAccessHits++;
            request.cacheHit = true;
            request.clockCyclesToBlock = ConfigManager.instance.ICacheLatency;
            request.needsBusAccess = false;
        }
        else
        {
            request.needsBusAccess = true;
            // if (MemoryBusManager.instance.iCacheCanProceed())
            // {
            // request.hasBusAccess = true;
            // }
            request.clockCyclesToBlock = get2TPlusKValue();

        }
        request.clockCyclesToBlock--; // since we do +1 in entry clock
        System.out.println(CPU.CLOCK + " " + this.getClass().getSimpleName()
                + " " + request.toDebugString());
    }

    public boolean validateClockElapsed()
    {
        return ((CPU.CLOCK - request.lastRequestInstructionEntryClock >= request.clockCyclesToBlock));
    }

    // will be called by isReadytoSend
    public boolean canProceed() throws Exception
    {
        System.out.println(CPU.CLOCK + " " + this.getClass().getSimpleName()
                + " " + request.toDebugString());
        if (!request.cacheHit)
        {
            if (request.needsBusAccess && !request.hasBusAccess)
            {
                if (MemoryBusManager.instance.iCacheCanProceed())
                {
                    request.hasBusAccess = true;
                    request.lastRequestInstructionEntryClock = CPU.CLOCK;
                }
                return false;
            }
            if (request.hasBusAccess)
            {
                if (!MemoryBusManager.instance.canICacheAccessBus())
                    request.hasBusAccess = false;
                else if (validateClockElapsed())
                {
                    MemoryBusManager.instance.setBusFree(0);
                    cache.setInCache(request.lastRequestInstruction);
                    request.cacheHit = true;
                    request.needsBusAccess = false;
                    request.hasBusAccess = false;
                    return true;
                }
                return false;
            }
            return validateClockElapsed(); // can also be return true
        }
        else
        {
            if (validateClockElapsed())
            {
                cache.setInCache(request.lastRequestInstruction);
                request.cacheHit = true;
                request.needsBusAccess = false;
                return true;
            }
            else
                return false;
        }
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
                "Total number of requests to instruction cache",
                getICacheAccessRequests()));
        sb.append('\n');
        sb.append(String.format(format,
                "Total number of instruction cache hit", getICacheAccessHits()));
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
    boolean needsBusAccess;

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
        needsBusAccess = false;
    }

    public String toDebugString()
    {
        return String
                .format("iCacheRequest [instruction: %s , entryclk: %s , clkblock: %s , hit: %s , needsbusAccess: %s hasBusAccess: %s]",
                        lastRequestInstruction,
                        lastRequestInstructionEntryClock, clockCyclesToBlock,
                        cacheHit, needsBusAccess, hasBusAccess);
    }
}
