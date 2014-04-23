package org.apache.uima.graph.exceptions;

public class CannotCreateFactory extends UIMAGraphExceptionBase {

	private static final long	serialVersionUID	= -5421188584047700063L;

	public CannotCreateFactory() {
		super();
	}

	public CannotCreateFactory(String aMessageKey,
		Object[] aArguments) {
		super(aMessageKey, aArguments);
	}

	public CannotCreateFactory(Throwable aCause) {
		super(aCause);
	}
}
