package com.umbc.courses.aca.projects.mips.cache;

import com.umbc.courses.aca.projects.mips.Utils.Utils;
import com.umbc.courses.aca.projects.mips.config.ConfigManager;
import com.umbc.courses.aca.projects.mips.instructions.Instruction;
import com.umbc.courses.aca.projects.mips.instructions.InstructionUtils;
import com.umbc.courses.aca.projects.mips.instructions.LD;
import com.umbc.courses.aca.projects.mips.instructions.SD;
import com.umbc.courses.aca.projects.mips.stages.CPU;

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
        boolean accessingMemory = false;
        int address = (int) inst.getDestinationAddress();
        if (request.lastRequestInstruction == null)
        {
            // first time request
            request.lastRequestInstruction = inst;
            request.lastRequestInstructionEntryClock = CPU.CLOCK;
            request.clockCyclesToBlock = 0;

            if (InstructionUtils.isStore(inst))
            {

                // check if address of this instruction actually exists in
                // DCache
                // if it does, then just write to DCache & mark it dirty. --> it
                // is a cache hit
                if (cache.doesAddressExist(address))
                {
                    request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
                    System.out.println("DCACHEMANAGER: HIT for instruction "
                            + inst.debugString() + " address " + address);
                }
                else
                {
                    // if address doesnt exist in DCache, then check if a block
                    // is
                    // free in Dcache
                    // if free, then just write to Dcache and mark it dirty
                    if (cache.isThereAFreeBlock(address))
                    {
                        int memoryDelay = MemoryBusManager.instance
                                .getDelayForDCache();
                        accessingMemory = true;
                        request.clockCyclesToBlock += memoryDelay;
                        request.clockCyclesToBlock += get2TPlusKValue();
                    }
                    else
                    {
                        // if doesnt exist && lru block is dirty, then writeback
                        // to
                        // memory , then update dcache & mark dirty
                        // if lru block is not dirty, then just write to dcache
                        if (cache.isLRUBlockDirty(address))
                        {
                            int memoryDelay = MemoryBusManager.instance
                                    .getDelayForDCache();
                            accessingMemory = true;
                            request.clockCyclesToBlock += memoryDelay;
                            request.clockCyclesToBlock += get2TPlusKValue();
                        }
                        else
                        {
                            request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
                        }
                    }

                }
                request.clockCyclesToBlock--;
            }
            else
            {
                // Cache hit and found the same address - > return the value
                // from cache
                // latency is cache access time

                if (cache.doesAddressExist(address))
                {
                    request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
                    System.out.println("DCACHEMANAGER: HIT for instruction "
                            + inst.debugString() + " address " + address);
                }
                else
                {
                    // Cache miss and cache block is free - > write in the
                    // cache
                    // and
                    // return the value
                    // latency is 2(t + k)
                    if (cache.isThereAFreeBlock(address))
                    {
                        request.clockCyclesToBlock += MemoryBusManager.instance
                                .getDelayForDCache();
                        accessingMemory = true;
                        request.clockCyclesToBlock += get2TPlusKValue();
                    }
                    else
                    {
                        // Cache miss and cache block is full - > Check if
                        // dirty
                        // if dirty - > write back and update cache
                        // else(not dirty) - > just update cache
                        if (cache.isLRUBlockDirty(address))
                        {
                            request.clockCyclesToBlock += MemoryBusManager.instance
                                    .getDelayForDCache();
                            accessingMemory = true;
                            request.clockCyclesToBlock += (ConfigManager.instance.MemoryLatency);
                            request.clockCyclesToBlock += get2TPlusKValue();
                        }
                        else
                        {
                            request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
                        }
                    }
                } // end of cache.doesAddressExist(address)
                if (inst instanceof LD
                        && (Utils.getDCacheTag(address) != Utils
                                .getDCacheTag(address + 4) || Utils
                                .getDCacheSet(address) != Utils
                                .getDCacheSet(address + 4)))
                {
                    address += 4;
                    if (cache.doesAddressExist(address))
                    {
                    }
                    else
                    {
                        if (cache.isThereAFreeBlock(address))
                        {
                            request.clockCyclesToBlock += MemoryBusManager.instance
                                    .getDelayForDCache();
                            accessingMemory = true;
                            request.clockCyclesToBlock += get2TPlusKValue();
                        }
                        else
                        {
                            if (cache.isLRUBlockDirty(address))
                            {
                                request.clockCyclesToBlock += MemoryBusManager.instance
                                        .getDelayForDCache();
                                accessingMemory = true;
                                // request.clockCyclesToBlock +=
                                // (ConfigManager.instance.MemoryLatency);
                                request.clockCyclesToBlock += get2TPlusKValue();
                            }
                            else
                            {
                            }
                        }
                    } // end of cache.doesAddressExist(address)
                    //request.clockCyclesToBlock--; // HACK!!!
                }// end of instanceofLD
                //request.clockCyclesToBlock--; // HACK!!!
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
        if (accessingMemory)
        {
            MemoryBusManager.instance.setDCacheBusy();
        }

        return validateClockCyclesToBlock();
    }

    public void updateCacheBlock(Instruction inst) throws Exception
    {
        int address = (int) inst.getDestinationAddress();
        if (!request.hasAccessVariablesSet)
        {
            dCacheAccessRequests++;
            if (cache.doesAddressExist(address))
                dCacheAccessHits++;
            cache.updateBlock(address, InstructionUtils.isStore(inst));
            if (inst instanceof LD || inst instanceof SD)
            {
                dCacheAccessRequests++;
                if (cache.doesAddressExist(address + 4))
                    dCacheAccessHits++;
            }
            cache.updateBlock(address + 4, InstructionUtils.isStore(inst));

            request.hasAccessVariablesSet = true;
            MemoryBusManager.instance.setDCacheFree();
            System.out.println(CPU.CLOCK+ " DCacheM set Bus Manager free");
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