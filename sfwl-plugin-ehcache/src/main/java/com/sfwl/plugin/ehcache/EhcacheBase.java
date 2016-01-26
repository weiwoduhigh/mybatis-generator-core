/**
 * 
 */
package com.sfwl.plugin.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

/**
 * @Description:
 * @author:Vincent.Zhang
 * 
 * 						<pre>
 * 版本号					修改人					修改说明
 * 2015年10月20日 .1			Vincent.Zhang    	新增
 *                       </pre>
 */
public class EhcacheBase {

	private static final CacheManager ehcacheManager = new CacheManager();

	protected static Cache createCache(CacheConfiguration config) {
		if (ehcacheManager.cacheExists(config.getName())) {
			return ehcacheManager.getCache(config.getName());
		} else {
			Cache cache = new Cache(config);
			ehcacheManager.addCache(cache);
			return cache;
		}
	}

	protected static Cache getCache(String name) {
		if (ehcacheManager.cacheExists(name)) {
			return ehcacheManager.getCache(name);
		} else {
			return null;
		}
	}

}
