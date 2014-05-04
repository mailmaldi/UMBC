package functionalUnits;

import instructions.Instruction;
import instructions.NOOP;

import java.util.ArrayDeque;
import java.util.Iterator;

import stages.CPU;

public abstract class FunctionalUnit
{

    public boolean                 isPipelined;
    public int                     clockCyclesRequired;
    public int                     pipelineSize;
    public int                     stageId;
    public ArrayDeque<Instruction> instructionQueue;

    public abstract void executeUnit() throws Exception;

    public abstract int getClockCyclesRequiredForNonPipeLinedUnit()
            throws Exception;

    // TODO: Increment entry cycle and exit cycle clock depending on stage
    // number
    public void acceptInstruction(Instruction instruction) throws Exception
    {

        if (!checkIfFree(instruction))
            throw new Exception("FUNCTIONALUNIT: Illegal state of queue");

        instructionQueue.removeFirst();
        instructionQueue.addFirst(instruction);

        validateQueueSize();

        if (this.stageId > 0)
            instruction.exitCycle[this.stageId - 1] = CPU.CLOCK;

        // This is hack for IU to MEM
        /* if (instruction.entryCycle[this.stageId] == 0) - Removed */
        instruction.entryCycle[this.stageId] = CPU.CLOCK;

    }

    protected void validateQueueSize() throws Exception
    {
        if (instructionQueue.size() != pipelineSize)
            throw new Exception("FUNCTIONALUNIT: Invalid Queue Size for unit "
                    + this.getClass().getName());
    }

    // This is being done for the execute stage functional units.
    public boolean checkIfFree(Instruction instruction) throws Exception
    {
        validateQueueSize();
        return (instructionQueue.peekFirst() instanceof NOOP) ? true : false;

    }

    public boolean isReadyToSend() throws Exception
    {
        if (isPipelined)
        {
            if (!(instructionQueue.peekLast() instanceof NOOP))
            {
                return true;
            }
        }
        else
        {
            if (!(instructionQueue.peekLast() instanceof NOOP)
                    && ((CPU.CLOCK - instructionQueue.peekLast().entryCycle[stageId]) >= getClockCyclesRequiredForNonPipeLinedUnit()))
            {
                return true;
            }
        }

        return false;
    }

    public void markStructHazard() throws Exception
    {
        // defensive, call validateQueueSize, may call isReadyToSend too!
        validateQueueSize();

        // starting from last inst till we reach first of Q or a NOOP & mark the
        // inst.StructHazard = true

        // TODO find this out else do
        // instructionQueue.peekLast().STRUCT = true;

        for (Iterator<Instruction> itr = this.instructionQueue
                .descendingIterator(); itr.hasNext();)
        {
            Instruction inst = itr.next();
            if (inst instanceof NOOP)
                break;

            inst.STRUCT = true;
        }

    }
}
