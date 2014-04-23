package org.apache.uima.graph;

import org.apache.uima.graph.backends.TitanGraphFactory;
import org.apache.uima.graph.impl.MetaFactoryBase;

public final class GraphMetaFactory extends MetaFactoryBase<IGraphFactory> {
	public static final String		DEFAULT_FACTORY_NAME	= TitanGraphFactory.class.getName();

	private static GraphMetaFactory	instance				= null;

	public static GraphMetaFactory Instance() {
		if (instance == null)
			instance = new GraphMetaFactory();
		return instance;
	}

	private GraphMetaFactory() {
		super(IGraphFactory.class);
	}

	@Override
	protected String getDefaultFactoryName() {
		return DEFAULT_FACTORY_NAME;
	}
}
