package org.apache.uima.graph.impl;

import org.apache.uima.graph.IJCasWrapper;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.DocumentAnnotation;
import org.uimafit.util.JCasUtil;

public class DocumentAnnotationWrapper implements IJCasWrapper {

	public Object wrap(JCas doc) {
		return JCasUtil.selectSingle(doc, DocumentAnnotation.class);
	}

}
