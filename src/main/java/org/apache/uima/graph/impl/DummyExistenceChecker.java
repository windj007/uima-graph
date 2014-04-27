package org.apache.uima.graph.impl;

import org.apache.uima.graph.IExistenceChecker;

import com.tinkerpop.blueprints.Graph;

public class DummyExistenceChecker implements IExistenceChecker {
	public boolean exists(Object obj, Graph graph) {
		return false;
	}
}
