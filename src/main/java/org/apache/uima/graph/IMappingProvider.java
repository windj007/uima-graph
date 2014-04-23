package org.apache.uima.graph;

public interface IMappingProvider {
	IMapping getMappingForClass(Class<?> cls);
}
