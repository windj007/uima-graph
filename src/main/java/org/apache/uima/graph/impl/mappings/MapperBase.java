package org.apache.uima.graph.impl.mappings;

import org.apache.uima.graph.IMapper;
import org.apache.uima.graph.IMappingManager;

public abstract class MapperBase implements IMapper {

	private IMappingManager	provider	= null;

	public void setMappingProvider(IMappingManager provider) {
		this.provider = provider;
	}

	public IMappingManager getMappingManager() {
		return provider;
	}
}
