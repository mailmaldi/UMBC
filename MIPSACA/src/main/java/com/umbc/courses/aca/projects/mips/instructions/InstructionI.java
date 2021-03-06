package com.umbc.courses.aca.projects.mips.instructions;

import java.util.List;

public interface InstructionI
{
    /*
     * need list to check if not busy also need to set values in ID
     */
    public List<SourceObject> getSourceRegister();

    /*
     * Need to check for WAW hazards, WB & set dest busy
     */
    public WriteBackObject getDestinationRegister();

    /*
     * All execute does is locally do arithmetic operations or calculate target
     * address
     */
    public void executeInstruction() throws Exception;

    /*
     * For Decode Instruction we need the following
     */

}
