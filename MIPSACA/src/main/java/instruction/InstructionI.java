package instruction;

import java.util.List;

public interface InstructionI
{
    public List<String> getSourceRegister();

    public String getDestinationRegister();

    public void executeInstruction();

    public void decodeInstruction();

    public void writeBackResult();
}
