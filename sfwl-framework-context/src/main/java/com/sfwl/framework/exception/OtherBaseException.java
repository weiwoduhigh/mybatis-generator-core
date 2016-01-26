package com.sfwl.framework.exception;


/**
 * 转换、解析等异常抽象类，业务通过继承进行扩展
 * 
 * @author huabin.lin
 *
 */
@SuppressWarnings("serial")
public abstract class OtherBaseException extends AppBaseException {

	public OtherBaseException(String message) {
		super(message);
	}

	public OtherBaseException(Throwable cause) {
		super(cause);
	}

	public OtherBaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public OtherBaseException(String message, String exCode) {
		super(message);
		super.setExCode(exCode);
	}

	public OtherBaseException(String message, Throwable cause, String exCode) {
		super(message, cause);
		super.setExCode(exCode);
	}

}
