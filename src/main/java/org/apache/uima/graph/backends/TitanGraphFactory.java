package org.apache.uima.graph.backends;

import java.io.File;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.uima.graph.IGraphFactory;
import org.apache.uima.graph.impl.DefaultIndicesNames;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class TitanGraphFactory implements IGraphFactory {

	public static final String	ES_INDEX_NAME	= "es";

	public TitanGraphFactory() {
	}

	public Graph createGraph(String configString) {
//		BaseConfiguration config = new BaseConfiguration();
//		Configuration storage = config.subset(GraphDatabaseConfiguration.STORAGE_NAMESPACE);
//		// configuring local backend
//		storage.setProperty(
//			GraphDatabaseConfiguration.STORAGE_BACKEND_KEY,
//			"local");
//		storage.setProperty(
//			GraphDatabaseConfiguration.STORAGE_DIRECTORY_KEY,
//			configString);
//		// configuring elastic search index
//		Configuration index = storage.subset(
//			GraphDatabaseConfiguration.INDEX_NAMESPACE).subset(ES_INDEX_NAME);
//		index.setProperty(
//			GraphDatabaseConfiguration.INDEX_BACKEND_KEY,
//			"elasticsearch");
//		index.setProperty("local-mode", true);
//		index.setProperty("client-only", false);
//		index.setProperty(
//			GraphDatabaseConfiguration.STORAGE_DIRECTORY_KEY,
//			configString + File.separator + "es");

		TitanGraph graph = TitanFactory.open(configString);
		graph.makeKey(DefaultIndicesNames.CLASS.name()).dataType(String.class).indexed(
			Vertex.class).make();
		graph.makeKey(DefaultIndicesNames.LANGUAGE.name()).dataType(
			String.class).indexed(Vertex.class).make();
		graph.makeKey(DefaultIndicesNames.IS_NULL.name()).dataType(
			Boolean.class).indexed(Vertex.class).make();
		graph.makeKey(DefaultIndicesNames.AS_STRING.name()).dataType(
			String.class).indexed(ES_INDEX_NAME, Vertex.class).make();
		graph.makeKey(DefaultIndicesNames.COVERED_TEXT.name()).dataType(
			String.class).indexed(ES_INDEX_NAME, Vertex.class).make();
		graph.makeKey(DefaultIndicesNames.f_begin.name()).dataType(
			Integer.class).indexed(ES_INDEX_NAME, Vertex.class).make();
		graph.makeKey(DefaultIndicesNames.f_end.name()).dataType(Integer.class).indexed(
			ES_INDEX_NAME,
			Vertex.class).make();
		return graph;
	}

	public void commit(Graph graph) {
		if (!(graph instanceof TitanGraph))
			throw new IllegalArgumentException(
				String.format(
					"Can deal only with Titan graphs, but object of class '%s' was passed",
					graph.getClass()));
		((TitanGraph) graph).commit();
	}

}
