package com.sfwl.framework.web.exception;

import com.sfwl.framework.exception.PermissionBaseException;

@SuppressWarnings("serial")
public class FunctionPermissionException extends PermissionBaseException {

	public FunctionPermissionException(String message) {
		super(message);
	}

	public FunctionPermissionException(Throwable cause) {
		super(cause);
	}

	public FunctionPermissionException(String message, String exCode) {
		super(message, exCode);
	}

	public FunctionPermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public FunctionPermissionException(String message, Throwable cause, String exCode) {
		super(message, cause, exCode);
	}

	@Override
	public boolean isSendRedirect() {

		return false;
	}

}
