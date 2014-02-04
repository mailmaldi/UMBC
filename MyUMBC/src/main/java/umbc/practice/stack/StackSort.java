package umbc.practice.stack;

import java.util.Stack;

public class StackSort
{

    public static Stack<Integer> sort(Stack<Integer> stack)
    {
        Stack<Integer> buffer = new Stack<Integer>();

        while (!stack.isEmpty())
        {
            Integer temp = stack.pop();

            // Dont have to do a separate check for isEmpty since the next check
            // will take care of it
            while (!buffer.isEmpty() && buffer.peek() > temp)
                // Dont forget the isempty
                stack.push(buffer.pop());
            buffer.push(temp);
        }

        return buffer;
    }

    public static void main(String[] args)
    {
        Stack<Integer> stack = new Stack<Integer>();

        for (int i = 0; i < 20; i++)
            stack.push(i % 5);

        Stack<Integer> ret = sort(stack);

        while (!ret.isEmpty())
            System.out.print(ret.pop() + " ");

    }

}
