package umbc.practice.queue;

import java.util.LinkedList;

public class AnimalShelter
{
    LinkedList<Animal> animals;

    public AnimalShelter()
    {
        animals = new LinkedList<Animal>();

    }

    public void enqueue(Animal animal)
    {
        animals.addLast(animal);
    }

    public Animal dequeueAny()
    {
        return (animals.isEmpty() ? null : animals.getFirst());
    }

    public Dog dequeueDog()
    {
        Dog dog = null;
        for (Animal animal : animals)
        {
            if (animal instanceof Dog)
                dog = (Dog) animal;
        }
        return dog;
    }

    public Cat dequeueCat()
    {
        Cat cat = null;
        for (Animal animal : animals)
        {
            if (animal instanceof Cat)
                cat = (Cat) animal;
        }
        return cat;
    }

}

abstract class Animal
{

}

class Dog extends Animal
{

}

class Cat extends Animal
{

}