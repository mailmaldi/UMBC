package cache;

import instructions.Instruction;
import instructions.LD;
import instructions.SD;
import stages.CPU;
import config.ConfigManager;

public class DCacheManager
{

    private DCache                    cache;
    public DCacheRequestData          request;
    // private DCacheRequestData request;

    private int                       dCacheAccessRequests;
    private int                       dCacheAccessHits;
    public static final DCacheManager instance = new DCacheManager();

    private DCacheManager()
    {
        cache = new DCache();
        request = new DCacheRequestData();
        dCacheAccessRequests = 0;
        dCacheAccessHits = 0;
    }

    public boolean canProceed(Instruction inst) throws Exception
    {
        int address = (int) inst.address;
        if (request.lastRequestInstruction == null)
        {
            // first time request
            request.lastRequestInstruction = inst;
            request.lastRequestInstructionEntryClock = CPU.CLOCK;
            request.clockCyclesToBlock = 0;

            if (Instruction.isStore(inst))
            {

                // check if address of this instruction actually exists in
                // DCache
                // if it does, then just write to DCache & mark it dirty. --> it
                // is a cache hit
                if (cache.doesAddressExist(address))
                    request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
                else
                {
                    // if address doesnt exist in DCache, then check if a block
                    // is
                    // free in Dcache
                    // if free, then just write to Dcache and mark it dirty
                    if (cache.isThereAFreeBlock(address))
                    {
                        request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
                    }
                    else
                    {
                        // if doesnt exist && lru block is dirty, then writeback
                        // to
                        // memory , then update dcache & mark dirty
                        // if lru block is not dirty, then just write to dcache
                        if (cache.isLRUBlockDirty(address))
                        {
                            request.clockCyclesToBlock += MemoryBusManager.instance
                                    .getDelayForDCache();
                            request.clockCyclesToBlock += get2TPlusKValue();
                        }
                        else
                        {
                            request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
                        }
                    }

                }
            }
            else
            {
                // Cache hit and found the same address - > return the value
                // from cache
                // latency is cache access time
                if (cache.doesAddressExist(address))
                {
                    request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
                }
                else
                {
                    // Cache miss and cache block is free - > write in the cache
                    // and
                    // return the value
                    // latency is 2(t + k)
                    if (cache.isThereAFreeBlock(address))
                    {
                        request.clockCyclesToBlock += MemoryBusManager.instance
                                .getDelayForDCache();
                        request.clockCyclesToBlock += get2TPlusKValue();
                    }
                    else
                    {
                        // Cache miss and cache block is full - > Check if dirty
                        // if dirty - > write back and update cache
                        // else(not dirty) - > just update cache
                        if (cache.isLRUBlockDirty(address))
                        {
                            request.clockCyclesToBlock += MemoryBusManager.instance
                                    .getDelayForDCache();
                            request.clockCyclesToBlock += (ConfigManager.instance.MemoryLatency);
                            request.clockCyclesToBlock += get2TPlusKValue();
                        }
                        else
                        {
                            request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
                        }
                    }

                }
            }

        }
        else if (!request.lastRequestInstruction.equals(inst))
        {
            throw new Exception(this.getClass().getSimpleName()
                    + " Cannot get different instructions from memory unit");
        }
        else
        {
            //
        }

        return validateClockCyclesToBlock();
    }

    public void updateCacheBlock(Instruction inst) throws Exception
    {
        int address = (int) inst.address;
        if (!request.hasAccessVariablesSet)
        {
            if (inst instanceof LD || inst instanceof SD)
                dCacheAccessRequests++;
            dCacheAccessRequests++;
            if (cache.doesAddressExist(address))
                dCacheAccessHits++;
            cache.updateBlock(address, Instruction.isStore(inst));
            if (cache.doesAddressExist(address)
                    && (inst instanceof LD || inst instanceof SD))
                dCacheAccessHits++;
            request.hasAccessVariablesSet = true;
        }
        // For Store, find block, mark dirty & update address & lru
        // for Load, find block, update address & lru
        request.resetValues();
    }

    private boolean validateClockCyclesToBlock() throws Exception
    {
        if (CPU.CLOCK - request.lastRequestInstructionEntryClock >= request.clockCyclesToBlock)
        {
            // do any cache updates at this point?
            // cache.setInCache(request.lastRequestInstruction); // hack
            return true;
        }
        else
            return false;
    }

    public String getDCacheStatistics()
    {
        String format = "%-60s %4s";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(format,
                "Total number of access requests for Data cache:",
                dCacheAccessRequests));
        sb.append('\n');
        sb.append(String.format(format, "Number of Data cache hits:",
                dCacheAccessHits));
        return sb.toString();
    }

    public int get2TPlusKValue()
    {
        return 2 * (ConfigManager.instance.DCacheLatency + ConfigManager.instance.MemoryLatency);
    }

}

class DCacheRequestData
{
    Instruction lastRequestInstruction;
    int         lastRequestInstructionEntryClock;
    int         clockCyclesToBlock;
    boolean     hasAccessVariablesSet;

    public DCacheRequestData()
    {
        resetValues();
    }

    public void resetValues()
    {

        lastRequestInstruction = null;
        lastRequestInstructionEntryClock = -1;
        clockCyclesToBlock = -1;
        hasAccessVariablesSet = false;
    }

}