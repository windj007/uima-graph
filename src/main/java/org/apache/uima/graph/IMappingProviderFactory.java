package org.apache.uima.graph;

import org.apache.uima.graph.exceptions.CannotCreateMappingProviderException;

public interface IMappingProviderFactory {
	IMappingProvider createMappingProvider()
		throws CannotCreateMappingProviderException;
}
