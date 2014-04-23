package org.apache.uima.graph;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.graph.exceptions.UIMAGraphExceptionBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.factory.ConfigurationParameterFactory;

import com.tinkerpop.blueprints.Graph;

public class GraphWriter extends JCasAnnotator_ImplBase {
	public static final String	PARAM_GRAPH_FACTORY_NAME	= ConfigurationParameterFactory.createConfigurationParameterName(
																GraphWriter.class,
																"graphFactoryName");

	public static final String	PARAM_GRAPH_CONFIG_STRING	= ConfigurationParameterFactory.createConfigurationParameterName(
																GraphWriter.class,
																"graphConfigString");

	public static final String	PARAM_MAPPING_FACTORY_NAME	= ConfigurationParameterFactory.createConfigurationParameterName(
																GraphWriter.class,
																"mappingFactoryName");

	@ConfigurationParameter(description = "Name of factory to get from GraphMetaFactory to open connection to graph database")
	private String				graphFactoryName			= GraphMetaFactory.DEFAULT_FACTORY_NAME;

	@ConfigurationParameter(mandatory = true, description = "String to pass to the GraphFactory when opening connection")
	private String				graphConfigString			= null;

	@ConfigurationParameter(description = "Name of factory to get mapping")
	private String				mappingFactoryName			= MappingProviderMetaFactory.DEFAULT_FACTORY_NAME;

	private IGraphFactory		graphFactory				= null;
	private Graph				workingGraph				= null;
	private IMappingProvider	mapper						= null;

	@Override
	public void initialize(UimaContext context)
		throws ResourceInitializationException {
		super.initialize(context);

		try {
			graphFactory = GraphMetaFactory.Instance().getFactory(
				graphFactoryName);
			workingGraph = graphFactory.createGraph(graphConfigString);
			mapper = MappingProviderMetaFactory.Instance().getFactory(
				mappingFactoryName).createMappingProvider();
		} catch (UIMAGraphExceptionBase e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas doc) throws AnalysisEngineProcessException {
		IMapping mapping = mapper.getMappingForClass(doc.getClass());
		mapping.mapToGraph(doc, workingGraph);
		graphFactory.commit(workingGraph);
	}
}
