package com.ftf.coral.masking.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ftf.coral.masking.enums.MaskingDataType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DataMasking {

    String dataPath() default "$";

    MaskingDataType dataType() default MaskingDataType.object;

    String[] maskingRule(); // "a.b.c#rule1,a.b.d#rule2"
}
