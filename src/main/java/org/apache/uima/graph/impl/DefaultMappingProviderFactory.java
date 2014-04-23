package org.apache.uima.graph.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.graph.IMapping;
import org.apache.uima.graph.IMappingProvider;
import org.apache.uima.graph.IMappingProviderFactory;
import org.apache.uima.graph.exceptions.CannotCreateMappingProviderException;
import org.apache.uima.graph.impl.mappings.DefaultAnnotationMapping;
import org.apache.uima.graph.impl.mappings.DefaultFeatureStructureMapping;
import org.apache.uima.graph.impl.mappings.DefaultJCasMapping;
import org.apache.uima.graph.impl.mappings.DefaultObjectMapping;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public class DefaultMappingProviderFactory implements IMappingProviderFactory {

	private Map<Class<?>, Class<? extends IMapping>>	knownMappings	= new HashMap<Class<?>, Class<? extends IMapping>>();

	{
		knownMappings.put(Object.class, DefaultObjectMapping.class);
		knownMappings.put(FeatureStructure.class, DefaultFeatureStructureMapping.class);
		knownMappings.put(Annotation.class, DefaultAnnotationMapping.class);
		knownMappings.put(JCas.class, DefaultJCasMapping.class);
	}
	
	public synchronized Map<Class<?>, Class<? extends IMapping>> getMappings() {
		return Collections.unmodifiableMap(knownMappings);
	}
	
	public synchronized void registerMapping(Class<?> mappedClass,
		Class<? extends IMapping> mapperClass) {
		if (mappedClass == null)
			throw new IllegalArgumentException("Mapped class cannod be null");
		if (mapperClass == null)
			throw new IllegalArgumentException("Mapping class cannod be null");
		knownMappings.put(mappedClass, mapperClass);
	}

	public synchronized IMappingProvider createMappingProvider()
		throws CannotCreateMappingProviderException {
		try {
			DefaultMappingProvider result = new DefaultMappingProvider();
			for (Entry<Class<?>, Class<? extends IMapping>> mapping : knownMappings.entrySet()) {
				IMapping mapper = mapping.getValue().newInstance();
				result.registerMapping(mapping.getKey(), mapper);
			}
			return result;
		} catch (ReflectiveOperationException e) {
			throw new CannotCreateMappingProviderException(e);
		}
	}
}
