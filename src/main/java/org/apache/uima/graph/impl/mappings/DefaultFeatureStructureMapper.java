package org.apache.uima.graph.impl.mappings;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.MappingUtils;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class DefaultFeatureStructureMapper extends MapperBase {
	public void fillVertex(Vertex vertexForObj, Object obj, Graph graph) {
		FeatureStructure fs = MappingUtils.checkTypeAndCast(
			getClass(),
			FeatureStructure.class,
			obj);

		vertexForObj.setProperty(
			DefaultIndicesNames.CLASS.name(),
			obj.getClass().getName());

		for (Feature feat : fs.getType().getFeatures())
			MappingUtils.mapFeature(
				getMappingManager(),
				fs,
				feat,
				graph,
				vertexForObj);
	}
}
