package org.apache.uima.graph.exceptions;

public class CannotCreateMappingProviderException extends
	UIMAGraphExceptionBase {

	private static final long	serialVersionUID	= -7224850989965565805L;

	public CannotCreateMappingProviderException() {
	}

	public CannotCreateMappingProviderException(Throwable aCause) {
		super(aCause);
	}

	public CannotCreateMappingProviderException(String aMessageKey,
		Object[] aArguments) {
		super(aMessageKey, aArguments);
	}

}
