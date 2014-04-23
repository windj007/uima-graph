package org.apache.uima.graph.impl.mappings;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class DefaultObjectMapping extends MappingBase {

	public static final String VALUE_PROPERTY_NAME = "value";
	
	public Vertex mapToGraph(Object obj, Graph graph) throws IllegalArgumentException, IllegalStateException {
		Vertex result = graph.addVertex(null);
		result.setProperty(VALUE_PROPERTY_NAME, obj.toString());
		return result;
	}

}
