package com.umbc.courses.aca.projects.mips.functionalUnits;

import com.umbc.courses.aca.projects.mips.cache.DCacheManager;
import com.umbc.courses.aca.projects.mips.config.ConfigManager;
import com.umbc.courses.aca.projects.mips.instructions.Instruction;
import com.umbc.courses.aca.projects.mips.instructions.InstructionUtils;
import com.umbc.courses.aca.projects.mips.instructions.StoreInstruction;
import com.umbc.courses.aca.projects.mips.main.CPU;
import com.umbc.courses.aca.projects.mips.memory.DataMemoryManager;
import com.umbc.courses.aca.projects.mips.stages.StageType;
import com.umbc.courses.aca.projects.mips.stages.WriteBackStage;

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
        setPipelined(false);
        setClockCyclesRequired(ConfigManager.instance.MemoryLatency);
        pipelineSize = 1;
        stageId = StageType.EXSTAGE;
        createPipelineQueue(pipelineSize);
    }

    @Override
    public void acceptInstruction(Instruction inst) throws Exception
    {
        super.acceptInstruction(inst);

        // check if its Load-Store, and set request in cache
        if (InstructionUtils.isLoadStore(inst))
        {

            switch (CPU.RUN_TYPE)
            {
                case MEMORY:
                    DCacheManager.instance.setRequest(inst);
                    break;
                case PIPELINE:
                    break;
                default:
                    throw new Exception("MemoryUnit Illegal CPU.RUN_TYPE ");
            }

        }

    }

    @Override
    public void executeUnit() throws Exception
    {
        validateQueueSize();

        Instruction inst = peekFirst();
        if (InstructionUtils.isNOOP(inst))
            return;

        System.out.println(CPU.CLOCK + " Memory " + inst.debugString());

        if (!WriteBackStage.getInstance().checkIfFree(inst))
            throw new Exception(
                    "MemoryUnit: won tie, WB Stage should always be free");

        if (!isReadyToSend())
            return;

        if (InstructionUtils.isLoadStore(inst))
        {
            if (InstructionUtils.isStore(inst))
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

        updateExitClockCycle(inst);
        WriteBackStage.getInstance().acceptInstruction(inst);
        rotatePipe();
    }

    @Override
    public boolean isReadyToSend() throws Exception
    {
        Instruction inst = peekFirst();
        if (InstructionUtils.isNOOP(inst))
            return true;
        if (InstructionUtils.isLoadStore(inst))
        {
            switch (CPU.RUN_TYPE)
            {
                case MEMORY:
                    return DCacheManager.instance.canProceed(inst);
                case PIPELINE:
                    return ((CPU.CLOCK - inst.getEntryCycleForStage(stageId
                            .getId())) >= getClockCyclesRequired());
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
