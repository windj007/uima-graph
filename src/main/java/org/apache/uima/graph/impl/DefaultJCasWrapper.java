package org.apache.uima.graph.impl;

import org.apache.uima.graph.IJCasWrapper;
import org.apache.uima.jcas.JCas;

public class DefaultJCasWrapper implements IJCasWrapper {
	public Object wrap(JCas doc) {
		return new WrappedJCas(doc);
	}
}
