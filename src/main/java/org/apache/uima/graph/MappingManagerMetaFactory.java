package org.apache.uima.graph;

import org.apache.uima.graph.impl.DefaultMappingManagerFactory;
import org.apache.uima.graph.impl.MetaFactoryBase;

public final class MappingManagerMetaFactory extends
	MetaFactoryBase<IMappingManagerFactory> {
	public static final String					DEFAULT_FACTORY_NAME	= DefaultMappingManagerFactory.class.getName();

	private static MappingManagerMetaFactory	instance				= null;

	public static synchronized MappingManagerMetaFactory Instance() {
		if (instance == null)
			instance = new MappingManagerMetaFactory();
		return instance;
	}

	private MappingManagerMetaFactory() {
		super(IMappingManagerFactory.class);
	}

	@Override
	protected String getDefaultFactoryName() {
		return DEFAULT_FACTORY_NAME;
	}
}
