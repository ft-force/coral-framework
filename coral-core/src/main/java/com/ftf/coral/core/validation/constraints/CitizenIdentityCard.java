package com.ftf.coral.core.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.ftf.coral.core.validation.CitizenIdentityCardValidator;

/**
 * 身份证号格式校验
 */
@Constraint(validatedBy = CitizenIdentityCardValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface CitizenIdentityCard {

    String message() default "身份证号格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}