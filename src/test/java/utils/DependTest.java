package utils;

import org.testng.annotations.Test;

public class DependTest {
	
	@Test
	public void a() {
		System.out.println("test1 run");
	}
	
	@Test(dependsOnMethods = {"test2","test3"})
	public void test1() {
		System.out.println("test1 run");
	}

	@Test
	public void test2() {
		System.out.println("test2 run");
	}
	
	@Test
	public void test3() {
		System.out.println("test3 run");
	}

}
