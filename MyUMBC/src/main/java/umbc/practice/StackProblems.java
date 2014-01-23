package umbc.practice;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Stack;

public class StackProblems
{

	/*
	 * Sorting can be performed with one more stack The idea is to pull an item from the original stack and push it on the other stack If pushing this item would violate the sort
	 * order of the new stack, we need to remove enough items from it so that it’s possible to push the new item Since the items we removed are on the original stack, we’re back
	 * where we started The algorithm is O(N^2) and appears below
	 */
	public static Stack<Integer> sort(Stack<Integer> s)
	{
		Stack<Integer> r = new Stack<Integer>();
		while (!s.isEmpty())
		{
			int tmp = s.pop();
			while (!r.isEmpty() && r.peek() > tmp)
			{
				s.push(r.pop());
			}
			r.push(tmp);
		}
		return r;
	}

	// peekminimum problem
	// but we only pop from it when the value we pop from the main stack is equal to the one on the min stack. We only push to the min stack when the value being pushed onto the
	// main stack is less than or equal to the current min value. This allows duplicate min values. getMinimum() is still just a peek operation.
	// http://stackoverflow.com/questions/7134129/stack-with-find-min-find-max-more-efficient-than-on

}

class PeekStack extends MyStackList
{
	private MyStackList minStack = new MyStackList();

	private MyStackList maxStack = new MyStackList();

	@Override
	public void push(Integer item)
	{
		/*
		 * Integer min = Math.min(peekMinimum().intValue(), item.intValue()); minStack.push(min); Integer max = Math.max(peekMaximum().intValue(), item.intValue());
		 * maxStack.push(max);
		 */
		if (item <= peekMinimum())
			minStack.push(item);
		if (item >= peekMaximum())
			maxStack.push(item);
		super.push(item);
	}

	@Override
	public Integer pop()
	{
		/*
		 * minStack.pop(); maxStack.pop();
		 */
		Integer item = super.pop();
		if (item == peekMinimum())
			minStack.pop();
		if (item == peekMaximum())
			maxStack.pop();
		return item;
	}

	@Override
	public Integer peek()
	{
		return super.peek();
	}

	public Integer peekMinimum()
	{
		return minStack.peek();

	}

	public Integer peekMaximum()
	{
		return maxStack.peek();

	}

}

interface MyStackInterface
{

	void push(Integer item);

	Integer pop();

	Integer peek();

	boolean isEmpty();

	void clear();

	int size();
}

class MyStackList implements MyStackInterface
{

	private ArrayList<Integer> list = new ArrayList<Integer>();

	@Override
	public void push(Integer item)
	{
		list.add(item);
		// list.addFirst(item);

	}

	@Override
	public Integer pop()
	{
		return list.remove(list.size() - 1);
		// return list.removeFirst();
	}

	@Override
	public Integer peek()
	{
		return list.get(list.size() - 1);
		// return list.getFirst();
	}

	@Override
	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	@Override
	public void clear()
	{
		list.clear();
	}

	@Override
	public int size()
	{
		return list.size();
	}

}

class MyStackArray implements MyStackInterface
{

	private Integer[] array;

	private int size = 0;

	public MyStackArray(int capacity)
	{
		array = new Integer[capacity];
	}

	@Override
	public void push(Integer item)
	{
		if (size == array.length)
		{
			throw new IllegalStateException("Cannot add to full stack");
		}
		array[size++] = item;
	}

	@Override
	public Integer pop()
	{
		if (size == 0)
		{
			throw new NoSuchElementException("Cannot pop from empty stack");
		}
		Integer result = array[size - 1];
		array[--size] = null;
		return result;
	}

	@Override
	public Integer peek()
	{
		if (size == 0)
		{
			throw new NoSuchElementException("Cannot peek into empty stack");
		}
		return array[size - 1];
	}

	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}

	@Override
	public void clear()
	{
		size = 0;

	}

	@Override
	public int size()
	{
		return size;
	}

}
