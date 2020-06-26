package com.ftf.coral.masking.function;

import java.util.function.Function;

import com.ftf.coral.masking.exception.DataMaskingException;

/**
 * 密码默认脱敏规则
 */
public class PasswordMaskingHandlerFunction implements Function<Object, Object> {

    private static int STARLEN = 6;

    @Override
    public Object apply(Object rawData) {

        if (rawData == null) {
            return null;
        }

        if (rawData instanceof String) {

            return GenStars.genStars(STARLEN);
        } else {

            throw new DataMaskingException("数据脱敏失败，脱敏数据需要为String类型，当前数据类型为：" + rawData.getClass().getTypeName());
        }
    }
}
