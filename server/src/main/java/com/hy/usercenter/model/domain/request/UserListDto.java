package com.hy.usercenter.model.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.usercenter.utils.annotation.DateFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserListDto {

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 页的大小
     */
    private Integer pageSize;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 登录账户
     */
    private String userAccount;

    /**
     * 用户头像地址
     */
    private String avatarUrl;

    /**
     * 用户性别
     */
    private Integer gender;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户角色
     */
    private Integer userRole;

    /**
     * 创建时间
     */
    @DateFormat(value={"yyyy-MM-dd","yyyy-MM-dd HH:ss:mm"})
    private Date startTime;
    @DateFormat(value={"yyyy-MM-dd","yyyy-MM-dd HH:ss:mm"})
    private Date endTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 用户状态
     */
    private Integer userStatus;

    private static final long serialVersionUID = 1L;
}
