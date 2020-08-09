package com.net.yoga.controller;


import com.alibaba.fastjson.JSONObject;
import com.net.yoga.common.base.BaseController;
import com.net.yoga.entity.WxUserModel;
import com.net.yoga.redis.RedisService;
import com.net.yoga.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author gaort
 * @since 2020-08-02
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户相关接口")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;



    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="获取用户列表", notes="获取用户列表")
    public Object getAll() {
        return buildSuccJson(userService.findAllUser());
    }
}
