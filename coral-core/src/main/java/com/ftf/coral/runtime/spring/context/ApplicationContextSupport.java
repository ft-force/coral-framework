package com.ftf.coral.runtime.spring.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.ftf.coral.runtime.CoralContext;

@Component
public class ApplicationContextSupport implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        CoralContext.registAPI(ApplicationContext.class, ctx);
    }
}