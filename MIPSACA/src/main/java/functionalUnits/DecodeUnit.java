package functionalUnits;

import instructions.BEQ;
import instructions.BNE;
import instructions.ConditionalBranchInstruction;
import instructions.FunctionalUnitType;
import instructions.HLT;
import instructions.Instruction;
import instructions.J;
import instructions.NOOP;
import instructions.SourceObject;
import instructions.WriteBackObject;

import java.util.ArrayDeque;
import java.util.List;

import program.ProgramManager;
import registers.RegisterManager;
import results.ResultsManager;
import stages.CPU;
import stages.ExStage;
import stages.FetchStage;

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
        this.isPipelined = false;
        this.clockCyclesRequired = 1;
        this.pipelineSize = 1;
        this.stageId = 1;

        this.instructionQueue = new ArrayDeque<Instruction>();
        for (int i = 0; i < this.pipelineSize; i++)
            this.instructionQueue.add(new NOOP());

    }

    private boolean processStruct(Instruction inst) throws Exception
    {
        // Check for possible STRUCT hazards
        FunctionalUnitType type = inst.functionalUnitType;
        if (!type.equals(FunctionalUnitType.UNKNOWN))
        {
            if (!(ExStage.getInstance().checkIfFree(inst)))
            {
                inst.STRUCT = true;
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
                    inst.RAW = true;
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
                inst.WAW = true;
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

    @Override
    public void executeUnit() throws Exception
    {
        // Called by the decode stage
        validateQueueSize();

        Instruction inst = instructionQueue.peekLast();

        if (inst instanceof NOOP)
            return;

        boolean hazards = processHazards(inst);

        if (!hazards)
            executeDecode(inst);

        validateQueueSize();

    }

    private void executeDecode(Instruction inst) throws Exception
    {
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
        if (inst instanceof J)
        {

            // update PC to label address
            CPU.PROGRAM_COUNTER = ProgramManager.instance
                    .getInstructionAddreessForLabel(((J) inst)
                            .getDestinationLabel());

            flushFetchAndReturn(inst);

        }
        // process BNE,BEQ instruction
        else if (inst instanceof ConditionalBranchInstruction)
        {
            if (inst instanceof BEQ)
            {
                if (((ConditionalBranchInstruction) inst).compareRegisters())
                {
                    // update PC
                    CPU.PROGRAM_COUNTER = ProgramManager.instance
                            .getInstructionAddreessForLabel(((BEQ) inst)
                                    .getDestinationLabel());
                    // Flush fetch stage
                    flushFetchAndReturn(inst);
                }
            }
            else if (inst instanceof BNE)
            {
                if (!((ConditionalBranchInstruction) inst).compareRegisters())
                {
                    // update PC
                    CPU.PROGRAM_COUNTER = ProgramManager.instance
                            .getInstructionAddreessForLabel(((BNE) inst)
                                    .getDestinationLabel());
                    // Flush fetch stage
                    flushFetchAndReturn(inst);
                }
            }
        }
        // process HLT instruction
        else if (inst instanceof HLT)
        {
            // flush fetch
            flushFetchAndReturn(inst);
        }
        else
        {

            if (!ExStage.getInstance().checkIfFree(inst))
                throw new Exception(
                        "DecodeUnit: failed in exstage.checkIfFree after resolving struct hazard "
                                + inst.toString());

            ExStage.getInstance().acceptInstruction(inst);

            instructionQueue.removeLast();
            instructionQueue.addFirst(new NOOP());
        }

        validateQueueSize();

    }

    private void flushFetchAndReturn(Instruction inst) throws Exception
    {
        // update inst exitcycle
        inst.exitCycle[stageId] = CPU.CLOCK;
        // send to result manager
        ResultsManager.instance.addInstruction(inst);
        // remove inst & add NOOP
        instructionQueue.removeLast();
        instructionQueue.addFirst(new NOOP());
        // Flush Fetch Stage, no matter what
        // TODO Flush Fetch call here
        FetchStage.getInstance().flushStage();

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
