/**
 * 
 */
package com.sfwl.framework.server.daosupport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.sfwl.framework.context.operatethreadcache.DefaultOperateInfo;
import com.sfwl.framework.context.operatethreadcache.OperateThreadCacheHolder;
import com.sfwl.framework.context.userglobalcache.DefaultUserInfo;
import com.sfwl.framework.context.userglobalcache.UserGlobalCacheHolder;

/**
 * @Description:
 * @author:Vincent.Zhang
 * 
 * 						<pre>
 * 版本号					修改人					修改说明
 * 2015年9月26日 .1			Vincent.Zhang    	新增
 *                       </pre>
 */

public abstract class BaseServiceImpl {

	protected abstract BaseMapper getMapper();

	@Autowired
	private OperateThreadCacheHolder operatorThreadCacheHolder;
	@Autowired
	private UserGlobalCacheHolder userGlobalCacheHolder;

	protected DefaultUserInfo getUserInfo() {
		String userId = operatorThreadCacheHolder.getOperateInfo().getUserId();
		return userGlobalCacheHolder.getUserInfo(userId);
	}

	protected DefaultOperateInfo getOperateInfo() {
		return operatorThreadCacheHolder.getOperateInfo();
	}

	public BaseModel save(BaseModel baseModel) {
		Collection models = new ArrayList<BaseModel>();
		models.add(baseModel);
		models = save(models);
		if (null != models || models.size() > 0) {
			for (Object model : models) {
				return (BaseModel) model;
			}
		}
		return null;
	}

	public Collection save(Collection models) {
		if (null == models || models.size() <= 0) {
			throw new DaoException("Save failed,Models is null!");
		}
		Collection<BaseModel> insertModels = new ArrayList<BaseModel>();
		Collection<BaseModel> updateModels = new ArrayList<BaseModel>();
		Date now = new Date();
		// 取登录用户session进行赋值
		String loginUserId = getOperateInfo().getUserId();
		try {
			for (Object object : models) {
				BaseModel baseModel = (BaseModel) object;
				String id = baseModel.getId();
				baseModel.setModifier(loginUserId);
				baseModel.setModifyTime(now);
				if (null == id || "".equals(id)) {
					baseModel.setCreator(loginUserId);
					baseModel.setCreateTime(now);
					baseModel.setRecordVersion(1l);
					baseModel.setId(UUIDGenerator.getUUID());
					insertModels.add(baseModel);
				} else {
					if (null == baseModel.getRecordVersion()) {
						throw new DaoException("Update failed,Does not define RecordVersion values!");
					}
					baseModel.setRecordVersion(baseModel.getRecordVersion() + 1);
					updateModels.add(baseModel);
				}
			}
			if (null != insertModels && insertModels.size() > 0) {
				if (getMapper().insert(insertModels) != insertModels.size()) {
					throw new DaoException("Insert failed!");
				}
			}
			if (null != updateModels && updateModels.size() > 0) {
				int a = getMapper().update(updateModels) ;
				if (a != updateModels.size()) {
					throw new DaoException("Update failed,May be data has expired!");
				}
			}
			insertModels.addAll(updateModels);
		} catch (Exception e) {
			throw e;
		}
		return insertModels;
	}

	public int delete(String id) {
		String[] ids = { id };
		return delete(ids);
	}

	public int delete(String[] ids) {
		return getMapper().delete(ids);
	}

	public BaseModel getById(String id) {
		String[] ids = { id };
		Collection<BaseModel> models = getById(ids);
		if (null != models || models.size() > 0) {
			for (BaseModel model : models) {
				return model;
			}
		}
		return null;
	}

	public Collection getById(String[] ids) {
		return getMapper().getById(ids);
	}

}
