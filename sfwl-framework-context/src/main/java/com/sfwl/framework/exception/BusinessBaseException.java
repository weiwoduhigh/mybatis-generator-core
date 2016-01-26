package com.sfwl.framework.exception;


/**
 * 操作异常（流程控制）
 * 
 * @author huabin.lin
 *
 */
@SuppressWarnings("serial")
public abstract class BusinessBaseException extends AppBaseException {

	public BusinessBaseException(String message) {
		super(message);
	}

	public BusinessBaseException(Throwable cause) {
		super(cause);
	}

	public BusinessBaseException(String message, String exCode) {
		super(message, exCode);
	}

	public BusinessBaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessBaseException(String message, Throwable cause, String exCode) {
		super(message, cause, exCode);
	}

}
