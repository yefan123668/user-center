package com.hy.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hy.usercenter.common.ResultEnum;
import com.hy.usercenter.exception.CommonException;
import com.hy.usercenter.model.domain.SysUser;
import com.hy.usercenter.model.domain.request.UserListDto;
import com.hy.usercenter.service.SysUserService;
import com.hy.usercenter.mapper.SysUserMapper;
import com.hy.usercenter.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.hy.usercenter.constants.UserConstant.SALT;
import static com.hy.usercenter.constants.UserConstant.USER_LOGIN_STATE;

/**
 * @description 用户服务实现类
 *
 * @author minsf
 * @createDate 2023-02-08 11:36:05
 *
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword)
    {
        //1、校验(也可以在实体类校验，推荐在这里)
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword)) {
            throw new CommonException(ResultEnum.PARAMS_ERROR, "表单必填参数存在空值");
        }
        if (userAccount.length() < 4) {
            throw new CommonException(ResultEnum.PARAMS_ERROR, "账户长度小于4");
        }
        if (userPassword.length()<8 || checkPassword.length()<8) {
            throw new CommonException(ResultEnum.PARAMS_ERROR, "密码长度小于8");
        }

        //账户不能有特殊字符，仅能包含字母、数字和下划线
        String pattern = "^[a-zA-Z0-9_]+$";
        boolean matches = userAccount.matches(pattern);
        if (!matches) {
            throw new CommonException(ResultEnum.PARAMS_ERROR, "账户包含了特殊字符");
        }

        if (!userPassword.equals(checkPassword)) {
            throw new CommonException(ResultEnum.PARAMS_ERROR, "两次密码不匹配");
        }

        //用户账户不能重复,放到最后，防止造成性能的浪费
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(SysUser::getUserAccount, userAccount);
        long count = this.count(lambdaQueryWrapper);
        if (count > 0) {
            throw new CommonException(ResultEnum.DATABASE_ERROR, "改用户已被注册");
        }

        //2、加密 JAVA包自带的加密工具
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));

        //插入数据
        SysUser user = new SysUser();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("默认用户名，请修改");
        boolean save = this.save(user);

        if (!save) {
            throw new CommonException(ResultEnum.DATABASE_ERROR, "数据库异常，请联系管理员");
        }
        return user.getId();
    }

    @Override
    public SysUser userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1、校验用户登录信息是否合法
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new CommonException(ResultEnum.PARAMS_ERROR, "表单必填参数存在空值");
        }
        if (userAccount.length() < 4) {
            throw new CommonException(ResultEnum.PARAMS_ERROR, "账户长度小于4");
        }
        if (userPassword.length() < 8) {
            throw new CommonException(ResultEnum.PARAMS_ERROR, "密码长度小于8");
        }

        //账户不能有特殊字符，仅能包含字母、数字和下划线
        String pattern = "^[a-zA-Z0-9_]+$";
        boolean matches = userAccount.matches(pattern);
        if (!matches) {
            throw new CommonException(ResultEnum.PARAMS_ERROR, "账户包含了特殊字符");
        }

        // 2. 校验密码是否输入正确，要和数据库中的密文密码去对比
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(SysUser::getUserAccount, userAccount);
        SysUser user = Optional.ofNullable(this.getOne(lambdaQueryWrapper)).orElse(new SysUser());
        if (! encryptPassword.equals(user.getUserPassword())) {
            throw new CommonException(ResultEnum.NO_LOGIN, "密码错误!");
        }

        user = maskUser(user);

        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE, user);
        // 30分钟
        session.setMaxInactiveInterval(30 * 60);
        return user;
    }

    @Override
    public Page<SysUser> userList(UserListDto userParam) {
        Page<SysUser> page = new Page<>();
        page.setSize(Optional.ofNullable(userParam.getPageSize()).orElse(5));
        page.setCurrent(Optional.ofNullable(userParam.getCurrent()).orElse(1));

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(userParam.getUserAccount()!=null, SysUser::getUserAccount, userParam.getUserAccount());
        queryWrapper.like(userParam.getPhone()!=null,SysUser::getPhone, userParam.getPhone());
        queryWrapper.like(userParam.getUserName()!=null,SysUser::getUserName, userParam.getUserName());
        queryWrapper.like(userParam.getEmail()!=null,SysUser::getEmail, userParam.getEmail());
        queryWrapper.like(userParam.getGender()!=null,SysUser::getGender, userParam.getGender());
        queryWrapper.like(userParam.getUserStatus()!=null,SysUser::getUserStatus, userParam.getUserStatus());
        queryWrapper.like(userParam.getUserRole()!=null,SysUser::getUserRole, userParam.getUserRole());

        queryWrapper.ge(userParam.getStartTime()!=null,SysUser::getCreateTime,userParam.getStartTime());
        queryWrapper.le(userParam.getEndTime()!=null,SysUser::getCreateTime,userParam.getEndTime());

        Page<SysUser> userPage = this.page(page, queryWrapper);

        userPage.getRecords().forEach(this::maskUser);
        return userPage;
    }

    @Override
    public SysUser maskUser(SysUser user) {
        if (user == null) {
            throw new CommonException(ResultEnum.NO_LOGIN, "用户信息异常");
        }
        // 3.用户信息脱敏，隐藏敏感信息，防止数据库中的字段泄露
        String maskPhoneNumber = UserUtils.maskPhoneNumber(user.getPhone());
        user.setPhone(maskPhoneNumber);
        user.setUserPassword("");
        user.setIsDeleted(null);
        return user;
    }

}




