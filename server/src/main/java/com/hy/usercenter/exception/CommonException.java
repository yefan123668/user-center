package com.hy.usercenter.exception;

import com.hy.usercenter.common.ResultEnum;
import lombok.extern.java.Log;

/**
 * @Description 通用业务异常类，多用于业务类
 *
 * @Author minsf
 * @Date 2023/2/14
 */
@Log
public class CommonException extends RuntimeException{

    private  int code;

    private  String description;


    public CommonException(int code, String msg,String description) {
        super(msg);
        this.code = code;
        this.description = description;
    }

    public CommonException(ResultEnum errorInfo) {
        super(errorInfo.getMessage());
        this.code = errorInfo.getCode();
        this.description = errorInfo.getDescription();
    }

    public CommonException(ResultEnum errorInfo, String description) {
        super(errorInfo.getMessage());
        this.code = errorInfo.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
