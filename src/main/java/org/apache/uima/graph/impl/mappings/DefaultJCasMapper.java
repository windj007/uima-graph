package org.apache.uima.graph.impl.mappings;

import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.MappingUtils;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.uimafit.util.JCasUtil;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class DefaultJCasMapper extends MapperBase {
	public void fillVertex(Vertex vertexForObj, Object obj, Graph graph) {
		JCas doc = MappingUtils.checkTypeAndCast(getClass(), JCas.class, obj);
		
		vertexForObj.setProperty(
			DefaultIndicesNames.CLASS.name(),
			obj.getClass().getName());
		vertexForObj.setProperty(
			DefaultIndicesNames.COVERED_TEXT.name(),
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
