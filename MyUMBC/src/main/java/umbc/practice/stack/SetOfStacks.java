package umbc.practice.stack;

import java.util.ArrayList;

public class SetOfStacks
{

    ArrayList<SimpleStack> stacks;

    public SetOfStacks()
    {
        stacks = new ArrayList<SimpleStack>();

    }

    public boolean push(int val)
    {
        if (stacks.size() == 0)
            stacks.add(new SimpleStack(5));

        SimpleStack stack = getLastStack();
        if (stack.getSize() >= stack.getCapacity())
            stacks.add(new SimpleStack(5));

        stack = getLastStack();

        return stack.push(val);

    }

    public int pop()
    {
        if (stacks.size() == 0)
            return -1;

        SimpleStack stack = getLastStack();
        while (stack != null && stack.getSize() == 0)
        {
            stacks.remove(stacks.size() - 1);
            stack = getLastStack();
        }

        return (stack == null) ? -1 : stack.pop();

    }

    public int popAt(int i)
    {
        if (i < 0 || i > (stacks.size() - 1))
            return -1;

        SimpleStack stack = stacks.get(i);

        return stack.pop();
    }

    private SimpleStack getLastStack()
    {
        if (stacks.size() > 0)
            return stacks.get(stacks.size() - 1);
        return null;
    }

    public static void main(String[] args)
    {
        SetOfStacks stacks = new SetOfStacks();
        for (int i = 0; i < 20; i++)
            System.out.println(stacks.push(i));

        for (int i = 0; i < 5; i++)
            System.out.println(stacks.popAt(0));

        for (int i = 0; i < 15; i++)
            System.out.println(stacks.pop());

        for (int i = 0; i < 20; i++)
            System.out.println(stacks.push(i));

        for (int i = 0; i < 25; i++)
            System.out.println(stacks.pop());

        for (int i = 0; i < 20; i++)
            System.out.println(stacks.push(i));

        for (int i = 0; i < 5; i++)
            System.out.println(stacks.popAt(i));
        for (int i = 0; i < 25; i++)
            System.out.println(stacks.pop());
    }

}
