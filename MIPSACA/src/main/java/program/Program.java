package program;

import instruction.ADDD;
import instruction.AND;
import instruction.ANDI;
import instruction.BEQ;
import instruction.BNE;
import instruction.DADD;
import instruction.DADDI;
import instruction.DIVD;
import instruction.DSUB;
import instruction.DSUBI;
import instruction.HLT;
import instruction.Instruction;
import instruction.J;
import instruction.LD;
import instruction.LW;
import instruction.MULD;
import instruction.OR;
import instruction.ORI;
import instruction.SD;
import instruction.SUBD;
import instruction.SW;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class Program
{

    public static final Program instance = new Program();

    private Program()
    {

    }

    public int                       instructionCount = 0;
    public Map<Integer, Instruction> InstructionList  = new TreeMap<Integer, Instruction>();
    public Map<String, Integer>      LabelMap         = new TreeMap<String, Integer>();

    public void parse(String filePath) throws Exception
    {

        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(new File(filePath)));

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                parseInstLine(line);
            }

        }
        finally
        {
            if (reader != null)
                reader.close();
        }
    }

    private void parseInstLine(String line) throws Exception
    {
        Instruction inst = null;
        String tokens[] = new String[5];
        line = line.trim();
        line = line.toUpperCase();
        String sourceRegister1, sourceRegister2, destinationRegister;
        String[] operands;
        int offset;
        int immediate;

        /* CHECK IF IT HAS A LOOP */
        String loopName = "";
        if (line.contains(":"))
        {
            int index = line.lastIndexOf(':');
            loopName = line.substring(0, index);
            line = line.substring(index + 1);
            line = line.trim();
            LabelMap.put(loopName.trim(), instructionCount);
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
                inst = new LD(sourceRegister1, destinationRegister, offset);
                break;
            case "SW":
                operands = getOperands(tokens);
                sourceRegister1 = operands[0];
                offset = Integer.parseInt(operands[1].substring(0,
                        operands[1].lastIndexOf('(')));
                destinationRegister = operands[1].substring(
                        operands[1].lastIndexOf('(') + 1,
                        operands[1].lastIndexOf(')'));
                inst = new SW(sourceRegister1, destinationRegister, offset);
                break;
            case "S.D":
                operands = getOperands(tokens);
                sourceRegister1 = operands[0];
                offset = Integer.parseInt(operands[1].substring(0,
                        operands[1].lastIndexOf('(')));
                destinationRegister = operands[1].substring(
                        operands[1].lastIndexOf('(') + 1,
                        operands[1].lastIndexOf(')'));
                inst = new SD(sourceRegister1, destinationRegister, offset);
                break;
            case "ADD.D":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new ADDD(sourceRegister1, sourceRegister2,
                        destinationRegister);
                break;
            case "SUB.D":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new SUBD(sourceRegister1, sourceRegister2,
                        destinationRegister);
                break;
            case "MUL.D":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new MULD(sourceRegister1, sourceRegister2,
                        destinationRegister);
                break;
            case "DIV.D":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new DIVD(sourceRegister1, sourceRegister2,
                        destinationRegister);
                break;
            case "DADD":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new DADD(sourceRegister1, sourceRegister2,
                        destinationRegister);
                break;
            case "DADDI":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                immediate = Integer.parseInt(operands[2]);
                inst = new DADDI(sourceRegister1, destinationRegister,
                        immediate);
                break;
            case "DSUB":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new DSUB(sourceRegister1, sourceRegister2,
                        destinationRegister);
                break;
            case "DSUBI":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                immediate = Integer.parseInt(operands[2]);
                inst = new DSUBI(sourceRegister1, destinationRegister,
                        immediate);
                break;
            case "AND":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new AND(sourceRegister1, sourceRegister2,
                        destinationRegister);
                break;
            case "ANDI":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                immediate = Integer.parseInt(operands[2]);
                inst = new ANDI(sourceRegister1, destinationRegister, immediate);
                break;
            case "OR":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                sourceRegister2 = operands[2];
                inst = new OR(sourceRegister1, sourceRegister2,
                        destinationRegister);
                break;
            case "ORI":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                sourceRegister1 = operands[1];
                immediate = Integer.parseInt(operands[2]);
                inst = new ORI(sourceRegister1, destinationRegister, immediate);
                break;
            case "BEQ":
                operands = getOperands(tokens);
                sourceRegister1 = operands[0];
                sourceRegister2 = operands[1];
                destinationRegister = operands[2];
                inst = new BEQ(sourceRegister1, sourceRegister2,
                        destinationRegister);
                break;
            case "BNE":
                operands = getOperands(tokens);
                sourceRegister1 = operands[0];
                sourceRegister2 = operands[1];
                destinationRegister = operands[2];
                inst = new BNE(sourceRegister1, sourceRegister2,
                        destinationRegister);
                break;
            case "HLT":
                inst = new HLT();
                break;
            case "J":
                operands = getOperands(tokens);
                destinationRegister = operands[0];
                inst = new J(destinationRegister);
                break;
            default:
                throw new Exception("Illegal Instruction encountered");

        }
        inst.setLabel(loopName);

        InstructionList.put(instructionCount, inst);
        instructionCount++;
    }

    private String[] getOperands(String[] tokens) throws Exception
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
                        if (!LabelMap.containsKey(arg))
                            if (!tokens[0].equalsIgnoreCase("BEQ")
                                    && !tokens[0].equalsIgnoreCase("BNE")
                                    && !tokens[0].equalsIgnoreCase("J"))
                                throw new Exception(
                                        "Incorrect Format in inst.txt at Line"
                                                + instructionCount);
                }
                arg1[i] = argListArray[i];
            }
        }
        return arg1;
    }

    public void dumpProgram()
    {

        for (int key : InstructionList.keySet())
        {
            System.out.println(key + " " + InstructionList.get(key).toString());
        }
        System.out.println(instructionCount);

        for (String Key : LabelMap.keySet())
        {
            System.out.println(Key + " " + LabelMap.get(Key));
        }
    }

}
