package com.sfwl.framework.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

public class ParamPrefixArgumentResolver implements HandlerMethodArgumentResolver {
	public static final String BINDING_RESULT_LIST_NAME = "_bindingResultList_";
	private static final String DEFAULT_SEPARATOR = ".";
	private String separator;
	private WebBindingInitializer webBindingInitializer;

	public ParamPrefixArgumentResolver() {
		this.separator = DEFAULT_SEPARATOR;
	}

	public void setSeparator(String separator) {
		this.separator = (null == separator ? DEFAULT_SEPARATOR : separator);
	}

	public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
		this.webBindingInitializer = webBindingInitializer;
	}

	public boolean supportsParameter(MethodParameter parameter) {
		ParamPrefix formBeanAnnotation = getFormBeanAnnotation(parameter);
		if (formBeanAnnotation != null) {
			String prefix = formBeanAnnotation.value();
			return prefix != null && prefix.length() > 0;
		}
		return false;
	}

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		ServletRequest servletRequest = (ServletRequest) webRequest.getNativeRequest();

		PropertyValues pvs = null;
		Object bindObject = null;

		String prefix = getPrefix(parameter);

		Class paramType = parameter.getParameterType();

		if ((Collection.class.isAssignableFrom(paramType)) || (paramType.isArray())) {
			Class genericClass = null;
			if (paramType.isArray()) {
				genericClass = paramType.getComponentType();
			} else {
				Type type = parameter.getGenericParameterType();
				if ((type instanceof ParameterizedType)) {
					ParameterizedType pt = (ParameterizedType) type;
					genericClass = (Class) pt.getActualTypeArguments()[0];
				}
			}
			if (genericClass != null) {
				Map mappedValues = createMappedValues(genericClass, webRequest, parameter, prefix);
				if (!mappedValues.isEmpty()) {
					List targetObject = new ArrayList(mappedValues.values());
					WebDataBinder binder = new WebDataBinder(null);
					if (this.webBindingInitializer != null) {
						this.webBindingInitializer.initBinder(binder, webRequest);
					}
					bindObject = binder.convertIfNecessary(targetObject, paramType);
				}
			}
		} else if (Map.class.isAssignableFrom(paramType)) {
			Class genericClass = null;
			Type type = parameter.getGenericParameterType();
			if ((type instanceof ParameterizedType)) {
				ParameterizedType pt = (ParameterizedType) type;
				genericClass = (Class) pt.getActualTypeArguments()[1];
			}

			if (genericClass != null) {
				Map mappedValues = createMappedValues(genericClass, webRequest, parameter, prefix);
				if (!mappedValues.isEmpty()) {
					Map targetObject = new HashMap();

					Set set = mappedValues.entrySet();
					Iterator i = set.iterator();
					while (i.hasNext()) {
						Map.Entry<String, Object> entry = (Map.Entry<String, Object>) i.next();
						String key = (String) entry.getKey();
						key = key.substring(key.indexOf("['") + 2, key.indexOf("']"));
						targetObject.put(key, entry.getValue());
					}
					// for (Map.Entry entry : mappedValues.entrySet()) {
					// String key = (String) entry.getKey();
					// key = key.substring(key.indexOf("['") + 2,
					// key.indexOf("']"));
					// targetObject.put(key, entry.getValue());
					// }
					WebDataBinder binder = new WebDataBinder(null);
					if (this.webBindingInitializer != null) {
						this.webBindingInitializer.initBinder(binder, webRequest);
					}
					bindObject = binder.convertIfNecessary(targetObject, paramType);
				}
			}
		} else {
			pvs = new ServletRequestParameterPropertyValues(servletRequest, prefix, this.separator);

			if (null == bindObject) {
				bindObject = BeanUtils.instantiateClass(paramType);
			}

			WebDataBinder binder = new WebDataBinder(bindObject, prefix);
			if (this.webBindingInitializer != null) {
				this.webBindingInitializer.initBinder(binder, webRequest);
			}

			binder.bind(pvs);
		}
		Object aa = WebUtils.getParametersStartingWith(servletRequest,
				(prefix != null) ? prefix + this.separator : null);

		return bindObject;
	}

	private String getPrefix(MethodParameter parameter) {
		for (Annotation annotation : parameter.getParameterAnnotations()) {
			if ((annotation instanceof ParamPrefix)) {
				return ((ParamPrefix) annotation).value();
			}
		}
		return null;
	}

	private ParamPrefix getFormBeanAnnotation(MethodParameter parameter) {
		for (Annotation annotation : parameter.getParameterAnnotations()) {
			if ((annotation instanceof ParamPrefix)) {
				return (ParamPrefix) annotation;
			}
		}
		return null;
	}

	private Map<String, Object> createMappedValues(Class<?> genericClass, NativeWebRequest webRequest,
			MethodParameter parameter, String prefix) {
		ServletRequest servletRequest = (ServletRequest) webRequest.getNativeRequest();
		Map resultMap = new LinkedHashMap();
		WebDataBinder binderHelper = new WebDataBinder(null);
		if (this.webBindingInitializer != null) {
			this.webBindingInitializer.initBinder(binderHelper, webRequest);
		}

		Set keySet = getSortedKeySet(servletRequest, prefix + '[');
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			Object genericObj = null;
			if (key.endsWith(this.separator)) {
				ServletRequestParameterPropertyValues pvs = new ServletRequestParameterPropertyValues(servletRequest,
						key, "");

				String realKey = key.substring(0, key.length() - 1);
				if (null == genericObj) {
					genericObj = BeanUtils.instantiateClass(genericClass);
				}

				WebDataBinder objectBinder = new WebDataBinder(genericObj, realKey);
				if (this.webBindingInitializer != null) {
					this.webBindingInitializer.initBinder(objectBinder, webRequest);
				}
				objectBinder.bind(pvs);
			} else {
				Map paramValues = WebUtils.getParametersStartingWith(servletRequest, key);
				if (!paramValues.isEmpty()) {
					if (Collection.class.isAssignableFrom(genericClass))
						genericObj = binderHelper.convertIfNecessary(paramValues.values(), genericClass);
					else {
						genericObj = binderHelper.convertIfNecessary(paramValues.values().iterator().next(),
								genericClass);
					}
				}
			}

			if (genericObj != null) {
				resultMap.put(key, genericObj);
			}
		}
		return resultMap;
	}

	private Set<String> getSortedKeySet(ServletRequest request, String prefix) {
		Assert.notNull(request, "Request must not be null");
		Assert.notNull(prefix, "Prefix must not be null");
		Enumeration paramNames = request.getParameterNames();
		Set keySet = new TreeSet(ComparatorImpl.INSTANCE);
		while ((paramNames != null) && (paramNames.hasMoreElements())) {
			String paramName = (String) paramNames.nextElement();
			if (paramName.startsWith(prefix)) {
				String key = paramName;
				int lastScopeIndex = paramName.indexOf(']');
				int firstSeparator = paramName.indexOf(this.separator);
				if (firstSeparator > lastScopeIndex) {
					key = paramName.substring(0, firstSeparator + 1);
				}
				if (!keySet.contains(key)) {
					keySet.add(key);
				}
			}
		}
		return keySet;
	}

	private static final class ComparatorImpl implements Comparator<String> {
		public static final ComparatorImpl INSTANCE = new ComparatorImpl();

		public int compare(String left, String right) {
			int lengthCompare = left.length() - right.length();
			return lengthCompare != 0 ? lengthCompare : left.compareTo(right);
		}
	}
}
