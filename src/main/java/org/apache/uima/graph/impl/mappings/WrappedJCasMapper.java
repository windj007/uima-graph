package org.apache.uima.graph.impl.mappings;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.MappingUtils;
import org.apache.uima.graph.impl.WrappedJCas;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class WrappedJCasMapper extends MapperBase {
	protected Set<String>	typesNotToMap	= new HashSet<String>();

	public WrappedJCasMapper() {
	}

	public WrappedJCasMapper(Collection<String> typesNotToMap) {
		this.typesNotToMap.addAll(typesNotToMap);
	}

	public void fillVertex(Vertex vertexForObj, Object obj, Graph graph) {
		WrappedJCas wrapper = MappingUtils.checkTypeAndCast(
			getClass(),
			WrappedJCas.class,
			obj);

		JCas doc = wrapper.getDoc();

		vertexForObj.setProperty(
			DefaultIndicesNames.CLASS.name(),
			obj.getClass().getName());
		vertexForObj.setProperty(
			DefaultIndicesNames.TEXT.name(),
			doc.getDocumentText());
		vertexForObj.setProperty(
			DefaultIndicesNames.LANGUAGE.name(),
			doc.getDocumentLanguage());

		for (TOP fs : JCasUtil.selectAll(doc)) {
			if (typesNotToMap.contains(fs.getType().getName()))
				continue;
			MappingUtils.addLink(
				getMappingManager(),
				graph,
				vertexForObj,
				fs,
				DefaultIndicesNames.CONTAINS_FS.name());
		}
	}
}
