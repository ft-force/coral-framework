package com.ftf.coral.masking.spring;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.core.Ordered;

import com.ftf.coral.masking.annotation.DataMasking;
import com.ftf.coral.masking.interceptor.DataMaskingInterceptor;
import com.ftf.coral.runtime.spring.util.SpringProxyUtils;
import com.ftf.coral.util.CollectionUtils;

public class DataMaskingMethodScanner extends AbstractAutoProxyCreator {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(DataMaskingMethodScanner.class);

    private int order = Ordered.HIGHEST_PRECEDENCE;

    private static final Set<String> PROXYED_SET = new HashSet<>();

    private MethodInterceptor interceptor;

    @SuppressWarnings("unchecked")
    public DataMaskingMethodScanner(Map<String, String> ruleFunction) {

        if (ruleFunction != null && !ruleFunction.isEmpty()) {

            ruleFunction.forEach((name, functionClass) -> {

                try {

                    Object function = Class.forName(functionClass).newInstance();

                    if (function instanceof Function) {
                        DataMaskingInterceptor.addRuleFunction(name, (Function<Object, Object>)function);
                    } else {
                        LOGGER.warn("{}:{} （脱敏函数注册失败，不能识别的函数类型。）", name, functionClass);
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.warn("{}:{} （脱敏函数注册失败，系统找不到指定的类。）", name, functionClass, e);
                } catch (Throwable e) {
                    LOGGER.warn("{}:{} （脱敏函数注册失败）", name, functionClass, e);
                }
            });
        }
    }

    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {

        try {
            synchronized (PROXYED_SET) {
                if (PROXYED_SET.contains(beanName)) {
                    return bean;
                }
                interceptor = null;

                Class<?> serviceInterface = SpringProxyUtils.findTargetClass(bean);
                Class<?>[] interfacesIfJdk = SpringProxyUtils.findInterfaces(bean);

                if (!existsAnnotation(new Class[] {serviceInterface}) && !existsAnnotation(interfacesIfJdk)) {
                    return bean;
                }

                // 待优化，考虑使用单例模式
                interceptor = new DataMaskingInterceptor();

                if (!AopUtils.isAopProxy(bean)) {
                    bean = super.wrapIfNecessary(bean, beanName, cacheKey);
                } else {
                    AdvisedSupport advised = SpringProxyUtils.getAdvisedSupport(bean);
                    Advisor[] advisor = buildAdvisors(beanName, getAdvicesAndAdvisorsForBean(null, null, null));
                    for (Advisor avr : advisor) {
                        advised.addAdvisor(0, avr);
                    }
                }

                PROXYED_SET.add(beanName);
                return bean;
            }
        } catch (Exception exx) {
            throw new RuntimeException(exx);
        }
    }

    private boolean existsAnnotation(Class<?>[] classes) {
        if (CollectionUtils.isNotEmpty(classes)) {
            for (Class<?> clazz : classes) {
                if (clazz == null) {
                    continue;
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    DataMasking dmAnno = method.getAnnotation(DataMasking.class);
                    if (dmAnno != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName,
        TargetSource customTargetSource) throws BeansException {
        return new Object[] {interceptor};
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
