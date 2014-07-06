package org.apache.uima.graph.impl.mappings;

import java.util.Collection;

import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.MappingUtils;
import org.apache.uima.jcas.tcas.Annotation;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class DefaultAnnotationMapper extends DefaultFeatureStructureMapper {

	private boolean	mapCovered;

	public DefaultAnnotationMapper() {
		super();
		mapCovered = true;
	}

	public DefaultAnnotationMapper(Collection<String> typesNotToMap,
		Collection<String> featuresNotToMap, boolean mapCovered) {
		super(typesNotToMap, featuresNotToMap);
		this.mapCovered = mapCovered;
	}

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

		if (mapCovered) {
			for (AnnotationFS covered : JCasUtil.selectCovered(
				Annotation.class,
				annot)) {
				if (typesNotToMap.contains(covered.getType().getName()))
					continue;
				MappingUtils.addLink(
					getMappingManager(),
					graph,
					vertexForObj,
					covered,
					DefaultIndicesNames.COVERS.name());
			}
		}
	}

}
