package umbc.practice.lists;

import java.util.HashSet;
import java.util.Stack;

public class LinkedList<T extends Comparable<T>>
{

    private Node<T> head   = null;

    private int     length = 0;

    public LinkedList()
    {
        this.length = 0;
        this.head = null;
    }

    public int getLength()
    {
        return length;
    }

    public void addNodeToHead(Node<T> node)
    {
        Node<T> currHead = this.head;
        this.head = node;
        node.setNext(currHead);

    }

    public void addNodeToHead(T value)
    {
        Node<T> node = new Node<T>(value);
        addNodeToHead(node);
    }

    public void addNodeToTail(Node<T> node)
    {
        Node<T> curr = this.head;

        if (curr == null)
            this.head = node;
        else
        {
            while (curr.getNext() != null)
                curr = curr.getNext();
            curr.setNext(node);
        }
    }

    public void addNodeToTail(T value)
    {
        Node<T> node = new Node<T>(value);
        addNodeToTail(node);
    }

    public void delete(T value)
    {
        Node<T> curr = this.head;
        Node<T> prev = null;

        while (curr != null)
        {
            if (value.compareTo(curr.getValue()) == 0)
            {
                if (prev == null)
                    this.head = this.head.getNext();
                else
                    prev.setNext(curr.getNext());
            }
            prev = curr;
            curr = curr.getNext();
        }
    }

    public void printList()
    {
        Node<T> curr = head;

        StringBuilder sb = new StringBuilder();
        while (curr != null)
        {
            sb.append(curr.getValue()).append(" ");
            curr = curr.getNext();

        }
        System.out.println(sb.toString());
    }

    /**
     * 
     * MAIN FUNCTION
     */

    public static void main(String[] args)
    {
        LinkedList<Integer> list = new LinkedList<Integer>();
        list.printList();
        for (int i = 0; i < 20; i++)
        {
            list.addNodeToTail(i % 10);
        }
        list.addNodeToTail(11);
        list.printList();

        list.delete(0);
        list.printList();

        list.removeDuplicates();
        list.printList();

        System.out.println("Kth last: " + list.getKthLastElement(5).getValue());

        list.deleteMiddle(list.getKthLastElement(10));

        list.printList();

        for (int i = 10; i > 0; i--)
            list.addNodeToTail(i);

        list.printList();

        list.partitionList(2);
        list.printList();

        testAddLists();

        testForLoop();
        
        testPalindrome();

    }

    public void removeDuplicates()
    {
        HashSet<T> existing = new HashSet<T>();

        Node<T> curr = this.head;
        Node<T> prev = null;

        while (curr != null)
        {
            if (existing.contains(curr.getValue()))
            {
                prev.setNext(curr.getNext());
            }
            else
            {
                existing.add(curr.getValue());
                prev = curr;

            }
            curr = curr.getNext();
        }
    }

    public Node<T> getKthLastElement(int k)
    {
        if (k <= 0)
            return null;

        Node<T> fast = this.head;
        Node<T> slow = this.head;

        // move fast k ahead
        for (int i = 0; i < k; i++)
        {
            if (fast == null)
                return null;
            fast = fast.getNext();
        }

        while (fast != null)
        {
            fast = fast.getNext();
            slow = slow.getNext();
        }

        return slow;

    }

    public void deleteMiddle(Node<T> node)
    {
        // if node->next is null, then cannot delete
        // else copy value of next into current and set next as next's next

        if (node.getNext() == null)
        {
            // DO something
            return;
        }

        node.setValue(node.getNext().getValue());
        node.setNext(node.getNext().getNext());
    }

    public void partitionList(T value)
    {
        // iterate thru list, if curr < value , then add to less, else to
        // greater
        // at the end set next of last less to start of greater
        Node<T> lessHead = null;
        Node<T> greaterHead = null;
        Node<T> lessIter = null;
        Node<T> greaterIter = null;
        Node<T> curr = this.head;

        while (curr != null)
        {
            Node<T> next = curr.getNext();
            curr.setNext(null);

            if (value.compareTo(curr.getValue()) > 0)
            {
                if (lessHead == null)
                {
                    lessHead = curr;
                    lessIter = curr;
                }
                else
                {
                    lessIter.setNext(curr);
                    lessIter = curr;
                }
            }
            else
            {
                if (greaterHead == null)
                {
                    greaterHead = curr;
                    greaterIter = curr;
                }
                else
                {
                    greaterIter.setNext(curr);
                    greaterIter = curr;

                }
            }

            curr = next;

        }
        if (lessIter != null)
        {
            lessIter.setNext(greaterHead);
        }

        if (lessHead != null)
            this.head = lessHead;

    }

