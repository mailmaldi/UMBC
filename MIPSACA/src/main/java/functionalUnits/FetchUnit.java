package functionalUnits;

import instructions.Instruction;
import instructions.NOOP;
import results.ResultsManager;
import stages.CPU;
import stages.DecodeStage;
import stages.StageType;

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
        this.stageId = StageType.IFSTAGE;
        createPipelineQueue(pipelineSize);
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

        Instruction inst = peekFirst();

        if (inst instanceof NOOP)
            return;

        System.out.println(CPU.CLOCK + " Fetch  " + inst.debugString());

        if (DecodeStage.getInstance().checkIfFree(inst))
        {

            DecodeStage.getInstance().acceptInstruction(inst);
            updateExitClockCycle(inst);
            rotatePipe();
        }
    }

    public void flushUnit() throws Exception
    {
        validateQueueSize();

        Instruction inst = peekFirst();

        System.out.println("FetchUnit flushUnit called for inst: "
                + inst.debugString());

        if (inst instanceof NOOP)
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
}
