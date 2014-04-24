package org.apache.uima.graph.impl.mappings;

import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.MappingUtils;
import org.apache.uima.graph.impl.WrappedJCas;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.uimafit.util.JCasUtil;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class WrappedJCasMapper extends MapperBase {
	public void fillVertex(Vertex vertexForObj, Object obj, Graph graph) {
		WrappedJCas wrapper = MappingUtils.checkTypeAndCast(getClass(), WrappedJCas.class, obj);
		
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
			MappingUtils.addLink(
				getMappingManager(),
				graph,
				vertexForObj,
				fs,
				DefaultIndicesNames.CONTAINS_FS.name());
		}
	}
}
