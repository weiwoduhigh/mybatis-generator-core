/**
 * 
 */
package com.sfwl.framework.context.paginationthreadcache;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @author:Vincent.Zhang
 * 
 * 						<pre>
 * 版本号					修改人					修改说明
 * 2015年9月28日 .1			Vincent.Zhang    	新增
 *                       </pre>
 */
public class PageDataModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5547849681213054580L;

	private PageInfoModel pageInfoModel;

	private List<?> pageData;

	/**
	 * @return the pageInfoModel
	 */
	public PageInfoModel getPageInfoModel() {
		return pageInfoModel;
	}

	/**
	 * @param pageInfoModel
	 *            the pageInfoModel to set
	 */
	public void setPageInfoModel(PageInfoModel pageInfoModel) {
		this.pageInfoModel = pageInfoModel;
	}

	/**
	 * @return the pageData
	 */
	public List<?> getPageData() {
		return pageData;
	}

	/**
	 * @param pageData
	 *            the pageData to set
	 */
	public void setPageData(List<?> pageData) {
		this.pageData = pageData;
	}

}
