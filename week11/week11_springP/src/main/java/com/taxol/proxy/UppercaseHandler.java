package com.taxol.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author taxol1203@gmail.com
 */
public class UppercaseHandler implements InvocationHandler {
	private Hello target;
	
	public UppercaseHandler(Hello target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object ret = method.invoke(target, args);
		
		if (ret instanceof String && method.getName().startsWith("say")) {
			return ((String) ret).toUpperCase();
		}
		return ret;
	}
}
