package instruction;

import java.util.List;

public interface InstructionI
{
    public List<String> getSourceRegister();

    public String getDestinationRegister();

    /*
     * All execute does is locally do arithmetic operations or calculate target
     * address
     */
    public void executeInstruction();

    /*
     * 
     */
    public void decodeInstruction();

    /*
     * For WB, Instruction needs to return dest register label & its value
     */
    /**
     * 
     * @return null if instruction doesnt WB, an object else.
     */
    public WriteBackObject getWriteBackObject();
}
