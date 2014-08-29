package org.apache.uima.graph.impl;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.graph.IMappingManager;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public final class MappingUtils {
	public static final String	FEATURE_PREFIX				= "f_";
	public static final String	SIMPLE_FEATURE_NULL_VALUE	= "";

	private MappingUtils() {
	}

	public static void mapFeature(IMappingManager manager,
		FeatureStructure origObj, Feature feat, Graph graph, Vertex origVertex, boolean separateSimpleValues) {
		String linkName = FEATURE_PREFIX + feat.getShortName();
		if (feat.getRange().isPrimitive()) {
			String value = origObj.getFeatureValueAsString(feat);
			if (value == null)
				value = SIMPLE_FEATURE_NULL_VALUE;
			if (separateSimpleValues)
				addLink(manager,
					graph,
					origVertex,
					FeatureValueWrapper.wrap(feat.getName(), value),
					linkName);
			else
				origVertex.setProperty(linkName, value);
		} else {
			addLink(
				manager,
				graph,
				origVertex,
				origObj.getFeatureValue(feat),
				linkName);
		}
	}

	public static Edge addLink(IMappingManager manager, Graph graph,
		Vertex from, Object toObj, String label) {
		Vertex toVertex = manager.enqueueObject(toObj);
		Edge edge = from.addEdge(label, toVertex);
		return edge;
	}

	public static <T> T checkTypeAndCast(Class<?> mapperCls,
		Class<? extends T> acceptableType, Object obj) {
		if (obj == null)
			throw new IllegalArgumentException(String.format(
				"%s was asked to process null (object of type %s expected)",
				mapperCls.getName(),
				acceptableType.getName()));
		if (!acceptableType.isInstance(obj))
			throw new IllegalArgumentException(String.format(
				"%s expects subclasses of '%s', but object of '%s' was passed",
				mapperCls.getName(),
				acceptableType.getName(),
				obj.getClass()));
		return acceptableType.cast(obj);
	}
}
