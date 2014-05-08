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
        request.lastRequestInstructionEntryClock = CPU.CLOCK;

        System.out.println(CPU.CLOCK + " " + request.toDebugString());
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

        if (!request.cacheHit)
        {
            if (request.hasBusAccess
                    && ((CPU.CLOCK - request.lastRequestInstructionEntryClock >= request.clockCyclesToBlock)))
            {
                MemoryBusManager.instance.setBusFree(0);
                cache.setInCache(request.lastRequestInstruction);
                return true;
            }
            else
            {
                if (MemoryBusManager.instance.iCacheCanProceed()
                        && !request.hasBusAccess)
                {
                    request.lastRequestInstructionEntryClock = CPU.CLOCK;
                    request.hasBusAccess = true;
                }
                return false;
            }

        }
        else
        {
            if ((CPU.CLOCK - request.lastRequestInstructionEntryClock >= request.clockCyclesToBlock))
            {
                cache.setInCache(request.lastRequestInstruction);
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
        sb.append(String.format(format, "Total number of instruction cache hit",
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
                .format("iCacheRequest [address: %s , entryclk: %s , clkblock: %s , hit: %s , needsbusAccess: %s]",
                        lastRequestInstruction,
                        lastRequestInstructionEntryClock, clockCyclesToBlock,
                        cacheHit, needsBusAccess);
    }

}
