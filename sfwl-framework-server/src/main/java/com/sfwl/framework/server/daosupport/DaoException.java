/**
 * 
 */
package com.sfwl.framework.server.daosupport;

import com.sfwl.framework.exception.DaoBaseException;


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
public class DaoException extends DaoBaseException {

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

	public DaoException(String message, String exCode) {
		super(message);
		super.setExCode(exCode);
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(String message, Throwable cause, String exCode) {
		super(message, cause);
		super.setExCode(exCode);
	}

}
