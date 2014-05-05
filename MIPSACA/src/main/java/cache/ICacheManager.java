package cache;

import instructions.Instruction;
import program.ProgramManager;
import stages.CPU;
import config.ConfigManager;

public class ICacheManager
{

    private ICache                    cache;
    private ICacheRequestData         request;
    public static final ICacheManager instance = new ICacheManager();

    private ICacheManager()
    {
        request = new ICacheRequestData();
        cache = new ICache();
    }

    public Instruction getInstructionFromCache(int pc) throws Exception
    {

        if (request.lastRequestInstruction == -1)
        {

            if (cache.checkInCache(pc))
            {

                request.lastRequestInstruction = pc;
                request.lastRequestCycle = CPU.CLOCK;
                request.clockCyclesToBlock = ConfigManager.instance.ICacheLatency - 1;
                request.cacheHit = true;

                if (request.clockCyclesToBlock == 0)
                {

                    request.resetValues();
                    return ProgramManager.instance.getInstructionAtAddress(pc);

                }
                else
                    return null;

            }
            else
            {

                request.lastRequestInstruction = pc;
                request.lastRequestCycle = CPU.CLOCK;
                request.delayToBus = MemoryBusManager.instance.getDelay();
                request.clockCyclesToBlock = 2
                        * (ConfigManager.instance.ICacheLatency + ConfigManager.instance.MemoryLatency)
                        + request.delayToBus - 1;

                return null;

            }

        }
        else
        {

            if (CPU.CLOCK - request.lastRequestCycle == request.clockCyclesToBlock)
            {
                if (!request.cacheHit)
                    cache.setInCache(pc);
                request.resetValues();
                return ProgramManager.instance.getInstructionAtAddress(pc);

            }
            else
                return null;

        }

    }
}

class ICacheRequestData
{
    int     lastRequestInstruction;
    int     lastRequestCycle;
    int     clockCyclesToBlock;
    int     delayToBus;
    boolean cacheHit;

    public ICacheRequestData()
    {
        resetValues();
    }

    public void resetValues()
    {

        lastRequestInstruction = -1;
        lastRequestCycle = -1;
        clockCyclesToBlock = -1;
        delayToBus = -1;
        cacheHit = false;
    }

}
