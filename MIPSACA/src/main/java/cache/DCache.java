package cache;

public class DCache
{
    DCacheSet[] dCacheSet;

    public DCache()
    {
        dCacheSet = new DCacheSet[2];
        dCacheSet[0] = new DCacheSet();
        dCacheSet[1] = new DCacheSet();
    }

    //
    // public int isPresent(int address)
    // {
    // DCacheSet set = getSet(address);
    // int baseAddress = getBaseAddress(address);
    //
    // for (int i = 0; i < 2; i++)
    // {
    // if (baseAddress == set.dCacheBlocks[i].baseAddress)
    // return i;
    // }
    // return -1;
    // }
    //
    // public DCacheBlock getBlockForAddress(int address)
    // {
    // DCacheSet set = getSet(address);
    //
    // int blockId = isPresent(address);
    // if (blockId == -1)
    // {
    // blockId = set.lru;
    // set.lru ^= 1;
    // }
    // else
    // {
    // // when found same address in cache
    // }
    // return set.dCacheBlocks[blockId];
    // }
    //
    // public boolean isDirty(int address)
    // {
    // DCacheBlock block = getBlockForAddress(address);
    // return (block != null && block.dirty);
    // }
    //
    // public void setData(int address)
    // {
    // DCacheBlock block = getBlockForAddress(address);
    // block.baseAddress = getBaseAddress(address);
    // block.dirty = true;
    // }

    private DCacheSet getSet(int address)
    {
        int setId = address & 0b10000;
        setId = setId >> 4;
        return dCacheSet[setId];
    }

    private int getBaseAddress(int address)
    {
        int baseAddress = address >> 2;
        baseAddress = baseAddress << 2;
        return baseAddress;
    }

    public boolean doesAddressExist(int address)
    {
        DCacheSet set = getSet(address);
        int baseAddress = getBaseAddress(address);
        return set.doesAddressExist(baseAddress);
    }

    public boolean isThereAFreeBlock(int address)
    {
        DCacheSet set = getSet(address);
        return set.hasFreeBlock();
    }

    public boolean isLRUBlockDirty(int address)
    {
        DCacheSet set = getSet(address);
        return set.isLRUBlockDirty();
    }

    public void updateBlock(int address, boolean store) throws Exception
    {
        // TODO Auto-generated method stub
        DCacheSet set = getSet(address);
        int baseAddress = getBaseAddress(address);

        DCacheBlock block = null;
        // update same address block, if not then free block , if not then
        // lrublock
        if (doesAddressExist(address))
        {
            block = set.getAddressBlock(baseAddress);
        }
        else if (isThereAFreeBlock(address))
        {
            block = set.getEmptyBlock(baseAddress);
        }
        else
        {
            block = set.getLRUBlock();
        }
        if (block == null)
            throw new Exception("DCache cannot find a null block");
        block.baseAddress = baseAddress;
        block.dirty = store;
        set.toggleLRU(block);
    }

}

class DCacheSet
{
    DCacheBlock[] dCacheBlocks;
    int           lru;

    public DCacheSet()
    {
        dCacheBlocks = new DCacheBlock[2];
        dCacheBlocks[0] = new DCacheBlock(-1);
        dCacheBlocks[1] = new DCacheBlock(-1);
        lru = 0;
    }

    public void toggleLRU(DCacheBlock block)
    {
        lru = (dCacheBlocks[0].equals(block)) ? 1 : 0;
    }

    public boolean isFree(int id)
    {
        return dCacheBlocks[id].isFree();
    }

    public boolean hasFreeBlock()
    {
        return isFree(0) || isFree(1);
    }

    public boolean doesAddressExist(int baseAddress)
    {
        return (dCacheBlocks[0].baseAddress == baseAddress)
                || (dCacheBlocks[0].baseAddress == baseAddress);
    }

    public boolean isLRUBlockDirty()
    {
        return dCacheBlocks[lru].dirty;
    }

    public DCacheBlock getAddressBlock(int baseAddress)
    {

        if (!doesAddressExist(baseAddress))
            return null;

        if (dCacheBlocks[0].baseAddress == baseAddress)
            return dCacheBlocks[0];
        return dCacheBlocks[1];
    }

    public DCacheBlock getEmptyBlock(int baseAddress)
    {
        if (!hasFreeBlock())
            return null;
        if (dCacheBlocks[0].baseAddress == -1)
            return dCacheBlocks[0];
        return dCacheBlocks[1];
    }

    public DCacheBlock getLRUBlock()
    {
        return dCacheBlocks[lru];
    }

}

class DCacheBlock
{
    int     baseAddress;
    boolean dirty;

    public DCacheBlock(int baseAddress)
    {
        this.baseAddress = baseAddress;
        this.dirty = false;
    }

    public boolean isFree()
    {
        return (baseAddress == -1);
    }

}
