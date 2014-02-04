package umbc.practice.stack;

public class SimpleStack
{
    private Node top;

    private int  size;

    private int  capacity;
    

    public SimpleStack()
    {
        this(Integer.MAX_VALUE);
    }

    public SimpleStack(int capacity)
    {
        this.capacity = capacity;
        this.size = 0;
        top = null;
    }

    public boolean push(int value)
    {
        if (size >= capacity)
            return false;

        Node node = new Node(value);
        node.next = top;
        top = node;
        size++;

        return true;
    }

    public int pop()
    {
        if (top == null)
            return -1;
        Node node = top;
        top = top.next;
        size--;
        return node.value;
    }

    public int peek()
    {
        if (top == null)
            return -1;

        return top.value;
    }

    public int popBottom()
    {
        if (top == null)
            return -1;

        return -1;
    }

    public int getCapacity()
    {
        return this.capacity;
    }

    public int getSize()
    {
        return this.size;
    }
}
