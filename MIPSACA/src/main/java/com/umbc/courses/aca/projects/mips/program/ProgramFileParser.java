package com.umbc.courses.aca.projects.mips.program;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.umbc.courses.aca.projects.mips.instructions.ADDD;
import com.umbc.courses.aca.projects.mips.instructions.AND;
import com.umbc.courses.aca.projects.mips.instructions.ANDI;
import com.umbc.courses.aca.projects.mips.instructions.BEQ;
import com.umbc.courses.aca.projects.mips.instructions.BNE;
import com.umbc.courses.aca.projects.mips.instructions.DADD;
import com.umbc.courses.aca.projects.mips.instructions.DADDI;
import com.umbc.courses.aca.projects.mips.instructions.DIVD;
import com.umbc.courses.aca.projects.mips.instructions.DSUB;
import com.umbc.courses.aca.projects.mips.instructions.DSUBI;
import com.umbc.courses.aca.projects.mips.instructions.HLT;
import com.umbc.courses.aca.projects.mips.instructions.Instruction;
import com.umbc.courses.aca.projects.mips.instructions.J;
import com.umbc.courses.aca.projects.mips.instructions.LD;
import com.umbc.courses.aca.projects.mips.instructions.LW;
import com.umbc.courses.aca.projects.mips.instructions.MULD;
import com.umbc.courses.aca.projects.mips.instructions.OR;
import com.umbc.courses.aca.projects.mips.instructions.ORI;
import com.umbc.courses.aca.projects.mips.instructions.SD;
import com.umbc.courses.aca.projects.mips.instructions.SUBD;
import com.umbc.courses.aca.projects.mips.instructions.SW;
import com.umbc.courses.aca.projects.mips.registers.ValidateRegisterName;
import com.umbc.courses.aca.projects.mips.results.ResultsManager;

public class ProgramFileParser
{

    public static void parse(String filePath) throws Exception
    {

        BufferedReader reader = null;
        int count = 0;
        String line = null;
        List<String> labels = new ArrayList<String>();
        try
        {
            reader = new BufferedReader(new FileReader(new File(filePath)));
            while ((line = reader.readLine()) != null)
            {
                count++;
                parseInstLine(line, labels);
            }

            // check if all labels have instructions
            // also check if all branch,J inst's labels are present
            for (String label : labels)
            {
                if (!ProgramManager.instance.LabelMap.containsKey(label))
                    throw new Exception("Label [" + label + "]"
                            + " not defined anywhere in program");
            }

            // HACK

            Instruction inst = new HLT();
            inst.setPrintableInstruction(inst.toString());
            ProgramManager.instance.InstructionList.put(
                    ProgramManager.instance.instructionCount++, inst);

        }
        catch (Exception e)
        {
            String message = "Invalid instructions file: " + e.getMessage()
                    + " Line [" + count + "]" + " Line Value: " + line;
            System.err.println(message);
            ResultsManager.instance.writeLine(message);
            throw e;

        }
        finally
        {
            if (reader != null)
                reader.close();
        }

    }

