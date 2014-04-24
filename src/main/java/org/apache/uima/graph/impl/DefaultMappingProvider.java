package org.apache.uima.graph.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.graph.IMapping;
import org.apache.uima.graph.IMappingProvider;

public class DefaultMappingProvider implements IMappingProvider {

	public Map<Class<?>, IMapping>	mappings	= new HashMap<Class<?>, IMapping>();

	public void registerMapping(Class<?> mappedClass, IMapping mapper) {
		if (mappedClass == null)
			throw new IllegalArgumentException("Mapped class cannod be null");
		if (mapper == null)
			throw new IllegalArgumentException("Mapping cannod be null");
		mappings.put(mappedClass, mapper);
	}

	public IMapping getMappingForClass(Class<?> cls) {
		Class<?> key = findBestKey(cls);
		IMapping result = uncheckedGet(key);
		if (!key.equals(cls))
			mappings.put(cls, result);
		return result;
	}

	private Class<?> findBestKey(Class<?> cls) {
		// serially check all implemented interfaces and superclasses
		// (interfaces are priority)
		while (!mappings.containsKey(cls) && !cls.equals(Object.class)) {
			for (Class<?> iface : cls.getInterfaces())
				if (mappings.containsKey(iface))
					return iface;
			cls = cls.getSuperclass();
		}
		return cls;
	}

	private IMapping uncheckedGet(Class<?> key) {
		IMapping result = mappings.get(key);
		result.setMappingProvider(this);
		return result;
	}
}
