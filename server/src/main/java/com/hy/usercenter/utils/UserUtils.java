package com.hy.usercenter.utils;

import org.apache.commons.lang3.StringUtils;

public class UserUtils {

    /**
     * 对用户手机号脱敏处理
     *
     * @param phoneNumber 原始邮箱
     * @return 脱敏后手机号
     */
    public static String maskPhoneNumber(String phoneNumber) {
        if (StringUtils.isAllBlank(phoneNumber)) {
            return "";
        }
        int length = phoneNumber.length();
        return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(length - 4, length);
    }

    /**
     * 对用户邮箱进行脱敏处理
     *
     * @param email 原始邮箱
     * @return 脱敏后的邮箱
     */
    public static String maskEmail(String email) {
        if (StringUtils.isAllBlank(email)) {
            return "";
        }
        int index = email.indexOf("@");
        String maskedEmail = email.substring(0, 2) + "****" + email.substring(index - 1, index) + email.substring(index);
        return maskedEmail;
    }
}
