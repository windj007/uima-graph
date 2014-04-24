package org.apache.uima.graph.impl.mappings;

import java.util.List;

import org.apache.uima.cas.CASException;
import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.MappingUtils;
import org.apache.uima.jcas.tcas.Annotation;
import org.uimafit.util.JCasUtil;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class SequenceMapper extends DefaultAnnotationMapper {

	private Class<? extends Annotation>	annotOfInterestCls	= null;
	private Class<? extends Annotation>	linkWithCls			= null;

	public SequenceMapper(Class<? extends Annotation> annotOfInterestCls,
		Class<? extends Annotation> linkWithCls) {
		this.annotOfInterestCls = annotOfInterestCls;
		this.linkWithCls = linkWithCls;
	}

	@Override
	public void fillVertex(Vertex vertexForObj, Object obj, Graph graph) {
		super.fillVertex(vertexForObj, obj, graph);

		if (!annotOfInterestCls.isInstance(obj))
			return;

		Annotation annot = MappingUtils.checkTypeAndCast(
			getClass(),
			Annotation.class,
			obj);
		try {
			linkFirst(graph, vertexForObj, JCasUtil.selectFollowing(
				annot.getView().getJCas(),
				linkWithCls,
				annot,
				1), DefaultIndicesNames.FOLLOWING.name());
			linkFirst(graph, vertexForObj, JCasUtil.selectPreceding(
				annot.getView().getJCas(),
				linkWithCls,
				annot,
				1), DefaultIndicesNames.PRECEDING.name());
		} catch (CASException e) {
			throw new IllegalArgumentException(
				"An error has occurred during getting preceding and following annotations",
				e);
		}
	}

	protected void linkFirst(Graph graph, Vertex vertexForObj,
		List<? extends Annotation> annots, String linkLabel) {
		if (annots.isEmpty())
			return;
		MappingUtils.addLink(
			getMappingManager(),
			graph,
			vertexForObj,
			annots.get(0),
			linkLabel);
	}
}
