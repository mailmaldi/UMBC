package cache;

import instructions.Instruction;
import program.ProgramManager;
import results.ResultsManager;
import stages.CPU;
import stages.StageType;
import config.ConfigManager;

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

    public Instruction getInstructionFromCache(int address) throws Exception
    {
        if (request.lastRequestInstruction != -1
                && request.lastRequestInstruction != address)
            throw new Exception(
                    "ICacheManager Requested an instruction address when already blocking address="
                            + address + " lastrequestaddress="
                            + request.lastRequestInstruction);

        if (request.lastRequestInstruction == -1)
        {
            System.out.println(CPU.CLOCK + " ICacheM First Request for PC="
                    + address);
            request.lastRequestInstruction = address;
            request.lastRequestInstructionEntryClock = CPU.CLOCK;
            iCacheAccessRequests++;

            if (cache.checkInCache(address))
            {
                iCacheAccessHits++;
                request.clockCyclesToBlock = ConfigManager.instance.ICacheLatency - 1;
            }
            else
            {
                int delayToBus = MemoryBusManager.instance.getDelayForICache();
                if (delayToBus == 0)
                    MemoryBusManager.instance.setICacheBusy();
                // else
                // request.resetValues();
                request.clockCyclesToBlock = delayToBus + get2TPlusKValue() - 1;
                System.out.println(CPU.CLOCK + " ICacheM "
                        + request.toDebugString() + " delayToBus " + delayToBus
                        + " 2T+K " + get2TPlusKValue());
            }
        }
        return validateClockCyclesToBlock();
    }

    private Instruction validateClockCyclesToBlock() throws Exception
    {
        if ((request.lastRequestInstruction >= 0)
                && (CPU.CLOCK - request.lastRequestInstructionEntryClock == request.clockCyclesToBlock))
        {
            MemoryBusManager.instance.setICacheFree();

            cache.setInCache(request.lastRequestInstruction); // hack
            return getInstructionAndResetRequest();
        }
        else
            return null;
    }

    private Instruction getInstructionAndResetRequest() throws Exception
    {
        Instruction inst = ProgramManager.instance
                .getInstructionAtAddress(request.lastRequestInstruction);
        request.resetValues();
        return inst;
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

    public void flush() throws Exception
    {
        // TODO this is weird, fix it!!
        if (request.lastRequestInstruction >= 0)
        {
            Instruction inst = ProgramManager.instance
                    .getInstructionAtAddress(request.lastRequestInstruction);
            inst.setEntryCycleForStage(StageType.IFSTAGE.getId(), CPU.CLOCK - 1);
            inst.setExitCycleForStage(StageType.IFSTAGE.getId(), CPU.CLOCK);
            ResultsManager.instance.addInstruction(inst);
        }

        this.request.resetValues();
        MemoryBusManager.instance.setICacheFree();
    }

}

class ICacheRequestData
{
    int lastRequestInstruction;
    int lastRequestInstructionEntryClock;
    int clockCyclesToBlock;

    public ICacheRequestData()
    {
        resetValues();
    }

    public void resetValues()
    {

        lastRequestInstruction = -1;
        lastRequestInstructionEntryClock = -1;
        clockCyclesToBlock = -1;
    }

    public String toDebugString()
    {
        return lastRequestInstructionEntryClock + " " + lastRequestInstruction
                + " " + clockCyclesToBlock;
    }

}
