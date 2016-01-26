/**
 *  具体写法可以再参考下 com.github.miemiedev.mybatis.paginator.OffsetLimitInterceptor
 */
package com.sfwl.framework.server.daosupport;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.sfwl.framework.context.paginationthreadcache.PageInfoModel;
import com.sfwl.framework.context.paginationthreadcache.PageThreadCacheHolder;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PageInterceptor implements Interceptor {

	private static String dialect = "";

	private PageThreadCacheHolder pageThreadCacheHolder;

	public Object intercept(Invocation ivk) throws Throwable {
		if (ivk.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) getValueByFieldName(statementHandler, "delegate");
			MappedStatement mappedStatement = (MappedStatement) getValueByFieldName(delegate, "mappedStatement");
			PageInfoModel pageInfo = pageThreadCacheHolder.getPageInfo();
			if (null != pageInfo && null != pageInfo.getPageSqlId() && !"".equals(pageInfo.getPageSqlId())
					&& null != mappedStatement.getId() && mappedStatement.getId().endsWith(pageInfo.getPageSqlId())) {
				BoundSql boundSql = delegate.getBoundSql();
				Object parameterObject = boundSql.getParameterObject();
				Connection connection = (Connection) ivk.getArgs()[0];
				String sql = boundSql.getSql();
				String countSql = "select count(1) from (" + sql + ") myCount";
				PreparedStatement countStmt = null;
				ResultSet rs = null;
				try {
					countStmt = connection.prepareStatement(countSql);
					BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
							boundSql.getParameterMappings(), parameterObject);
					Field metaParamsField = getFieldByFieldName(boundSql, "metaParameters");
					if (metaParamsField != null) {
						MetaObject mo = (MetaObject) getValueByFieldName(boundSql, "metaParameters");
						setValueByFieldName(countBS, "metaParameters", mo);
					}
					setParameters(countStmt, mappedStatement, countBS, parameterObject);
					rs = countStmt.executeQuery();
					int count = 0;
					if (rs.next()) {
						count = rs.getInt(1);
					}
					pageThreadCacheHolder.setPageTotalInfo(count);
				} catch (Exception e) {
					e.printStackTrace();
					throw new DaoException("Paging failed,Failed to query the total number of datas!");
				} finally {
					if (null != rs) {
						rs.close();
					}
					if (null != countStmt) {
						countStmt.close();
					}
				}
				String pageSql = generatePageSql(sql, pageInfo);
				setValueByFieldName(boundSql, "sql", pageSql);
			}

		}
		return ivk.proceed();
	}

	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
							&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value)
									.getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName
								+ " of statement " + mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}

	private String generatePageSql(String sql, PageInfoModel pageInfo) {
		if (pageInfo != null && (dialect != null || !dialect.equals(""))) {
			if (pageInfo.getPageIndex() <= 0) {
				pageInfo.setPageIndex(1);
			}
			StringBuffer pageSql = new StringBuffer();
			if ("mysql".equals(dialect)) {
				pageSql.append(sql);
				pageSql.append(" limit " + (pageInfo.getPageIndex() - 1) * pageInfo.getPageSize() + ","
						+ pageInfo.getPageSize());
			} else if ("oracle".equals(dialect)) {
				pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
				pageSql.append(sql);
				pageSql.append(")  tmp_tb where ROWNUM<=");
				pageSql.append((pageInfo.getPageIndex() - 1) * pageInfo.getPageSize() + pageInfo.getPageSize());
				pageSql.append(") where row_id>");
				pageSql.append((pageInfo.getPageIndex() - 1) * pageInfo.getPageSize());
			}else if("sqlserver".equals(dialect)){
				//TO-DO
			}
			return pageSql.toString();
		} else {
			return sql;
		}
	}

	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	/**
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private Object getValueByFieldName(Object obj, String fieldName)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = getFieldByFieldName(obj, fieldName);
		Object value = null;
		if (field != null) {
			if (field.isAccessible()) {
				value = field.get(obj);
			} else {
				field.setAccessible(true);
				value = field.get(obj);
				field.setAccessible(false);
			}
		}
		return value;
	}

	/**
	 * @return the dialect
	 */
	public static String getDialect() {
		return dialect;
	}

	/**
	 * @param dialect
	 *            the dialect to set
	 */
	public static void setDialect(String dialect) {
		PageInterceptor.dialect = dialect;
	}

	/**
	 * @return the pageSqlId
	 */
	// public static String getPageSqlId() {
	// return pageSqlId;
	// }
	//
	// /**
	// * @param pageSqlId
	// * the pageSqlId to set
	// */
	// public static void setPageSqlId(String pageSqlId) {
	// PageInterceptor.pageSqlId = pageSqlId;
	// }

	public PageThreadCacheHolder getPageThreadCacheHolder() {
		return pageThreadCacheHolder;
	}

	public void setPageThreadCacheHolder(PageThreadCacheHolder pageThreadCacheHolder) {
		this.pageThreadCacheHolder = pageThreadCacheHolder;
	}

	/**
	 * 
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void setValueByFieldName(Object obj, String fieldName, Object value)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = obj.getClass().getDeclaredField(fieldName);
		if (field.isAccessible()) {
			field.set(obj, value);
		} else {
			field.setAccessible(true);
			field.set(obj, value);
			field.setAccessible(false);
		}
	}

	private Field getFieldByFieldName(Object obj, String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}

	@Override
	public void setProperties(Properties paramProperties) {
	}

}
