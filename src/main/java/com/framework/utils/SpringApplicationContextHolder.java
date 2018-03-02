package com.framework.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/*************************************************************************
 * 版本： V1.0
 * 
 * 文件名称 ：SpringApplicationContextHolder.java 描述说明 ：
 * 
 * 创建信息 : create by 刘佳佳 on 2015-8-8 下午2:56:27 修订信息 : modify by ( ) on (date) for
 * ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/
public class SpringApplicationContextHolder implements ApplicationContextAware {
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}
}
