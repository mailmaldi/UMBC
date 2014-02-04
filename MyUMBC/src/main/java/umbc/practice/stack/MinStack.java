package umbc.practice.stack;

public class MinStack extends SimpleStack
{

    SimpleStack minStack;

    public MinStack()
    {
        super();
        minStack = new SimpleStack();
    }

    public boolean push(int value)
    {
        if (super.push(value))
        {
            int minVal = minStack.peek();
            if (minVal == -1)
                minStack.push(value);
            else if (value <= minVal)
                minStack.push(value);
        }
        return true;
    }

    public int pop()
    {
        int val = super.pop();

        if (val != -1)
        {
            if (val == minStack.peek())
                minStack.pop();
        }

        return val;
    }

    public int peekMin()
    {

        return minStack.peek();
    }

    public static void main(String[] args)
    {
        MinStack stack = new MinStack();
        for (int i = 1; i < 10; i++)
            System.out.println(stack.push(i));
        for (int i = 0; i < 10; i++)
            System.out.println(stack.push(i));

        for (int i = 0; i < 20; i++)
        {
            System.out.print("min  " + stack.peekMin());
            System.out.print(" peek " + stack.peek());
            System.out.println(" pop  " + stack.pop());
        }

    }

}
