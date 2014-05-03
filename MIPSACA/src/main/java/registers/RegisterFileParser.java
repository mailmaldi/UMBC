package registers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class RegisterFileParser
{

    // Single instance.
    public static final RegisterFileParser instance = new RegisterFileParser();

    public void parseRegister(String fileName) throws Exception
    {

        try
        {
            BufferedReader bfread = new BufferedReader(new FileReader(new File(
                    fileName)));

            String line = null;
            int count = 0;
            while ((line = bfread.readLine()) != null)
            {
                line = line.trim();
                if (line.length() == 0)
                    throw new Exception(
                            "Less than 32 Integer register data in reg.txt, count= "
                                    + count);
                int value = Integer.parseInt(line, 2);
                RegisterManager.instance.setRegisterValue("R" + count, value);

                count++;
                if (count == 32)
                    break;
            }

            bfread.close();
        }
        catch (Exception e)
        {
            throw new Exception("Error Reading reg.txt ");
        }
    }
}
