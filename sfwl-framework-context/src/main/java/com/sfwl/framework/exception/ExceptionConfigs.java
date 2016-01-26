package com.sfwl.framework.exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ExceptionConfigs {

	private static ExceptionConfigs configs = new ExceptionConfigs();

	private ExceptionConfigs() {
	}

	public String getExceptionInfo(String exceptionCode) throws IOException {
		Properties p = new Properties();
		InputStream in = ExceptionConfigs.class.getResourceAsStream("/exception.properties");
		String exceptionInfo = "";
		p.load(in);
		exceptionInfo = p.getProperty(exceptionCode);
		return exceptionInfo;
	}

	public static ExceptionConfigs getConfigs() {
		return configs;
	}
}
