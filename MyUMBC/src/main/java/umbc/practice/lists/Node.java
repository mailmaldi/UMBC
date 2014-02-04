package umbc.practice.lists;

public class Node<T extends Comparable<T>>
{

    private T       value;

    private Node<T> next;

    public Node(T value)
    {
        this.setValue(value);
        this.setNext(null);
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }

    public Node<T> getNext()
    {
        return next;
    }

    public void setNext(Node<T> next)
    {
        this.next = next;
    }

}
