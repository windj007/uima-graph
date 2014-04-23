package org.apache.uima.graph.exceptions;

import org.apache.uima.UIMAException;

public class UIMAGraphExceptionBase extends UIMAException {

	private static final long	serialVersionUID	= 6878358054016689782L;

	public UIMAGraphExceptionBase() {
		super();
	}

	public UIMAGraphExceptionBase(Throwable aCause) {
		super(aCause);
	}

	public UIMAGraphExceptionBase(String aMessageKey, Object[] aArguments) {
		super(aMessageKey, aArguments);
	}
}
