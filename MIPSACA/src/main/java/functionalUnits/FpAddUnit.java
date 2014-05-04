package functionalUnits;

import instructions.Instruction;
import instructions.NOOP;

import java.util.ArrayDeque;

import stages.WriteBackStage;
import config.ConfigManager;

public class FpAddUnit extends FunctionalUnit
{

    private static volatile FpAddUnit instance;

    public static FpAddUnit getInstance()
    {
        if (null == instance)
            synchronized (FpAddUnit.class)
            {
                if (null == instance)
                    instance = new FpAddUnit();
            }

        return instance;
    }

    private FpAddUnit()
    {
        super();

        this.isPipelined = ConfigManager.instance.FPAdderPipelined;
        this.clockCyclesRequired = ConfigManager.instance.FPAdderLatency;

        this.pipelineSize = ConfigManager.instance.FPAdderPipelined ? ConfigManager.instance.FPAdderLatency
                : 1;

        this.instructionQueue = new ArrayDeque<Instruction>();
        for (int i = 0; i < this.pipelineSize; i++)
            this.instructionQueue.add(new NOOP());

        this.stageId = 2;
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

    /*
     * public void dumpUnitDetails() { System.out.println("isPipelined - " +
     * instance.isPipelined()); System.out.println("isAvailable - " +
     * instance.isAvailable()); System.out.println("Pipeline Size - " +
     * instance.getPipelineSize());
     * System.out.println("Clock Cycles required - " +
     * instance.getClockCyclesRequired()); }
     */

}