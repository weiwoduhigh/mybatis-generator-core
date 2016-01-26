package com.sfwl.framework.web.exception;

import com.sfwl.framework.exception.PermissionBaseException;

@SuppressWarnings("serial")
public class LoginPermissionException extends PermissionBaseException {

	public LoginPermissionException(String message) {
		super(message);
	}

	public LoginPermissionException(Throwable cause) {
		super(cause);
	}

	public LoginPermissionException(String message, String exCode) {
		super(message, exCode);
	}

	public LoginPermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginPermissionException(String message, Throwable cause, String exCode) {
		super(message, cause, exCode);
	}

	@Override
	public boolean isSendRedirect() {

		return false;
	}

}
