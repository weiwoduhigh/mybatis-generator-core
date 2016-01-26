package com.sfwl.framework.context.permissionglobalcache;

import java.util.Map;

import com.sfwl.plugin.ehcache.EhcacheAbstract;

import net.sf.ehcache.config.CacheConfiguration;

public class PermissionGlobalCacheHolder extends EhcacheAbstract {

	private final String PERMISSION_GLOBAL_CACHE_NAME = "PERMISSION_GLOBAL_CACHE";

	@SuppressWarnings("unchecked")
	protected Map<String, Map<String, Object>> getPermissionMap(String userId) {
		return (Map<String, Map<String, Object>>) this.getValue(userId);
	}

	protected void setPermissionMap(String userId, Map<String, Map<String, Object>> permissionMap) {
		this.setValue(userId, permissionMap);
	}

	@Override
	protected CacheConfiguration getCacheConfig() {
		CacheConfiguration sessionConfiguration = new CacheConfiguration();
		sessionConfiguration.setName(PERMISSION_GLOBAL_CACHE_NAME);// 设置Cache变量名称
		// sessionConfiguration.setMaxElementsInMemory(100);// 内存中保持的对象数量
		sessionConfiguration.setEternal(true);// 是否是永恒数据，如果是，则它的超时设置会被忽略，默认false
		sessionConfiguration.setMaxEntriesLocalHeap(0);
		sessionConfiguration.setTimeToIdleSeconds(30);
		sessionConfiguration.setTimeToLiveSeconds(30);
		return sessionConfiguration;
	}

}
