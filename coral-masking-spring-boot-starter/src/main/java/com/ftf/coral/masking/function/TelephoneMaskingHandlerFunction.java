package com.ftf.coral.masking.function;

import java.util.function.Function;

import com.ftf.coral.masking.exception.DataMaskingException;

/**
 * 默认手机号脱敏函数
 */
public class TelephoneMaskingHandlerFunction implements Function<Object, Object> {

    private static int STARLEN = 6;

    @Override
    public Object apply(Object rawData) {

        if (rawData == null) {
            return null;
        }

        if (rawData instanceof String) {

            String telephone = (String)rawData;
            telephone = telephone.trim();

            int len = telephone.length();
            if (len >= 3) {
                String left = telephone.substring(0, 3);
                String right = telephone.substring(len - 2, len);
                return left + GenStars.genStars(STARLEN) + right;
            } else {
                return GenStars.genStars(STARLEN);
            }
        } else {

            throw new DataMaskingException("数据脱敏失败，脱敏数据需要为String类型，当前数据类型为：" + rawData.getClass().getTypeName());
        }
    }
}
