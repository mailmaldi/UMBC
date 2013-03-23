package com.umbc.practice;

public class LinkedListProblems
{
	public static void reverse(ListNode head)
	{
		ListNode current = head;
		head = null;
		while (current != null)
		{
			ListNode save = current;
			current = current.next;
			save.next = head;
			head = save;
		}
	}

	// The idea is to build up a new list from the old one, by walking down the old list, stripping off each node in turn and attaching it to the front of the new list. You need to
	// work through the code yourself to be satisfied that it works. Note that no new storage is required. All that happens is that references to existing nodes are reassigned.

	public static DoubleListNode reverse(DoubleListNode head)
	{
		DoubleListNode current = head;
		DoubleListNode temp = null;

		while (current != null)
		{
			temp = current.next; // temporarily store the next node
			current.next = current.prev; // now assign the prev!node to next of current node
			current.prev = temp; // update the previous pointer
			if (current.prev == null)
			{
				head = current;
				break;
			}
			current = current.prev;
		}
		return head;
	}

	public static void main(String[] args)
	{

		DoubleListNode tail = null;
		DoubleListNode head = null;
		for (int i = 0; i < 10; i++)
		{
			DoubleListNode node = new DoubleListNode();
			node.value = i;
			if (head == null)
			{
				head = node;
				tail = node;
			}
			if (i != 0)
			{
				tail.next = node;
				node.prev = tail;
				tail = node;
			}
		}
		printList(head);

		System.out.println("");
		System.out.println("Reversing the list");

		printList(reverse(head));
	}

	private static void printList(DoubleListNode head)
	{
		System.out.print("List : ");
		while (head != null)
		{
			System.out.print(head.value + " ");
			head = head.next;
		}
	}
}

class ListNode
{
	public int value;

	public ListNode next;
}

class DoubleListNode
{
	public int value;

	public DoubleListNode next;

	public DoubleListNode prev;
}
