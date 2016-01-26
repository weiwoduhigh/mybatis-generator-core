package com.sfwl.framework.web.exception;

import com.sfwl.framework.exception.OtherBaseException;

@SuppressWarnings("serial")
public class OtherSpringException extends OtherBaseException {

	public OtherSpringException(String message) {
		super(message);
	}

	public OtherSpringException(Throwable cause) {
		super(cause);
	}

	public OtherSpringException(String message, String exCode) {
		super(message, exCode);
	}

	public OtherSpringException(String message, Throwable cause) {
		super(message, cause);
	}

	public OtherSpringException(String message, Throwable cause, String exCode) {
		super(message, cause, exCode);
	}

}
