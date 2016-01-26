package com.sfwl.framework.web.exception;

import com.sfwl.framework.exception.PermissionBaseException;

@SuppressWarnings("serial")
public class DataPermissionException extends PermissionBaseException {

	public DataPermissionException(String message) {
		super(message);
	}

	public DataPermissionException(Throwable cause) {
		super(cause);
	}

	public DataPermissionException(String message, String exCode) {
		super(message, exCode);
	}

	public DataPermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataPermissionException(String message, Throwable cause, String exCode) {
		super(message, cause, exCode);
	}

	@Override
	public boolean isSendRedirect() {

		return false;
	}

}
