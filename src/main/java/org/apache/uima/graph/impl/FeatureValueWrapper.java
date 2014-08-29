package org.apache.uima.graph.impl;


public class FeatureValueWrapper {
	private String	fullFeatureName	= "";
	private Object	value			= null;

	protected FeatureValueWrapper(String fullFeatureName, Object value) {
		this.fullFeatureName = fullFeatureName;
		this.value = value;
	}

	public String getFullFeatureName() {
		return fullFeatureName;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
			+ ((fullFeatureName == null) ? 0 : fullFeatureName.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureValueWrapper other = (FeatureValueWrapper) obj;
		if (fullFeatureName == null) {
			if (other.fullFeatureName != null)
				return false;
		} else if (!fullFeatureName.equals(other.fullFeatureName))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return fullFeatureName + " = " + value;
	}

	public static FeatureValueWrapper wrap(String fullFeatureName, Object value) {
		return new FeatureValueWrapper(fullFeatureName, value);
	}
}
