package com.sfwl.framework.web;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import com.sfwl.framework.context.operatethreadcache.OperateThreadCacheHolder;
import com.sfwl.framework.context.paginationthreadcache.PageDataModel;
import com.sfwl.framework.context.paginationthreadcache.PageThreadCacheHolder;
import com.sfwl.framework.context.permissionglobalcache.PermissionHolder;
import com.sfwl.framework.context.permissionglobalcache.PermissionInfo;
import com.sfwl.framework.web.exception.FunctionPermissionException;

/**
 * 执行controller切面
 *
 */
public class ControllerAspect {

	@Autowired
	private PageThreadCacheHolder pageThreadCacheHolder;

	@Autowired
	private PermissionHolder permissionHolder;

	@Autowired
	private OperateThreadCacheHolder operateThreadCacheHolder;

	/**
	 * Around方式：对Controller做Around方式的切面。线程变量初始化（分页信息、session信息）
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();
		// 权限校验
		// PermissionInfo permissionInfo = operateThreadCacheHolder.getOperateInfo();
		// boolean isPermission = permissionHolder.checkPermission(permissionInfo);
		// if (!isPermission) {
		// throw new FunctionPermissionException("未授权");
		// }
		Object result = pjp.proceed(args);
		if (result instanceof PageDataModel) {
			((PageDataModel) result).setPageInfoModel(pageThreadCacheHolder.getPageInfo());
		}
		return result;
	}

}
