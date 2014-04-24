package org.apache.uima.graph.impl;

import org.apache.uima.jcas.JCas;

public class WrappedJCas {

	private JCas doc = null;
	
	public WrappedJCas(JCas doc) {
		this.doc = doc;
	}
	
	public JCas getDoc() {
		return doc;
	}
}
