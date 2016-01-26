package com.sfwl.framework.web.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sfwl.framework.context.operatethreadcache.OperateThreadCacheHolder;
import com.sfwl.framework.context.paginationthreadcache.PageThreadCacheHolder;
import com.sfwl.framework.context.permissionglobalcache.PermissionHolder;
import com.sfwl.framework.context.permissionglobalcache.PermissionInfo;
import com.sfwl.framework.context.userglobalcache.UserGlobalCacheHolder;
import com.sfwl.plugin.threadcache.ThreadCacheStore;

/**
 * @Description:
 * @author:hongqing.zhang@sfwl.com.cn
 * @version:2016年1月5日 下午2:04:31
 */
public final class LoginFilter implements Filter {
	/**
	 * 需要排除（不拦截）的URL的正则表达式
	 */

	private Pattern excepUrlPattern;

	private OperateThreadCacheHolder operateThreadCacheHolder;

	private PageThreadCacheHolder pageThreadCacheHolder;

	private UserGlobalCacheHolder userGlobalCacheHolder;

	private PermissionHolder permissionHolder;

	public void init(final FilterConfig filterConfig) throws ServletException {
		ServletContext context = filterConfig.getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(context);
		operateThreadCacheHolder = (OperateThreadCacheHolder) ac.getBean("operateThreadCacheHolder");
		pageThreadCacheHolder = (PageThreadCacheHolder) ac.getBean("pageThreadCacheHolder");
		userGlobalCacheHolder = (UserGlobalCacheHolder) ac.getBean("userGlobalCacheHolder");
		permissionHolder = (PermissionHolder) ac.getBean("permissionHolder");
		// 添加过滤URL
		String casExcepUrlRegex = context.getInitParameter("casExcepUrlRegex");
		if (StringUtils.isNotBlank(casExcepUrlRegex)) {
			excepUrlPattern = Pattern.compile(casExcepUrlRegex);
		}
	}

	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		// 如果请求的路径是排除的URL时，则直接放行(过滤js+css+img)
		if ((excepUrlPattern != null && excepUrlPattern.matcher(request.getServletPath()).matches())) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}
		// 从CAS中获取登录名，并设置SESSION的线程缓存

		HttpServletResponse response = (HttpServletResponse) servletResponse;
		final HttpSession session = request.getSession(false);
		final Assertion assertion = (Assertion) (session == null ? request.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) : session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION));
		if (null == assertion || null == assertion.getPrincipal() || null == assertion.getPrincipal().getAttributes()) {
			// CAS校验失败，没有登录
			throw new RuntimeException("Login error!");
		}
		// 获取CAS返回的MAP信息
		Map<String, Object> principalMap = assertion.getPrincipal().getAttributes();
		// 登录用户id
		String userId = (String) principalMap.get("userId");
		if (StringUtils.isBlank(userId)) {
			// CAS校验失败，没有登录
			throw new RuntimeException("Login error!");
		}
		// 判断是否是登录动作,session中是否有userId
		String sfwlLoginId = (String) session.getAttribute("SFWL_LOGIN_ID");
		if (StringUtils.isBlank(sfwlLoginId)) {// 表示当前是用户浏览器的第一次请求
			sfwlLoginId = userId;
			// 设置用户ID至SESSION,以便下次校验是否已经登录过
			session.setAttribute("SFWL_LOGIN_ID", sfwlLoginId);
			// 构建用户的全局缓存信息，如果缓存中已经有，则不操作
			UserInfo userInfo = new UserInfo();
			String userName = (String) principalMap.get("userName");
			String orgId = (String) principalMap.get("orgId");
			String orgName = (String) principalMap.get("orgName");
			userInfo.setUserId(userId);
			userInfo.setUserName(userName);
			userInfo.setOrgId(orgId);
			userInfo.setOrgName(orgName);
			userGlobalCacheHolder.setUserInfo(userId, userInfo);
			// 重新构建用户的权限缓存
			// TO-DO
			// 从数据库中
			List<PermissionInfo> permissionInfoList = new ArrayList<PermissionInfo>();
			permissionHolder.bulidPermission(permissionInfoList);
		} else if (!sfwlLoginId.equals(userId)) {// CAS与SESSION中的用户ID不一致，拒绝响应请求
			// 请求非法
			throw new RuntimeException("Login error!");
		}

		// 构建请求operateThreadCache信息
		String operateInfoOrgId = request.getParameter("operateInfo.orgId");
		String operateInfoOrgType = request.getParameter("operateInfo.orgType");
		String operateInfoReqUrl = request.getServletPath();
		// 线程操作信息不存在，则禁止请求
		// if (StringUtils.isNotBlank(operateInfoOrgId) ||
		// StringUtils.isNotBlank(operateInfoOrgType) ||
		// StringUtils.isNotBlank(operateInfoReqUrl)) {
		// // 请求非法,没有线程变量信息
		// response.sendRedirect(ERROR_PATH);
		// return;
		// }
		OperateInfo operateInfo = new OperateInfo();
		operateInfo.setUserId(userId);
		operateInfo.setOrgId(operateInfoOrgId);
		operateInfo.setOrgType(operateInfoOrgType);
		operateInfo.setReqUrl(operateInfoReqUrl);
		operateInfo.setReqUrlType(null);// 暂时不使用

		// 构建请求PAGE缓存信息
		String pageSqlId = request.getParameter("pageInfo.pageSqlId");
		String pageIndexStr = request.getParameter("pageInfo.pageIndex");
		String pageSizeStr = request.getParameter("pageInfo.pageSize");

		try {
			// 设置CAS线程缓存
			AssertionHolder.setAssertion(assertion);
			// 设置OPERATE线程缓存
			operateThreadCacheHolder.setOperateInfo(operateInfo);
			// 设置PAGE线程缓存
			if (StringUtils.isNotBlank(pageSqlId) && StringUtils.isNumeric(pageIndexStr) && StringUtils.isNumeric(pageSizeStr)) {
				pageThreadCacheHolder.setPageInfo(pageSqlId, Integer.parseInt(pageIndexStr), Integer.parseInt(pageSizeStr));
			}
			// 执行业务
			filterChain.doFilter(servletRequest, servletResponse);

		} finally {
			// 重置CAS线程变量
			AssertionHolder.clear();
			// 重置ThreadCacheStore线程变量
			ThreadCacheStore.reset();
		}
	}

	public void destroy() {
		// nothing to do
	}
}
