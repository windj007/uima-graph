package org.apache.uima.graph.impl.mappings;

import org.apache.uima.graph.impl.DefaultIndicesNames;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class DefaultObjectMapping extends MappingBase {
	public Vertex mapToGraph(Object obj, Graph graph) throws IllegalArgumentException, IllegalStateException {
		Vertex result = graph.addVertex(null);
		result.setProperty(DefaultIndicesNames.CLASS.name(), obj.getClass().getName());
		result.setProperty(DefaultIndicesNames.AS_STRING.name(), obj.toString());
		return result;
	}
}
