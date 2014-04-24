package org.apache.uima.graph.impl.mappings;

import org.apache.uima.cas.CASException;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.graph.impl.AnnotUtils;
import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.MappingUtils;
import org.apache.uima.jcas.tcas.Annotation;

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
			DefaultIndicesNames.COVERED_TEXT.name(),
			annot.getCoveredText());

		try {
			for (AnnotationFS intersecting : AnnotUtils.getIntersectingAnnotations(
				annot.getView().getJCas(),
				annot))
				MappingUtils.addLink(
					getMappingManager(),
					graph,
					vertexForObj,
					intersecting,
					DefaultIndicesNames.INTERSECTS_WITH.name());
		} catch (CASException e) {
			throw new IllegalArgumentException(
				"An error has occurred during getting intersecting annotations",
				e);
		}
	}

}
