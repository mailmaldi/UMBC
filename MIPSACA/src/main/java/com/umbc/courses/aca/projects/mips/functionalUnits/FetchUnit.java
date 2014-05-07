package com.umbc.courses.aca.projects.mips.functionalUnits;

import com.umbc.courses.aca.projects.mips.cache.ICacheManager;
import com.umbc.courses.aca.projects.mips.instructions.Instruction;
import com.umbc.courses.aca.projects.mips.instructions.InstructionUtils;
import com.umbc.courses.aca.projects.mips.main.CPU;
import com.umbc.courses.aca.projects.mips.program.ProgramManager;
import com.umbc.courses.aca.projects.mips.results.ResultsManager;
import com.umbc.courses.aca.projects.mips.stages.DecodeStage;
import com.umbc.courses.aca.projects.mips.stages.StageType;

public class FetchUnit extends FunctionalUnit
{

    private boolean                   hasBeenFlushed;

    private static volatile FetchUnit instance;

    public static FetchUnit getInstance()
    {
        if (null == instance)
            synchronized (FetchUnit.class)
            {
                if (null == instance)
                    instance = new FetchUnit();
            }

        return instance;
    }

    private FetchUnit()
    {
        super();
        this.setPipelined(false);
        this.setClockCyclesRequired(1);
        this.pipelineSize = 1;
        this.stageId = StageType.IFSTAGE;
        createPipelineQueue(pipelineSize);
        hasBeenFlushed = false;
    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        return getClockCyclesRequired();
    }

    @Override
    public boolean isReadyToSend() throws Exception
    {
        if (!checkIfFree())
        {
            switch (CPU.RUN_TYPE)
            {
                case MEMORY:
                    return ICacheManager.instance.canProceed();
                case PIPELINE:
                    return true;
            }
        }
        return false;
    }

    @Override
    public void executeUnit() throws Exception
    {
        validateQueueSize();
        Instruction inst = peekFirst();
        if (hasBeenFlushed)
        {
            System.out.println(CPU.CLOCK + " FetchUnit: Running Flush hack");
            hasBeenFlushed = false;
            if (!InstructionUtils.isNOOP(inst))
            {
                updateExitClockCycle(inst);
                ResultsManager.instance.addInstruction(inst);
                rotatePipe();
                fetchNextInstruction();
                // Tell ICache that instruction was flushed ,
            }
            else
            {
                System.out.println(CPU.CLOCK + this.getClass().getSimpleName()
                        + " Flush should never find NOOP");
                throw new Exception(CPU.CLOCK + this.getClass().getSimpleName()
                        + " Flush should never find NOOP");
            }
        }
        else
        {
            if (checkIfFree())
            {
                fetchNextInstruction();
            }
            else
            {
                System.out.println(CPU.CLOCK + " Fetch  " + inst.debugString());
                if ((DecodeStage.getInstance().checkIfFree(inst))
                        && isReadyToSend())
                {
                    DecodeStage.getInstance().acceptInstruction(inst);
                    updateExitClockCycle(inst);
                    rotatePipe();
                    fetchNextInstruction();
                    // Set ICache Bus Free
                    // increment PC here
                }
                else
                {

                }
            }
        }
    }

    public void flushUnit() throws Exception
    {
        validateQueueSize();

        hasBeenFlushed = true;

        // flush the ICacheManager
        // ICacheManager.instance.flush();

        Instruction inst = peekFirst();
        System.out.println(CPU.CLOCK + " FetchUnit flushUnit called for inst: "
                + inst.debugString());

    }

    private void fetchNextInstruction() throws Exception
    {
        // fetch a new instruction only if ifStage is free
        if (checkIfFree())
        {

            Instruction next = ProgramManager.instance
                    .getInstructionAtAddress(CPU.PROGRAM_COUNTER);

            switch (CPU.RUN_TYPE)
            {
                case MEMORY:
                    ICacheManager.instance.setRequest(CPU.PROGRAM_COUNTER);
                    break;
                case PIPELINE:
                    break;
            }

            acceptInstruction(next);
            CPU.PROGRAM_COUNTER++;

        } // end ifStage.checkIfFree
    }

}
