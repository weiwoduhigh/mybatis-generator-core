/**
 * 
 */
package com.sfwl.framework.server.daosupport;

import java.util.Date;

public abstract interface Recordable {

	public abstract String getId();
	
	public abstract void setId(String id);

	public abstract String getCreator();

	public abstract void setCreator(String paramString);

	public abstract Date getCreateTime();

	public abstract void setCreateTime(Date paramDate);

	public abstract String getModifier();

	public abstract void setModifier(String paramString);

	public abstract Date getModifyTime();

	public abstract void setModifyTime(Date paramDate);

	public abstract Long getRecordVersion();

	public abstract void setRecordVersion(Long recordVersion);

}