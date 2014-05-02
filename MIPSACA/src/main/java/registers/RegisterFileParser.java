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

        BufferedReader bfread = new BufferedReader(new FileReader(new File(
                fileName)));

        String line = null;
        int count = 0;
        while ((line = bfread.readLine()) != null)
        {
            line = line.trim();
            int value = Integer.parseInt(line, 2);
            RegisterManager.instance.setRegisterValue("R" + count, value);
            // System.out.println(value);
            count++;
            if (count == 32)
                break;
        }

        bfread.close();
    }
}
