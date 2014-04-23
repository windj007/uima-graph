package org.apache.uima.graph.impl;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.graph.IMapping;
import org.apache.uima.graph.IMappingProvider;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public final class GraphUtils {

	private GraphUtils() {
	}

	public static Vertex tryGetVertexByAddr(Graph graph, int addr)
		throws IllegalStateException {
		Iterable<Vertex> candidates = graph.getVertices(
			DefaultIndicesNames.ADDR,
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

	public static Vertex mapFeature(IMappingProvider provider,
		FeatureStructure origObj, Feature feat, Graph graph, Vertex origVertex) {
		Object featValue = origObj.getFeatureValue(feat);
		IMapping featMapping = provider.getMappingForClass(
			featValue.getClass());
		Vertex featVertex = featMapping.mapToGraph(featValue, graph);
		origVertex.addEdge(DefaultIndicesNames.FEATURE_LABEL, featVertex);
		return featVertex;
	}

}
