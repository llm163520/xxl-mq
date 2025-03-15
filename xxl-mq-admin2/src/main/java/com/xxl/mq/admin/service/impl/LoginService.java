package com.xxl.mq.admin.service.impl;

import com.alibaba.fastjson2.JSON;
import com.xxl.mq.admin.constant.enums.RoleEnum;
import com.xxl.mq.admin.constant.enums.UserStatuEnum;
import com.xxl.mq.admin.mapper.UserMapper;
import com.xxl.mq.admin.model.adaptor.UserAdaptor;
import com.xxl.mq.admin.model.dto.LoginUserDTO;
import com.xxl.mq.admin.model.entity.User;
import com.xxl.mq.admin.util.I18nUtil;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.net.CookieTool;
import com.xxl.tool.response.Response;
import com.xxl.tool.response.ResponseBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * @author xuxueli 2019-05-04 22:13:264
 */
@Configuration
public class LoginService {

    public static final String LOGIN_IDENTITY_KEY = "XXL_MQ_LOGIN_IDENTITY";

    @Resource
    private UserMapper xxlJobUserMapper;

    // ********************** for token **********************

    /**
     * make token from user
     */
    private String makeToken(LoginUserDTO loginUserDTO){
        String tokenJson = JSON.toJSONString(loginUserDTO);
        String tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
        return tokenHex;
    }

    /**
     * parse token to user
     */
    private LoginUserDTO parseToken(String tokenHex){
        LoginUserDTO loginUser = null;
        if (tokenHex != null) {
            String tokenJson = new String(new BigInteger(tokenHex, 16).toByteArray());      // username_password(md5)
            loginUser = JSON.parseObject(tokenJson, LoginUserDTO.class);
        }
        return loginUser;
    }

    // ********************** for login **********************

    /**
     * login (write cookie)
     *
     * @param response
     * @param username
     * @param password
     * @param ifRemember
     * @return
     */
    public Response<String> login(HttpServletResponse response, String username, String password, boolean ifRemember){

        // param
        if (StringTool.isBlank(username) || StringTool.isBlank(password)){
            return new ResponseBuilder<String>().fail( I18nUtil.getString("login_param_empty") ).build();
        }

        // valid user, empty、status、passowrd
        User user = xxlJobUserMapper.loadByUserName(username);
        if (user == null) {
            return new ResponseBuilder<String>().fail( I18nUtil.getString("login_param_unvalid") ).build();
        }
        if (user.getStatus() != UserStatuEnum.NORMAL.getValue()) {
            return new ResponseBuilder<String>().fail( I18nUtil.getString("login_status_invalid") ).build();
        }
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!passwordMd5.equals(user.getPassword())) {
            return new ResponseBuilder<String>().fail( I18nUtil.getString("login_param_unvalid") ).build();
        }

        // find resource

        // make token
        LoginUserDTO loginUserDTO = UserAdaptor.adapt2LoginUser(user);
        String loginToken = makeToken(loginUserDTO);

        // do login
        CookieTool.set(response, LOGIN_IDENTITY_KEY, loginToken, ifRemember);
        return new ResponseBuilder<String>().success().build();
    }

    /**
     * logout (remove cookie)
     *
     * @param request
     * @param response
     */
    public Response<String> logout(HttpServletRequest request, HttpServletResponse response){
        CookieTool.remove(request, response, LOGIN_IDENTITY_KEY);
        return new ResponseBuilder<String>().success().build();
    }

    /**
     * check iflogin (match cookie and db, del cookie if invalid)
     *
     * @param request
     * @return
     */
    public LoginUserDTO checkLogin(HttpServletRequest request, HttpServletResponse response){
        String cookieToken = CookieTool.getValue(request, LOGIN_IDENTITY_KEY);
        if (cookieToken != null) {
            LoginUserDTO loginUser = null;
            try {
                loginUser = parseToken(cookieToken);
            } catch (Exception e) {
                logout(request, response);
            }
            if (loginUser != null) {
                User dbUser = xxlJobUserMapper.loadByUserName(loginUser.getUsername());
                if (dbUser != null) {
                    if (loginUser.getPassword().equals(dbUser.getPassword())) {
                        return loginUser;
                    }
                }
            }
        }
        return null;
    }

    /**
     * get login user (from request, copy from cookie)
     *
     * @param request
     * @return
     */
    public LoginUserDTO getLoginUser(HttpServletRequest request){
        LoginUserDTO loginUser = (LoginUserDTO) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
        return loginUser;
    }

    /**
     * login user is admin
     *
     * @param request
     * @return
     */
    public boolean isAdmin(HttpServletRequest request){
        LoginUserDTO loginUser = getLoginUser(request);
        return loginUser != null && RoleEnum.matchByValue(loginUser.getRole()) == RoleEnum.ADMIN;
    }

}
