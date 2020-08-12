package com.ftf.coral.runtime.spring.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import com.ftf.coral.core.enums.ValueEnum;

public class String2ValueEnumConverterFactory implements ConverterFactory<String, ValueEnum> {

    @Override
    public <T extends ValueEnum> Converter<String, T> getConverter(Class<T> targetType) {

        if (!targetType.isEnum()) {
            throw new UnsupportedOperationException("只支持转换到枚举类型");
        }

        return new String2ValueEnumConverter(targetType);
    }

    private class String2ValueEnumConverter<T extends ValueEnum> implements Converter<String, T> {

        private final Class<T> enumType;

        public String2ValueEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String s) {
            for (T t : enumType.getEnumConstants()) {
                if (s.equals(t.getValue())) {
                    return t;
                }
            }
            return null;
        }
    }
}
