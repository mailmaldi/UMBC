package main.java.cz.cvut.fel.minimalSpanningTree.core;

import java.util.Random;

public class test
{

	public static void main(String[] args)
	{
		Random random = new Random();
		int rand = getRandom(random, 4, 5);
		System.out.println(rand);
	}

	public static int getRandom(Random random, int i, int range)
	{
		int rand = random.nextInt(range);
		// if rand is i or i+1 , then generate rand again
		while ((rand == i) || (rand >= range) || (rand == ((i + 1) % range)))
		{
			rand = random.nextInt(range);
		}
		return rand;
	}

}
