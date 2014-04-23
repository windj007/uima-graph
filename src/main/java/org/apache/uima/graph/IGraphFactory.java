package org.apache.uima.graph;

import com.tinkerpop.blueprints.Graph;

public interface IGraphFactory {
	Graph createGraph(String configString);

	void commit(Graph graph);
}
