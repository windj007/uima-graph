package org.apache.uima.graph.impl.mappings;

import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.GraphUtils;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.uimafit.util.JCasUtil;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class DefaultJCasMapping extends MappingBase {

	public Vertex mapToGraph(Object obj, Graph graph)
		throws IllegalArgumentException, IllegalStateException {
		if (!(obj instanceof JCas))
			throw new IllegalArgumentException(
				String.format(
					"DefaultJCasMapping can only map objects of subtypes of '%s', but '%s' was passed",
					JCas.class,
					obj.getClass()));

		JCas doc = (JCas) obj;
		int addr = getAddress(doc);
		Vertex result = GraphUtils.tryGetVertexByAddr(graph, addr);
		if (result != null)
			return result;

		result = graph.addVertex(null);
		result.setProperty(DefaultIndicesNames.ADDR.name(), addr);
		result.setProperty(
			DefaultIndicesNames.CLASS.name(),
			obj.getClass().getName());
		result.setProperty(
			DefaultIndicesNames.COVERED_TEXT.name(),
			doc.getDocumentText());
		result.setProperty(
			DefaultIndicesNames.LANGUAGE.name(),
			doc.getDocumentLanguage());

		for (TOP fs : JCasUtil.selectAll(doc)) {
			GraphUtils.addLink(
				getMappingProvider(),
				graph,
				result,
				fs,
				DefaultIndicesNames.CONTAINS_FS.name());
		}

		return result;
	}
	
	protected int getAddress(JCas doc) {
		return doc.hashCode();
	}

}
