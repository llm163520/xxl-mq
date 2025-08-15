package com.xxl.mq.admin.controller.biz;

import com.xxl.mq.admin.annotation.Permission;
import com.xxl.mq.admin.constant.consts.Consts;
import com.xxl.mq.admin.constant.enums.RoleEnum;
import com.xxl.mq.admin.constant.enums.UserStatuEnum;
import com.xxl.mq.admin.model.dto.LoginUserDTO;
import com.xxl.mq.admin.model.dto.UserDTO;
import com.xxl.mq.admin.model.entity.Application;
import com.xxl.mq.admin.service.ApplicationService;
import com.xxl.mq.admin.service.UserService;
import com.xxl.mq.admin.service.impl.LoginService;
import com.xxl.tool.response.PageModel;
import com.xxl.tool.response.Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxueli 2019-05-04 16:39:50
 */
@Controller
@RequestMapping("/user")
public class UserController {
    public static final String ADMIN_ROLE = "ADMIN";

    @Resource
    private UserService userService;
    @Resource
    private LoginService loginService;
    @Resource
    private ApplicationService applicationService;

    @RequestMapping
    @Permission(Consts.ADMIN_PERMISSION)
    public String index(Model model) {

        model.addAttribute("UserStatuEnum", UserStatuEnum.values());
        model.addAttribute("RoleEnum", RoleEnum.values());

        // applicationList
        Response<List<Application>> appResp = applicationService.findAll();
        List<Application> applicationList = appResp!=null?appResp.getData():null;
        model.addAttribute("applicationList", applicationList);

        return "biz/user";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @Permission(Consts.ADMIN_PERMISSION)
    public Response<PageModel<UserDTO>> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                                 @RequestParam(required = false, defaultValue = "10") int length,
                                                 String username,
                                                 @RequestParam(required = false, defaultValue = "-1") int status) {

        PageModel<UserDTO> pageModel = userService.pageList(start, length, username, status);
        return Response.ofSuccess(pageModel);
    }

    @RequestMapping("/add")
    @ResponseBody
    @Permission(Consts.ADMIN_PERMISSION)
    public Response<String> add(UserDTO user) {
        return userService.insert(user);
    }

    @RequestMapping("/update")
    @ResponseBody
    @Permission(Consts.ADMIN_PERMISSION)
    public Response<String> update(HttpServletRequest request, UserDTO userDTO) {
        LoginUserDTO loginUser = loginService.getLoginUser(request);
        return userService.update(userDTO, loginUser);
    }

    @RequestMapping("/delete")
    @ResponseBody
    @Permission(Consts.ADMIN_PERMISSION)
    public Response<String> delete(HttpServletRequest request,
                                   @RequestParam("ids[]") List<Integer> ids) {
        LoginUserDTO loginUser = loginService.getLoginUser(request);
        return userService.deleteByIds(ids, loginUser);
    }

    @RequestMapping("/grantPermission")
    @ResponseBody
    @Permission(Consts.ADMIN_PERMISSION)
    public Response<String> grantPermission(@RequestParam("username") String username,
                                            @RequestParam("permission") String permission) {
        return userService.grantPermission(username, permission);
    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    @Permission
    public Response<String> updatePwd(HttpServletRequest request, String password){
        LoginUserDTO loginUser = loginService.getLoginUser(request);
        return userService.updatePwd(loginUser, password);
    }

}
