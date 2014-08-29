package org.apache.uima.graph.impl.mappings;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.graph.impl.DefaultIndicesNames;
import org.apache.uima.graph.impl.MappingUtils;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class DefaultFeatureStructureMapper extends MapperBase {
	protected Set<String>	typesNotToMap		= new HashSet<String>();
	protected Set<String>	featuresNotToMap	= new HashSet<String>();

	private boolean			separateSimpleValues		= false;

	public DefaultFeatureStructureMapper() {
		this(false);
	}

	public DefaultFeatureStructureMapper(boolean separateSimpleValues) {
		this.separateSimpleValues = separateSimpleValues;
		typesNotToMap.add("uima.cas.Sofa");
		typesNotToMap.add("org.apache.uima.jcas.cas.Sofa");
		featuresNotToMap.add("sofa");
		featuresNotToMap.add("sofaArray");
	}

	public DefaultFeatureStructureMapper(boolean separateSimpleValues,
		Collection<String> typesNotToMap, Collection<String> featuresNotToMap) {
		this.separateSimpleValues = separateSimpleValues;
		this.typesNotToMap.addAll(typesNotToMap);
		this.featuresNotToMap.addAll(featuresNotToMap);
	}

	public void fillVertex(Vertex vertexForObj, Object obj, Graph graph) {
		FeatureStructure fs = MappingUtils.checkTypeAndCast(
			getClass(),
			FeatureStructure.class,
			obj);

		vertexForObj.setProperty(
			DefaultIndicesNames.CLASS.name(),
			obj.getClass().getName());

		for (Feature feat : fs.getType().getFeatures()) {
			if (featuresNotToMap.contains(feat.getShortName())
				|| typesNotToMap.contains(feat.getRange().getName()))
				continue;
			MappingUtils.mapFeature(
				getMappingManager(),
				fs,
				feat,
				graph,
				vertexForObj,
				separateSimpleValues);
		}
	}
}
