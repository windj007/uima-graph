package org.apache.uima.graph.impl.mappings;

import org.apache.uima.graph.impl.DefaultIndicesNames;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class DefaultObjectMapper extends MapperBase {
	public void fillVertex(Vertex vertexForObj, Object obj, Graph graph) {
		vertexForObj.setProperty(
			DefaultIndicesNames.CLASS.name(),
			obj.getClass().getName());
		vertexForObj.setProperty(
			DefaultIndicesNames.AS_STRING.name(),
			obj.toString());
	}
}