    public static LinkedList<Integer> addLists(LinkedList<Integer> list1,
            LinkedList<Integer> list2)
    {

        Node<Integer> l1iter = list1.head;
        Node<Integer> l2iter = list2.head;

        LinkedList<Integer> sum = new LinkedList<Integer>();

        int carry = 0;
        while (l1iter != null && l2iter != null)
        {
            int val = l1iter.getValue() + l2iter.getValue() + carry;
            sum.addNodeToTail(val % 10);
            carry = val / 10;
            l1iter = l1iter.getNext();
            l2iter = l2iter.getNext();
        }

        while (l1iter != null)
        {
            int val = l1iter.getValue() + carry;
            sum.addNodeToTail(val % 10);
            carry = val / 10;
            l1iter = l1iter.getNext();
        }
        while (l2iter != null)
        {
            int val = l2iter.getValue() + carry;
            sum.addNodeToTail(val % 10);
            carry = val / 10;
            l2iter = l2iter.getNext();
        }
        if (carry > 0)
        {
            sum.addNodeToTail(carry);
        }

        return sum;
    }

    public static void testAddLists()
    {
        LinkedList<Integer> l1 = new LinkedList<Integer>();
        l1.addNodeToTail(6);
        l1.addNodeToTail(7);
        l1.addNodeToTail(8);
        l1.addNodeToTail(9);
        l1.printList();

        LinkedList<Integer> l2 = new LinkedList<Integer>();
        l2.addNodeToTail(9);
        l2.addNodeToTail(8);
        l2.addNodeToTail(7);
        l2.printList();

        LinkedList<Integer> l3 = addLists(l1, l2);
        l3.printList();
    }

    // Check if linkedlist is a palindrome
    public boolean checkPalindrome()
    {
        if(this.head ==null)
            return false;
        
        Node<T> slow = this.head;
        Node<T> fast = this.head;

        Stack<T> stack = new Stack<T>();

        while (fast != null && fast.getNext() != null)
        {
            stack.push(slow.getValue());
            slow = slow.getNext();
            fast = fast.getNext().getNext();

        }

        if (fast != null) // length is odd fast jumps at length 1,3,5
        {
            slow = slow.getNext();
        }

        while (slow != null)
        {
            if(slow.getValue().compareTo(stack.pop()) !=0)
                return false;
            slow = slow.getNext();
        }

        return true;
    }
    
    public static void testPalindrome()
    {
        LinkedList<Character> list = new LinkedList<Character>();
        list.addNodeToTail('A');
        list.addNodeToTail('B');
        list.addNodeToTail('B');
        list.addNodeToTail('A');
        
        list.printList();
        
        System.out.println("PALINDROME? "+ list.checkPalindrome());
    }

    // get the node at the start of the loop
    public Node<T> getLoopStartNode()
    {
        Node<T> slow = this.head;
        Node<T> fast = this.head;

        while (fast != null && fast.getNext() != null)
        {
            slow = slow.getNext();
            fast = fast.getNext().getNext();

            if (slow == fast)
                break;
        }

        if (fast == null || fast.getNext() == null)
            return null;

        slow = this.head;

        while (slow != fast)
        {
            slow = slow.getNext();
            fast = fast.getNext();
        }

        return fast;
    }

    // check if loop in list
    public boolean checkForLoop()
    {
        Node<T> slow = this.head;
        Node<T> fast = this.head;

        while (fast != null && fast.getNext() != null)
        {
            slow = slow.getNext();
            fast = fast.getNext().getNext();

            if (slow == fast)
                return true;
        }

        return false;
    }

    public static void testForLoop()
    {
        LinkedList<Integer> list = new LinkedList<Integer>();
        for (int i = 0; i < 15; i++)
            list.addNodeToTail(i);
        list.printList();

        list.getKthLastElement(1).setNext(list.getKthLastElement(10));

        System.out.println("hasloop? " + list.checkForLoop());

        Node<Integer> loopNode = list.getLoopStartNode();
        if (loopNode != null)
            System.out.println("loop start element ? " + loopNode.getValue());
    }
}
