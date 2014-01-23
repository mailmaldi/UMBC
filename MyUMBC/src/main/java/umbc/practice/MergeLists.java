package umbc.practice;

public class MergeLists
{

	public static int[] mergeWithAux(int[] a, int[] b)
	{

		int[] answer = new int[a.length + b.length];
		int i = 0, j = 0, k = 0;

		while (i < a.length && j < b.length)
		{
			if (a[i] < b[j])
				answer[k++] = a[i++];

			else
				answer[k++] = b[j++];
		}

		while (i < a.length)
			answer[k++] = a[i++];

		while (j < b.length)
			answer[k++] = b[j++];

		return answer;
	}

	public static void mergeInPlace(int[] a, int[] b, int elemsA, int elemsB)
	{
		// assuming a.length = elemsA + elemsB , else add checks
		int i = elemsA - 1;
		int j = elemsB - 1;
		int k = elemsA + elemsB - 1;

		while (i >= 0 && j >= 0)
		{
			if (a[i] > b[j])
			{
				a[k--] = a[i--];
			}
			else
			{
				a[k--] = b[j--];
			}
		}
		while (j >= 0)
			a[k--] = b[j--];
		while (i >= 0)
			a[k--] = a[i--];
	}

	public static Node mergeListsNew(Node head1, Node head2)
	{
		if (head1 == null && head2 != null)
			return head2;
		if (head2 == null && head1 != null)
			return head1;
		if (head1 == null && head2 == null)
			return null;

		Node newHead = null;
		Node iter1 = head1;
		Node iter2 = head2;

		if (head1.data > head2.data)
		{
			newHead = head2;
			iter2 = head2.next;
		}
		else
		{
			newHead = head1;
			iter1 = head1.next;
		}
		Node iter3 = newHead;

		while (iter1 != null && iter2 != null)
		{
			if (iter1.data < iter2.data)
			{
				Node temp = iter1;
				iter3.setNext(temp);
				iter3 = iter3.next;
				iter1 = iter1.next;
			}
			else
			{
				Node temp = iter2;
				iter3.setNext(temp);
				iter3 = iter3.next;
				iter2 = iter2.next;
			}
		}
		if (iter1 != null)
			iter3.setNext(iter1);
		if (iter2 != null)
			iter3.setNext(iter2);

		return newHead;
	}

	public static Node mergeLists(Node head1, Node head2)
	{
		if (head1 == null && head2 != null)
			return head2;
		if (head2 == null && head1 != null)
			return head1;
		if (head1 == null && head2 == null)
			return null;

		Node head3 = null, latest3 = head3;

		Node iter1 = head1;
		Node iter2 = head2;
		while (iter1 != null && iter2 != null)
		{
			if (iter1.data < iter2.data)
			{
				if (head3 == null)
				{
					head3 = iter1;
					latest3 = head3;
				}
				else
				{
					Node temp = iter1;
					latest3.setNext(temp);
					latest3 = latest3.next;
				}
				iter1 = iter1.next;
			}
			else
			{
				if (head3 == null)
				{
					head3 = iter2;
					latest3 = head3;
				}
				else
				{
					latest3.setNext(iter2);
					latest3 = latest3.next;
				}
				iter2 = iter2.next;
			}
		}
		if (iter1 != null)
			latest3.setNext(iter1);
		if (iter2 != null)
			latest3.setNext(iter2);

		return head3;
	}

	public static Node createList(int min, int max)
	{
		Node head = null;
		Node latest = null;
		for (int i = min; i <= max; i++)
		{
			Node temp = new Node(i, null);
			if (head == null) // initial case
			{
				head = temp;
				latest = temp;
				continue;
			}
			// now head is initialized,we wont change head again, only the latest ptr to create list
			latest.setNext(temp);
			latest = latest.next;
		}
		return head;
	}

	public static void printList(Node head)
	{
		if (head == null)
			return;
		Node iter = head;
		StringBuffer sb = new StringBuffer();
		while (true)
		{
			sb.append(iter.data).append(" ");
			iter = iter.next;
			if (iter == null)
				break;
		}
		System.out.println(sb.toString());
	}

	public static void printArray(int[] arr)
	{
		for (int i = 0; i < arr.length; i++)
			System.out.print(arr[i] + " ");
		System.out.println();
		System.out.println("sum=" + sumArray(arr));
	}

	public static int sumArray(int[] array)
	{
		int sum = 0;
		for (int i = 0; i < array.length; i++)
			sum += array[i];
		return sum;
	}

	public static void main(String[] args)
	{
		Node head1 = createList(0, 10);
		Node head2 = createList(5, 15);
		printList(head1);
		printList(head2);
		Node head3 = mergeListsNew(head1, head2);
		printList(head3);

		int[] a = new int[10];
		int[] b = new int[5];
		for (int i = 0; i < 4; i++)
			a[i] = i * 2 + 1;
		for (int i = 0; i < 5; i++)
			b[i] = i * 2;

		printArray(a);
		printArray(b);
		mergeInPlace(a, b, 4, 5);
		printArray(a);

	}
}

class Node
{
	Node next;

	int data;

	public Node(int data, Node next)
	{
		this.data = data;
		this.next = next;
	}

	public void setNext(Node next)
	{
		this.next = next;
	}
}
