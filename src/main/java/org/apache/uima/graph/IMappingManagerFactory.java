package org.apache.uima.graph;

import org.apache.uima.graph.exceptions.CannotCreateMappingProviderException;

import com.tinkerpop.blueprints.Graph;

public interface IMappingManagerFactory {
	IMappingManager createMappingProvider(Graph graph)
		throws CannotCreateMappingProviderException;
}
