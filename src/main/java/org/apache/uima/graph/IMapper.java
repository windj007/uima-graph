package org.apache.uima.graph;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public interface IMapper {
	void setMappingProvider(IMappingManager provider);

	IMappingManager getMappingManager();

	void fillVertex(Vertex vertexForObj, Object obj, Graph graph);
}
