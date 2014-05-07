package com.umbc.courses.aca.projects.mips.functionalUnits;

import java.util.ArrayDeque;

import com.umbc.courses.aca.projects.mips.instructions.Instruction;
import com.umbc.courses.aca.projects.mips.instructions.InstructionUtils;
import com.umbc.courses.aca.projects.mips.instructions.NOOP;
import com.umbc.courses.aca.projects.mips.main.CPU;
import com.umbc.courses.aca.projects.mips.stages.StageType;

public abstract class FunctionalUnit
{

    public boolean                  isPipelined;
    public int                      clockCyclesRequired;
    public int                      pipelineSize;
    public StageType                stageId;
    private ArrayDeque<Instruction> instructionQueue;

    public abstract void executeUnit() throws Exception;

    public abstract int getClockCyclesRequiredForNonPipeLinedUnit()
            throws Exception;

    public void acceptInstruction(Instruction instruction) throws Exception
    {

        if (!checkIfFree(instruction))
            throw new Exception("FUNCTIONALUNIT: Illegal state of queue "
                    + this.getClass().getSimpleName());

        removeLast();
        addLast(instruction);

        instruction.setEntryCycleForStage(stageId.getId(), CPU.CLOCK);

        validateQueueSize();

        // System.out.format("%-3s  %-20s %50s %n", CPU.CLOCK, this.getClass()
        // .getSimpleName(), instruction.debugString());

    }

    protected void validateQueueSize() throws Exception
    {
        if (instructionQueue.size() != pipelineSize)
            throw new Exception("FUNCTIONALUNIT: Invalid Queue Size for unit "
                    + this.getClass().getName());
    }

    public boolean checkIfFree(Instruction instruction) throws Exception
    {
        validateQueueSize();
        return InstructionUtils.isNOOP(peekLast());

    }

    // currently only FetchUnit uses this to call itself!!!
    // why does my brain work like this
    public boolean checkIfFree() throws Exception
    {
        return checkIfFree(null);
    }

    // Some Functional Units override this, Memory Unit
    public boolean isReadyToSend() throws Exception
    {
        if (isPipelined)
        {
            if (!InstructionUtils.isNOOP(peekFirst()))
                return true;
        }
        else
        {
            if (!InstructionUtils.isNOOP(peekFirst())
                    && ((CPU.CLOCK - peekFirst().getEntryCycleForStage(
                            stageId.getId())) >= getClockCyclesRequiredForNonPipeLinedUnit()))
                return true;
        }
        return false;
    }

    /**
     * This will mark ONLY the last instruction in pipeline as Struct hazard
     * 
     * @throws Exception
     */
    public void markStructHazard() throws Exception
    {
        // defensive, call validateQueueSize, may call isReadyToSend too!
        validateQueueSize();

        peekFirst().setStruct(true);

        // // starting from last inst till we reach first of Q or a NOOP mark
        // // Struct Hazard
        // for (Iterator<Instruction> itr = this.instructionQueue
        // .descendingIterator(); itr.hasNext();)
        // {
        // Instruction inst = itr.next();
        // if (inst instanceof NOOP)
        // break;
        //
        // inst.STRUCT = true;
        // }
    }

    protected void updateExitClockCycle(Instruction inst)
    {
        inst.setExitCycleForStage(this.stageId.getId(), CPU.CLOCK);
    }

    /**
     * Functions to update instructionQueue
     * 
     */

    protected Instruction[] pipelineToArray()
    {
        return (Instruction[]) instructionQueue
                .toArray(new Instruction[instructionQueue.size()]);
    }

    protected void createPipelineQueue(int size)
    {
        instructionQueue = new ArrayDeque<Instruction>();
        for (int i = 0; i < size; i++)
            instructionQueue.addLast(new NOOP());
    }

    protected void rotatePipe() throws Exception
    {
        validateQueueSize();
        instructionQueue.removeFirst();
        instructionQueue.addLast(new NOOP());
    }

    public Instruction peekFirst()
    {
        return instructionQueue.peekFirst();
    }

    public Instruction peekLast()
    {
        return instructionQueue.peekLast();
    }

    protected void addFirst(Instruction inst)
    {
        instructionQueue.addFirst(inst);
    }

    protected void addLast(Instruction inst)
    {
        instructionQueue.addLast(inst);
    }

    protected Instruction removeFirst()
    {
        return instructionQueue.removeFirst();
    }

    protected Instruction removeLast()
    {
        return instructionQueue.removeLast();
    }
}
