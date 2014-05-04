package functionalUnits;

import java.util.ArrayDeque;

import results.ResultsManager;
import stages.CPU;
import stages.DecodeStage;
import instructions.Instruction;
import instructions.NOOP;

public class FetchUnit extends FunctionalUnit
{

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
        this.stageId = 0;
        this.instructionQueue = new ArrayDeque<Instruction>();
        for (int i = 0; i < this.pipelineSize; i++)
            this.instructionQueue.add(new NOOP());

    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        // TODO Auto-generated method stub
        return clockCyclesRequired;
    }

    @Override
    public void executeUnit() throws Exception
    {

        validateQueueSize();

        Instruction inst = instructionQueue.peekLast();

        if (inst instanceof NOOP)
            return;

        if (DecodeStage.getInstance().checkIfFree(inst))
        {

            DecodeStage.getInstance().acceptInstruction(inst);
            instructionQueue.removeLast();
            instructionQueue.add(new NOOP());
        }

        validateQueueSize();

    }

    public void flushUnit() throws Exception
    {
        // TODO Auto-generated method stub
        validateQueueSize();

        Instruction inst = instructionQueue.peekLast();

        if (inst instanceof NOOP)
            return;

        // update inst exitcycle
        inst.exitCycle[stageId] = CPU.CLOCK;
        // send to result manager
        ResultsManager.instance.addInstruction(inst);
        // remove inst & add NOOP
        instructionQueue.removeLast();
        instructionQueue.addFirst(new NOOP());

        validateQueueSize();
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
