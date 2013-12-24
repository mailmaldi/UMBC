package test1;

//Read more: http://javarevisited.blogspot.com/2011/11/decorator-design-pattern-java-example.html#ixzz2PFovNzvm

//Component on Decorator design pattern

abstract class Currency
{
	String description = "Unknown currency";

	public String getDescription()
	{
		return description;
	}

	public abstract double cost(double value);

}

// Concrete Component

class Rupee extends Currency
{
	double value;

	public Rupee()
	{
		description = "indian rupees";
	}

	public double cost(double v)
	{
		value = v;
		return value;
	}

}

// Another Concrete Component

class Dollar extends Currency
{
	double value;

	public Dollar()
	{
		description = "Dollar";
	}

	public double cost(double v)
	{
		value = v;
		return value;

	}

}

// Decorator

abstract class Decorator extends Currency
{

	public abstract String getDescription();

}

// Concrete Decorator

class USDDecorator extends Decorator
{

	Currency currency;

	public USDDecorator(Currency currency)
	{
		this.currency = currency;
	}

	public String getDescription()
	{
		return currency.getDescription() + " ,its US Dollar";
	}

	@Override
	public double cost(double value)
	{
		return currency.cost(value);
	}

}

// Another Concrete Decorator

class SGDDecorator extends Decorator
{
	Currency currency;

	public SGDDecorator(Currency currency)
	{
		this.currency = currency;
	}

	public String getDescription()
	{
		return currency.getDescription() + " ,its singapore Dollar";
	}

	@Override
	public double cost(double value)
	{
		return currency.cost(value);
	}

}

public class DecoratorTest
{

	public static void main(String[] args)
	{

		// without adding decorators

		Currency curr = new Dollar();

		System.out.println(curr.getDescription() + " dollar. " + curr.cost(2.0));

		// adding decorators

		Currency curr2 = new USDDecorator(new Dollar());

		System.out.println(curr2.getDescription() + " dollar. " + curr2.cost(4.0));

		Currency curr3 = new SGDDecorator(new Dollar());

		System.out.println(curr3.getDescription() + " dollar. " + curr3.cost(4.0));
	}
}
