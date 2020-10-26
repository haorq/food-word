package com.meiyuan.catering.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * description：
 *
 * @author fql
 * @version 1.5.0
 * @date 2020/10/20 9:26
 */
@Component
public class ApplicationUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ApplicationUtil.applicationContext == null) {
            ApplicationUtil.applicationContext = applicationContext;
        }
    }
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> c){
        return applicationContext.getBean(c);
    }
}
