package main.java.umbc.practice;

import java.util.Random;

public class ProducerConsumer
{

	public static void main(String[] args)
	{
		IntBuffer b = new IntBuffer();
		Producer p = new Producer(b);
		Consumer c = new Consumer(b);
		p.start();
		c.start();
	}

}

// the code inside the methods needs to be changed so that the
// producer suspends itself when the buffer is full and waits for a slot to open up, while the consumer suspends
// itself if the buffer is empty and waits for a new value to arrive
class IntBuffer
{
	private int index;

	private int[] buffer = new int[8];

	public synchronized void add(int num)
	{
		while (index == buffer.length - 1)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
			}
		}
		buffer[index++] = num;
		notifyAll();
	}

	public synchronized int remove()
	{
		while (index == 0)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
			}
		}
		int ret = buffer[--index];
		notifyAll();
		return ret;
	}
}

class Producer extends Thread
{
	private IntBuffer buffer;

	public Producer(IntBuffer buffer)
	{
		this.buffer = buffer;
	}

	public void run()
	{
		Random r = new Random();
		while (true)
		{
			int num = r.nextInt();
			buffer.add(num);
			System.out.println("Produced " + num);
		}
	}
}

class Consumer extends Thread
{
	private IntBuffer buffer;

	public Consumer(IntBuffer buffer)
	{
		this.buffer = buffer;
	}

	public void run()
	{
		while (true)
		{
			int num = buffer.remove();
			System.out.println("Consumed " + num);
		}
	}
}
