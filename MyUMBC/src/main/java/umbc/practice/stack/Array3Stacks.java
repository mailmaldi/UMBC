package umbc.practice.stack;

import umbc.practice.Utils;

public class Array3Stacks
{

    // 0 -> n/3 , n/3 -> 2n/3 , 2n/3 -> n fixed
    // push , pop from stack 0,1,2

    private int[] array;

    private int   N;

    private int[] stackPointers;

    private int[] stackBase;

    private int   stackCapacity;

    public Array3Stacks(int arr_size)
    {
        this.N = arr_size;
        array = new int[this.N];
        stackPointers = new int[]
        { 0, this.N / 3, 2 * this.N / 3 };
        stackBase = new int[]
        { 0, this.N / 3, 2 * this.N / 3 };
        this.stackCapacity = this.N / 3;
    }

    public boolean push(int stackNum, int value)
    {
        if ((stackPointers[stackNum] - stackBase[stackNum]) == (this.stackCapacity))
            return false;

        array[stackPointers[stackNum]++] = value;

        return true;
    }

    public int pop(int stackNum)
    {
        if (stackPointers[stackNum] - stackBase[stackNum] == 0)
            return -1;

        return array[--stackPointers[stackNum]];
    }

    public int peek(int stackNum)
    {
        if (stackPointers[stackNum] - stackBase[stackNum] <= 0)
            return -1;

        return array[stackPointers[stackNum] - 1];
    }

    public static void main(String[] args)
    {
        Array3Stacks stacks = new Array3Stacks(11);
        for (int i = 0; i < 10; i++)
            System.out.println("" + i % 3 + " " + i + " "
                    + stacks.push(i % 3, i));

        stacks.printStacks();

        for (int i = 0; i <= 12; i++)
            System.out.println("" + i % 3 + " " + stacks.pop(i % 3));

        stacks.printStacks();

    }

    public void printStacks()
    {
        System.out.println("Arr Len: " + N);
        System.out.println("Capacity: " + stackCapacity);
        System.out.print("Array: ");
        Utils.printArray(array);
        System.out.print("Stackbase: ");
        Utils.printArray(stackBase);
        System.out.print("StackPointer: ");
        Utils.printArray(stackPointers);
    }

}
