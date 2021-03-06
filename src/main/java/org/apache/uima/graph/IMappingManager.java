package org.apache.uima.graph;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public interface IMappingManager {
	Vertex enqueueObject(Object obj);

	void processQueue();
	
	Graph getGraph();
}
