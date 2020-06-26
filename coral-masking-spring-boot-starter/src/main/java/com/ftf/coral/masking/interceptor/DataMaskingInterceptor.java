package com.ftf.coral.masking.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftf.coral.masking.annotation.DataMasking;
import com.ftf.coral.masking.enums.MaskingDataType;
import com.ftf.coral.masking.function.FirstLastShowHandlerFunction;
import com.ftf.coral.masking.function.IDCardMaskingHandlerFunction;
import com.ftf.coral.masking.function.PasswordMaskingHandlerFunction;
import com.ftf.coral.masking.function.TelephoneMaskingHandlerFunction;
import com.ftf.coral.util.BeanPath;
import com.ftf.coral.util.JSONUtils;

public class DataMaskingInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(DataMaskingInterceptor.class);

    private static final ConcurrentHashMap<String, Function<Object, Object>> maskingRuleFunction = new ConcurrentHashMap<>();

    static {
        addRuleFunction("IDCard", new IDCardMaskingHandlerFunction());
        addRuleFunction("firstLastShow", new FirstLastShowHandlerFunction());
        addRuleFunction("password", new PasswordMaskingHandlerFunction());
        addRuleFunction("telephone", new TelephoneMaskingHandlerFunction());
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object returnObject = invocation.proceed();// 得到方法返回的最终结果

        Method method = invocation.getMethod();
        DataMasking masking = method.getAnnotation(DataMasking.class);

        if (masking != null) {

            String dataPath = masking.dataPath();
            MaskingDataType dataType = masking.dataType();
            String[] maskingRule = masking.maskingRule();

            BeanPath objectBeanPath = BeanPath.compile(dataPath);

            Function<Object, Object> maskingHandler = new Function<Object, Object>() {

                @Override
                public Object apply(Object bean) {

                    boolean isJSON = MaskingDataType.json.getCode().equals(dataType.getCode());
                    Object maskBean = new Object();

                    if (isJSON) {
                        String beanStr = (String) bean;
                        maskBean = JSONUtils.formJSON(beanStr, Object.class);
                    } else {
                        maskBean = bean;
                    }

                    // 脱敏
                    for (int i = 0; i < maskingRule.length; i++) {

                        String[] pathRule = maskingRule[i].trim().split("#");
                        String path = pathRule[0];
                        String rule = pathRule[1];

                        BeanPath tempBeanPath = BeanPath.compile(path);
                        Function<Object, Object> tempMasking = maskingRuleFunction.get(rule);
                        maskBean = tempBeanPath.process(maskBean, tempMasking);
                    }

                    if (isJSON) {
                        return JSONUtils.toJSON(maskBean);
                    } else {
                        return maskBean;
                    }
                }
            };

            Object resultObject = objectBeanPath.process(returnObject, maskingHandler);
            return resultObject;
        }

        return returnObject;
    }

    public static void addRuleFunction(String name, Function<Object, Object> function) {
        Function<Object, Object> temp = maskingRuleFunction.putIfAbsent(name, function);
        if (temp != null) {
            if (logger.isWarnEnabled()) {
                logger.warn("函数({})已经注册，请勿再次注册，当前值为:{}，尝试注册值为:{}", name, temp.getClass().getTypeName(),
                                function.getClass().getName());
            }
        }
    }
}
