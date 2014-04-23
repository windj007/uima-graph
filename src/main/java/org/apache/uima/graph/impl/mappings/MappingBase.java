package org.apache.uima.graph.impl.mappings;

import org.apache.uima.graph.IMapping;
import org.apache.uima.graph.IMappingProvider;

public abstract class MappingBase implements IMapping {

	private IMappingProvider	provider	= null;

	public void setMappingProvider(IMappingProvider provider) {
		this.provider = provider;
	}

	public IMappingProvider getMappingProvider() {
		return provider;
	}
}
