package org.apache.uima.graph.impl;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.graph.IMapping;
import org.apache.uima.graph.IMappingProvider;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public final class GraphUtils {
	public static final int NULL_VERTEX_ADDR = -1;

	private GraphUtils() {
	}

	public static Vertex tryGetVertexByAddr(Graph graph, int addr)
		throws IllegalStateException {
		Iterable<Vertex> candidates = graph.getVertices(
			DefaultIndicesNames.ADDR.name(),
			addr);
		Vertex result = null;
		for (Vertex cand : candidates) {
			if (result == null)
				result = cand;
			else
				throw new IllegalStateException(String.format(
					"Multiple vertices have '%s' field with value '%i'",
					DefaultIndicesNames.ADDR,
					addr));
		}
		return result;
	}

	public static Edge mapFeature(IMappingProvider provider,
		FeatureStructure origObj, Feature feat, Graph graph, Vertex origVertex) {

		Object featValue = feat.getRange().isPrimitive() ? origObj.getFeatureValueAsString(feat)
			: origObj.getFeatureValue(feat);
		Edge edge = addLink(
			provider,
			graph,
			origVertex,
			featValue,
			DefaultIndicesNames.FEATURE.name());
		edge.setProperty(DefaultIndicesNames.NAME.name(), feat.getShortName());
		return edge;
	}

	public static Edge addLink(IMappingProvider provider, Graph graph,
		Vertex from, Object toObj, String label) {
		Vertex toVertex = null;
		if (toObj == null)
			toVertex = getNullVertex(graph);
		else {
			IMapping mapping = provider.getMappingForClass(toObj.getClass());
			toVertex = mapping.mapToGraph(toObj, graph);
		}
		Edge edge = from.addEdge(label, toVertex);
		return edge;
	}
	
	public static Vertex getNullVertex(Graph graph) {
		Vertex result = tryGetVertexByAddr(graph, NULL_VERTEX_ADDR);
		if (result != null)
			return result;
		result = graph.addVertex(null);
		result.setProperty(DefaultIndicesNames.ADDR.name(), NULL_VERTEX_ADDR);
		return result;
	}
	
}
