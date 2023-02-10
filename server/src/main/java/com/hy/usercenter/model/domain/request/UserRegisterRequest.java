package com.hy.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author minsf
 */
@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = -8701337648541511521L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
