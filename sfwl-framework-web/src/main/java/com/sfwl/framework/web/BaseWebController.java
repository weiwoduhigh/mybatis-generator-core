/**
 * 
 */
package com.sfwl.framework.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sfwl.framework.exception.AppBaseException;
import com.sfwl.framework.exception.PermissionBaseException;
import com.sfwl.framework.web.exception.OtherSpringException;

/**
 * @Description:
 * @author:hongqing.zhang@sfwl.com.cn
 * @version:2016年1月5日 上午11:53:51
 */
public class BaseWebController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(BaseWebController.class);

	// 默认的异常处理机制
	@ExceptionHandler
	protected String defaultException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws ServletException, IOException {
		String resultViewName = "";
		String xRequestedWith = request.getHeader("X-Requested-With");
		if (null != xRequestedWith && !"".equals(xRequestedWith)) {
			resultViewName = "errors/ajax-error";
		} else {
			resultViewName = "errors/error";
		}
		// 判断框架抛出的异常类型
		if (ex instanceof PermissionBaseException) {
			if (((PermissionBaseException) ex).isSendRedirect()) {
				resultViewName = "index";
			}
		} else if (ex instanceof AppBaseException) {
		} else {
			ex = new OtherSpringException(ex.getCause());
		}
		// 异常日志记录
		logger.warn(((AppBaseException) ex).getExValue(), ex);
		request.setAttribute("ex", ex);
		return resultViewName;
	}

}
