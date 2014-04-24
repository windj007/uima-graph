package org.apache.uima.graph.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.uimafit.util.CasUtil;

public final class AnnotUtils {
	private AnnotUtils() {
	}

	public static List<AnnotationFS> getIntersectingAnnotations(JCas view,
		Annotation focusAnnotation) {
		List<AnnotationFS> result = new ArrayList<AnnotationFS>();

		Type interType = CasUtil.getType(view.getCas(), Annotation.class);

		FSIterator<AnnotationFS> itr = view.getCas().getAnnotationIndex(
			interType).iterator();
		itr.moveTo(focusAnnotation);

		while (itr.isValid() && itr.get().getEnd() > focusAnnotation.getBegin()) {
			itr.moveToPrevious();
		}

		if (!itr.isValid())
			itr.moveToFirst();

		while (itr.isValid()
			&& (itr.get().getBegin() < focusAnnotation.getEnd())) {
			if (!itr.get().equals(focusAnnotation)
				&& itr.get().getEnd() > focusAnnotation.getBegin())
				result.add(itr.get());
			itr.moveToNext();
		}

		return result;
	}
}
