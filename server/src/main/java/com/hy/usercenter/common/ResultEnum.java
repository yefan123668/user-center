package com.hy.usercenter.common;

/**
 * @Description 响应枚举，枚举出业务信息
 *
 * @Author minsf
 * @Date 2023/2/14
 */
public enum ResultEnum {

    SUCCESS(2000, "ok", ""),
    PARAMS_ERROR(5000, "参数错误", ""),
    DATA_ERROR(5001, "数据异常", ""),
    NO_AUTHORITY(4001, "无权限",""),
    NO_LOGIN(4003, "未登录",""),
    DATABASE_ERROR(5005, "数据库异常", ""),
    SYSTEM_ERROR(50000, "系统异常", ""),
    DATA_DELETE_FAIL_ERROR(5006, "数据删除失败", "");


    private final Integer code;

    private final String message;

    private final String description;

    ResultEnum(int code, String message, String description) {
        this.code=code;
        this.message=message;
        this.description=description;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
