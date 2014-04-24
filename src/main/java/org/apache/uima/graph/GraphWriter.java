package org.apache.uima.graph;

import org.apache.uima.UIMAException;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.graph.exceptions.CannotCreateMappingProviderException;
import org.apache.uima.graph.exceptions.UIMAGraphExceptionBase;
import org.apache.uima.graph.impl.DefaultJCasWrapper;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasCopier;
import org.apache.uima.util.Level;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.factory.ConfigurationParameterFactory;
import org.uimafit.factory.JCasFactory;

import com.tinkerpop.blueprints.Graph;

public class GraphWriter extends JCasAnnotator_ImplBase {
	public static final String		PARAM_GRAPH_FACTORY_NAME		= ConfigurationParameterFactory.createConfigurationParameterName(
																		GraphWriter.class,
																		"graphFactoryName");

	public static final String		PARAM_GRAPH_CONFIG_STRING		= ConfigurationParameterFactory.createConfigurationParameterName(
																		GraphWriter.class,
																		"graphConfigString");

	public static final String		PARAM_MAPPING_FACTORY_NAME		= ConfigurationParameterFactory.createConfigurationParameterName(
																		GraphWriter.class,
																		"mappingFactoryName");

	public static final String		PARAM_JCAS_WRAPPER_CLASS_NAME	= ConfigurationParameterFactory.createConfigurationParameterName(
																		GraphWriter.class,
																		"jcasWrapperClassName");

	@ConfigurationParameter(description = "Name of factory to get from GraphMetaFactory to open connection to graph database")
	private String					graphFactoryName				= GraphMetaFactory.DEFAULT_FACTORY_NAME;

	@ConfigurationParameter(mandatory = true, description = "String to pass to the GraphFactory when opening connection")
	private String					graphConfigString				= null;

	@ConfigurationParameter(description = "Name of factory to get mapping")
	private String					mappingFactoryName				= MappingManagerMetaFactory.DEFAULT_FACTORY_NAME;

	@ConfigurationParameter(description = "Full name of IJCasWrapper implementor to instantiate to get serializable objects from JCas")
	private String					jcasWrapperClassName			= DefaultJCasWrapper.class.getName();

	private IGraphFactory			graphFactory					= null;
	private Graph					workingGraph					= null;
	private IMappingManagerFactory	mapperFactory					= null;
	private IMappingManager mapper = null;
	private IJCasWrapper			jcasWrapper						= null;

	@Override
	public void initialize(UimaContext context)
		throws ResourceInitializationException {
		super.initialize(context);

		try {
			Class<?> aCls = Class.forName(jcasWrapperClassName);
			Class<? extends IJCasWrapper> wrapperCls = aCls.asSubclass(IJCasWrapper.class);
			jcasWrapper = wrapperCls.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new ResourceInitializationException(e);
		}

		try {
			graphFactory = GraphMetaFactory.Instance().getFactory(
				graphFactoryName);
			workingGraph = graphFactory.createGraph(graphConfigString);
			mapperFactory = MappingManagerMetaFactory.Instance().getFactory(
				mappingFactoryName);
			mapper = mapperFactory.createMappingProvider(workingGraph);
		} catch (UIMAGraphExceptionBase e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas doc) throws AnalysisEngineProcessException {
		try {
			JCas copy = JCasFactory.createJCas();
			CasCopier.copyCas(doc.getCas(), copy.getCas(), true);
			
			mapper.enqueueObject(jcasWrapper.wrap(copy));
			mapper.processQueue();
			graphFactory.commit(workingGraph);
			getLogger().log(Level.INFO, "Doc serialized");
		} catch (UIMAException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
}
