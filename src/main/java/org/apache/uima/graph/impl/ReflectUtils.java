package org.apache.uima.graph.impl;

public final class ReflectUtils {
	private ReflectUtils() {
	}

	public static <T> T tryLoad(String clsToLoad, Class<T> superCls)
		throws ReflectiveOperationException {
		Class<?> aCls = Class.forName(clsToLoad);
		Class<? extends T> wrapperCls = aCls.asSubclass(superCls);
		return wrapperCls.newInstance();
	}
}