    private static void parseInstLine(String line, List<String> labels)
            throws Exception
    {
        line = line.trim().toUpperCase();

        Instruction inst = null;
        String tokens[] = new String[5];

        String sourceRegister1, sourceRegister2, destinationRegister;
        String[] operands;
        int offset;
        int immediate;
        String destinatonLabel;

        /* CHECK IF IT HAS A LOOP , i.e. a LABEL */
        String loopName = "";
        if (line.contains(":"))
        {
            int index = line.lastIndexOf(':');
            loopName = line.substring(0, index);
            loopName = validateAndGetLabelString(loopName);
            line = line.substring(index + 1);
            line = line.trim();
            ProgramManager.instance.LabelMap.put(loopName.trim(),
                    ProgramManager.instance.instructionCount);
        }

        tokens = line.split("[\\s]", 2);
        String opcode = tokens[0].trim().toUpperCase();

        // System.out.println(Arrays.toString(tokens));

        switch (opcode)
        {
            case "LW":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                offset = Integer.parseInt(operands[1].substring(0,
                        operands[1].lastIndexOf('(')));
                sourceRegister1 = operands[1].substring(
                        operands[1].lastIndexOf('(') + 1,
                        operands[1].lastIndexOf(')'));
                sourceRegister1 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister1);
                destinationRegister = ValidateRegisterName
                        .getValidIntegerRegisterName(destinationRegister);
                inst = new LW(sourceRegister1, destinationRegister, offset);
                break;
            case "L.D":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                offset = Integer.parseInt(operands[1].substring(0,
                        operands[1].lastIndexOf('(')));
                sourceRegister1 = operands[1].substring(
                        operands[1].lastIndexOf('(') + 1,
                        operands[1].lastIndexOf(')'));
                sourceRegister1 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister1);
                destinationRegister = ValidateRegisterName
                        .getValidFPRegisterName(destinationRegister);
                inst = new LD(sourceRegister1, destinationRegister, offset);
                break;
            case "SW":
                operands = getOperands(tokens);
                sourceRegister1 = operands[0];
                offset = Integer.parseInt(operands[1].substring(0,
                        operands[1].lastIndexOf('(')));
                sourceRegister2 = operands[1].substring(
                        operands[1].lastIndexOf('(') + 1,
                        operands[1].lastIndexOf(')'));
                sourceRegister1 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister1);
                sourceRegister2 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister2);
                inst = new SW(sourceRegister1, sourceRegister2, offset);
                break;
            case "S.D":
                operands = getOperands(tokens);
                sourceRegister1 = operands[0];
                offset = Integer.parseInt(operands[1].substring(0,
                        operands[1].lastIndexOf('(')));
                sourceRegister2 = operands[1].substring(
                        operands[1].lastIndexOf('(') + 1,
                        operands[1].lastIndexOf(')'));
                sourceRegister1 = ValidateRegisterName
                        .getValidFPRegisterName(sourceRegister1);
                sourceRegister2 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister2);
                inst = new SD(sourceRegister1, sourceRegister2, offset);
                break;
            case "ADD.D":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new ADDD(sourceRegister1, sourceRegister2,
                        destinationRegister);
                sourceRegister1 = ValidateRegisterName
                        .getValidFPRegisterName(sourceRegister1);
                sourceRegister2 = ValidateRegisterName
                        .getValidFPRegisterName(sourceRegister2);
                destinationRegister = ValidateRegisterName
                        .getValidFPRegisterName(destinationRegister);
                break;
            case "SUB.D":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new SUBD(sourceRegister1, sourceRegister2,
                        destinationRegister);
                sourceRegister1 = ValidateRegisterName
                        .getValidFPRegisterName(sourceRegister1);
                sourceRegister2 = ValidateRegisterName
                        .getValidFPRegisterName(sourceRegister2);
                destinationRegister = ValidateRegisterName
                        .getValidFPRegisterName(destinationRegister);
                break;
            case "MUL.D":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new MULD(sourceRegister1, sourceRegister2,
                        destinationRegister);
                sourceRegister1 = ValidateRegisterName
                        .getValidFPRegisterName(sourceRegister1);
                sourceRegister2 = ValidateRegisterName
                        .getValidFPRegisterName(sourceRegister2);
                destinationRegister = ValidateRegisterName
                        .getValidFPRegisterName(destinationRegister);
                break;
            case "DIV.D":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new DIVD(sourceRegister1, sourceRegister2,
                        destinationRegister);
                sourceRegister1 = ValidateRegisterName
                        .getValidFPRegisterName(sourceRegister1);
                sourceRegister2 = ValidateRegisterName
                        .getValidFPRegisterName(sourceRegister2);
                destinationRegister = ValidateRegisterName
                        .getValidFPRegisterName(destinationRegister);
                break;
            case "DADD":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new DADD(sourceRegister1, sourceRegister2,
                        destinationRegister);
                sourceRegister1 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister1);
                sourceRegister2 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister2);
                destinationRegister = ValidateRegisterName
                        .getValidIntegerRegisterName(destinationRegister);
                break;
            case "DADDI":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                immediate = Integer.parseInt(operands[2]);
                inst = new DADDI(sourceRegister1, destinationRegister,
                        immediate);
                sourceRegister1 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister1);
                destinationRegister = ValidateRegisterName
                        .getValidIntegerRegisterName(destinationRegister);
                break;
            case "DSUB":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new DSUB(sourceRegister1, sourceRegister2,
                        destinationRegister);
                sourceRegister1 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister1);
                sourceRegister2 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister2);
                destinationRegister = ValidateRegisterName
                        .getValidIntegerRegisterName(destinationRegister);
                break;
            case "DSUBI":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                immediate = Integer.parseInt(operands[2]);
                inst = new DSUBI(sourceRegister1, destinationRegister,
                        immediate);
                sourceRegister1 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister1);
                destinationRegister = ValidateRegisterName
                        .getValidIntegerRegisterName(destinationRegister);
                break;
            case "AND":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new AND(sourceRegister1, sourceRegister2,
                        destinationRegister);
                sourceRegister1 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister1);
                sourceRegister2 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister2);
                destinationRegister = ValidateRegisterName
                        .getValidIntegerRegisterName(destinationRegister);
                break;
            case "ANDI":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                immediate = Integer.parseInt(operands[2]);
                inst = new ANDI(sourceRegister1, destinationRegister, immediate);
                sourceRegister1 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister1);
                destinationRegister = ValidateRegisterName
                        .getValidIntegerRegisterName(destinationRegister);
                break;
            case "OR":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new OR(sourceRegister1, sourceRegister2,
                        destinationRegister);
                sourceRegister1 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister1);
                sourceRegister2 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister2);
                destinationRegister = ValidateRegisterName
                        .getValidIntegerRegisterName(destinationRegister);
                break;
            case "ORI":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                immediate = Integer.parseInt(operands[2]);
                inst = new ORI(sourceRegister1, destinationRegister, immediate);
                sourceRegister1 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister1);
                destinationRegister = ValidateRegisterName
                        .getValidIntegerRegisterName(destinationRegister);
                break;
            case "BEQ":
                operands = getOperands(tokens);
                sourceRegister1 = operands[0];
                sourceRegister2 = operands[1];
                destinatonLabel = validateAndGetLabelString(operands[2]);
                inst = new BEQ(sourceRegister1, sourceRegister2,
                        destinatonLabel);
                sourceRegister1 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister1);
                sourceRegister2 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister2);
                labels.add(destinatonLabel);
                break;
            case "BNE":
                operands = getOperands(tokens);
                sourceRegister1 = operands[0];
                sourceRegister2 = operands[1];
                destinatonLabel = validateAndGetLabelString(operands[2]);
                inst = new BNE(sourceRegister1, sourceRegister2,
                        destinatonLabel);
                sourceRegister1 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister1);
                sourceRegister2 = ValidateRegisterName
                        .getValidIntegerRegisterName(sourceRegister2);
                labels.add(destinatonLabel);
                break;
            case "HLT":
                inst = new HLT();
                break;
            case "J":
                operands = getOperands(tokens);
                destinatonLabel = validateAndGetLabelString(operands[0]);
                inst = new J(destinatonLabel);
                labels.add(destinatonLabel);
                break;
            default:
                throw new Exception("Illegal Instruction ");

        }
        loopName = (loopName != null && loopName.length() > 0) ? loopName
                + ": " : "";
        inst.setPrintableInstruction(loopName + inst.toString());

        ProgramManager.instance.InstructionList.put(
                ProgramManager.instance.instructionCount, inst);
        ProgramManager.instance.instructionCount++;
    }

    private static String[] getOperands(String[] tokens) throws Exception
    {

        String argListArray[] = new String[3];
        String arg1[] = new String[3];
        if (!tokens[0].trim().equalsIgnoreCase("HLT"))
        {
            String argList = tokens[1];
            argListArray = argList.trim().split(",");
            for (int i = 0; i < argListArray.length; i++)
            {
                String arg = argListArray[i] = argListArray[i].trim();
                /* VALIDATE ARG */
                if (arg.charAt(0) != 'R' && arg.charAt(0) != 'F')
                {
                    if (arg.charAt(0) < '0' || arg.charAt(0) > '9')
                        if (!ProgramManager.instance.LabelMap.containsKey(arg))
                            if (!tokens[0].equalsIgnoreCase("BEQ")
                                    && !tokens[0].equalsIgnoreCase("BNE")
                                    && !tokens[0].equalsIgnoreCase("J"))
                                throw new Exception(
                                        "Incorrect Format in inst.txt at Line"
                                                + ProgramManager.instance.instructionCount);
                }
                arg1[i] = argListArray[i];
            }
        }
        return arg1;
    }

    private static String validateAndGetLabelString(String string)
            throws Exception
    {
        if (string == null || string.length() == 0)
            throw new Exception("LABEL String is empty");
        return string.trim();
    }

}
