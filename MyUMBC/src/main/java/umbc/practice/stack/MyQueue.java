package umbc.practice.stack;

import java.util.Stack;

/**
 * Queue using 2 stacks. enqueue: push into stack1. dequeue: if empty stack2,
 * pop all from stack1 into stack2 and pop size: stack1.size + stack2.size
 * 
 * @author Maldi
 * 
 * @param <T>
 */
public class MyQueue<T>
{
    Stack<T> stack1;
    Stack<T> stack2;

    public MyQueue()
    {
        this.stack1 = new Stack<T>();
        this.stack2 = new Stack<T>();
    }

    public int size()
    {
        return this.stack1.size() + this.stack2.size();
    }

    public void enqueue(T item)
    {
        this.stack1.push(item);
    }

    public T dequeue()
    {
        if (!stack2.isEmpty())
            return stack2.pop();

        while (!stack1.isEmpty())
        {
            stack2.push(stack1.pop());
        }

        return stack2.isEmpty() ? null : stack2.pop();
    }

}
