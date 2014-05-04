package stages;

import instructions.Instruction;
import functionalUnits.DecodeUnit;

public class DecodeStage extends Stage
{

    private static volatile DecodeStage instance;

    public static DecodeStage getInstance()
    {

        if (null == instance)
            synchronized (DecodeStage.class)
            {
                if (null == instance)
                    instance = new DecodeStage();
            }

        return instance;
    }

    private DecodeUnit decode;

    private DecodeStage()
    {
        super();
        decode = DecodeUnit.getInstance();
    }

    @Override
    public void execute() throws Exception
    {
        decode.executeUnit();
    }

    @Override
    public boolean checkIfFree(Instruction instruction) throws Exception
    {
        return decode.checkIfFree(instruction);
    }

    @Override
    public boolean acceptInstruction(Instruction instruction) throws Exception
    {
        if (!decode.checkIfFree(instruction))
            throw new Exception("DECODESTAGE: Illegal state exception "
                    + instruction.toString());

        decode.acceptInstruction(instruction);

        return true;
    }

}