package org.apache.uima.graph.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.graph.IMapper;
import org.apache.uima.graph.IMappingManager;
import org.apache.uima.graph.IMappingManagerFactory;
import org.apache.uima.graph.exceptions.CannotCreateMappingProviderException;
import org.apache.uima.graph.impl.mappings.DefaultAnnotationMapper;
import org.apache.uima.graph.impl.mappings.DefaultFSArrayMapper;
import org.apache.uima.graph.impl.mappings.DefaultFeatureStructureMapper;
import org.apache.uima.graph.impl.mappings.DefaultObjectMapper;
import org.apache.uima.graph.impl.mappings.NullMapper;
import org.apache.uima.graph.impl.mappings.WrappedJCasMapper;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import com.tinkerpop.blueprints.Graph;

public class DefaultMappingManagerFactory implements IMappingManagerFactory {

	private Map<Class<?>, Class<? extends IMapper>>	knownMappings	= new HashMap<Class<?>, Class<? extends IMapper>>();

	{
		knownMappings.put(Object.class, DefaultObjectMapper.class);
		knownMappings.put(
			FeatureStructure.class,
			DefaultFeatureStructureMapper.class);
		knownMappings.put(Annotation.class, DefaultAnnotationMapper.class);
		knownMappings.put(WrappedJCas.class, WrappedJCasMapper.class);
		knownMappings.put(FSArray.class, DefaultFSArrayMapper.class);
		knownMappings.put(null, NullMapper.class);
	}

	public synchronized Map<Class<?>, Class<? extends IMapper>> getMappings() {
		return Collections.unmodifiableMap(knownMappings);
	}

	public synchronized void registerMapping(Class<?> mappedClass,
		Class<? extends IMapper> mapperClass) {
		if (mappedClass == null)
			throw new IllegalArgumentException("Mapped class cannod be null");
		if (mapperClass == null)
			throw new IllegalArgumentException("Mapping class cannod be null");
		knownMappings.put(mappedClass, mapperClass);
	}

	public synchronized IMappingManager createMappingProvider(Graph graph)
		throws CannotCreateMappingProviderException {
		try {
			DefaultMappingManager result = new DefaultMappingManager(graph);
			for (Entry<Class<?>, Class<? extends IMapper>> mapping : knownMappings.entrySet()) {
				IMapper mapper = mapping.getValue().newInstance();
				result.registerMapping(mapping.getKey(), mapper);
			}
			return result;
		} catch (ReflectiveOperationException e) {
			throw new CannotCreateMappingProviderException(e);
		}
	}
}
