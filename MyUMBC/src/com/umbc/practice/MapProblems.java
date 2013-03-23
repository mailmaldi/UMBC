package com.umbc.practice;

import java.util.HashSet;
import java.util.LinkedList;

public class MapProblems
{

	public static void main(String[] args)
	{
		MyHashMap<Integer, Integer> myMap = new MyHashMap<Integer, Integer>(10);
		myMap.put(10, 100);
		System.out.println(myMap.get(10));
		myMap.put(10, 200);
		System.out.println(myMap.get(10));

		System.out.println();
		System.out.println();

		MyHashMapOnSet<Integer, Integer> newMap = new MyHashMapOnSet<Integer, Integer>();
		System.out.println(newMap.get(100));
		newMap.put(100, 200);
		System.out.println(newMap.get(100));
	}

}

class MyHashMap<K, V>
{
	private int N = 100; // N is number of buckets

	private LinkedList<MyHashObject<K, V>>[] myArray = null;

	public MyHashMap(int numBuckets)
	{
		this.N = numBuckets;
		myArray = (LinkedList<MyHashObject<K, V>>[]) new LinkedList[N];
	}

	public synchronized void put(K key, V value)
	{
		if (value == null || key == null)
			return;
		int bucketId = key.hashCode() % N;
		if (myArray[bucketId] == null)
			myArray[bucketId] = new LinkedList<MyHashObject<K, V>>();
		for (MyHashObject<K, V> obj : myArray[bucketId])
		{
			if (obj.key.equals(key))
			{
				// clash , replace value in obj
				obj.value = value;
				return;
			}
		}
		// didnt find key in list
		MyHashObject<K, V> newObj = new MyHashObject<K, V>(key, value);
		myArray[bucketId].add(newObj);
	}

	public synchronized V get(K key)
	{
		if (key == null)
			return null;
		int bucketId = key.hashCode() % N;
		if (myArray[bucketId] == null)
			return null;
		for (MyHashObject<K, V> obj : myArray[bucketId])
		{
			if (obj.key.equals(key))
			{
				return obj.value;
			}
		}
		return null;

	}
	// lst[0] = new LinkedList<String>();
	// lst[1] = new LinkedList<String>();
}

class MyHashObject<K, V>
{
	K key;

	V value;

	public MyHashObject(K k, V v)
	{
		key = k;
		value = v;
	}

	public String toString()
	{
		return key + ":" + value;
	}
}

class MyHashMapOnSetObject<K, V> extends MyHashObject<K, V>
{

	public MyHashMapOnSetObject(K k, V v)
	{
		super(k, v);
	}

	@Override
	public int hashCode()
	{
		return key.hashCode();
	}

	@Override
	public boolean equals(Object anObject)
	{
		if (this == anObject)
		{
			return true;
		}
		if (anObject instanceof MyHashMapOnSetObject<?, ?>)
		{
			return key.equals(((MyHashMapOnSetObject<K, V>) anObject).key);
		}
		return false;
	}

}

// Uses HashSet
class MyHashMapOnSet<K, V>
{
	private HashSet<MyHashMapOnSetObject<K, V>> set = new HashSet<MyHashMapOnSetObject<K, V>>();

	public MyHashMapOnSet()
	{
	}

	public synchronized void put(K k, V v)
	{
		MyHashMapOnSetObject<K, V> obj = new MyHashMapOnSetObject<K, V>(k, v);
		set.add(obj);

	}

	public synchronized boolean get(K k)
	{
		MyHashMapOnSetObject<K, V> obj = new MyHashMapOnSetObject<K, V>(k, null);
		return set.contains(obj);
	}
}