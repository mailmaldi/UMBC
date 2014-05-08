package com.umbc.courses.aca.projects.mips.functionalUnits;

import java.util.List;

import com.umbc.courses.aca.projects.mips.instructions.ConditionalBranchInstruction;
import com.umbc.courses.aca.projects.mips.instructions.EXFunctionalUnitType;
import com.umbc.courses.aca.projects.mips.instructions.Instruction;
import com.umbc.courses.aca.projects.mips.instructions.InstructionUtils;
import com.umbc.courses.aca.projects.mips.instructions.J;
import com.umbc.courses.aca.projects.mips.instructions.SourceObject;
import com.umbc.courses.aca.projects.mips.instructions.WriteBackObject;
import com.umbc.courses.aca.projects.mips.main.CPU;
import com.umbc.courses.aca.projects.mips.program.ProgramManager;
import com.umbc.courses.aca.projects.mips.registers.RegisterManager;
import com.umbc.courses.aca.projects.mips.results.ResultsManager;
import com.umbc.courses.aca.projects.mips.stages.ExStage;
import com.umbc.courses.aca.projects.mips.stages.FetchStage;
import com.umbc.courses.aca.projects.mips.stages.StageType;

public class DecodeUnit extends FunctionalUnit
{

    private static volatile DecodeUnit instance;

    public static DecodeUnit getInstance()
    {
        if (null == instance)
            synchronized (DecodeUnit.class)
            {
                if (null == instance)
                    instance = new DecodeUnit();
            }

        return instance;
    }

    private DecodeUnit()
    {
        super();
        setPipelined(true);
        setClockCyclesRequired(1);
        pipelineSize = 1;
        stageId = StageType.IDSTAGE;
        createPipelineQueue(pipelineSize);
    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        return getClockCyclesRequired();
    }

    @Override
    public void executeUnit() throws Exception
    {
        // Called by the decode stage
        validateQueueSize();

        Instruction inst = peekFirst();

        if (InstructionUtils.isNOOP(inst))
            return;

        System.out.println(CPU.CLOCK + " Decode " + inst.debugString());

        boolean hazards = processHazards(inst);

        if (!hazards)
            executeDecode(inst);

        validateQueueSize();

    }

    private void executeDecode(Instruction inst) throws Exception
    {

        updateExitClockCycle(inst);
        ResultsManager.instance.addInstruction(inst);

        // read source registers
        List<SourceObject> sources = inst.getSourceRegister();
        if (sources != null)
        {
            for (SourceObject register : sources)
            {
                register.setSource(RegisterManager.instance
                        .getRegisterValue(register.getSourceLabel()));
            }
        }

        // lock destination register
        WriteBackObject destReg = inst.getDestinationRegister();
        if (destReg != null)
            RegisterManager.instance.setRegisterBusy(destReg
                    .getDestinationLabel());

        // process J instruction
        if (InstructionUtils.isJump(inst))
        {
            // update PC to label address
            CPU.PROGRAM_COUNTER = ProgramManager.instance
                    .getInstructionAddreessForLabel(((J) inst)
                            .getDestinationLabel());

            FetchStage.getInstance().flushStage();

        }
        // process BNE,BEQ instruction
        else if (InstructionUtils.isBranch(inst))
        {
            ConditionalBranchInstruction temp = (ConditionalBranchInstruction) inst;
            if (temp.shouldBranch())
            {
                // update PC
                CPU.PROGRAM_COUNTER = ProgramManager.instance
                        .getInstructionAddreessForLabel(temp
                                .getDestinationLabel());
                // Flush fetch stage
                FetchStage.getInstance().flushStage();
            }
        }
        // process HLT instruction
        else if (InstructionUtils.isHLT(inst))
        {
            ResultsManager.instance.setHALT(true);
            return;
        }
        else
        {

            if (!ExStage.getInstance().checkIfFree(inst))
                throw new Exception(
                        "DecodeUnit: failed in exstage.checkIfFree after resolving struct hazard "
                                + inst.toString());

            ExStage.getInstance().acceptInstruction(inst);

        }

        rotatePipe();
    }

    private boolean processStruct(Instruction inst) throws Exception
    {
        // Check for possible STRUCT hazards
        EXFunctionalUnitType type = inst.getFunctionalUnitType();
        if (!type.equals(EXFunctionalUnitType.UNKNOWN))
        {
            if (!(ExStage.getInstance().checkIfFree(inst)))
            {
                inst.setStruct(true);
                return true;
            }
        }
        return false;
    }

    private boolean processRAW(Instruction inst) throws Exception
    {
        // Check for possible RAW hazards

        List<SourceObject> sources = inst.getSourceRegister();
        if (sources != null)
        {
            for (SourceObject register : sources)
            {
                if (!RegisterManager.instance.isRegisterFree(register
                        .getSourceLabel()))
                {
                    inst.setRAW(true);
                    return true;
                }
            }
        }

        return false;
    }

    private boolean processWAW(Instruction inst) throws Exception
    {
        WriteBackObject dest = inst.getDestinationRegister();
        if (dest != null)
        {

            if (!RegisterManager.instance.isRegisterFree(dest
                    .getDestinationLabel()))
            {
                inst.setWAW(true);
                return true;
            }
        }
        return false;
    }

    private boolean processWAR(Instruction inst)
    {
        return false;
    }

    private boolean processHazards(Instruction inst) throws Exception
    {
        return (processRAW(inst) || processWAR(inst) || processWAW(inst) || processStruct(inst));
    }
}
