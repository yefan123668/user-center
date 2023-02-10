package com.hy.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author minsf
 */
@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -7176187806215965499L;

    private String userAccount;

    private String userPassword;
}
