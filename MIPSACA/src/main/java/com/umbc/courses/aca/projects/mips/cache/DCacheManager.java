package com.umbc.courses.aca.projects.mips.cache;

import com.umbc.courses.aca.projects.mips.Utils.Utils;
import com.umbc.courses.aca.projects.mips.config.ConfigManager;
import com.umbc.courses.aca.projects.mips.instructions.Instruction;
import com.umbc.courses.aca.projects.mips.instructions.InstructionUtils;
import com.umbc.courses.aca.projects.mips.instructions.LD;
import com.umbc.courses.aca.projects.mips.instructions.SD;
import com.umbc.courses.aca.projects.mips.main.CPU;

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

    public void setRequest(Instruction inst) throws Exception
    {
        int address = (int) inst.getDestinationAddress();
        // check if request.address is not same here? Do I terminate here?
        if (request.lastRequestInstruction == address)
            System.err.println(CPU.CLOCK + this.getClass().getSimpleName()
                    + " duplicate request address=" + address + " request="
                    + request.toDebugString());

        request = new DCacheRequestData();
        request.lastRequestInstruction = address;
        request.lastRequestInstructionEntryClock = CPU.CLOCK;
        request.dInstruction = InstructionUtils.isDoubleLoadStore(inst);

        // check hit
        dCacheAccessRequests += request.dInstruction ? 2 : 1;

        System.out.println(CPU.CLOCK + " " + request.toDebugString());
        if ((!request.dInstruction && cache.doesAddressExist(address))
                || (request.dInstruction && cache.doesAddressExist(address + 4) && cache
                        .doesAddressExist(address + 4)))
        {
            dCacheAccessHits += request.dInstruction ? 2 : 1;
            request.cacheHit = true;
            request.clockCyclesToBlock = ConfigManager.instance.DCacheLatency
                    + ((request.dInstruction) ? ConfigManager.instance.DCacheLatency
                            : 0);
        }
        else
        {
            request.beedsBusAccess = true;

            // determine clockstoblock here according to inst
            request.clockCyclesToBlock = getClocksToBlock(inst) - 1;
        }
        System.out.println(CPU.CLOCK + " " + this.getClass().getSimpleName()
                + " " + request.toDebugString() + " clockToBlock "
                + request.clockCyclesToBlock);
    }

    // TODO, check lru is dirty or not
    // TODO, a Load from a dirty block found in location will still set it as
    // dirty
    public int getClocksToBlock(Instruction inst) throws Exception
    {
        int address = (int) inst.getDestinationAddress();

        boolean isDoubleInstruction = InstructionUtils.isDoubleLoadStore(inst);

        int clocksToBlock = 0;

        if (isDoubleInstruction)
        {
            if (cache.doesAddressExist(address))
            {
                clocksToBlock += ConfigManager.instance.DCacheLatency;
                if (Utils.getDCacheTag(address + 4) != Utils
                        .getDCacheTag(address)
                        || Utils.getDCacheSet(address + 4) != Utils
                                .getDCacheSet(address))
                    if (cache.doesAddressExist(address + 4))
                    {
                        clocksToBlock += ConfigManager.instance.DCacheLatency;
                    }
                    else
                    {
                        clocksToBlock += get2TPlusKValue();
                    }
                else
                    clocksToBlock += ConfigManager.instance.DCacheLatency;
            }
            else
            {
                clocksToBlock += get2TPlusKValue();
                if (Utils.getDCacheTag(address + 4) != Utils
                        .getDCacheTag(address)
                        || Utils.getDCacheSet(address + 4) != Utils
                                .getDCacheSet(address))
                    if (cache.doesAddressExist(address + 4))
                    {
                        clocksToBlock += ConfigManager.instance.DCacheLatency;
                    }
                    else
                    {
                        clocksToBlock += get2TPlusKValue();
                    }
                else
                    clocksToBlock += ConfigManager.instance.DCacheLatency;
            }
        }
        else
        {
            if (cache.doesAddressExist(address))
            {
                clocksToBlock = ConfigManager.instance.DCacheLatency;
            }
            else
            {
                clocksToBlock = get2TPlusKValue();
            }
        }

        return clocksToBlock;
    }

    public boolean canProceed(Instruction inst) throws Exception
    {
        if (request.beedsBusAccess)
        {
            if (request.hasBusAccess
                    && ((CPU.CLOCK - request.lastRequestInstructionEntryClock >= request.clockCyclesToBlock)))
            {
                MemoryBusManager.instance.setBusFree(1);
                int address = (int) inst.getDestinationAddress();
                cache.updateBlock(address, InstructionUtils.isStore(inst));
                if (request.dInstruction)
                    cache.updateBlock(address + 4,
                            InstructionUtils.isStore(inst));
                return true;
            }
            else
            {
                if (MemoryBusManager.instance.dCacheCanProceed())
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
                int address = (int) inst.getDestinationAddress();
                cache.updateBlock(address, InstructionUtils.isStore(inst));
                if (request.dInstruction)
                    cache.updateBlock(address + 4,
                            InstructionUtils.isStore(inst));
                return true;
            }
            return false;
        }
        // if (!MemoryBusManager.instance.canDCacheAccessBus()
        // && !request.cacheHit)
        // {
        // if (MemoryBusManager.instance.dCacheCanProceed())
        // {
        // request.lastRequestInstructionEntryClock = CPU.CLOCK;
        // }
        // return false;
        // }
        // if ((MemoryBusManager.instance.canDCacheAccessBus() ||
        // request.cacheHit)
        // && (CPU.CLOCK - request.lastRequestInstructionEntryClock >=
        // request.clockCyclesToBlock))
        // {
        // // This can run multiple times because of a HAZARD
        // // set bus free only once
        // if (request.beedsBusAccess)
        // MemoryBusManager.instance.setBusFree(1);
        // int address = (int) inst.getDestinationAddress();
        // cache.updateBlock(address, InstructionUtils.isStore(inst));
        // if (request.dInstruction)
        // cache.updateBlock(address + 4, InstructionUtils.isStore(inst));
        // return true;
        // }
        // return false;

    }

    public void setBusFree(Instruction inst) throws Exception
    {
        if (request.beedsBusAccess)
            MemoryBusManager.instance.setBusFree(1);
    }

    // public boolean canProceed(Instruction inst) throws Exception
    // {
    // boolean accessingMemory = false;
    // int address = (int) inst.getDestinationAddress();
    // if (request.lastRequestInstruction == null)
    // {
    // // first time request
    // request.lastRequestInstruction = inst;
    // request.lastRequestInstructionEntryClock = CPU.CLOCK;
    // request.clockCyclesToBlock = 0;
    //
    // if (InstructionUtils.isStore(inst))
    // {
    //
    // // check if address of this instruction actually exists in
    // // DCache
    // // if it does, then just write to DCache & mark it dirty. --> it
    // // is a cache hit
    // if (cache.doesAddressExist(address))
    // {
    // request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
    // System.out.println("DCACHEMANAGER: HIT for instruction "
    // + inst.debugString() + " address " + address);
    // }
    // else
    // {
    // // if address doesnt exist in DCache, then check if a block
    // // is
    // // free in Dcache
    // // if free, then just write to Dcache and mark it dirty
    // if (cache.isThereAFreeBlock(address))
    // {
    // int memoryDelay = MemoryBusManager.instance
    // .getDelayForDCache();
    // accessingMemory = true;
    // request.clockCyclesToBlock += memoryDelay;
    // request.clockCyclesToBlock += get2TPlusKValue();
    // }
    // else
    // {
    // // if doesnt exist && lru block is dirty, then writeback
    // // to
    // // memory , then update dcache & mark dirty
    // // if lru block is not dirty, then just write to dcache
    // if (cache.isLRUBlockDirty(address))
    // {
    // int memoryDelay = MemoryBusManager.instance
    // .getDelayForDCache();
    // accessingMemory = true;
    // request.clockCyclesToBlock += memoryDelay;
    // request.clockCyclesToBlock += get2TPlusKValue();
    // }
    // else
    // {
    // request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
    // }
    // }
    //
    // }
    // request.clockCyclesToBlock--;
    // }
    // else
    // {
    // // Cache hit and found the same address - > return the value
    // // from cache
    // // latency is cache access time
    //
    // if (cache.doesAddressExist(address))
    // {
    // request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
    // System.out.println("DCACHEMANAGER: HIT for instruction "
    // + inst.debugString() + " address " + address);
    // }
    // else
    // {
    // // Cache miss and cache block is free - > write in the
    // // cache
    // // and
    // // return the value
    // // latency is 2(t + k)
    // if (cache.isThereAFreeBlock(address))
    // {
    // request.clockCyclesToBlock += MemoryBusManager.instance
    // .getDelayForDCache();
    // accessingMemory = true;
    // request.clockCyclesToBlock += get2TPlusKValue();
    // }
    // else
    // {
    // // Cache miss and cache block is full - > Check if
    // // dirty
    // // if dirty - > write back and update cache
    // // else(not dirty) - > just update cache
    // if (cache.isLRUBlockDirty(address))
    // {
    // request.clockCyclesToBlock += MemoryBusManager.instance
    // .getDelayForDCache();
    // accessingMemory = true;
    // request.clockCyclesToBlock += (ConfigManager.instance.MemoryLatency);
    // request.clockCyclesToBlock += get2TPlusKValue();
    // }
    // else
    // {
    // request.clockCyclesToBlock += ConfigManager.instance.DCacheLatency;
    // }
    // }
    // } // end of cache.doesAddressExist(address)
    // if (inst instanceof LD
    // && (Utils.getDCacheTag(address) != Utils
    // .getDCacheTag(address + 4) || Utils
    // .getDCacheSet(address) != Utils
    // .getDCacheSet(address + 4)))
    // {
    // address += 4;
    // if (cache.doesAddressExist(address))
    // {
    // }
    // else
    // {
    // if (cache.isThereAFreeBlock(address))
    // {
    // request.clockCyclesToBlock += MemoryBusManager.instance
    // .getDelayForDCache();
    // accessingMemory = true;
    // request.clockCyclesToBlock += get2TPlusKValue();
    // }
    // else
    // {
    // if (cache.isLRUBlockDirty(address))
    // {
    // request.clockCyclesToBlock += MemoryBusManager.instance
    // .getDelayForDCache();
    // accessingMemory = true;
    // // request.clockCyclesToBlock +=
    // // (ConfigManager.instance.MemoryLatency);
    // request.clockCyclesToBlock += get2TPlusKValue();
    // }
    // else
    // {
    // }
    // }
    // } // end of cache.doesAddressExist(address)
    // // request.clockCyclesToBlock--; // HACK!!!
    // }// end of instanceofLD
    // // request.clockCyclesToBlock--; // HACK!!!
    // }
    //
    // }
    // else if (!request.lastRequestInstruction.equals(inst))
    // {
    // throw new Exception(this.getClass().getSimpleName()
    // + " Cannot get different instructions from memory unit");
    // }
    // else
    // {
    // //
    // }
    // if (accessingMemory)
    // {
    // MemoryBusManager.instance.setDCacheBusy();
    // }
    //
    // return validateClockCyclesToBlock();
    // }
    //

    //
    // private boolean validateClockCyclesToBlock() throws Exception
    // {
    // if (CPU.CLOCK - request.lastRequestInstructionEntryClock >=
    // request.clockCyclesToBlock)
    // {
    // // do any cache updates at this point?
    // // cache.setInCache(request.lastRequestInstruction); // hack
    // return true;
    // }
    // else
    // return false;
    // }

    public String getDCacheStatistics()
    {
        String format = "%-60s %4s";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(format,
                "Total number of requests to data cache", dCacheAccessRequests));
        sb.append('\n');
        sb.append(String.format(format, "Total number of data cache hit",
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
    int     lastRequestInstruction;
    int     lastRequestInstructionEntryClock;
    int     clockCyclesToBlock;
    boolean hasAccessVariablesSet;
    boolean dInstruction;

    boolean cacheHit;
    boolean beedsBusAccess;
    boolean hasBusAccess;

    public DCacheRequestData()
    {
        resetValues();
    }

    public void resetValues()
    {

        lastRequestInstruction = -1;
        lastRequestInstructionEntryClock = -1;
        clockCyclesToBlock = -1;
        hasAccessVariablesSet = false;
        cacheHit = false;
        beedsBusAccess = false;
        dInstruction = false;
        hasBusAccess = false;
    }

    public String toDebugString()
    {
        return String
                .format("dCacheRequest [address: %s , entry: %s , block: %s , hit: %s , bus: %s]",
                        lastRequestInstruction,
                        lastRequestInstructionEntryClock, clockCyclesToBlock,
                        cacheHit, beedsBusAccess);
    }

}