package cache;

public class ICache
{
    private int[] cache;

    public ICache()
    {
        cache = new int[4];

        for (int i = 0; i < 4; i++)
            cache[i] = -1;
    }

    public boolean checkInCache(int address)
    {
        int blockId = getICacheBlockId(address);
        int addressTag = getICacheTag(address);

        if ((-1 != cache[blockId]) && (addressTag == cache[blockId]))
            return true;
        return false;
    }

    public void setInCache(int address)
    {
        int blockId = getICacheBlockId(address);
        int addressTag = getICacheTag(address);

        cache[blockId] = addressTag;
    }

    public int getICacheBlockId(int address)
    {
        int blockId = address & 0b1100;
        blockId = blockId >> 2;
        return blockId;
    }

    public int getICacheTag(int address)
    {
        return address & 0b10000;
    }
}
