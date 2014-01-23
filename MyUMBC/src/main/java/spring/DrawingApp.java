package main.java.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

// http://javabrains.koushik.org/p/home.html
public class DrawingApp
{
	public static void main(String[] args)
	{
		// Triangle t = new Triangle();
		ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		Triangle_1 t = (Triangle_1) context.getBean("triangle_1");
		t.draw();

		Triangle t1 = (Triangle) context.getBean("triangle");
		t1.draw();
	}
}
