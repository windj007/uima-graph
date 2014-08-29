package org.apache.uima.graph.impl.mappings;

import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.FeatureValueWrapper;
import org.apache.uima.graph.impl.MappingUtils;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class FeatureValueWrapperMapper extends MapperBase {

	public void fillVertex(Vertex vertexForObj, Object obj, Graph graph) {
		FeatureValueWrapper wrapper = MappingUtils.checkTypeAndCast(
			getClass(),
			FeatureValueWrapper.class,
			obj);

		vertexForObj.setProperty(
			DefaultIndicesNames.CLASS.name(),
			obj.getClass().getName());
		vertexForObj.setProperty(
			DefaultIndicesNames.NAME.name(),
			wrapper.getFullFeatureName());
		vertexForObj.setProperty(
			DefaultIndicesNames.VALUE.name(),
			wrapper.getValue());
	}

}
