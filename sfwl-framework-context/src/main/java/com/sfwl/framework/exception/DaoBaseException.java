/**
 * 
 */
package com.sfwl.framework.exception;


/**
 * @Description:
 * @author:Vincent.Zhang
 * 
 *                       <pre>
 * 版本号					修改人					修改说明
 * 2015年10月8日 .1			Vincent.Zhang    	新增
 * </pre>
 */
@SuppressWarnings("serial")
public abstract class DaoBaseException extends AppBaseException {

	public DaoBaseException(String message) {
		super(message);
	}

	public DaoBaseException(Throwable cause) {
		super(cause);
	}

	public DaoBaseException(String message, String exCode) {
		super(message);
		super.setExCode(exCode);
	}

	public DaoBaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoBaseException(String message, Throwable cause, String exCode) {
		super(message, cause);
		super.setExCode(exCode);
	}

}
