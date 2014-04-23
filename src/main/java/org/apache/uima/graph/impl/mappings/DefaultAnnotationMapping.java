package org.apache.uima.graph.impl.mappings;

import org.apache.uima.cas.CASException;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.graph.impl.AnnotUtils;
import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.GraphUtils;
import org.apache.uima.jcas.tcas.Annotation;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class DefaultAnnotationMapping extends DefaultFeatureStructureMapping {

	public DefaultAnnotationMapping() {
	}

	public Vertex mapToGraph(Object obj, Graph graph)
		throws IllegalArgumentException, IllegalStateException {
		if (!(obj instanceof Annotation))
			throw new IllegalArgumentException(
				String.format(
					"DefaultAnnotationMapping can only map objects of subtypes of '%s', but '%s' was passed",
					Annotation.class,
					obj.getClass()));

		Annotation annot = (Annotation) obj;

		Vertex result = GraphUtils.tryGetVertexByAddr(graph, annot.getAddress());
		if (result != null)
			return result;

		result = super.mapToGraph(obj, graph);

		result.setProperty(
			DefaultIndicesNames.COVERED_TEXT.name(),
			annot.getCoveredText());

		try {
			for (AnnotationFS intersecting : AnnotUtils.getIntersectingAnnotations(
				annot.getView().getJCas(),
				annot))
				GraphUtils.addLink(
					getMappingProvider(),
					graph,
					result,
					intersecting,
					DefaultIndicesNames.INTERSECTS_WITH.name());
		} catch (CASException e) {
			throw new IllegalArgumentException(
				String.format("An error has occurred during getting intersecting annotations"),
				e);
		}

		return result;
	}

}
