package com.sfwl.framework.context.permissionglobalcache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class PermissionHolder {

	private PermissionGlobalCacheHolder permissionGlobalCacheHolder;

	// 校验权限
	public boolean checkPermission(PermissionInfo permissionInfo) {
		if (null == permissionInfo || StringUtils.isEmpty(permissionInfo.getUserId()) || StringUtils.isEmpty(permissionInfo.getOrgId()) || StringUtils.isEmpty(permissionInfo.getOrgType())
				|| StringUtils.isEmpty(permissionInfo.getReqUrl())) {
			return false;
		}
		Map<String, Map<String, Object>> userPermissionMap = permissionGlobalCacheHolder.getPermissionMap(permissionInfo.getUserId());
		if (null == userPermissionMap) {
			return false;
		}
		Map<String, Object> urlMap = userPermissionMap.get(permissionInfo.getOrgId() + "@@" + permissionInfo.getOrgType());
		if (null == urlMap) {
			return false;
		}
		if (!urlMap.containsKey(permissionInfo.getReqUrl())) {
			return false;
		} else {
			return true;
		}
	}

	// 构建权限MAP
	public void bulidPermission(List<PermissionInfo> permissionInfoList) {
		updataPermission(permissionInfoList, "bulid");
	}

	// 添加权限
	public void addPermission(List<PermissionInfo> permissionInfoList) {
		updataPermission(permissionInfoList, "add");
	}

	// 删除权限
	public void deletePermission(List<PermissionInfo> permissionInfoList) {
		updataPermission(permissionInfoList, "delete");
	}

	private void updataPermission(List<PermissionInfo> permissionInfoList, String funType) {
		// 权限LIST为空，则直接返回
		if (null == permissionInfoList || permissionInfoList.size() <= 0) {
			return;
		}
		// 以第一条权限的USERID为准
		String userId = permissionInfoList.get(0).getUserId();
		// USERID必须有值
		if (StringUtils.isEmpty(userId)) {
			return;
		}

		// 在循环之后先从缓存中获取权限MAP，提高效率
		Map<String, Map<String, Object>> userPermissionMap = permissionGlobalCacheHolder.getPermissionMap(userId);

		// 根据操作类型对权限MAP进行不同初始化操作
		if ("bulid".equals(funType)) {
			// [重新构建]权限时需要确保MAP为无值且有定义
			if (null == userPermissionMap) {
				userPermissionMap = new HashMap<String, Map<String, Object>>();
			} else {
				userPermissionMap.clear();
			}
		} else if ("add".equals(funType)) {
			// [添加]权限时需要确保MAP有定义
			if (null == userPermissionMap) {
				userPermissionMap = new HashMap<String, Map<String, Object>>();
			}
		} else if ("delete".equals(funType)) {
			// [删除]权限时如果MAP为空则直接返回
			if (null == userPermissionMap || userPermissionMap.size() <= 0) {
				return;
			}
		}

		// 根据不同的操作类型调用权限设置方法
		if ("bulid".equals(funType) || "add".equals(funType)) {
			for (PermissionInfo permissionInfo : permissionInfoList) {
				if (!userId.equals(permissionInfo.getUserId())) {
					continue;
				}
				addOnePermission(userPermissionMap, permissionInfo);
			}
		} else if ("delete".equals(funType)) {
			for (PermissionInfo permissionInfo : permissionInfoList) {
				if (!userId.equals(permissionInfo.getUserId())) {
					continue;
				}
				deleteOnePermission(userPermissionMap, permissionInfo);
			}
		}

	}

	private void addOnePermission(Map<String, Map<String, Object>> userPermissionMap, PermissionInfo permissionInfo) {
		if (StringUtils.isEmpty(permissionInfo.getOrgId()) || StringUtils.isEmpty(permissionInfo.getOrgType()) || StringUtils.isEmpty(permissionInfo.getReqUrl())) {
			return;
		}
		String orgKey = permissionInfo.getOrgId() + "@@" + permissionInfo.getOrgType();
		Map<String, Object> urlMap = userPermissionMap.get(orgKey);
		if (null == urlMap) {
			urlMap = new HashMap<String, Object>();
			userPermissionMap.put(orgKey, urlMap);
		}
		urlMap.put(permissionInfo.getReqUrl(), permissionInfo.getReqUrlType());
	}

	private void deleteOnePermission(Map<String, Map<String, Object>> userPermissionMap, PermissionInfo permissionInfo) {
		// 删除某用户下的所有权限信息
		if (StringUtils.isEmpty(permissionInfo.getOrgId()) && StringUtils.isEmpty(permissionInfo.getOrgType())) {
			userPermissionMap.clear();
			return;
		}

		if (StringUtils.isNotEmpty(permissionInfo.getOrgId()) && StringUtils.isNotEmpty(permissionInfo.getOrgType())) {
			String orgKey = permissionInfo.getOrgId() + "@@" + permissionInfo.getOrgType();
			Map<String, Object> urlMap = userPermissionMap.get(orgKey);
			if (null == urlMap) {
				return;
			}
			if (StringUtils.isEmpty(permissionInfo.getReqUrl())) {// 删除某用户下某组织+组织类型的权限信息
				urlMap.clear();
				return;
			} else {// 删除某条具体权限
				urlMap.remove(permissionInfo.getReqUrl());
				return;
			}
		}
	}

	public void setPermissionGlobalCacheHolder(PermissionGlobalCacheHolder permissionGlobalCacheHolder) {
		this.permissionGlobalCacheHolder = permissionGlobalCacheHolder;
	}

}
