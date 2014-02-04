package umbc.practice.designpattern.factory;

//Read more: http://javarevisited.blogspot.com/2011/12/factory-design-pattern-java-example.html

//Factroy Class code
class CurrencyFactory
{

	public static Currency createCurrency(String country)
	{
		if (country.equalsIgnoreCase("India"))
		{
			return new Rupee();
		}
		else if (country.equalsIgnoreCase("Singapore"))
		{
			return new SGDDollar();
		}
		else if (country.equalsIgnoreCase("US"))
		{
			return new USDollar();
		}
		throw new IllegalArgumentException("No such currency");
	}
}

// Factory client code
public class Factory
{
	public static void main(String args[])
	{
		String country = "India";
		Currency rupee = CurrencyFactory.createCurrency(country);
		System.out.println(rupee.getSymbol());
	}
}

interface Currency
{
	String getSymbol();
}

// Concrete Rupee Class code
class Rupee implements Currency
{
	@Override
	public String getSymbol()
	{
		return "Rs";
	}
}

// Concrete SGD class Code
class SGDDollar implements Currency
{
	@Override
	public String getSymbol()
	{
		return "SGD";
	}
}

// Concrete US Dollar code
class USDollar implements Currency
{
	@Override
	public String getSymbol()
	{
		return "USD";
	}
}
