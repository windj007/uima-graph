package org.apache.uima.graph;

import org.apache.uima.jcas.JCas;

public interface IJCasWrapper {
	Object wrap(JCas doc);
}
