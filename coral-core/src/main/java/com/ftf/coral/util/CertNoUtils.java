package com.ftf.coral.util;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import com.ftf.coral.core.enums.IntegerValueEnum;
import com.ftf.coral.core.exception.BusinessException;

/**
 * CertNo工具类
 */
public class CertNoUtils {

    private static final String certNoFormat =
        "^\\d{6}(((19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\d{3}([0-9]|x|X))|(\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\d{3}))$";

    public static boolean isCertNo(String certNo) {
        return Pattern.matches(certNoFormat, certNo)
            || (certNo.length() == 17 && Pattern.matches(certNoFormat, certNo.substring(0, 15)));
    }

    /**
     * 解析 certNo
     */
    public static CertInfo parseCertNo(String certNo) {

        boolean valid = Pattern.matches(certNoFormat, certNo)
            || (certNo.length() == 17 && Pattern.matches(certNoFormat, certNo.substring(0, 15)));

        if (!valid) {
            throw new BusinessException("证件号码不规范");
        }

        CertInfo certInfo = new CertInfo();

        int idxSexStart = 16;
        int birthYearSpan = 4;
        // 如果是15位的证件号码
        if (certNo.length() == 15) {
            idxSexStart = 14;
            birthYearSpan = 2;
        }

        // 性别
        String idxSexStr = certNo.substring(idxSexStart, idxSexStart + 1);
        int idxSex = Integer.parseInt(idxSexStr) % 2;
        Sex sex = (idxSex == 1) ? Sex.Male : Sex.Female;
        certInfo.setSex(sex);

        // 出生日期
        String year = (birthYearSpan == 2 ? "19" : "") + certNo.substring(6, 6 + birthYearSpan);
        String month = certNo.substring(6 + birthYearSpan, 6 + birthYearSpan + 2);
        String day = certNo.substring(8 + birthYearSpan, 8 + birthYearSpan + 2);
        Calendar.Builder birthdayBuilder = new Calendar.Builder();
        birthdayBuilder.setDate(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
        certInfo.setBirthday(birthdayBuilder.build().getTime());

        return certInfo;
    }

    public static class CertInfo {

        private Sex sex;
        private Date birthday;

        public Sex getSex() {
            return sex;
        }

        public void setSex(Sex sex) {
            this.sex = sex;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }
    }

    public enum Sex implements IntegerValueEnum {

        Male(0), // 男
        Female(1);// 女

        private Integer value;

        private Sex(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        public static Sex parse(Integer value) {

            if (value == null) {
                return null;
            }

            for (Sex e_value : Sex.values()) {
                if (e_value.getValue().equals(value)) {
                    return e_value;
                }
            }

            throw new IllegalArgumentException("Unable to parse the provided value " + value);
        }
    }
}