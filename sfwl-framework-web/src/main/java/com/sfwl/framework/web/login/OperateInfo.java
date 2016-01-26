/**
 * 
 */
package com.sfwl.framework.web.login;

import com.sfwl.framework.context.operatethreadcache.DefaultOperateInfo;

/**
 * @Description:
 * @author:hongqing.zhang@sfwl.com.cn
 * @version:2016年1月20日 上午9:16:35
 */
public class OperateInfo implements DefaultOperateInfo {

	private String userId;
	private String orgId;
	private String orgType;
	private String reqUrl;
	private String reqUrlType;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId
	 *            the orgId to set
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return the orgType
	 */
	public String getOrgType() {
		return orgType;
	}

	/**
	 * @param orgType
	 *            the orgType to set
	 */
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	/**
	 * @return the reqUrl
	 */
	public String getReqUrl() {
		return reqUrl;
	}

	/**
	 * @param reqUrl
	 *            the reqUrl to set
	 */
	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	/**
	 * @return the reqUrlType
	 */
	public String getReqUrlType() {
		return reqUrlType;
	}

	/**
	 * @param reqUrlType
	 *            the reqUrlType to set
	 */
	public void setReqUrlType(String reqUrlType) {
		this.reqUrlType = reqUrlType;
	}

}
