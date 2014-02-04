package umbc.practice.stack;

public class SimpleQueue
{

    Node first, last;

    int  length;

    int  capacity;

    public SimpleQueue()
    {
        this(Integer.MAX_VALUE);
    }

    public SimpleQueue(int capacity)
    {
        this.capacity = capacity;
        this.length = 0;
        first = last = null;
    }

    public boolean enqueue(int value)
    {
        if (length >= capacity)
            return false;

        Node node = new Node(value);
        if (first == null)
        {
            first = node;
        }
        else
        {
            last.next = node;
        }
        last = node;
        length++;

        return true;
    }

    public int dequeue()
    {
        if (first == null)
            return -1;

        Node node = first;
        first = first.next;

        return node.value;
    }

    public void printQueue()
    {
        StringBuilder sb = new StringBuilder("Queue: ");
        Node curr = first;
        while (curr != null)
        {
            sb.append(curr.value).append(" ");
            curr = curr.next;
        }

        System.out.println(sb.toString());
    }

    public static void main(String[] args)
    {
        SimpleQueue que = new SimpleQueue();
        for (int i = 0; i < 10; i++)
        {
            que.enqueue(i);
            System.out.println(que.dequeue());
            System.out.println(que.dequeue());
        }

        que.printQueue();

        // for (int i = 0; i < 5; i++)
        // {
        // que.dequeue();
        // que.printQueue();
        // }
    }
}
