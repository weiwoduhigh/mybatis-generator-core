/**
 * 
 */
package com.sfwl.framework.web;

import org.springframework.beans.factory.annotation.Autowired;

import com.sfwl.framework.context.operatethreadcache.DefaultOperateInfo;
import com.sfwl.framework.context.operatethreadcache.OperateThreadCacheHolder;
import com.sfwl.framework.context.userglobalcache.DefaultUserInfo;
import com.sfwl.framework.context.userglobalcache.UserGlobalCacheHolder;

/**
 * @Description:
 * @author:hongqing.zhang@sfwl.com.cn
 * @version:2016年1月5日 上午11:53:51
 */
public abstract class BaseController {

	@Autowired
	private OperateThreadCacheHolder operatorThreadCacheHolder;
	@Autowired
	private UserGlobalCacheHolder userGlobalCacheHolder;

	protected DefaultUserInfo getUserInfo() {
		String userId = operatorThreadCacheHolder.getOperateInfo().getUserId();
		return userGlobalCacheHolder.getUserInfo(userId);
	}

	protected DefaultOperateInfo getOperateInfo() {
		return operatorThreadCacheHolder.getOperateInfo();
	}

}
