package org.apache.uima.graph;

import com.tinkerpop.blueprints.Graph;

public interface IExistenceChecker {
	boolean exists(Object obj, Graph graph);
}
