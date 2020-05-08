/**
 * Copyright (c) 2017 Sunshine Insurance Group Inc
 * Created by guowenchao on 2017/9/21.
 */
package com.open.boot.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 *Spring对象获取工具
 *@author guowenchao
 *@create 2017-09-21
 *@Email guowenchao-ghq@sinosig.com
 */
@Repository("springContextHolder")
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        LinkedHashMap<String,T> beanMap = (LinkedHashMap<String, T>) applicationContext.getBeansOfType(clazz);
        if(beanMap.size()==0) throw new IllegalStateException(clazz.getName()+"未被Spring托管");
        Set<String> keySet = beanMap.keySet();
        for (String key:keySet){
            T t=  beanMap.get(key);
            return t;
        }
        return null;
    }

    public final static Object getBean(String name, Class<?> clazz) {
        return applicationContext.getBean(name, clazz);
    }



}