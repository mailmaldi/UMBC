package com.umbc.courses.aca.projects.mips.functionalUnits;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;

import com.umbc.courses.aca.projects.mips.instructions.DADDI;
import com.umbc.courses.aca.projects.mips.instructions.HLT;
import com.umbc.courses.aca.projects.mips.instructions.Instruction;
import com.umbc.courses.aca.projects.mips.instructions.InstructionUtils;
import com.umbc.courses.aca.projects.mips.instructions.J;
import com.umbc.courses.aca.projects.mips.instructions.NOOP;
import com.umbc.courses.aca.projects.mips.stages.WriteBackStage;

public abstract class FPFunctionalUnit extends FunctionalUnit
{

    @Override
    public void executeUnit() throws Exception
    {
        validateQueueSize();

        Instruction inst = peekFirst();
        inst.executeInstruction();

        // if a NOOP, then dont care abt rotating
        if (!InstructionUtils.isNOOP(inst))
        {
            // if piped or non are ready to send
            // send to WB
            if (isReadyToSend())
            {
                if (!WriteBackStage.getInstance().checkIfFree(inst))
                    throw new Exception(this.getClass().getSimpleName()
                            + " won tie, WB Stage should always be free");

                WriteBackStage.getInstance().acceptInstruction(inst);
                updateExitClockCycle(inst);
            }
            else if (!isPipelined())
            {
                // if not pipelined code is not ready to send,
                // then dont rotate pipe
                return;
            }
        }

        rotatePipe();
    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        return getClockCyclesRequired();
    }

    public void rotatePipelineOnHazard() throws Exception
    {
        validateQueueSize();
        if (!isPipelined())
            return;
        // non pipelined, now iterate in reverse
        System.out.println(this.getClass().getSimpleName()
                + " Rotating pipeline on Hazard");

        Instruction objects[] = pipelineToArray();

        for (int i = 0; i < objects.length - 1; i++)
        {
            if (InstructionUtils.isNOOP(objects[i]))
            {
                Instruction temp = objects[i];
                objects[i] = objects[i + 1];
                objects[i + 1] = temp;
            }
        }

        createPipelineQueue(0);
        for (int i = 0; i < objects.length; i++)
        {
            addLast(objects[i]);
        }
        validateQueueSize();
    }

    public static void main(String[] args)
    {
        ArrayDeque<Instruction> deque = new ArrayDeque<Instruction>();
        deque.add(new HLT());
        deque.add(new NOOP());
        deque.add(new J("Jump"));
        deque.add(new NOOP());
        deque.add(new NOOP());
        deque.add(new DADDI("src1", "src2", 123));

        Instruction[] objects = (Instruction[]) deque
                .toArray(new Instruction[deque.size()]);

        System.out.println(Arrays.toString(objects));

        for (int i = 0; i < objects.length - 1; i++)
        {
            if (InstructionUtils.isNOOP(objects[i]))
            {
                Instruction temp = objects[i];
                objects[i] = objects[i + 1];
                objects[i + 1] = temp;
            }
        }

        System.out.println(Arrays.toString(objects));

        deque = new ArrayDeque<Instruction>();
        for (Instruction instruction : objects)
        {
            deque.add(instruction);
        }

        for (Iterator<Instruction> itr = deque.iterator(); itr.hasNext();)
        {
            System.out.print(itr.next().toString() + " ");
        }

    }
}
