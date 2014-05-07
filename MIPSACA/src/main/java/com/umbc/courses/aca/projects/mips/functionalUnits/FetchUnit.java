package com.umbc.courses.aca.projects.mips.functionalUnits;

import com.umbc.courses.aca.projects.mips.cache.ICacheManager;
import com.umbc.courses.aca.projects.mips.instructions.Instruction;
import com.umbc.courses.aca.projects.mips.instructions.InstructionUtils;
import com.umbc.courses.aca.projects.mips.program.ProgramManager;
import com.umbc.courses.aca.projects.mips.results.ResultsManager;
import com.umbc.courses.aca.projects.mips.stages.CPU;
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
        this.isPipelined = false;
        this.clockCyclesRequired = 1;
        this.pipelineSize = 1;
        this.stageId = StageType.IFSTAGE;
        createPipelineQueue(pipelineSize);
        hasBeenFlushed = false;
    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        return clockCyclesRequired;
    }

    @Override
    public void executeUnit() throws Exception
    {
        validateQueueSize();
        if (hasBeenFlushed)
        {
            System.out.println(CPU.CLOCK + " FetchUnit: Running Flush hack");
            hasBeenFlushed = false;
            return;
        }
        Instruction inst = peekFirst();

        if (!InstructionUtils.isNOOP(inst))
        {
            System.out.println(CPU.CLOCK + " Fetch  " + inst.debugString());

            if (DecodeStage.getInstance().checkIfFree(inst))
            {
                DecodeStage.getInstance().acceptInstruction(inst);
                updateExitClockCycle(inst);
                rotatePipe();
            }
        }

        fetchNextInstruction();
    }

    public void flushUnit() throws Exception
    {
        validateQueueSize();

        // flush the ICacheManager
        hasBeenFlushed = true;
        ICacheManager.instance.flush();

        Instruction inst = peekFirst();

        System.out.println("FetchUnit flushUnit called for inst: "
                + inst.debugString());

        if (InstructionUtils.isNOOP(inst))
            return;

        // update inst exitcycle
        // updateEntryClockCycle(inst); // hack dont do this!!!
        updateExitClockCycle(inst);
        // send to result manager
        ResultsManager.instance.addInstruction(inst);
        // remove inst & add NOOP
        rotatePipe();

        validateQueueSize();
    }

    private void fetchNextInstruction() throws Exception
    {
        // fetch a new instruction only if ifStage is free
        if (checkIfFree())
        {
            boolean checkInst = false;

            Instruction next = null;
            switch (CPU.RUN_TYPE)
            {
                case MEMORY:
                    next = ICacheManager.instance
                            .getInstructionFromCache(CPU.PROGRAM_COUNTER);
                    if (next != null)
                        checkInst = true;
                    break;

                case PIPELINE:
                    next = ProgramManager.instance
                            .getInstructionAtAddress(CPU.PROGRAM_COUNTER);
                    checkInst = true;
                    break;
            }

            if (checkInst && checkIfFree())
            {
                acceptInstruction(next);
                CPU.PROGRAM_COUNTER++;
            }

        } // end ifStage.checkIfFree

    }
}
