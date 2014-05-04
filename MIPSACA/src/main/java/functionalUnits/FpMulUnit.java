package functionalUnits;

import java.util.ArrayDeque;

import stages.WriteBackStage;
import config.ConfigManager;
import instructions.Instruction;
import instructions.NOOP;

public class FpMulUnit extends FunctionalUnit
{

    private static volatile FpMulUnit instance;

    public static FpMulUnit getInstance()
    {
        if (null == instance)
            synchronized (FpMulUnit.class)
            {
                if (null == instance)
                    instance = new FpMulUnit();
            }

        return instance;
    }

    private FpMulUnit()
    {
        super();
        this.isPipelined = ConfigManager.instance.FPMultPipelined;
        this.clockCyclesRequired = ConfigManager.instance.FPMultLatency;

        this.pipelineSize = this.isPipelined ? ConfigManager.instance.FPMultLatency
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
                        "FPMULTUNIT: won tie, WB Stage should always be free");

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
     * public void dumpUnitDetails(){
     * System.out.println("isPipelined - "+instance.isPipelined());
     * System.out.println("isAvailable - "+instance.isAvailable());
     * System.out.println("Pipeline Size - "+instance.getPipelineSize());
     * System.
     * out.println("Clock Cycles required - "+instance.getClockCyclesRequired
     * ()); }
     */
}
