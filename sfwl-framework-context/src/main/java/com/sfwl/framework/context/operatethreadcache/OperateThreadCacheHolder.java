package com.sfwl.framework.context.operatethreadcache;

import com.sfwl.framework.context.userglobalcache.DefaultUserInfo;
import com.sfwl.framework.context.userglobalcache.UserGlobalCacheHolder;
import com.sfwl.plugin.threadcache.ThreadCacheAbstract;

/**
 * 操作者信息线程缓存
 * 
 * @author hongqing.zhang
 * @date 2015年10月26日
 */
public class OperateThreadCacheHolder extends ThreadCacheAbstract {

	private final String cacheName = "DefaultOperateInfo";

	private UserGlobalCacheHolder userGlobalCacheHolder;

	protected String getCacheName() {
		return cacheName;
	}

	public void setOperateInfo(DefaultOperateInfo defaultOperateInfo) {
		this.setCache(defaultOperateInfo);
	}

	public DefaultOperateInfo getOperateInfo() {
		return (DefaultOperateInfo) this.getCache();
	}

	public DefaultUserInfo getCurrentUserInfo() {
		return userGlobalCacheHolder.getUserInfo(this.getOperateInfo().getUserId());
	}

	public void setUserGlobalCacheHolder(UserGlobalCacheHolder userGlobalCacheHolder) {
		this.userGlobalCacheHolder = userGlobalCacheHolder;
	}
	

}
