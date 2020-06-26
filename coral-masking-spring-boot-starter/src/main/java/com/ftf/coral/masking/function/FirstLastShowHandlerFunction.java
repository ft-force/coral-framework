package com.ftf.coral.masking.function;

import java.util.function.Function;

import com.ftf.coral.masking.exception.DataMaskingException;

public class FirstLastShowHandlerFunction implements Function<Object, Object> {

    private static int STARLEN = 6;

    @Override
    public Object apply(Object rawData) {

        if (rawData == null) {
            return null;
        }

        if (rawData instanceof String) {

            String rawDataStr = (String) rawData;
            rawDataStr = rawDataStr.trim();

            int len = rawDataStr.length();
            if (len >= 1) {
                String left = rawDataStr.substring(0, 1);
                String right = rawDataStr.substring(len - 1, len);
                return left + GenStars.genStars(STARLEN) + right;
            } else {
                return rawData;
            }
        } else {

            throw new DataMaskingException("数据脱敏失败，脱敏数据需要为String类型，当前数据类型为：" + rawData.getClass().getTypeName());
        }
    }
}
