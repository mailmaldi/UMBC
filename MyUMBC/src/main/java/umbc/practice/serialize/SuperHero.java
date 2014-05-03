package umbc.practice.serialize;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SuperHero implements Serializable
{

    /**
     * 
     */
    private static final long   serialVersionUID = 5656670629730811672L;

    private static final Logger logger           = LoggerFactory
                                                         .getLogger(SuperHero.class);

    private transient String    name;
    private transient Date      debut;
    private transient int       numVillainsFought;
    private transient short     numAlterEgos;
    private transient boolean   masked;
    private transient boolean   female;
    private transient boolean   retired;

    public SuperHero(String name, Date debut, int numVillainsFought,
            short numAlterEgos, boolean masked, boolean female, boolean retired)
    {
        this.name = name;
        this.debut = debut;
        this.numVillainsFought = numVillainsFought;
        this.numAlterEgos = numAlterEgos;
        this.masked = masked;
        this.female = female;
        this.retired = retired;
    }

    private long getDebutTime()
    {
        return (debut != null) ? debut.getTime() : -1;
    }

    private void setDebutTime(long debutMillis)
    {
        debut = (debutMillis > 0) ? new Date(debutMillis) : new Date();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException
    {
        stream.defaultWriteObject();
        stream.writeUTF(name);
        stream.writeLong(getDebutTime());
        stream.writeInt(numVillainsFought);
        stream.writeShort(numAlterEgos);

        byte bools = 0x0;
        bools |= (masked) ? 0x1 : 0;
        bools |= (female) ? 0x2 : 0;
        bools |= (retired) ? 0x4 : 0;

        stream.writeByte(bools);

    }

    private void readObject(ObjectInputStream stream) throws IOException,
            ClassNotFoundException
    {
        stream.defaultReadObject();
        name = stream.readUTF();
        setDebutTime(stream.readLong());
        numVillainsFought = stream.readInt();
        numAlterEgos = stream.readShort();

        byte bools = stream.readByte();

        masked = (bools & 0x1) > 0;
        female = (bools & 0x2) > 0;
        female = (bools & 0x4) > 0;

    }

    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append(name).append(' ').append(debut.toString()).append(' ')
                .append(numVillainsFought);
        str.append(' ').append(numAlterEgos).append(' ').append(masked)
                .append(' ').append(female);
        str.append(' ').append(retired);
        return str.toString();
    }

    /*
     * Add getters and setters for fields that you need
     */

    public static void main(String[] args)
    {

        int N = 1000;

        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(new FileOutputStream(new File(
                    "SuperHero.ser")));

            for (int i = 0; i < N; i++)
            {
                SuperHero obj = new SuperHero("hero" + i, new Date(), i,
                        (short) i, i % 2 == 0, i % 2 == 0, i % 2 == 0);
                oos.writeObject(obj);
            }
            oos.flush();
            oos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        ObjectInputStream ois = null;
        try
        {

            ois = new ObjectInputStream(new FileInputStream(new File(
                    "SuperHero.ser")));

            while (true)
            {
                SuperHero obj = (SuperHero) ois.readObject();
                System.out.println(obj.toString());
            }

        }
        catch (EOFException eof)
        {
            // file ends
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (ois != null)
                try
                {
                    ois.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

        }
    }
}
