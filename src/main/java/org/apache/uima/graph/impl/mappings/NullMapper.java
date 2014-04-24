package org.apache.uima.graph.impl.mappings;

import org.apache.uima.graph.impl.DefaultIndicesNames;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class NullMapper extends MapperBase {
	public static final String	NULL_CLASS_NAME	= "null";

	public void fillVertex(Vertex vertexForObj, Object obj, Graph graph) {
		if (obj != null)
			throw new IllegalArgumentException(
				String.format(
					"NullMapper accepts only null values, object of type %s was passed",
					obj.getClass().getName()));
		vertexForObj.setProperty(
			DefaultIndicesNames.CLASS.name(),
			NULL_CLASS_NAME);
	}
}
