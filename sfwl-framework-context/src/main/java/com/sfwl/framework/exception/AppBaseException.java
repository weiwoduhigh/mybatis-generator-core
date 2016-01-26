/**
 * 
 */
package com.sfwl.framework.exception;

import java.io.IOException;

/**
 * @Description:
 * @author:Vincent.Zhang <pre>
 * 版本号					修改人					修改说明
 * 2015年12月24日 .1			Vincent.Zhang    	新增
 * </pre>
 */
@SuppressWarnings("serial")
public abstract class AppBaseException extends RuntimeException {

	private String exCode;

	public AppBaseException(String message) {
		super(message);
	}

	public AppBaseException(Throwable cause) {
		super(cause);
	}

	public AppBaseException(String message, String exCode) {
		super(message);
		this.exCode = exCode;
	}

	public AppBaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppBaseException(String message, Throwable cause, String exCode) {
		super(message, cause);
		this.exCode = exCode;
	}

	public String getExCode() {
		return exCode;
	}

	public void setExCode(String exCode) {
		this.exCode = exCode;
	}

	public String getExValue() throws IOException {
		String result = "";

		if (null != exCode && !"".equals(exCode)) {
			result = ExceptionConfigs.getConfigs().getExceptionInfo(exCode);
		}
		return result;
	}

}
