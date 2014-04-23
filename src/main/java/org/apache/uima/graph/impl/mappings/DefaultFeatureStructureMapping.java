package org.apache.uima.graph.impl.mappings;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.GraphUtils;
import org.apache.uima.jcas.cas.TOP;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class DefaultFeatureStructureMapping extends MappingBase {
	public Vertex mapToGraph(Object obj, Graph graph)
		throws IllegalArgumentException, IllegalStateException {
		if (!(obj instanceof FeatureStructure))
			throw new IllegalArgumentException(
				String.format(
					"DefaultFSMapping can only map objects of subtypes of FeatureStructure, but '%s' was passed",
					obj.getClass()));

		FeatureStructure fs = (FeatureStructure) obj;
		int address = (fs instanceof TOP) ? (((TOP) fs).getAddress())
			: fs.hashCode();

		Vertex result = GraphUtils.tryGetVertexByAddr(graph, address);
		if (result != null)
			return result;

		result = graph.addVertex(null);
		result.setProperty(DefaultIndicesNames.CLASS.name(), obj.getClass().getName());
		result.setProperty(DefaultIndicesNames.ADDR.name(), address);
		for (Feature feat : fs.getType().getFeatures())
			GraphUtils.mapFeature(getMappingProvider(), fs, feat, graph, result);

		return result;
	}
}
