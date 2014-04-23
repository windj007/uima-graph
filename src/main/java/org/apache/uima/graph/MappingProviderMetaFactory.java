package org.apache.uima.graph;

import org.apache.uima.graph.impl.DefaultMappingProviderFactory;
import org.apache.uima.graph.impl.MetaFactoryBase;

public final class MappingProviderMetaFactory extends
	MetaFactoryBase<IMappingProviderFactory> {
	public static final String					DEFAULT_FACTORY_NAME	= DefaultMappingProviderFactory.class.getName();

	private static MappingProviderMetaFactory	instance				= null;

	public static synchronized MappingProviderMetaFactory Instance() {
		if (instance == null)
			instance = new MappingProviderMetaFactory();
		return instance;
	}

	private MappingProviderMetaFactory() {
		super(IMappingProviderFactory.class);
	}

	@Override
	protected String getDefaultFactoryName() {
		return DEFAULT_FACTORY_NAME;
	}
}
