package org.gwt.mosaic2g.client.binding;

public abstract class Converter<S, T> {

	public abstract T convertForward(S value);
	
	public abstract S convertReverse(T value);

}
