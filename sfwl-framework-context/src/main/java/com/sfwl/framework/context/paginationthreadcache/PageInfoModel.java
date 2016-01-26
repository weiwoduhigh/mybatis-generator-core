/**
 * 
 */
package com.sfwl.framework.context.paginationthreadcache;

/**
 * @Description:
 * @author:Vincent.Zhang
 * 
 * 						<pre>
 * 版本号					修改人					修改说明
 * 2015年9月26日 .1			Vincent.Zhang    	新增
 *                       </pre>
 */
public class PageInfoModel {

	private String pageSqlId;
	private int pageIndex;
	private int pageSize;
	private int totalDataSize;
	private int totalPageSize;
	
	/**
	 * @return the pageSqlId
	 */
	public String getPageSqlId() {
		return pageSqlId;
	}
	/**
	 * @param pageSqlId the pageSqlId to set
	 */
	public void setPageSqlId(String pageSqlId) {
		this.pageSqlId = pageSqlId;
	}
	/**
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}
	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the totalDataSize
	 */
	public int getTotalDataSize() {
		return totalDataSize;
	}
	/**
	 * @param totalDataSize the totalDataSize to set
	 */
	public void setTotalDataSize(int totalDataSize) {
		this.totalDataSize = totalDataSize;
	}
	/**
	 * @return the totalPageSize
	 */
	public int getTotalPageSize() {
		return totalPageSize;
	}
	/**
	 * @param totalPageSize the totalPageSize to set
	 */
	public void setTotalPageSize(int totalPageSize) {
		this.totalPageSize = totalPageSize;
	}
	

	
}
