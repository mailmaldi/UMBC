package functionalUnits;

import instructions.Instruction;
import instructions.NOOP;
import stages.WriteBackStage;

public abstract class FPFunctionalUnit extends FunctionalUnit
{

    @Override
    public void executeUnit() throws Exception
    {
        validateQueueSize();

        Instruction inst = peekFirst();
        inst.executeInstruction();

        // TODO clean this up!!!
        if (!(inst instanceof NOOP))
        {
            if (isReadyToSend())
            {
                if (!WriteBackStage.getInstance().checkIfFree(inst))
                    throw new Exception(this.getClass().getSimpleName()
                            + " won tie, WB Stage should always be free");

                WriteBackStage.getInstance().acceptInstruction(inst);
                updateExitClockCycle(inst);
            }
            else if (!isPipelined)
            {
                return;
            }
        }

        // This is the same effect as running pipleine once :)
        rotatePipe();
    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        return clockCyclesRequired;
    }

    public void rotatePipelineOnHazard() throws Exception
    {
        validateQueueSize();
        if (!isPipelined)
            return;
        // non pipelined, now iterate in reverse

        Instruction objects[] = pipelineToArray();

        for (int i = objects.length; i > 0; i--)
        {
            if (objects[i] instanceof NOOP)
            {
                Instruction temp = objects[i];
                objects[i] = objects[i - 1];
                objects[i - 1] = temp;
            }
        }

        createPipelineQueue(0);
        for (int i = objects.length; i >= 0; i--)
        {
            addLast(objects[i]);
        }
        validateQueueSize();
    }
}
