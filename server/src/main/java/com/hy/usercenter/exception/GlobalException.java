package com.hy.usercenter.exception;

import com.hy.usercenter.common.BaseResponse;
import com.hy.usercenter.common.ResultEnum;
import com.hy.usercenter.common.ResultUtils;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description 全局异常处理器
 *
 * @Author minsf
 * @Date 2023/2/14
 */
@RestControllerAdvice
@Log
public class GlobalException {

    @ExceptionHandler(value = CommonException.class)
    public BaseResponse commonHandler(CommonException e) {
        e.printStackTrace();
        log.info("错误发生在======>"+e.getDescription());
        return ResultUtils.fail(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public BaseResponse runtimeHandler(RuntimeException e) {
        e.printStackTrace();
        log.info("错误发生在======>"+e.getMessage());
        return ResultUtils.fail(ResultEnum.SYSTEM_ERROR, "系统异常，请联系管理员");
    }
}
