package com.sfwl.framework.context.userglobalcache;

import com.sfwl.plugin.ehcache.EhcacheAbstract;

import net.sf.ehcache.config.CacheConfiguration;

/**
 * USERINFO全局缓存信息
 * 
 * @author hongqing.zhang
 * @date 2015年10月22日
 */
public class UserGlobalCacheHolder extends EhcacheAbstract {

	private final String USER_GLOBAL_CACHE_NAME = "DefaultUserInfo";

	public void setUserInfo(String userId, DefaultUserInfo userInfo) {
		this.setValue(userId, userInfo);
	}

	public DefaultUserInfo getUserInfo(String userId) {
		if (null == userId || "".equals(userId)) {
			return null;
		}
		return (DefaultUserInfo) this.getValue(userId);
	}

	/**
	 * Cache配置说明： name：Cache的名称，必须是唯一的(ehcache会把这个cache放到HashMap里)。
	 * maxElementsInMemory：内存中保持的对象数量。
	 * maxElementsOnDisk：DiskStore中保持的对象数量，默认值为0，表示不限制。
	 * eternal：是否是永恒数据，如果是，则它的超时设置会被忽略。
	 * overflowToDisk：如果内存中数据数量超过maxElementsInMemory限制，是否要缓存到磁盘上。
	 * timeToIdleSeconds：对象空闲时间，指对象在多长时间没有被访问就会失效。只对eternal为false的有效。默认值0，
	 * 表示一直可以访问。
	 * timeToLiveSeconds：对象存活时间，指对象从创建到失效所需要的时间。只对eternal为false的有效。默认值0，
	 * 表示一直可以访问。 diskPersistent：是否在磁盘上持久化。指重启jvm后，数据是否有效。默认为false。
	 * diskExpiryThreadIntervalSeconds：对象检测线程运行时间间隔。标识对象状态的线程多长时间运行一次。
	 * diskSpoolBufferSizeMB：DiskStore使用的磁盘大小，默认值30MB。每个cache使用各自的DiskStore。
	 * memoryStoreEvictionPolicy：如果内存中数据超过内存限制，向磁盘缓存时的策略。默认值LRU，可选FIFO、LFU。
	 */
	@Override
	protected CacheConfiguration getCacheConfig() {
		CacheConfiguration sessionConfiguration = new CacheConfiguration();
		sessionConfiguration.setName(USER_GLOBAL_CACHE_NAME);// 设置Cache变量名称
		// sessionConfiguration.setMaxElementsInMemory(100);// 内存中保持的对象数量
		sessionConfiguration.setEternal(true);// 是否是永恒数据，如果是，则它的超时设置会被忽略，默认false
		sessionConfiguration.setMaxEntriesLocalHeap(0);
		sessionConfiguration.setTimeToIdleSeconds(30);
		sessionConfiguration.setTimeToLiveSeconds(30);
		return sessionConfiguration;
	}

}
