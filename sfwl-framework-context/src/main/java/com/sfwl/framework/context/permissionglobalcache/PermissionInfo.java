package com.sfwl.framework.context.permissionglobalcache;

public interface PermissionInfo extends java.io.Serializable {

	public String getUserId();

	public String getOrgId();

	public String getOrgType();

	public String getReqUrl();

	public String getReqUrlType();

}
