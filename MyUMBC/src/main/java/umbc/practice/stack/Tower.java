package umbc.practice.stack;

import java.util.Stack;

public class Tower
{

    private Stack<Integer> disks;

    private char           index;

    public Tower(char i)
    {
        this.index = i;
        disks = new Stack<Integer>();
    }

    public void addToTop(int value)
    {
        // If non empty stack, then check if top is < adding,
        if (!disks.isEmpty() && disks.peek() < value)
            System.out.println("Cannot push " + value + " over " + disks.peek()
                    + " on Tower " + index);
        else
            disks.push(value);
    }

    public void moveTopTo(Tower tower)
    {
        int top = disks.pop();
        tower.addToTop(top);
        System.out.println("Move " + top + " from " + index + " to "
                + tower.index);
    }

    public void moveDisks(int N, Tower destination, Tower buffer)
    {
        System.out.println(index + "->" + "(" + N + " , " + destination.index
                + " , " + buffer.index + ")");
        if (N > 0)
        {
            moveDisks(N - 1, buffer, destination);
            moveTopTo(destination);
            buffer.moveDisks(N - 1, destination, this);
        }

    }

    public static void main(String[] args)
    {
        Tower[] towers = new Tower[3];
        towers[0] = new Tower('A');
        towers[1] = new Tower('B');
        towers[2] = new Tower('C');

        int N = 2;
        for (int i = N - 1; i >= 0; i--)
            towers[0].addToTop(i);

        towers[0].moveDisks(N, towers[1], towers[2]);

        for (int i = 0; i < N; i++)
            System.out.println(towers[1].disks.pop());
    }

}
