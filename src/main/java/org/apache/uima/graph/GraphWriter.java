package org.apache.uima.graph;

import org.apache.uima.UIMAException;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.graph.exceptions.UIMAGraphExceptionBase;
import org.apache.uima.graph.impl.DefaultJCasWrapper;
import org.apache.uima.graph.impl.DummyExistenceChecker;
import org.apache.uima.graph.impl.ReflectUtils;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCopier;
import org.apache.uima.util.Level;
import org.apache.uima.util.TypeSystemUtil;

import com.github.windj.utils.factories.exceptions.CannotCreateFactory;
import com.tinkerpop.blueprints.Graph;

public class GraphWriter extends JCasAnnotator_ImplBase {
	public static final String		PARAM_GRAPH_FACTORY_NAME			= "graphFactoryName";

	public static final String		PARAM_GRAPH_CONFIG_STRING			= "graphConfigString";

	public static final String		PARAM_MAPPING_FACTORY_NAME			= "mappingFactoryName";

	public static final String		PARAM_JCAS_WRAPPER_CLASS_NAME		= "jcasWrapperClassName";

	public static final String		PARAM_EXISTENCE_CHECKER_CLASS_NAME	= "existenceCheckerClassName";

	@ConfigurationParameter(mandatory = false, description = "Name of factory to get from GraphMetaFactory to open connection to graph database")
	private String					graphFactoryName					= GraphMetaFactory.DEFAULT_FACTORY_NAME;

	@ConfigurationParameter(mandatory = true, description = "String to pass to the GraphFactory when opening connection")
	private String					graphConfigString					= null;

	@ConfigurationParameter(mandatory = false, description = "Name of factory to get mapping")
	private String					mappingFactoryName					= MappingManagerMetaFactory.DEFAULT_FACTORY_NAME;

	@ConfigurationParameter(mandatory = false, description = "Full name of IJCasWrapper implementor to instantiate to get serializable objects from JCas")
	private String					jcasWrapperClassName				= DefaultJCasWrapper.class.getName();

	@ConfigurationParameter(mandatory = false, description = "Full name of IExistenceChecker implementor to instantiate to check if a document needs to be written")
	private String					existenceCheckerClassName			= DummyExistenceChecker.class.getName();

	private IGraphFactory			graphFactory						= null;
	private Graph					workingGraph						= null;
	private IMappingManagerFactory	mapperFactory						= null;
	private IMappingManager			mapper								= null;
	private IJCasWrapper			jcasWrapper							= null;
	private IExistenceChecker		existenceChecker					= null;

	@Override
	public void initialize(UimaContext context)
		throws ResourceInitializationException {
		super.initialize(context);

		try {
			jcasWrapper = ReflectUtils.tryLoad(
				jcasWrapperClassName,
				IJCasWrapper.class);
			existenceChecker = ReflectUtils.tryLoad(
				existenceCheckerClassName,
				IExistenceChecker.class);
		} catch (ReflectiveOperationException e) {
			throw new ResourceInitializationException(e);
		}

		try {
			graphFactory = GraphMetaFactory.Instance().getFactory(
				graphFactoryName);
			setWorkingGraph(graphFactory.createGraph(graphConfigString));
			mapperFactory = MappingManagerMetaFactory.Instance().getFactory(
				mappingFactoryName);
			mapper = mapperFactory.createMappingProvider(getWorkingGraph());
		} catch (UIMAGraphExceptionBase e) {
			throw new ResourceInitializationException(e);
		} catch (CannotCreateFactory e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas doc) throws AnalysisEngineProcessException {
		try {
			if (!existenceChecker.exists(doc, getWorkingGraph())) {
				getLogger().log(Level.INFO, "Serializing a doc...");
				
				TypeSystemDescription tsDesc = TypeSystemUtil.typeSystem2TypeSystemDescription(doc.getTypeSystem());
				JCas copy = JCasFactory.createJCas(tsDesc);
				CasCopier.copyCas(doc.getCas(), copy.getCas(), true);

				mapper.enqueueObject(jcasWrapper.wrap(copy));
				mapper.processQueue();
				graphFactory.commit(getWorkingGraph());
				
				getLogger().log(Level.INFO, "Doc serialized");
			}
			// TODO: add logging for else
		} catch (UIMAException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	protected Graph getWorkingGraph() {
		return workingGraph;
	}

	protected void setWorkingGraph(Graph workingGraph) {
		this.workingGraph = workingGraph;
	}
}
