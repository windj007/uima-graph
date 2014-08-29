package org.apache.uima.graph.impl.mappings;

import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.MappingUtils;
import org.apache.uima.jcas.cas.FSArray;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class DefaultFSArrayMapper extends DefaultFeatureStructureMapper {

	@Override
	public void fillVertex(Vertex vertexForObj, Object obj, Graph graph) {
		FSArray arr = MappingUtils.checkTypeAndCast(
			DefaultFSArrayMapper.class,
			FSArray.class,
			obj);

		super.fillVertex(vertexForObj, obj, graph);

		FeatureStructure[] elems = arr.toArray();
		for (int i = 0; i < elems.length; i++) {
			FeatureStructure elem = elems[i];
			Vertex elemVert = getMappingManager().enqueueObject(elem);
			Edge e = vertexForObj.addEdge(
				DefaultIndicesNames.HAS_ELEMENT.name(),
				elemVert);
			e.setProperty(DefaultIndicesNames.ELEMENT_I.name(), i);
		}
	}
}
