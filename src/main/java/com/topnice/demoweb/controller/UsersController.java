package com.topnice.demoweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.topnice.demoweb.entity.Users;
import com.topnice.demoweb.service.UsersService;
import com.topnice.demoweb.token.annotation.UserLoginToken;
import com.topnice.demoweb.token.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "/users", tags = {"用户操作接口"})
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    TokenService tokenService;

    private Map<String, String> mmap;

    @ApiOperation(value = "login", notes = "登录接口")
    @RequestMapping("/login")
    public Map<String, String> login(Users users) {
        System.out.println(users);
        mmap = new HashMap<>();
        Users data = usersService.login(users);
        if (data == null) {
            mmap.put("code", "1");
            mmap.put("msg", "登录失败,用户不存在");
            return mmap;
        } else {
            if (!data.getPassword().equals(users.getPassword())) {
                mmap.put("code", "1");
                mmap.put("msg", "登录失败,密码错误");
                return mmap;
            } else {
                String token = tokenService.getToken(data);
                mmap.put("code", "0");
                mmap.put("token", token);
                Map<String, String> map = new HashMap<>();
                map.put("name", data.getName());
                map.put("enterId", data.getEnterId());
                map.put("userName", data.getUserName());
                map.put("userId", data.getUserId());
                map.put("userType", data.getType());
                mmap.put("data", JSONObject.toJSONString(map));
                return mmap;
            }
        }
    }

    @UserLoginToken
    @ApiOperation(value = "/users", notes = "添加")
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public Map<String, String> add(Users users, String enterId) {
        mmap = new HashMap<>();
        Users users1 = usersService.add(users, enterId);
        return mmap;
    }

    @UserLoginToken
    @ApiOperation(value = "/users", notes = "修改用户信息")
    @RequestMapping(value = "/users", method = RequestMethod.PATCH)
    public Map<String, String> update(Users users, String enterId) {
        mmap = new HashMap<>();
        Users users1 = usersService.add(users, enterId);
        if (users1 == null) {
            mmap.put("code", "1");
            mmap.put("msg", "添加失败");
        } else {
            mmap.put("code", "0");
            mmap.put("msg", "添加成功");
            mmap.put("data", users1 + "");
        }
        return mmap;
    }

    @UserLoginToken
    @ApiOperation(value = "select", notes = "企业管理员查询所有")
    @RequestMapping("/select")
    public Map<String, String> select(String enterId, String loginName, String page, String size) {
        mmap = new HashMap<>();
        String users1 = usersService.findByNameList(enterId, loginName, page, size);
        mmap.put("data", users1 + "");
        return mmap;
    }

}
