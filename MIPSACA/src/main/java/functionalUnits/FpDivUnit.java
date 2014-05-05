package functionalUnits;

import instructions.Instruction;
import instructions.NOOP;

import java.util.ArrayDeque;

import stages.StageType;
import stages.WriteBackStage;
import config.ConfigManager;

public class FpDivUnit extends FunctionalUnit
{

    private static volatile FpDivUnit instance;

    public static FpDivUnit getInstance()
    {
        if (null == instance)
            synchronized (FpDivUnit.class)
            {
                if (null == instance)
                    instance = new FpDivUnit();
            }

        return instance;
    }

    private FpDivUnit()
    {
        super();
        this.isPipelined = ConfigManager.instance.FPDividerPipelined;
        this.clockCyclesRequired = ConfigManager.instance.FPDivideLatency;

        this.pipelineSize = this.isPipelined ? ConfigManager.instance.FPDivideLatency
                : 1;

        this.instructionQueue = new ArrayDeque<Instruction>();
        for (int i = 0; i < this.pipelineSize; i++)
            this.instructionQueue.add(new NOOP());

        this.stageId = StageType.EXSTAGE;

    }

    @Override
    public void executeUnit() throws Exception
    {
        validateQueueSize();

        Instruction inst = instructionQueue.peekLast();
        if (!(inst instanceof NOOP))
        {
            inst.executeInstruction();

            if (!WriteBackStage.getInstance().checkIfFree(inst))
                throw new Exception(
                        "FpDivUnit: won tie, WB Stage should always be free");

            WriteBackStage.getInstance().acceptInstruction(inst);
            updateExitClockCycle(inst);
        }
        instructionQueue.removeLast();
        instructionQueue.addFirst(new NOOP());

    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        // TODO Auto-generated method stub
        return clockCyclesRequired;
    }
}
