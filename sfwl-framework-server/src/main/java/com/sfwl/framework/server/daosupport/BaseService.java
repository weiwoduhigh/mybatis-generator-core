/**
 * 
 */
package com.sfwl.framework.server.daosupport;

import java.util.Collection;

public interface BaseService {

	BaseModel save(BaseModel baseModel);

	Collection save(Collection models);

	int delete(String id);

	int delete(String[] ids);

	BaseModel getById(String id);

	Collection getById(String[] ids);
}
