package org.apache.uima.graph.impl.mappings;

import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.MappingUtils;
import org.apache.uima.jcas.tcas.Annotation;
import org.uimafit.util.JCasUtil;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class DefaultAnnotationMapper extends DefaultFeatureStructureMapper {
	@Override
	public void fillVertex(Vertex vertexForObj, Object obj, Graph graph) {
		Annotation annot = MappingUtils.checkTypeAndCast(
			getClass(),
			Annotation.class,
			obj);

		super.fillVertex(vertexForObj, obj, graph);

		vertexForObj.setProperty(
			DefaultIndicesNames.TEXT.name(),
			annot.getCoveredText());
		vertexForObj.setProperty(
			DefaultIndicesNames.IS_NULL.name(),
			annot.getBegin() == 0 && annot.getEnd() == 0);

		for (AnnotationFS covered : JCasUtil.selectCovered(
			Annotation.class,
			annot))
			MappingUtils.addLink(
				getMappingManager(),
				graph,
				vertexForObj,
				covered,
				DefaultIndicesNames.COVERED.name());
	}

}
