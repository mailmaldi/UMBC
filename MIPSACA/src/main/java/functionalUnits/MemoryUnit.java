package functionalUnits;

import instructions.Instruction;
import instructions.InstructionUtils;
import instructions.NOOP;
import instructions.StoreInstruction;
import memory.DataMemoryManager;
import stages.CPU;
import stages.StageType;
import stages.WriteBackStage;
import cache.DCacheManager;
import config.ConfigManager;

public class MemoryUnit extends FunctionalUnit
{

    private static volatile MemoryUnit instance;

    public static MemoryUnit getInstance()
    {
        if (null == instance)
            synchronized (MemoryUnit.class)
            {
                if (null == instance)
                    instance = new MemoryUnit();
            }

        return instance;
    }

    private MemoryUnit()
    {
        super();
        isPipelined = false;
        clockCyclesRequired = ConfigManager.instance.MemoryLatency;
        pipelineSize = 1;
        stageId = StageType.EXSTAGE;
        createPipelineQueue(pipelineSize);
    }

    @Override
    public void executeUnit() throws Exception
    {
        validateQueueSize();

        Instruction inst = peekFirst();
        if ((inst instanceof NOOP))
            return;

        System.out.println(CPU.CLOCK + " Memory " + inst.debugString());

        if (!isReadyToSend())
            return;

        if (InstructionUtils.isLoadStore(inst))
        {
            if (CPU.RUN_TYPE.equals(CPU.RUN.MEMORY))
                DCacheManager.instance.updateCacheBlock(inst);
            if (inst instanceof StoreInstruction)
                DataMemoryManager.instance.setValueToAddress((int) inst
                        .getDestinationAddress(),
                        (int) ((StoreInstruction) inst).getValueToWrite()
                                .getSource());
            else
                inst.getDestinationRegister().setDestination(
                        DataMemoryManager.instance
                                .getValueFromAddress((int) inst
                                        .getDestinationAddress()));
        }

        if (!WriteBackStage.getInstance().checkIfFree(inst))
            throw new Exception(
                    "MemoryUnit: won tie, WB Stage should always be free");

        WriteBackStage.getInstance().acceptInstruction(inst);
        updateExitClockCycle(inst);
        rotatePipe();

    }

    public boolean isReadyToSend() throws Exception
    {
        Instruction inst = peekFirst();
        if (inst instanceof NOOP)
            return true;
        if (InstructionUtils.isLoadStore(inst))
        {
            switch (CPU.RUN_TYPE)
            {
                case MEMORY:
                    return DCacheManager.instance.canProceed(inst);
                case PIPELINE:
                    return ((CPU.CLOCK - inst.getEntryCycleForStage(stageId
                            .getId())) >= clockCyclesRequired);
                default:
                    throw new Exception("MemoryUnit Illegal CPU.RUN_TYPE ");
            }
        }
        else
        {
            // delay for non load-store in memory unit is always 1,
            // not configurable
            return ((CPU.CLOCK - inst.getEntryCycleForStage(stageId.getId())) >= 1);
        }
    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit() throws Exception
    {
        Instruction inst = peekFirst();
        throw new Exception("MemoryUnit: Should never call this function: "
                + inst.toString());
    }
}
