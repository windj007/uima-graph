package org.apache.uima.graph.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.WeakHashMap;

import org.apache.uima.graph.IMapper;
import org.apache.uima.graph.IMappingManager;
import org.apache.uima.graph.impl.mappings.NullMapper;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class DefaultMappingManager implements IMappingManager {

	private Map<Class<?>, IMapper>	mappings					= new HashMap<Class<?>, IMapper>();

	private Map<Object, Vertex>		objectsWithVerticesCreated	= new WeakHashMap<Object, Vertex>();

	private Queue<Object>			objectsToFill				= new LinkedList<Object>();

	private Graph					workingGraph				= null;

	public DefaultMappingManager(Graph graph) {
		this.workingGraph = graph;
		mappings.put(null, new NullMapper());
	}

	public Vertex enqueueObject(Object obj) {
		if (objectsWithVerticesCreated.containsKey(obj))
			return objectsWithVerticesCreated.get(obj);

		Vertex vertex = workingGraph.addVertex(null);
		objectsWithVerticesCreated.put(obj, vertex);
		objectsToFill.add(obj);
		return vertex;
	}

	public void processQueue() {
		while (!objectsToFill.isEmpty()) {
			Object obj = objectsToFill.remove();
			IMapper mapper = getMapping(obj);
			Vertex vertex = objectsWithVerticesCreated.get(obj);
			mapper.fillVertex(vertex, obj, workingGraph);
		}
	}

	public void registerMapping(Class<?> mappedClass, IMapper mapper) {
		if (mapper == null)
			throw new IllegalArgumentException("Mapping cannot be null");
		mappings.put(mappedClass, mapper);
	}

	public IMapper getMapping(Object obj) {
		if (obj != null) {
			Class<?> cls = obj.getClass();
			Class<?> key = findBestKey(cls);
			IMapper result = uncheckedGet(key);
			if (!key.equals(cls))
				mappings.put(cls, result);
			return result;
		} else {
			return mappings.get(null);
		}
	}

	public Graph getGraph() {
		return workingGraph;
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

	private IMapper uncheckedGet(Class<?> key) {
		IMapper result = mappings.get(key);
		result.setMappingProvider(this);
		return result;
	}
}
