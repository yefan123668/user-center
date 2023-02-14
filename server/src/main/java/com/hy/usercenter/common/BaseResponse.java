package com.hy.usercenter.common;

import lombok.Data;

/**
 * 公共返回类
 *
 * @author minsf
 * @param <T>
 */
@Data
public class BaseResponse<T> {

    private T data;

    private String msg;

    private Integer code;

    private String description;

    public BaseResponse(T data, String msg, Integer code, String description) {
        this.data = data;
        this.msg = msg;
        this.code = code;
        this.description = description;
    }

    public BaseResponse(T data, String msg, Integer code) {
        this(data, msg, code, "");
    }

    public BaseResponse(T data, Integer code) {
        this(data, "", code);
    }
}
