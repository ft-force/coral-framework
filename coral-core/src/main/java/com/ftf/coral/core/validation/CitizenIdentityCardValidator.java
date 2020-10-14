package com.ftf.coral.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ftf.coral.core.validation.constraints.CitizenIdentityCard;
import com.ftf.coral.util.CertNoUtils;
import com.ftf.coral.util.StringUtils;

public class CitizenIdentityCardValidator implements ConstraintValidator<CitizenIdentityCard, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value)) {
            return true;
        }

        return CertNoUtils.isCertNo(value);
    }
}