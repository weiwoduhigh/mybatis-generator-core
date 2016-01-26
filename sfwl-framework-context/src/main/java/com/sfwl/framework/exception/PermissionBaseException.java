package com.sfwl.framework.exception;

/**
 * 权限校验异常，通过子类化来设置不同的权限异常
 * 
 * @author huabin.lin
 *
 */
@SuppressWarnings("serial")
public abstract class PermissionBaseException extends AppBaseException {

	public PermissionBaseException(String message) {
		super(message);
	}

	public PermissionBaseException(Throwable cause) {
		super(cause);
	}

	public PermissionBaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public PermissionBaseException(String message, String exCode) {
		super(message);
		super.setExCode(exCode);
	}

	public PermissionBaseException(String message, Throwable cause, String exCode) {
		super(message, cause);
		super.setExCode(exCode);
	}

	/**
	 * 判断未具备权限时是否需要重定向，默认不需要
	 * 
	 * @return是否需要跳转
	 */
	public boolean isSendRedirect() {

		return false;
	}

}
