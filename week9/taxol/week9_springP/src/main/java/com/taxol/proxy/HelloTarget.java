package com.taxol.proxy;

/**
 * @author taxol1203@pusan.ac.kr
 */
public class HelloTarget implements Hello {
	@Override
	public String sayHello(String name) {
		return "Hello " + name;
	}

	@Override
	public String sayHi(String name) {
		return "Hi " + name;
	}

	@Override
	public String sayThankYou(String name) {
		return "Thank You " + name;
	}
}
