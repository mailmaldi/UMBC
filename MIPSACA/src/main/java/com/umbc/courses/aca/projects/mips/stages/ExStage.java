package com.umbc.courses.aca.projects.mips.stages;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.collections4.ListUtils;

import com.umbc.courses.aca.projects.mips.functionalUnits.FPFunctionalUnit;
import com.umbc.courses.aca.projects.mips.functionalUnits.FpAddUnit;
import com.umbc.courses.aca.projects.mips.functionalUnits.FpDivUnit;
import com.umbc.courses.aca.projects.mips.functionalUnits.FpMulUnit;
import com.umbc.courses.aca.projects.mips.functionalUnits.FunctionalUnit;
import com.umbc.courses.aca.projects.mips.functionalUnits.IntegerUnit;
import com.umbc.courses.aca.projects.mips.functionalUnits.MemoryUnit;
import com.umbc.courses.aca.projects.mips.instructions.EXFunctionalUnitType;
import com.umbc.courses.aca.projects.mips.instructions.Instruction;

public class ExStage extends Stage
{

    private static volatile ExStage instance;

    public static ExStage getInstance()
    {
        if (null == instance)
            synchronized (ExStage.class)
            {
                if (null == instance)
                    instance = new ExStage();
            }
        return instance;
    }

    private IntegerUnit          iu;
    private MemoryUnit           mem;
    private FpAddUnit            fpadd;
    private FpMulUnit            fpmul;
    private FpDivUnit            fpdiv;
    private List<FunctionalUnit> tieBreakerList;

    private ExStage()
    {
        super();

        this.stageType = StageType.EXSTAGE;

        iu = IntegerUnit.getInstance();
        mem = MemoryUnit.getInstance();
        fpadd = FpAddUnit.getInstance();
        fpmul = FpMulUnit.getInstance();
        fpdiv = FpDivUnit.getInstance();

        tieBreakerList = new ArrayList<FunctionalUnit>();
        tieBreakerList.add(mem);
        tieBreakerList.add(fpadd);
        tieBreakerList.add(fpmul);
        tieBreakerList.add(fpdiv);
    }

    @Override
    public void execute() throws Exception
    {

        List<FunctionalUnit> readyList = new ArrayList<FunctionalUnit>();

        for (FunctionalUnit fu : tieBreakerList)
        {
            if (fu.isReadyToSend())
                readyList.add(fu);
        }

        if (readyList.size() <= 1)
        {
            for (FunctionalUnit fu : tieBreakerList)
                fu.executeUnit();
        }
        else
        {
            List<FunctionalUnit> winnerList = new ArrayList<FunctionalUnit>();
            winnerList.add(tieBreaker(readyList));

            if (winnerList.size() == 0)
                throw new Exception(
                        "ExStage: units said ready to send but winnerslist is empty");

            List<FunctionalUnit> losersList = ListUtils.subtract(readyList,
                    winnerList);

            List<FunctionalUnit> exeList = ListUtils.subtract(tieBreakerList,
                    losersList);

            // for all losers, run mark StructHazard
            for (FunctionalUnit fu : losersList)
            {
                fu.markStructHazard();
                // TODO for pipelined FPFunctionalUnit, move things 1 right

                if (fu instanceof FPFunctionalUnit)
                    ((FPFunctionalUnit) fu).rotatePipelineOnHazard();

            }
            // for exeList, execute
            for (FunctionalUnit fu : exeList)
                fu.executeUnit();
        }

        iu.executeUnit(); // Special Handling for this

    }

    // This method will be called by ID while executing and passing on the
    // instruction
    @Override
    public boolean acceptInstruction(Instruction instruction) throws Exception
    {
        // TODO Implement this method
        FunctionalUnit functionalUnit = getFunctionalUnit(instruction);
        if (!functionalUnit.checkIfFree(instruction))
            throw new Exception("EXSTAGE: Illegal state exception "
                    + instruction.toString());

        functionalUnit.acceptInstruction(instruction);

        return true;
    }

    // This method will be called by ID while executing and passing on the
    // instruction, and check for STRUCT hazard
    @Override
    public boolean checkIfFree(Instruction instruction) throws Exception
    {
        FunctionalUnit functionalUnit = getFunctionalUnit(instruction);
        return functionalUnit.checkIfFree(instruction);
    }

    /**
     * 
     * @param instruction
     *            to find which FU to use
     * @return
     * @throws Exception
     *             defensive
     */
    @SuppressWarnings("incomplete-switch")
    private FunctionalUnit getFunctionalUnit(Instruction instruction)
            throws Exception
    {

        if (instruction.getFunctionalUnitType() == EXFunctionalUnitType.UNKNOWN
                || instruction.getFunctionalUnitType() == null)
            throw new Exception("EXSTAGE: Incorrect type"
                    + instruction.toString());

        switch (instruction.getFunctionalUnitType())
        {

            case FPADD:
                return fpadd;

            case FPDIV:
                return fpdiv;

            case FPMUL:
                return fpmul;

            case IU:
                return iu;
        }

        return null;
    }

    private FunctionalUnit tieBreaker(List<FunctionalUnit> tieList)
    {
        TreeMap<Integer, FunctionalUnit> fUMap = new TreeMap<Integer, FunctionalUnit>();

        for (FunctionalUnit fu : tieList)
        {

            if (fu instanceof MemoryUnit)
            {
                mergeFUMap(fu.clockCyclesRequired + 1, fu, fUMap);
                continue;
            }

            if (fu.isPipelined)
            {
                mergeFUMap(fu.clockCyclesRequired, fu, fUMap);
            }
            else
            {
                mergeFUMap(1000 + fu.clockCyclesRequired, fu, fUMap);
            }
        }

        return fUMap.get(fUMap.lastKey());
    }

    private void mergeFUMap(int calculatedKey, FunctionalUnit fu,
            TreeMap<Integer, FunctionalUnit> map)
    {

        if (map.containsKey(calculatedKey))
        {

            FunctionalUnit mapEntry = (FunctionalUnit) map.get(calculatedKey);
            int fuEntry = fu.peekFirst().getEntryCycleForStage(
                    StageType.IFSTAGE.getId());
            int localEntry = mapEntry.peekFirst().getEntryCycleForStage(
                    StageType.IFSTAGE.getId());
            if (fuEntry < localEntry)
                map.put(calculatedKey, fu);

        }
        else
        {
            map.put(calculatedKey, fu);
        }
    }

}
