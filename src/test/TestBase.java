package test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

import ninjabrainbot.util.Logger;

public abstract class TestBase {

	public static void main(String[] args) {
		try {
			Logger.enabled = false;
			for (Class<?> c : findAllTests()) {
				if (c == TestBase.class)
					continue;
				Method m[] = c.getDeclaredMethods();
				for (Method method : m) {
					Object o = c.getConstructors()[0].newInstance(new Object[0]);
					if (method.canAccess(o))
					method.invoke(o, new Object[0]);
					System.out.println("Test passed: " + c.getName() + "." + method.getName());
				}
			}
			System.out.println("All tests passed.");
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | SecurityException e) {
			e.printStackTrace();
		}
	}

	private static Set<Class<?>> findAllTests() {
		String packageName = "test";
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		return reader.lines().filter(line -> line.endsWith(".class")).map(line -> getClass(line, packageName)).collect(Collectors.toSet());
	}

	private static Class<?> getClass(String className, String packageName) {
		try {
			return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

}
