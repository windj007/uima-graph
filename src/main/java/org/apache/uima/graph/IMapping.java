package org.apache.uima.graph;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public interface IMapping {
	void setMappingProvider(IMappingProvider provider);

	IMappingProvider getMappingProvider();

	Vertex mapToGraph(Object obj, Graph graph) throws IllegalArgumentException, IllegalStateException;
}
