/**
 * 
 */
package com.sfwl.framework.web;

import java.io.Serializable;

public class AjaxMessage implements Serializable {

	private static final long serialVersionUID = 8548032386082569951L;
	private Boolean success;
	private String exception;
	private String message;
	private Serializable data;
	
	public AjaxMessage() {
		this.success = true;
	}

	public void setSuccess(String msg) {
		this.setSuccess(true);
		this.setMessage(msg);
	}

	public void setError(String msg) {
		this.setSuccess(false);
		this.setMessage(msg);
	}

	/**
	 * @return the success
	 */
	public Boolean getSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	/**
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}

	/**
	 * @param exception
	 *            the exception to set
	 */
	public void setException(String exception) {
		this.exception = exception;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the data
	 */
	public Serializable getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Serializable data) {
		this.data = data;
	}

}