package com.hy.usercenter.common;

/**
 * 统一返回类工具
 *
 * @author minsf
 */
public class ResultUtils {

    /**
     * 响应成功返回信息
     *
     * @param data 响应的数据
     * @param <T> 响应的数据的类型
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(data, "ok", 200);
    }

    public static <T> BaseResponse<T> success(T data, String msg) {
        return new BaseResponse<>(data, msg, 200);
    }

    public static <T> BaseResponse<T> success(T data, ResultEnum resultEnum) {
        return new BaseResponse<>(data, resultEnum.getMessage(),
                resultEnum.getCode(), resultEnum.getDescription());
    }

    /**
     * 响应失败返回信息
     *
     * @param resultEnum 失败枚举
     * @return
     */
    public static BaseResponse fail(ResultEnum resultEnum) {
        return new BaseResponse(null, resultEnum.getMessage(),
                resultEnum.getCode(), resultEnum.getDescription());
    }

    /**
     * 业务错误
     *
     * @param resultEnum 错误枚举
     * @param description 错误具体描述
     * @return
     */
    public static BaseResponse fail(ResultEnum resultEnum, String description) {
        return new BaseResponse(null, resultEnum.getMessage(),
                resultEnum.getCode(), description);
    }

    public static BaseResponse fail(Integer code,String msg, String description) {
        return new BaseResponse(null, msg,
                code, description);
    }
}
