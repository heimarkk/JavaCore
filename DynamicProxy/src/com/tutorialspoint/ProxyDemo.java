package com.tutorialspoint;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyDemo {
   public static void main(String[] args) throws IllegalArgumentException {
      
	   Foo foo = (Foo) DebugProxy.newInstance(new FooImpl());
	   foo.bar("Parameter");
      
   }
}


class DebugProxy implements java.lang.reflect.InvocationHandler {

	private Object obj;

	public static Object newInstance(Object obj) {
		return java.lang.reflect.Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
				new DebugProxy(obj));
	}

	private DebugProxy(Object obj) {
		this.obj = obj;
	}

	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
		Object result;
		try {
			System.out.println("before method " + m.getName());
			result = m.invoke(obj, args);
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		} catch (Exception e) {
			throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
		} finally {
			System.out.println("after method " + m.getName());
		}
		return result;
	}
}

interface Foo {
	Object bar(Object obj);
}

class FooImpl implements Foo {
	public Object bar(Object obj) {
		System.out.println("Calling bar");
		System.out.println(obj.toString());
		return null;
	}
}
