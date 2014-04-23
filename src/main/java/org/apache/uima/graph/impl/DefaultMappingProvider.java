package org.apache.uima.graph.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.graph.IMapping;
import org.apache.uima.graph.IMappingProvider;

public class DefaultMappingProvider implements IMappingProvider {

	public Map<Class<?>, IMapping> mappings = new HashMap<Class<?>, IMapping>();
	
	public void registerMapping(Class<?> mappedClass, IMapping mapper) {
		if (mappedClass == null)
			throw new IllegalArgumentException("Mapped class cannod be null");
		if (mapper == null)
			throw new IllegalArgumentException("Mapping cannod be null");
		mappings.put(mappedClass, mapper);
	}
	
	public IMapping getMappingForClass(Class<?> cls) {
		Class<?> curCls = cls;
		
		// serially check all implemented interfaces and superclasses (interfaces are priority)
		while (!mappings.containsKey(curCls) && !curCls.equals(Object.class)) {
			for (Class<?> iface : curCls.getInterfaces())
				if (mappings.containsKey(iface))
					return uncheckedGet(iface);
			curCls = curCls.getSuperclass();
		}
		
		return uncheckedGet(curCls);
	}
	
	private IMapping uncheckedGet(Class<?> key) {
		IMapping result = mappings.get(key);
		result.setMappingProvider(this);
		return result;
	}
}
