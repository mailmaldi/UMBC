package com.umbc.courses.aca.projects.mips.cache;

import com.umbc.courses.aca.projects.mips.Utils.Utils;
import com.umbc.courses.aca.projects.mips.config.ConfigManager;
import com.umbc.courses.aca.projects.mips.instructions.Instruction;
import com.umbc.courses.aca.projects.mips.instructions.InstructionUtils;
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
            System.err.println(CPU.CLOCK + " "
                    + this.getClass().getSimpleName()
                    + " duplicate request address=" + address + " request="
                    + request.toDebugString());

        request = new DCacheRequestData();
        request.lastRequestInstruction = address;
        request.lastRequestInstructionEntryClock = CPU.CLOCK + 1;
        request.dInstruction = InstructionUtils.isDoubleLoadStore(inst);

        // check hit
        dCacheAccessRequests += request.dInstruction ? 2 : 1;
        if (request.dInstruction)
        {
            if (Utils.areDCacheAddressesSameBlock(address, address + 4))
            {
                if (cache.doesAddressExist(address))
                    dCacheAccessHits += 2;
                else
                    dCacheAccessHits++;
            }
            else
            {
                if (cache.doesAddressExist(address))
                    dCacheAccessHits++;
                if (cache.doesAddressExist(address + 4))
                    dCacheAccessHits++;
            }
        }
        else
        {
            dCacheAccessHits += cache.doesAddressExist(address) ? 1 : 0;
        }

        // System.out.println(CPU.CLOCK + " " + request.toDebugString());
        if ((!request.dInstruction && cache.doesAddressExist(address))
                || (request.dInstruction && cache.doesAddressExist(address + 4) && cache
                        .doesAddressExist(address + 4)))
        {
            // dCacheAccessHits += request.dInstruction ? 2 : 1;
            request.cacheHit = true; // when both are true
            request.clockCyclesToBlock = ConfigManager.instance.DCacheLatency
                    + ((request.dInstruction) ? ConfigManager.instance.DCacheLatency
                            : 0);
        }
        else
        {
            request.needsBusAccess = true;
            request.hasBusAccess = false;
            // determine clockstoblock here according to inst
            request.clockCyclesToBlock = getClocksToBlock(inst);
        }
        request.clockCyclesToBlock--;
        System.out.println(CPU.CLOCK + " " + this.getClass().getSimpleName()
                + " " + request.toDebugString());
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
                if (!Utils.areDCacheAddressesSameBlock(address, address + 4))
                    if (cache.doesAddressExist(address + 4))
                    {
                        clocksToBlock += ConfigManager.instance.DCacheLatency;
                    }
                    else
                    {
                        if (cache.isThereAFreeBlock(address + 4)
                                || !cache.isLRUBlockDirty(address + 4))
                            clocksToBlock += get2TPlusKValue();
                        else
                            clocksToBlock += 2 * get2TPlusKValue();
                    }
                else
                    clocksToBlock += ConfigManager.instance.DCacheLatency;
            }
            else
            {
                if (cache.isThereAFreeBlock(address)
                        || !cache.isLRUBlockDirty(address))
                    clocksToBlock += get2TPlusKValue();
                else
                    clocksToBlock += 2 * get2TPlusKValue();

                if (!Utils.areDCacheAddressesSameBlock(address, address + 4))
                    if (cache.doesAddressExist(address + 4))
                    {
                        clocksToBlock += ConfigManager.instance.DCacheLatency;
                    }
                    else
                    {
                        if (cache.isThereAFreeBlock(address + 4)
                                || !cache.isLRUBlockDirty(address + 4))
                            clocksToBlock += get2TPlusKValue();
                        else
                            clocksToBlock += 2 * get2TPlusKValue();
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
                if (cache.isThereAFreeBlock(address)
                        || !cache.isLRUBlockDirty(address))
                    clocksToBlock += get2TPlusKValue();
                else
                    clocksToBlock += 2 * get2TPlusKValue();
            }
        }

        return clocksToBlock;
    }

    public boolean canProceed(Instruction inst) throws Exception
    {
        System.out.println(CPU.CLOCK + " " + this.getClass().getSimpleName()
                + " " + request.toDebugString());
        if (!request.cacheHit)
        {
            if (request.needsBusAccess && !request.hasBusAccess)
            {
                if (MemoryBusManager.instance.dCacheCanProceed())
                {
                    request.hasBusAccess = true;
                    request.lastRequestInstructionEntryClock = CPU.CLOCK;
                }
                return false;
            }
            if (request.hasBusAccess)
            {
                if (!MemoryBusManager.instance.canDCacheAccessBus())
                    request.hasBusAccess = false;
                else if (validateClockElapsed())
                {
                    MemoryBusManager.instance.setBusFree(1);
                    int address = (int) inst.getDestinationAddress();
                    cache.updateBlock(address, InstructionUtils.isStore(inst));
                    if (request.dInstruction)
                        cache.updateBlock(address + 4,
                                InstructionUtils.isStore(inst));
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
                int address = (int) inst.getDestinationAddress();
                cache.updateBlock(address, InstructionUtils.isStore(inst));
                if (request.dInstruction)
                    cache.updateBlock(address + 4,
                            InstructionUtils.isStore(inst));
                return true;
            }
            return false;
        }
    }

    public boolean validateClockElapsed()
    {
        return ((CPU.CLOCK - request.lastRequestInstructionEntryClock >= request.clockCyclesToBlock));
    }

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
    boolean dInstruction;

    boolean cacheHit;
    boolean needsBusAccess;
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
        cacheHit = false;
        needsBusAccess = false;
        dInstruction = false;
        hasBusAccess = false;
    }

    public String toDebugString()
    {
        return String
                .format("dCacheRequest [address: %s , clkentry: %s , clkblock: %s , hit: %s , needsBus: %s hasBusAccess: %s]",
                        lastRequestInstruction,
                        lastRequestInstructionEntryClock, clockCyclesToBlock,
                        cacheHit, needsBusAccess, hasBusAccess);
    }

}