package org.apache.uima.graph.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.graph.exceptions.CannotCreateFactory;

public abstract class MetaFactoryBase<T> {

	private Map<String, T>	knownFactories	= new HashMap<String, T>();
	private Class<T>		baseCls			= null;

	public synchronized T getFactory() throws CannotCreateFactory {
		return getFactory(getDefaultFactoryName());
	}

	public synchronized T getFactory(String factoryName)
		throws CannotCreateFactory {
		if (!knownFactories.containsKey(factoryName)
			&& !tryLoadFactory(factoryName))
			throw new CannotCreateFactory(
				"Cannot create factory with name",
				new Object[] { factoryName });
		return knownFactories.get(factoryName);
	}

	public synchronized void registerFactory(String name, T factory)
		throws IllegalArgumentException {
		if (name == null)
			throw new IllegalArgumentException(
				"Cannot register factory with null name");
		if (knownFactories.containsKey(name))
			throw new IllegalArgumentException(String.format(
				"Factory with name '%s' already registered",
				name));
		if (factory == null)
			throw new IllegalArgumentException(String.format(
				"Tried to register null factory with name '%s'",
				name));
		knownFactories.put(name, factory);
	}

	protected MetaFactoryBase(Class<T> baseCls) {
		this.baseCls = baseCls;
	}

	protected abstract String getDefaultFactoryName();

	protected boolean tryLoadFactory(String name) throws CannotCreateFactory {
		try {
			Class<?> aCls = Class.forName(name);
			Class<? extends T> factCls = aCls.asSubclass(baseCls);
			T factory = factCls.newInstance();
			knownFactories.put(name, factory);
			return true;
		} catch (ClassNotFoundException e) {
			throw new CannotCreateFactory(e);
		} catch (ReflectiveOperationException e) {
			throw new CannotCreateFactory(e);
		}
	}
}
