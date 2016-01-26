/**
 * 
 */
package com.sfwl.framework.context.paginationthreadcache;

import java.util.List;

import com.sfwl.plugin.threadcache.ThreadCacheAbstract;

/**
 * @Description:
 * @author:Vincent.Zhang
 * 
 * 						<pre>
 * 版本号					修改人					修改说明
 * 2015年9月28日 .1			Vincent.Zhang    	新增
 *                       </pre>
 */
public class PageThreadCacheHolder extends ThreadCacheAbstract {

	private final String cacheName = "PageInfoModel";

	public void setPageInfo(String pageSqlId, int pageIndex, int pageSize) {
		PageInfoModel pageInfoModel = new PageInfoModel();
		pageInfoModel.setPageSqlId(pageSqlId);
		pageInfoModel.setPageIndex(pageIndex);
		pageInfoModel.setPageSize(pageSize);
		this.setCache(pageInfoModel);
	}

	public void setPageTotalInfo(int totalDataSize) {
		getPageInfo().setTotalDataSize(totalDataSize);
	}

	public PageInfoModel getPageInfo() {
		return (PageInfoModel) this.getCache();
	}

	public PageDataModel getPageData(List<Object> dateList) {
		PageDataModel pageDataModel = new PageDataModel();
		pageDataModel.setPageData(dateList);
		pageDataModel.setPageInfoModel(this.getPageInfo());
		return pageDataModel;
	}

	protected String getCacheName() {
		return cacheName;
	}

	public void cleanPageInfo() {
		super.cleanCache();
	}

}
